package controller.tda.graph;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

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
        System.out.println("Modelo cargado: " + parque.getidParques() + " - " + parque.getNombre() + " - " + parque.getLatitud() + " - " + parque.getLongitud());
    }
}


    // Método para cargar el grafo desde un archivo JSON
    public void loadGraph(String filename) throws Exception {
        System.out.println("Cargando archivo: " + filename);
    
        try (FileReader fileReader = new FileReader(filePath + filename)) {
            Gson gson = new Gson();
            JsonArray graphArray = gson.fromJson(fileReader, JsonArray.class);
            System.out.println("Contenido del archivo cargado.");
    
            // Iterar sobre los vértices en el grafo
            for (JsonElement vertexElement : graphArray) {
                JsonObject vertexObject = vertexElement.getAsJsonObject();
                System.out.println("Procesando vértice: " + vertexObject);
    
                // Obtener el ID del vértice
                Integer labelId = vertexObject.get("labelId").getAsInt();
                System.out.println("ID del vértice: " + labelId);
    
                // Obtener el modelo ya existente en lugar de crear uno nuevo
                Parques model = vertexModels.get(labelId);
    
                if (model == null) {
                    System.out.println("Error: No se encontró un modelo existente para el vértice " + labelId);
                    continue; // Si no existe, pasar al siguiente vértice
                }
    
                // Asociar el modelo al vértice
                this.addVertexWithModel(labelId, model);
                System.out.println("Modelo asociado al vértice: " + labelId);
    
                // Obtener el array de destinos
                JsonArray destinationsArray = vertexObject.getAsJsonArray("destinations");
                System.out.println("Destinos encontrados para el vértice " + labelId + ": " + destinationsArray.size());
    
                for (JsonElement destinationElement : destinationsArray) {
                    JsonObject destinationObject = destinationElement.getAsJsonObject();
                    System.out.println("Procesando destino: " + destinationObject);
    
                    // Obtener los valores "from", "to"
                    Integer from = destinationObject.get("from").getAsInt();
                    Integer to = destinationObject.get("to").getAsInt();
                    System.out.println("Desde: " + from + " hasta: " + to);
    
                    // Obtener los modelos correspondientes a los vértices 'from' y 'to'
                    Parques modelFrom = vertexModels.get(from);
                    Parques modelTo = vertexModels.get(to);
    
                    if (modelFrom == null || modelTo == null) {
                        System.out.println("Error: no se encontraron los modelos para los vértices: " + from + " y " + to);
                    } else {
                        System.out.println("Modelos obtenidos: " + modelFrom + " y " + modelTo);
    
                        // Calcular la distancia utilizando la función 'calcularDistancia'
                        Float weight = (float) calcularDistancia(modelFrom, modelTo);
                        System.out.println("Distancia calculada entre " + from + " y " + to + ": " + weight);
    
                        // Agregar la arista solo con "from" y "to" (sin el peso en el JSON)
                        this.add_edge(from, to, weight); // Aquí el peso se calcula y se usa internamente, pero no se guarda en el JSON.
                        System.out.println("Arista añadida de " + from + " a " + to + " con peso calculado: " + weight);
                    }
                }
            }
            System.out.println("Cargando grafo completado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    

    // Método para agregar un vértice con su modelo asociado
    public void addVertexWithModel(Integer vertexId, Parques model) {
        vertexModels.put(vertexId, model);  // Asociar el vértice con su modelo
    }

    // Método para verificar si un archivo existe en la ruta especificada
    public boolean existsFile(String filename) {
        File file = new File(filePath + filename);
        return file.exists();
    }

    // Método para calcular la distancia entre dos parques
    public static double calcularDistancia(Parques parque1, Parques parque2) {
        // Verificar que las coordenadas no sean nulas
        if (parque1.getLatitud() == null || parque1.getLongitud() == null || 
            parque2.getLatitud() == null || parque2.getLongitud() == null) {
            System.err.println("Error: Una o más coordenadas son nulas.");
            return Double.NaN;  // Retorna NaN si alguna coordenada es nula
        }
    
        // Imprimir las coordenadas de los parques para depuración
        System.out.println("Coordenadas de Parque 1: Lat: " + parque1.getLatitud() + " Long: " + parque1.getLongitud());
        System.out.println("Coordenadas de Parque 2: Lat: " + parque2.getLatitud() + " Long: " + parque2.getLongitud());
    
        // Convertir las coordenadas de grados a radianes
        double lat1 = toRadians(parque1.getLatitud().doubleValue());
        double lon1 = toRadians(parque1.getLongitud().doubleValue());
        double lat2 = toRadians(parque2.getLatitud().doubleValue());
        double lon2 = toRadians(parque2.getLongitud().doubleValue());
    
        // Fórmula de Haversine
        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    
        // Radio de la Tierra en metros
        final double R = 6371000.0; // Radio de la Tierra en metros
        double distancia = R * c;
    
        // Redondear la distancia a 2 decimales y devolver
        return Math.round(distancia * 100.0) / 100.0;
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
