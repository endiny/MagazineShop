package com.epam.jc.REST.Resources.Users;

import com.epam.jc.DbController.DAOFactory;
import com.epam.jc.DbController.Entities.User;
import com.epam.jc.REST.Common;
import com.epam.jc.REST.Security.LoginDispatcher;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

/**
 * Created on 12/04/16.
 *
 * @author Vladislav Boboshko
 */

@Path("user")
public class UserInfo {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@Context HttpServletRequest requestContext) {
        HttpSession session = requestContext.getSession();
        JSONObject result = new JSONObject();
        Optional<User> optionalUser = Optional.ofNullable((User)session.getAttribute("user"));
        if (!optionalUser.isPresent()) {
            result.put("result", "forbidden");
            return Response.status(403).entity(result.toJSONString()).build();
        }
        Long id = optionalUser.orElse(new User(0L, "", "", "", 0L)).getId();
        User user = DAOFactory.getUserDAO().getUser(id);
        result.put("id", user.getId());
        result.put("login", user.getLogin());
        result.put("name", user.getName());
        result.put("role", user.getRole());
        return Response.ok().entity(result.toJSONString()).build();
    }

    @GET
    @Path("id/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@Context HttpServletRequest requestContext, @PathParam("userId") String userId) {
        HttpSession session = requestContext.getSession();
        JSONObject result = new JSONObject();
        Long id;
        try {
            id = Long.parseLong(userId);
        }
        catch (NumberFormatException e) {
            return Response.status(401).build();
        }
        if (!LoginDispatcher.getInstance().isUserInRole(session, "admin")) {
            return Response.status(403).build();
        }
        User user = DAOFactory.getUserDAO().getUser(id);
        if (user.getId() == 0) {
            return Response.status(404).build();
        }
        result.put("id", user.getId());
        result.put("login", user.getLogin());
        result.put("name", user.getName());
        result.put("role", user.getRole());
        return Response.ok().entity(result.toJSONString()).build();
    }

    @POST
    @Path("id/{userId")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editUser(@Context HttpServletRequest requestContext) {
        JSONObject response = new JSONObject();
        Optional<User> user = Optional.ofNullable((User) requestContext.getSession().getAttribute("user"));
        User user1;
        if (!user.isPresent()) {
            return Response.status(401).build();
        }
        try {
            JSONObject request = (JSONObject) new JSONParser().parse(Common.getRequestBody(requestContext));
            user1 = new User(
                    ((Long) request.get("id")),
                    ((String) request.get("login")),
                    ((String) request.get("name")),
                    ((String) request.get("passwd")),
                    user.get().getRole());
        } catch (ParseException e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
        if (!DAOFactory.getUserDAO().updateUser(user1)) {
            return Response.status(500).build();
        }
        return Response.ok().build();


    }
}
