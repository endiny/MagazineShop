package com.epam.jc.REST.Resources.Users;

import com.epam.jc.DbController.DAOFactory;
import com.epam.jc.DbController.Entities.User;
import com.epam.jc.Common.RequestService;
import com.epam.jc.REST.Security.LoginDispatcher;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
        Long id = optionalUser.orElse(new User(0L, "", "", "", "")).getId();
        User user = DAOFactory.getUserDAO().getUser(id).get();
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
        User user = DAOFactory.getUserDAO().getUser(id).get();
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
    @Path("edit")
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
            JSONObject request = (JSONObject) new JSONParser().parse(RequestService.getRequestBody(requestContext));
            Optional<User> editUser = DAOFactory.getUserDAO().getUser(user.get().getId());
            if (!editUser.isPresent()) {
                throw new Exception("User not found");
            }
            String name = new String(((String) request.get("name")).getBytes("ISO-8859-1"), "UTF-8");
            String password = new String(((String) request.get("password")).getBytes("ISO-8859-1"), "UTF-8");
            editUser.get().setName(name);
            if (!password.equals("none")) {
                editUser.get().setPasswd(password);
            }
            boolean result = DAOFactory.getUserDAO().updateUser(editUser.get());
            response.put("update", result);
            return Response.status(result?200:400).entity(response.toJSONString()).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
    }
}
