package com.epam.jc.REST.Resources.Users;

import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

/**
 * Created on 12/05/16.
 *
 * @author Vladislav Boboshko
 */
@Path("alive")
public class SessionCheck {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response isAlive(@Context HttpServletRequest request) {
        JSONObject jsonResponse = new JSONObject();
        boolean result = Optional.ofNullable(request.getSession().getAttribute("user")).isPresent();
        jsonResponse.put("alive", result);
        return Response.status(result?200:401).entity(jsonResponse.toJSONString()).build();
    }
}
