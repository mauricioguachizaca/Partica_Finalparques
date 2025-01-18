package controller.tda.graph;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import controller.tda.list.LinkedList;

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

    // Método getVertex
    public Integer getVertex(Integer label) throws Exception {
        return label;
    }

    @Override
    public String toString() {
        String grafo = "";
        try {
            for (int i = 1; i <= this.nro_vertices(); i++) {
                grafo += "Vertice: " + i + "\n";
                LinkedList<Adyecencia> lista = this.adyecencias(i);
                if (!lista.isEmpty()) {
                    Adyecencia[] ady = lista.toArray();
                    for (int j = 0; j < ady.length; j++) {
                        Adyecencia a = ady[j];
                        grafo += "ady: " + "V" + a.getdestination() + " weight: " + a.getweight() + "\n";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grafo;
    }

    // Método para guardar el grafo en formato JSON en la ruta especificada
    public void saveGraphLabel(String filename) throws  Exception {
        JsonArray graphArray = new JsonArray();
        for (int i = 1; i <= this.nro_vertices(); i++) {
            JsonObject vertexObject = new JsonObject();
            try {
                vertexObject.addProperty("labelId", this.getVertex(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    // Método para cargar el grafo desde un archivo JSON en la ruta especificada
    public void loadGraph(String filename) throws Exception {
        FileReader fileReader = new FileReader(filePath + filename);
        Gson gson = new Gson();
        JsonArray graphArray = gson.fromJson(fileReader, JsonArray.class);
        fileReader.close();

        for (JsonElement vertexElement : graphArray) {
            JsonObject vertexObject = vertexElement.getAsJsonObject();
            Integer labelId = vertexObject.get("labelId").getAsInt();
            JsonArray destinationsArray = vertexObject.getAsJsonArray("destinations");

            for (JsonElement destinationElement : destinationsArray) {
                JsonObject destinationObject = destinationElement.getAsJsonObject();
                Integer from = destinationObject.get("from").getAsInt();
                Integer to = destinationObject.get("to").getAsInt();
                Float weight = destinationObject.get("weight").getAsFloat();

                this.add_edge(from, to, weight);  // Insertar la arista con peso
            }
        }
    }

    // Método para verificar si el archivo existe en la ruta especificada
    public boolean existsFile(String filename) {
        File file = new File(filePath + filename);
        return file.exists();
    }
}
