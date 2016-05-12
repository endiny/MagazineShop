package com.epam.jc.REST.Resources.Users;

import com.epam.jc.DbController.Entities.User;
import com.epam.jc.REST.Common;
import com.epam.jc.REST.Security.LoginDispatcher;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Optional;

/**
 * Created on 06.04.16.
 *
 * @author Vladislav Boboshko
 */
@Path("login")
public class Login {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginHandler(@Context HttpServletRequest requestContext) {
        JSONObject response = new JSONObject();
        JSONParser parser = new JSONParser();
        HttpSession session = requestContext.getSession();
        int code = 400;
        try {
            JSONObject JSONRequest = (JSONObject) parser.parse(Common.getRequestBody(requestContext));
            String login = (String) JSONRequest.get("login");
            String passwd = (String) JSONRequest.get("password");
            Optional<User> logon = LoginDispatcher.getInstance().authorize(login, passwd, session);
            logon.ifPresent(user -> {
                response.put("id", user.getId());
                response.put("login", user.getLogin());
                response.put("name", user.getName());
                response.put("role", user.getRole());
            });
            code = (logon.isPresent())?200:401;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Response.status(code).entity(response.toJSONString()).build();
    }
}