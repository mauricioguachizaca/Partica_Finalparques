package com.parques;


import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import controller.Dao.servicies.ParquesServices;
@Path("/parques")
public class ParquesApi {
    
    @Path("/lista")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllParques() {
        HashMap<String, Object> map = new HashMap<>();
        ParquesServices ps = new ParquesServices();
        map.put("msg", "Lista de parques");
        map.put("data", ps.listAll().toArray());
        if (ps.listAll().isEmpty()) {
            map.put("data", new Object[]{});
        }
        return Response.ok(map).build();		
    }


    @Path("/guardar")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(HashMap<String, Object> map) {
        HashMap<String, Object> res = new HashMap<>();
        try {
            ParquesServices ps = new ParquesServices();
            ps.getParques().setNombre(map.get("nombre").toString());
            ps.getParques().setdescripcion(map.get("descripcion").toString());
            //es un double
            ps.getParques().setLongitud(Double.parseDouble(map.get("longitud").toString()));
            ps.getParques().setLatitud(Double.parseDouble(map.get("latitud").toString()));
            ps.save();

            res.put("msg", "Ok");
            res.put("data", "guardado exitoso");
            return Response.ok(res).build();

        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();

        }
    }

    @Path("/lista/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getParques(@PathParam("id") Integer id) {
        HashMap<String, Object> map = new HashMap<>();
        ParquesServices ps = new ParquesServices();
        try {
            ps.setParques(ps.get(id));
        } catch (Exception e) {
            
        }
        map.put("msg", "parque");
        map.put("data", ps.getParques());
        if(ps.getParques().getidParques() == null){
            map.put("data", "no exiten datos");
            return Response.status(Response.Status.NOT_FOUND).entity(map).build();
        }
        return Response.ok(map).build();
       }

    @Path("/actualizar")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(HashMap<String, Object> map) {
        HashMap<String, Object> res = new HashMap<>();
        try {
            ParquesServices ps = new ParquesServices();
            ps.getParques().setidParques(Integer.parseInt(map.get("id").toString()));
            ps.getParques().setNombre(map.get("nombre").toString());
            ps.getParques().setdescripcion(map.get("descripcion").toString());
            ps.getParques().setLongitud(Double.parseDouble(map.get("longitud").toString()));
            ps.getParques().setLatitud(Double.parseDouble(map.get("latitud").toString()));
            ps.update();
            res.put("msg", "Ok");
            res.put("data", "Actualizado exitoso");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }


    @Path("/eliminar/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Integer id) {
        HashMap<String, Object> res = new HashMap<>();
        try {
            ParquesServices ps = new ParquesServices();
            ps.setParques(ps.get(id));
            ps.delete(id);
            res.put("msg", "Ok");
            res.put("data", "Eliminado exitoso");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }
}
