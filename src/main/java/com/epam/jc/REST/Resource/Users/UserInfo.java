package com.epam.jc.REST.Resource.Users;

import com.epam.jc.DbController.DAOFactory;
import com.epam.jc.DbController.Entities.User;
import com.epam.jc.REST.Security.LoginDispatcher;
import org.json.simple.JSONObject;

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

//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
//    public String getUser(@Context HttpServletRequest request) {
//        return request.getSession().getId();
//    }
    @GET
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
    public Response getUserById(@Context HttpServletRequest requestContext, @PathParam("userId") String userId) {
        HttpSession session = requestContext.getSession();
        JSONObject result = new JSONObject();
        Long id;
        try {
            id = Long.parseLong(userId);
        }
        catch (NumberFormatException e) {
            id = 0L;
        }
        if ((id==0) || !LoginDispatcher.getInstance().isUserInRole(session, "admin")) {
            result.put("error", 403);
            return Response.ok().entity(result.toJSONString()).build();
        }
        User user = DAOFactory.getUserDAO().getUser(id);
        result.put("id", user.getId());
        result.put("login", user.getLogin());
        result.put("name", user.getName());
        result.put("role", user.getRole());
        return Response.ok().entity(result.toJSONString()).build();
    }
}
