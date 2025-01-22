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

import com.google.gson.JsonArray;

import controller.Dao.servicies.ParquesServices;
import controller.tda.graph.graphlablenodirect;
import controller.tda.list.LinkedList;
import models.Parques;
import controller.Dao.ParquesDao;
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
    @Path("/grafo_ver_admin")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response grafoVerAdmin() {
        HashMap<String, Object> res = new HashMap<>();
        try {
            // Crear instancia de DAO
            ParquesDao parquesDao = new ParquesDao();
            
            // Obtener lista de parques
            LinkedList<Parques> listaParques = parquesDao.getListAll();
                    
            // Crear y obtener el grafo
            parquesDao.creategraph();
            
            // Guardar el grafo
            parquesDao.saveGraph(); // Asegúrate de que este método se llame para guardar el grafo
            
            // Obtener pesos del grafo
            JsonArray pesos = parquesDao.obtainWeights();
            
            // Construir respuesta
            res.put("msg", "Grafo generado exitosamente");
            res.put("lista", listaParques.toArray());
            res.put("pesos", pesos);
    
            return Response.ok(res).build();
    
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }
@Path("/union")
@GET
@Produces(MediaType.APPLICATION_JSON)
public Response unionesgrafos() {
    HashMap<String, Object> res = new HashMap<>();
    try {
        ParquesDao parquesDao = new ParquesDao();

        // Obtener el grafo antes de la modificación
        graphlablenodirect<String> graph = parquesDao.obtenerGrafo();
        System.out.println("Estado inicial del grafo:");
        System.out.println(graph.toString());

        // Intentar agregar la arista
        try {
        } catch (Exception e) {
            throw e;
        }

        // Guardar cambios en el grafo
        parquesDao.saveGraph();

        // Verificar el estado final
        System.out.println("Estado final del grafo:");
        System.out.println(graph.toString());

        res.put("msg", "Grafo actualizado exitosamente");
        return Response.ok(res).build();
    
    } catch (Exception e) {
        res.put("msg", "Error");
        res.put("data", e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
    }
}


@Path("/camino_corto/{origen}/{destino}/{algoritmo}")
@GET
@Produces(MediaType.APPLICATION_JSON)
public Response calcularCaminoCorto(@PathParam("origen") int origen, 
                                     @PathParam("destino") int destino, 
                                     @PathParam("algoritmo") int algoritmo) {
    HashMap<String, Object> res = new HashMap<>();
    try {
        // Crear instancia de ParquesDao
        ParquesDao parquesDao = new ParquesDao();
        
        // Obtener el grafo
        graphlablenodirect<String> graph = parquesDao.obtenerGrafo();
        
        // Llamar al método caminoCorto
        String resultado = parquesDao.caminoCorto(origen, destino, algoritmo);
        
        // Construir la respuesta
        res.put("msg", "Camino corto calculado exitosamente");
        res.put("resultado", resultado);
        
        return Response.ok(res).build();
    } catch (Exception e) {
        res.put("msg", "Error");
        res.put("data", e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
    }
}




}
