package controller.Dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import controller.Dao.implement.AdapterDao;
import controller.tda.graph.graphlablenodirect;
import controller.tda.graph.algoritmos.BFS;
import controller.tda.graph.algoritmos.BellmanFord;
import controller.tda.graph.algoritmos.Floyd;
import controller.tda.list.LinkedList;
import models.Parques;

public class ParquesDao extends AdapterDao<Parques> {

    private Parques parques;
    private LinkedList<Parques> listAll;
    private graphlablenodirect<String> graph;
    private LinkedList<String> vertexName;
    private String name = "Parquesgrafo.json";

    public graphlablenodirect<String> creategraph() {
        if (vertexName == null) {
            vertexName = new LinkedList<>();
        }
        LinkedList<Parques> list = this.getListAll();
        if (!list.isEmpty()) {
            if (graph == null) {
                System.out.println("Grafo mio" + graph);
                graph = new graphlablenodirect<>(list.getSize(), String.class);
            }
            
            Parques[] parques = list.toArray();
            for (int i = 0; i < parques.length; i++) {
                this.graph.labelsVertices(i + 1, parques[i].getNombre());
                System.out.println("Mis vertecis " + vertexName);

                vertexName.add(parques[i].getNombre());
            }
            this.graph.drawGraph();
        }
        System.out.println("Grafo creado" + graph);
        return this.graph;
    }

    // Guardar el grafo en un archivo
    public void saveGraph() throws Exception {
        this.graph.saveGraphLabel(name);
    }
    
    //quiero obtener todos los datos de grafo sin que se guarde nuevo solo obtener los que ya estan
    public JsonArray obtainWeights() throws Exception {
        if (graph == null) {
            creategraph();
        }
    
        if (graph.existsFile(name)) {
            graph.cargarModelosDesdeDao();
            graph.loadGraph(name);
    
            // Asegúrate de que el tipo de dato sea correcto
            JsonArray graphData = graph.obtainWeights(); // Aquí debe devolver el formato correcto
            System.out.println("Modelo de vis,js " + graphData);
            return graphData; // Se asume que graphData es un JsonObject que contiene datos correctos
        } else {
            throw new Exception("El archivo de grafo no existe.");
        }
    }
    
    
    public JsonObject getGraphData() throws Exception {
        if (graph == null) {
            creategraph();
        }
    
        if (graph.existsFile(name)) {
            graph.cargarModelosDesdeDao();
            graph.loadGraph(name);
    
            // Asegúrate de que el tipo de dato sea correcto
            JsonObject graphData = graph.getVisGraphData(); // Aquí debe devolver el formato correcto
            System.out.println("Modelo de vis,js " + graphData);
            return graphData; // Se asume que graphData es un JsonObject que contiene datos correctos
        } else {
            throw new Exception("El archivo de grafo no existe.");
        }
    }
    
    public graphlablenodirect<String> crearuniosnes() throws Exception {
        if (graph == null) {
            creategraph();
        }
        if (graph.existsFile(name)) {
            graph.cargarModelosDesdeDao();
            graph.loadGraphWithRandomEdges(name);
            System.out.println("Modelo asociado al grafo: " + name);
        } else {
            throw new Exception("El archivo de grafo no existe.");
        }
        saveGraph();
        return graph;
    }

    public String bfs(int origen) throws Exception {
    if (graph == null) {
        throw new Exception("El grafo no existe");
    }

    // Crear la instancia de BFS con el grafo y el nodo de origen
    BFS bfsAlgoritmo = new BFS(graph, origen);

    // Llamamos al método de recorrerGrafo() de la clase BFS para obtener el recorrido
    String recorrido = bfsAlgoritmo.recorrerGrafo();
    return recorrido;
}


  
    
    public String caminoCorto(int origen, int destino, int algoritmo) throws Exception {
    if (graph == null) {
        throw new Exception("Grafo no existe");
    }

    System.out.println("Calculando camino corto desde " + origen + " hasta " + destino);

    String camino = "";

    if (algoritmo == 1) { // Usar algoritmo de Floyd
        Floyd floydWarshall = new Floyd(graph, origen, destino);
        camino = floydWarshall.caminoCorto(); 
    } else { 
        BellmanFord bellmanFord = new BellmanFord(graph, origen, destino);
        camino = bellmanFord.caminoCorto(); 
    }

    System.out.println("Camino corto calculado: " + camino);	
    return camino; // Regresar el camino calculado

}


    
    public ParquesDao() {
        super(Parques.class);
    }

    public Parques getParques() {
        if (parques == null) {
            parques = new Parques();
        }
        return this.parques;
    }

    public void setParques(Parques parques) {
        this.parques = parques;
    }

    public LinkedList<Parques> getListAll() {
        if (this.listAll == null) {
            this.listAll = listAll();
        }
        return this.listAll;
    }

    public Boolean save() throws Exception {
        Integer id = getListAll().getSize() + 1;
        getParques().setidParques(id);
        persist(getParques());
        return true;
    }

    public Boolean update() throws Exception {
        this.merge(getParques(), getParques().getidParques() - 1);
        this.listAll = listAll();
        return true;
    }

    public Boolean delete(Integer id) throws Exception {
        for (int i = 0; i < getListAll().getSize(); i++) {
            Parques pro = getListAll().get(i);
            if (pro.getidParques().equals(id)) {
                getListAll().delete(i);
                return true;
            }
        }
        throw new Exception("Proyecto no encontrado con ID: " + id);
    }
}
