package controller.tda.graph;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import controller.Dao.ParquesDao;
import controller.tda.list.LinkedList;
import controller.tda.list.ListEmptyException;
import models.Parques;

import static java.lang.Math.*;

public abstract class Graph {

    // Ruta para guardar el archivo
    public static String filePath = "data/";

    private Map<Integer, Parques> vertexModels = new HashMap<>();

    // Métodos abstractos
    public abstract Integer nro_vertices();
    public abstract Integer nro_edges();
    public abstract Boolean is_edges(Integer v1, Integer v2) throws Exception;
    public abstract Float wieght_edge(Integer v1, Integer v2) throws Exception;
    public abstract void add_edge(Integer v1, Integer v2) throws Exception;
    public abstract void add_edge(Integer v1, Integer v2, Float weight) throws Exception;
    public abstract LinkedList<Adyecencia> adyecencias(Integer v1);

    // Método para obtener el ID de un vértice
    public Integer getVertex(Integer label) throws Exception {
        return label;
    }

    @Override
    public String toString() {
        StringBuilder grafo = new StringBuilder();
        try {
            for (int i = 1; i <= this.nro_vertices(); i++) {
                grafo.append("Vertice: ").append(i).append("\n");
                LinkedList<Adyecencia> lista = this.adyecencias(i);
                if (!lista.isEmpty()) {
                    Adyecencia[] ady = lista.toArray();
                    for (Adyecencia a : ady) {
                        grafo.append("ady: V").append(a.getdestination())
                             .append(" weight: ").append(a.getweight()).append("\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grafo.toString();
    }

    // Método para guardar el grafo en formato JSON
    public void saveGraphLabel(String filename) throws Exception {
        JsonArray graphArray = new JsonArray();
        for (int i = 1; i <= this.nro_vertices(); i++) {
            JsonObject vertexObject = new JsonObject();
            vertexObject.addProperty("labelId", this.getVertex(i));

            JsonArray destinationsArray = new JsonArray();
            LinkedList<Adyecencia> adyacencias = this.adyecencias(i);
            if (!adyacencias.isEmpty()) {
                for (int j = 0; j < adyacencias.getSize(); j++) {
                    Adyecencia adj = adyacencias.get(j);
                    JsonObject destinationObject = new JsonObject();
                    destinationObject.addProperty("from", this.getVertex(i));
                    destinationObject.addProperty("to", adj.getdestination());
                    destinationsArray.add(destinationObject);
                }
            }
            vertexObject.add("destinations", destinationsArray);
            graphArray.add(vertexObject);
        }

        Gson gson = new Gson();
        String json = gson.toJson(graphArray);

        // Crear el directorio si no existe
        File directory = new File(filePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Escribir el archivo JSON
        try (FileWriter fileWriter = new FileWriter(filePath + filename)) {
            fileWriter.write(json);
        }
    }

    public void cargarModelosDesdeDao() throws  ListEmptyException {
    ParquesDao parquesDao = new ParquesDao();
    LinkedList<Parques> parquesList = parquesDao.getListAll();

    for (int i = 0; i < parquesList.getSize(); i++) {
        Parques parque = parquesList.get(i);
        vertexModels.put(parque.getidParques(), parque);
    }
}


    // Método para cargar el grafo desde un archivo JSON
    public void loadGraph(String filename) throws Exception {
    
        try (FileReader fileReader = new FileReader(filePath + filename)) {
            Gson gson = new Gson();
            JsonArray graphArray = gson.fromJson(fileReader, JsonArray.class);
    
            for (JsonElement vertexElement : graphArray) {
                JsonObject vertexObject = vertexElement.getAsJsonObject();
    
                Integer labelId = vertexObject.get("labelId").getAsInt();
    
                Parques model = vertexModels.get(labelId);
    
                if (model == null) {
                    continue; 
                }
    
                this.addVertexWithModel(labelId, model);
    
                JsonArray destinationsArray = vertexObject.getAsJsonArray("destinations");
    
                for (JsonElement destinationElement : destinationsArray) {
                    JsonObject destinationObject = destinationElement.getAsJsonObject();
    
                    Integer from = destinationObject.get("from").getAsInt();
                    Integer to = destinationObject.get("to").getAsInt();
    
                    // Obtener los modelos correspondientes a los vértices 'from' y 'to'
                    Parques modelFrom = vertexModels.get(from);
                    Parques modelTo = vertexModels.get(to);
    
                    if (modelFrom == null || modelTo == null) {
                    } else {
    
                        Float weight = (float) calcularDistancia(modelFrom, modelTo);
    
                        // Agregar la arista solo con "from" y "to" (sin el peso en el JSON)
                        this.add_edge(from, to, weight); // Aquí el peso se calcula y se usa internamente, pero no se guarda en el JSON.
                        System.out.println("Arista añadida de " + from + " a " + to + " con peso calculado: " + weight);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para borrar todas las adyacencias de todos los vértices
    public void clearEdges() {
      for (int i = 1; i <= this.nro_vertices(); i++) {
        this.adyecencias(i).reset();  
       }
    }
//crea las adyacencias aleatorias
    public void loadGraphWithRandomEdges(String filename) throws Exception {
    loadGraph(filename);
    cargarModelosDesdeDao(); 
    clearEdges();  
    Random random = new Random();
    for (int i = 1; i <= this.nro_vertices(); i++) {
        LinkedList<Adyecencia> existingEdges = this.adyecencias(i);
        int connectionsCount = existingEdges.getSize();
        
        // Aseguramos que cada vértice tenga al menos 3 conexiones
        while (connectionsCount < 3) {
            // Generamos un vértice aleatorio diferente al actual
            int randomVertex = random.nextInt(this.nro_vertices()) + 1;
            while (randomVertex == i || is_edges(i, randomVertex)) {
                randomVertex = random.nextInt(this.nro_vertices()) + 1;
            }

            // Obtenemos los modelos de los parques correspondientes a los vértices
            Parques modelFrom = vertexModels.get(i);
            Parques modelTo = vertexModels.get(randomVertex);

            // Calculamos un peso aleatorio (o puedes usar el método calcularDistancia si ya tienes coordenadas)
            float weight = (float) calcularDistancia(modelFrom, modelTo);  // Peso aleatorio entre 0 y 100
            add_edge(i, randomVertex, weight);  // Agregar la arista
            connectionsCount++;
        }
    }    
    saveGraphLabel(filename);  
}

    // Método para agregar un vértice con su modelo asociado
    public void addVertexWithModel(Integer vertexId, Parques model) {
        vertexModels.put(vertexId, model);  
    }

    // Método para verificar si un archivo existe en la ruta especificada
    public boolean existsFile(String filename) {
        File file = new File(filePath + filename);
        return file.exists();
    }

    public static double calcularDistancia(Parques parque1, Parques parque2) {
        if (parque1.getLatitud() == null || parque1.getLongitud() == null || 
            parque2.getLatitud() == null || parque2.getLongitud() == null) {
            return Double.NaN; 
        }
        double lat1 = toRadians(parque1.getLatitud().doubleValue());
        double lon1 = toRadians(parque1.getLongitud().doubleValue());
        double lat2 = toRadians(parque2.getLatitud().doubleValue());
        double lon2 = toRadians(parque2.getLongitud().doubleValue());
    
        // Fórmula de Haversine
        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    
        final double R = 6371000.0; 
        double distancia = R * c;
    
        return Math.round((distancia / 1000) * 100.0) / 100.0;
    }
    



    // Método principal para guardar el grafo con el nombre "grafo.json"
    public void guardarGrafo() {
        try {
            // Asigna el nombre del archivo como "grafo.json"
            String filename = "grafo.json";  // El nombre que deseas para el archivo
            saveGraphLabel(filename);        // Llamas a tu método con el nombre del archivo
        } catch (Exception e) {
            e.printStackTrace();  // En caso de error, imprime la traza del error
        }
    }

    // Método para obtener los datos del grafo en formato Vis.js
public JsonObject getVisGraphData() throws Exception {
    JsonObject visGraph = new JsonObject();

    // Arrays para los nodos y las aristas
    JsonArray nodes = new JsonArray();
    JsonArray edges = new JsonArray();
    

    // Iteramos sobre los vértices
    for (int i = 1; i <= this.nro_vertices(); i++) {
        JsonObject node = new JsonObject();
        Parques model = vertexModels.get(i);
        if (model != null) {
            node.addProperty("name", model.getNombre());  // Nombre del vértice
        }
        node.addProperty("id", i);  // ID del nodo
        node.addProperty("label", "V" + i);  // Etiqueta del nodo (puedes personalizarlo)
        
        // Opcional: Agregar un color o más propiedades si lo deseas
        node.addProperty("color", "#ff0000");  // Un color de ejemplo, puedes personalizarlo
        nodes.add(node);

        // Obtener las adyacencias de este vértice
        LinkedList<Adyecencia> adyacencias = this.adyecencias(i);
        if (!adyacencias.isEmpty()) {
            for (int j = 0; j < adyacencias.getSize(); j++) {
                Adyecencia adj = adyacencias.get(j);
                JsonObject edge = new JsonObject();
                edge.addProperty("from", i);  // Nodo origen
                edge.addProperty("to", adj.getdestination());  // Nodo destino
                edge.addProperty("weight", adj.getweight());
                 
                 // Peso de la arista

                // Opcional: Puedes agregar más propiedades a la arista si lo deseas
                edge.addProperty("color", "#7CFC00");  // Color de la arista (personalizable)
                edges.add(edge);
            }
        }
    }

    // Añadir nodos y aristas al objeto principal
    visGraph.add("nodes", nodes);
    visGraph.add("edges", edges);
    
    return visGraph;
}

    
}
