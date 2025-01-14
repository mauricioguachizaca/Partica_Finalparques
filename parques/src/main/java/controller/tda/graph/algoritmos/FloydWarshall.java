package controller.tda.graph.algoritmos;

import controller.tda.list.LinkedList;
import controller.tda.list.ListEmptyException;
import controller.tda.graph.Adyecencia;
import controller.tda.graph.graphlabledirect;

public class FloydWarshall {
    private graphlabledirect<String> graph;

    public FloydWarshall(graphlabledirect<String> graph) {
        this.graph = graph;
    }

    public float[][] calculateShortestPaths() {
        int n = graph.nro_vertices();
        float[][] dist = new float[n + 1][n + 1];

        // Initialize distances with infinity
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (i == j) dist[i][j] = 0;
                else dist[i][j] = Float.MAX_VALUE;
            }
        }

        // Set initial edges distances
        for (int i = 1; i <= n; i++) {
            LinkedList<Adyecencia> adjacencies = graph.adyecencias(i);
            for (int j = 0; j < adjacencies.getSize(); j++) {
                try {
                    Adyecencia adj = adjacencies.get(j);
                    dist[i][adj.getdestination()] = adj.getweight();
                } catch (ListEmptyException e) {
                    e.printStackTrace();
                }
            }
        }

        // Apply Floyd-Warshall algorithm
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
