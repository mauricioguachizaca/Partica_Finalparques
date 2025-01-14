package controller.tda.graph;

import java.util.Arrays;

public class GraphAlgorithms {

    
    // Floyd-Warshall
    public static Float[][] floydWarshall(Graph graph) throws Exception {
        int n = graph.nro_vertices();
        Float[][] dist = new Float[n + 1][n + 1];

        // Inicializar matriz de distancias
        for (int i = 1; i <= n; i++) {
            Arrays.fill(dist[i], Float.POSITIVE_INFINITY);
            dist[i][i] = 0f;
        }

        // Llenar matriz con las distancias iniciales
        for (int i = 1; i <= n; i++) {
            for (Adyecencia adj : graph.adyecencias(i).toArray()) {
                dist[i][adj.getdestination()] = adj.getweight();
            }
        }

        // Actualizar usando Floyd-Warshall
        for (int k = 1; k <= n; k++) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        return dist;
    }
}
