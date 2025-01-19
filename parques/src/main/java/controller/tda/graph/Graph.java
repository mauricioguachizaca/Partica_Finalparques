package controller.tda.graph;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import controller.tda.list.LinkedList;
import models.Parques;

import static java.lang.Math.*;

public abstract class Graph {

    // Ruta para guardar el archivo
    public static String filePath = "data/";

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
                    destinationObject.addProperty("weight", adj.getweight());
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

        FileWriter fileWriter = new FileWriter(filePath + filename);
        fileWriter.write(json);
        fileWriter.close();
    }

    // Método para cargar el grafo desde un archivo JSON
    public void loadGraph(String filename) throws Exception {
        FileReader fileReader = new FileReader(filePath + filename);
        Gson gson = new Gson();
        JsonArray graphArray = gson.fromJson(fileReader, JsonArray.class);
        fileReader.close();
        
        for (JsonElement vertexElement : graphArray) {
            JsonObject vertexObject = vertexElement.getAsJsonObject();
            
            // Obtener el ID del vértice
            Integer labelId = vertexObject.get("labelId").getAsInt();
            
            // Obtener el array de destinos
            JsonArray destinationsArray = vertexObject.getAsJsonArray("destinations");
        
            for (JsonElement destinationElement : destinationsArray) {
                JsonObject destinationObject = destinationElement.getAsJsonObject();
                
                // Verificar que los valores "from", "to" y "weight" sean del tipo esperado
                JsonElement fromElement = destinationObject.get("from");
                JsonElement toElement = destinationObject.get("to");
                JsonElement weightElement = destinationObject.get("weight");
                
                // Verificar que "from" y "to" sean números enteros
                Integer from = fromElement.isJsonPrimitive() && fromElement.getAsJsonPrimitive().isNumber() ? fromElement.getAsInt() : null;
                Integer to = toElement.isJsonPrimitive() && toElement.getAsJsonPrimitive().isNumber() ? toElement.getAsInt() : null;
                
                // Verificar que "weight" esté presente, si no está, asignar 0.0f como valor predeterminado
                Float weight = 0.0f; // Peso predeterminado en caso de no estar presente
                if (weightElement != null && weightElement.isJsonPrimitive() && weightElement.getAsJsonPrimitive().isNumber()) {
                    weight = weightElement.getAsFloat(); // Convertir a float solo si es un número
                }
                
                // Si los valores son válidos, agregar la arista
                if (from != null && to != null) {
                    this.add_edge(from, to, weight); // Agregar la arista con el peso
                } else {
                    System.err.println("Invalid data for edge: from = " + from + ", to = " + to + ", weight = " + weight);
                }
            }
        }
    }

    // Método para verificar si un archivo existe en la ruta especificada
    public boolean existsFile(String filename) {
        File file = new File(filePath + filename);
        return file.exists();
    }

    // Método para calcular la distancia entre dos parques
    public static double calcularDistancia(Parques parque1, Parques parque2) {
        double lat1 = toRadians(parque1.getLatitud().doubleValue());
        double lon1 = toRadians(parque1.getLongitud().doubleValue());
        double lat2 = toRadians(parque2.getLatitud().doubleValue());
        double lon2 = toRadians(parque2.getLongitud().doubleValue());

        // Fórmula de Haversine
        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;
        double a = pow(sin(dlat / 2), 2) + cos(lat1) * cos(lat2) * pow(sin(dlon / 2), 2);
        double c = 2 * atan2(sqrt(a), sqrt(1 - a));
        final double R = 6371.0; // Earth's radius in kilometers
        double distancia = R * c;

        return Math.round(distancia * 100.0) / 100.0; // Redondear a 2 decimales
    }

    // Método para obtener los pesos de las aristas
    public JsonArray obtainWeights() throws Exception {
        JsonArray result = new JsonArray();
        
        // Iterar sobre todos los vértices del grafo
        for (int i = 1; i <= this.nro_vertices(); i++) {
            JsonObject vertexInfo = new JsonObject();
            vertexInfo.addProperty("labelId", this.getVertex(i)); // ID del vértice actual

            JsonArray destinations = new JsonArray(); // Lista de conexiones para el vértice
            LinkedList<Adyecencia> adyacencias = this.adyecencias(i);

            if (!adyacencias.isEmpty()) {
                for (int j = 0; j < adyacencias.getSize(); j++) {
                    Adyecencia adj = adyacencias.get(j);
                    JsonObject destinationInfo = new JsonObject();
                    destinationInfo.addProperty("from", this.getVertex(i)); // Desde el vértice actual
                    destinationInfo.addProperty("to", adj.getdestination()); // Al destino
                    destinationInfo.addProperty("weight", adj.getweight()); // Peso de la arista
                    destinations.add(destinationInfo);
                }
            }

            vertexInfo.add("destinations", destinations); // Agregar las conexiones al vértice
            result.add(vertexInfo); // Agregar la información del vértice al resultado
        }

        return result;
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
}
