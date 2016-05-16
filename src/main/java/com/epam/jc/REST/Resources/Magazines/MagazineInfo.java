package com.epam.jc.REST.Resources.Magazines;

import com.epam.jc.Common.RequestService;
import com.epam.jc.DbController.DAOFactory;
import com.epam.jc.DbController.Entities.Magazine;
import com.epam.jc.DbController.Entities.User;
import com.epam.jc.REST.Security.LoginDispatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
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
//// TODO: 16/05/16 magazineAPI
    private static final Logger logger = LogManager.getLogger(MagazineInfo.class);
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
        logger.debug("Got all magazines");
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
            return Response.status(403).entity("status:forbidden").build();
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("add")
    public Response deleteMagazine(@Context HttpServletRequest requestContext) {
        if (!LoginDispatcher.getInstance().isUserInRole(requestContext.getSession(), "admin")) {
            return Response.status(403).entity("status:forbidden").build();
        }
        JSONObject jsonResponse = new JSONObject();
        try {
            JSONObject jsonRequest = (JSONObject) new JSONParser().parse(RequestService.getRequestBody(requestContext));
            String name = new String(((String) jsonRequest.get("name")).getBytes("ISO-8859-1"), "UTF-8");
            String description = new String(((String) jsonRequest.get("description")).getBytes("ISO-8859-1"), "UTF-8");
            Double price = Double.valueOf(((String) jsonRequest.get("price")));
            Magazine magazine = new Magazine(name, price, description);
            boolean result = DAOFactory.getMagazineDAO().addMagazine(magazine);
            jsonResponse.put("result", result);
            if (result) {
                logger.debug("Magazine was added");
                return Response.ok().entity(jsonResponse.toJSONString()).build();
            } else {
                throw new Exception("Unable to add magazine");
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return Response.status(400).entity(jsonResponse.toJSONString()).build();
        }
    }
}
