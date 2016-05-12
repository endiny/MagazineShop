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
public class SessionCheck {

    @Path("alive")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response isAlive(@Context HttpServletRequest request) {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("alive", Optional.ofNullable(request.getSession().getAttribute("user")).isPresent());
        return Response.ok().entity(jsonResponse.toJSONString()).build();
    }
}
