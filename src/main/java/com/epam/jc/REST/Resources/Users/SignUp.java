package com.epam.jc.REST.Resources.Users;

import com.epam.jc.DbController.DAOFactory;
import com.epam.jc.DbController.Entities.User;
import com.epam.jc.REST.Common;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created on 14/04/16.
 *
 * @author Vladislav Boboshko
 */

@Path("signup")
public class SignUp {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signUp(@Context HttpServletRequest requestContext) {
        JSONObject response = new JSONObject();
        User user;
        try {
            JSONObject request = ((JSONObject) new JSONParser().parse(Common.getRequestBody(requestContext)));
            user = new User(new String(((String) request.get("login")).getBytes("ISO-8859-1"), "UTF-8"),
                    new String(((String) request.get("name")).getBytes("ISO-8859-1"), "UTF-8"),
                    new String(((String) request.get("password")).getBytes("ISO-8859-1"), "UTF-8"),
                    "user");
        } catch (Exception e) {
            System.err.println("Unable to parse request");
            response.put("status", "Wrong request.");
            return Response.status(400).entity(response.toJSONString()).build();
        }
        if (!DAOFactory.getUserDAO().addUser(user)) {
            response.put("status", "User exists or something unknown");
            return Response.status(400).entity(response.toJSONString()).build();
        }
        response.put("status", "User " + user.getLogin() + " has been created");
        return Response.ok().entity(response.toJSONString()).build();
    }
}
