package com.epam.jc.REST.Resources.Orders;

import com.epam.jc.DbController.DAOFactory;
import com.epam.jc.DbController.Entities.Magazine;
import com.epam.jc.DbController.Entities.Order;
import com.epam.jc.DbController.Entities.Subscription;
import com.epam.jc.DbController.Entities.User;
import com.epam.jc.REST.Common;
import com.epam.jc.REST.Security.LoginDispatcher;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created on 18/04/16.
 *
 * @author Vladislav Boboshko
 */

@Path("orders")
public class OrderREST {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrdersForUser(@Context HttpServletRequest request) {
        if (!Optional.ofNullable(request.getSession().getAttribute("user")).isPresent()) {
            return Response.status(403).entity("error:forbidden").build();
        }
        JSONObject jsonResponse = new JSONObject();
        List<Order> orders = DAOFactory.getOrderDAO().getOrdersForUser((User) request.getSession().getAttribute("user"));
        jsonResponse.put("amount", orders.size());
        JSONArray jsonOrders = new JSONArray();
        orders.forEach(order -> {
            JSONObject jsonOrder = order.toJSON();
            List<Subscription> subscriptions = DAOFactory.getSubscriptionDAO().getSubscriptionsByOrder(order);
            JSONArray jsonSubs = new JSONArray();
            subscriptions.forEach((subscription) -> jsonSubs.add(subscription.toJSON()));
            jsonOrder.put("subscriptions", jsonSubs);
            jsonOrders.add(jsonOrder);
        });
        jsonResponse.put("orders", jsonOrders);
        return Response.ok().entity(jsonResponse.toJSONString()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("delete/{id}")
    public Response deleteOrder(@Context HttpServletRequest request, @PathParam("id") String restId) {
        Optional<User> user = Optional.ofNullable((User)request.getSession().getAttribute("user"));
        if (!user.isPresent()) {
            return Response.status(403).entity("{error:forbidden}").build();
        }
        Long id;
        try {
            id = Long.parseLong(restId);
        } catch (NumberFormatException e) {
            return Response.status(400).entity("{\"result\":\"bad\"}").build();
        }
        Optional<Order> order = DAOFactory.getOrderDAO().getOrder(id);
        if (!order.isPresent() || !order.get().getUserId().equals(user.get().getId()) || order.get().isPaid()) {
            return Response.status(403).entity("{error:forbidden}").build();
        }
        DAOFactory.getSubscriptionDAO().deleteSubscriptionsForOrder(id);
        DAOFactory.getOrderDAO().deleteOrder(id);
        return Response.ok().entity("\"result\":\"true\"").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("pay/{id}")
    public Response payForOrderById(@Context HttpServletRequest request, @PathParam("id") String restId) {
        Optional<User> user = Optional.ofNullable((User)request.getSession().getAttribute("user"));
        if (!user.isPresent()) {
            return Response.status(403).entity("{error:forbidden}").build();
        }
        Long id;
        try {
            id = Long.parseLong(restId);
        } catch (NumberFormatException e) {
            return Response.status(400).entity("{\"result\":\"bad\"}").build();
        }
        Optional<Order> order = DAOFactory.getOrderDAO().getOrder(id);
        if (order.isPresent() && order.get().getUserId().equals(user.get().getId())) {
            order.get().setPaid(true);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("id", order.get().getId());
            jsonResponse.put("paid", DAOFactory.getOrderDAO().updateOrder(order.get()));
            return Response.ok().entity(jsonResponse.toJSONString()).build();
        } else {
            return Response.status(403).entity("{error:forbidden}").build();
        }

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("add")
    public Response placeOrder(@Context HttpServletRequest request) {
        if (!Optional.ofNullable(request.getSession().getAttribute("user")).isPresent()) {
            return Response.status(403).entity("error:forbidden").build();
        }
        List<Subscription> subscriptions = new ArrayList<>();
        try {
            JSONObject jsonRequest = ((JSONObject) new JSONParser().parse(Common.getRequestBody(request)));
            String address = new String(((String) jsonRequest.get("address")).getBytes("ISO-8859-1"), "UTF-8");
            Long userId = ((User) request.getSession().getAttribute("user")).getId();
            DAOFactory.getOrderDAO().addOrder(new Order(userId,
                    Timestamp.valueOf(LocalDateTime.now()),
                    0.0,
                    false,
                    address));
            Optional<Order> order = DAOFactory.getOrderDAO().getLatestForUser(userId);
            if (!order.isPresent()) {
                throw new Exception("No order");
            }
            JSONArray jsonSubscriptions = ((JSONArray) jsonRequest.get("order"));
            for (int i=0; i<jsonSubscriptions.size(); i++) {
                JSONObject subscription = (JSONObject) jsonSubscriptions.get(i);
                subscriptions.add(new Subscription(order.get().getId(),
                        ((Long) subscription.get("id")),
                        ((Long) subscription.get("months"))));
            }
            final Double[] price = {0.0};
            subscriptions.forEach((a) -> {
                DAOFactory.getSubscriptionDAO().addSubscription(a);
                price[0] +=a.getMonths()*DAOFactory.getMagazineDAO().
                        getMagazine(a.getMagazineId()).orElse(new Magazine(0L, "", 0.0, "")).getPrice();
            });
            order.get().setToPay(price[0]);
            if (!DAOFactory.getOrderDAO().updateOrder(order.get())) {
                throw new Exception("cannot");
            }
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("id", order.get().getId());
            jsonResponse.put("toPay", order.get().getToPay());
            jsonResponse.put("address", order.get().getShipAddress());
            return Response.ok().entity(jsonResponse.toJSONString()).build();
        }
        catch (Exception e) {
            return Response.status(400).build();
        }
    }
}
