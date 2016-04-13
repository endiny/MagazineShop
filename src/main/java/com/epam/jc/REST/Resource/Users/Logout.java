package com.epam.jc.REST.Resource.Users;

import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * Created on 14/04/16.
 *
 * @author Vladislav Boboshko
 */

@Path("logout")
public class Logout {

    @GET
    public Response logout(@Context HttpServletRequest requestContext) {
        HttpSession session = requestContext.getSession();
        session.invalidate();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "who cares");
        return Response.ok().entity(jsonObject.toJSONString()).build();
    }
}
