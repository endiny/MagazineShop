package com.epam.jc.REST.Resource.Users;

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
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

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
        StringBuffer income = new StringBuffer();
        try(ServletInputStream br = requestContext.getInputStream()) {
            Character symbol;
            while ((symbol = (char)br.read()) != 65535) {
                income.append(symbol);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject response = new JSONObject();
        JSONParser parser = new JSONParser();
        HttpSession session = requestContext.getSession();
        int code = 200;
        try {
            JSONObject JSONRequest = (JSONObject) parser.parse(income.toString());
            String login = (String) JSONRequest.get("login");
            String passwd = (String) JSONRequest.get("password");
            boolean logon = LoginDispatcher.getInstance().authorize(login, passwd, session);
            response.put("result", logon);
            code = logon?200:401;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Response.status(code).entity(response.toJSONString()).build();
    }

}
