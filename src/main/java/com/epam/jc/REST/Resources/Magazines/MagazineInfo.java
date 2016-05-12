package com.epam.jc.REST.Resources.Magazines;

import com.epam.jc.DbController.DAOFactory;
import com.epam.jc.DbController.Entities.Magazine;
import com.epam.jc.DbController.Entities.User;
import com.epam.jc.REST.Security.LoginDispatcher;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

/**
 * Created on 15/04/16.
 *
 * @author Vladislav Boboshko
 */

@Path("magazine")
public class MagazineInfo {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMagazines(@Context HttpServletRequest requestContext) {
        Optional<User> user = Optional.ofNullable((User) requestContext.getSession().getAttribute("user"));
        if (!user.isPresent()) {
            return Response.status(401).build();
        }
        List<Magazine> magazines = DAOFactory.getMagazineDAO().getAllMagazines();
        JSONObject magazinesJSON = new JSONObject();
        JSONArray arr = new JSONArray();
        magazinesJSON.put("amount", magazines.size());
        for (Magazine i: magazines) {
            JSONObject magazine = new JSONObject();
            magazine.put("id",i.getId());
            magazine.put("name", i.getName());
            magazine.put("description", i.getDescription());
            magazine.put("price", i.getPrice());
            arr.add(magazine);
        }
        magazinesJSON.put("magazines", arr);
        return Response.ok().entity(magazinesJSON.toJSONString()).build();
    }

    @GET
    @Path("id/{magazineId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMagazine(@Context HttpServletRequest requestContext, @PathParam("magazineId") String sId) {
        Optional<User> user = Optional.ofNullable((User) requestContext.getSession().getAttribute("user"));
        if (!user.isPresent()) {
            return Response.status(401).build();
        }
        Long id;
        try {
            id = Long.parseLong(sId);
            if (id == 0) throw new NumberFormatException();
        }
        catch (NumberFormatException e) {
            return Response.status(400).entity("{\"result\":\"bad request\"").build();
        }
        Magazine magazine = DAOFactory.getMagazineDAO().getMagazine(id).get();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", magazine.getId());
        jsonObject.put("name", magazine.getName());
        jsonObject.put("description", magazine.getDescription());
        jsonObject.put("price", magazine.getPrice());
        return Response.ok().entity(jsonObject.toJSONString()).build();
    }

    @GET
    @Path("id/{magazineId}/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMagazine(@Context HttpServletRequest requestContext, @PathParam("magazineId") String sId) {
        if (!LoginDispatcher.getInstance().isUserInRole(requestContext.getSession(), "admin")) {
            return Response.status(403).entity("Forbidden!").build();
        }
        JSONObject jsonResponse = new JSONObject();
        Long id;
        try {
            id = Long.parseLong(sId);
            if (id == 0) throw new NumberFormatException();
        }
        catch (NumberFormatException e) {
            return Response.status(400).build();
        }
        jsonResponse.put("result", DAOFactory.getMagazineDAO().deleteMagazine(id));
        return Response.ok().entity(jsonResponse.toJSONString()).build();
    }
}
