package controller.Dao;

import com.google.gson.JsonArray;

import controller.Dao.implement.AdapterDao;
import controller.tda.graph.graphlablenodirect;
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

    // Método para crear el grafo
    public graphlablenodirect<String> creategraph() {
        // Inicializar vertexName si es null
        if (vertexName == null) {
            vertexName = new LinkedList<>();
        }
        
        // Obtener lista de parques
        LinkedList<Parques> list = this.getListAll();
        if (!list.isEmpty()) {
            // Inicializar grafo si es null
            if (graph == null) {
                graph = new graphlablenodirect<>(list.getSize(), String.class);
            }
            
            Parques[] parques = list.toArray();
            for (int i = 0; i < parques.length; i++) {
                this.graph.labelsVertices(i + 1, parques[i].getNombre());
                vertexName.add(parques[i].getNombre());
            }
            this.graph.drawGraph();
        }
        return this.graph;
    }

    // Guardar el grafo en un archivo
    public void saveGraph() throws Exception {
        this.graph.saveGraphLabel(name);
    }

    // Obtener pesos del grafo
    public JsonArray obtainWeights() throws Exception {
            return this.graph.obtainWeights();
    }

    // Obtener el grafo (cargarlo si no está cargado)
    public graphlablenodirect<String> obtenerGrafo() throws Exception {
        if (graph == null) {
            creategraph();
        }

        // Verificar si el archivo existe y cargarlo
        if (graph.existsFile(name)) {
            graph.cargarModelosDesdeDao();
            graph.loadGraph(name);
            System.out.println("Modelo asociado al grafo: " + name);
        } else {
            throw new Exception("El archivo de grafo no existe.");
        }

        saveGraph();
        return graph;
    }

    

    // Método para calcular el camino corto
    public String caminoCorto(int origen, int destino, int algoritmo) throws Exception {
    if (graph == null) {
        throw new Exception("Grafo no existe");
    }

    System.out.println("Calculando camino corto desde " + origen + " hasta " + destino);

    String camino = "";

    if (algoritmo == 1) { // Usar algoritmo de Floyd
        Floyd floydWarshall = new Floyd(graph, origen, destino);
        camino = floydWarshall.caminoCorto(); // Se asume que Floyd tiene un método para calcular el camino corto
    } else { // Usar algoritmo de Bellman-Ford (o cualquier otro algoritmo como Dijkstra)
        BellmanFord bellmanFord = new BellmanFord(graph, origen, destino);
        camino = bellmanFord.caminoCorto(algoritmo); // Se asume que BellmanFord tiene un método para calcular el camino corto
    }

    System.out.println("Camino corto calculado: " + camino);	
    return camino; // Regresar el camino calculado

}

    



    // Constructor
    public ParquesDao() {
        super(Parques.class);
    }

    // Obtener o crear un nuevo objeto Parques
    public Parques getParques() {
        if (parques == null) {
            parques = new Parques();
        }
        return this.parques;
    }

    // Establecer objeto Parques
    public void setParques(Parques parques) {
        this.parques = parques;
    }

    // Obtener lista de todos los parques
    public LinkedList<Parques> getListAll() {
        if (this.listAll == null) {
            this.listAll = listAll();
        }
        return this.listAll;
    }

    // Guardar un nuevo parque
    public Boolean save() throws Exception {
        Integer id = getListAll().getSize() + 1;
        getParques().setidParques(id);
        persist(getParques());
        return true;
    }

    // Actualizar parque
    public Boolean update() throws Exception {
        this.merge(getParques(), getParques().getidParques() - 1);
        this.listAll = listAll();
        return true;
    }

    // Eliminar parque por ID
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
