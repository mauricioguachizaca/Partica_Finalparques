package controller.tda.graph.algoritmos;
import java.util.HashMap;
import java.util.Map;
import controller.tda.list.LinkedList;
import controller.tda.list.ListEmptyException;
import controller.tda.graph.Adyecencia;
import controller.tda.graph.graphlabledirect;

public class BellmanFord {
    private graphlabledirect<String> graph;

    public BellmanFord(graphlabledirect<String> graph) {
        this.graph = graph;
    }

    public Map<Integer, Float> calculateShortestPaths(Integer start) throws  ListEmptyException {
        int n = graph.nro_vertices();
        Map<Integer, Float> distances = new HashMap<>();
        Map<Integer, Integer> previous = new HashMap<>();

        // Inicializar las distancias con infinito
        for (int i = 1; i <= n; i++) {
            distances.put(i, Float.MAX_VALUE);
            previous.put(i, null);
        }

        distances.put(start, 0f);

        // Relajar todas las aristas n-1 veces
        for (int i = 1; i <= n - 1; i++) {
            for (int u = 1; u <= n; u++) {
                LinkedList<Adyecencia> adjacencies = graph.adyecencias(u);
                for (int j = 0; j < adjacencies.getSize(); j++) {
                    Adyecencia adj = adjacencies.get(j);
                    int v = adj.getdestination();
                    float weight = adj.getweight();
                    if (distances.get(u) + weight < distances.get(v)) {
                        distances.put(v, distances.get(u) + weight);
                        previous.put(v, u);
                    }
                }
            }
        }

        // Verificar la existencia de ciclos negativos
        for (int u = 1; u <= n; u++) {
            LinkedList<Adyecencia> adjacencies = graph.adyecencias(u);
            for (int j = 0; j < adjacencies.getSize(); j++) {
                Adyecencia adj = adjacencies.get(j);
                int v = adj.getdestination();
                float weight = adj.getweight();
                if (distances.get(u) + weight < distances.get(v)) {
                    System.out.println("El grafo contiene un ciclo negativo.");
                    return null;  // Si hay un ciclo negativo, el algoritmo termina
                }
            }
        }

        return distances;
    }
}
