package controller.tda.graph.algoritmos;

import controller.tda.graph.graphlablenodirect;

public class Floyd {
    private graphlablenodirect<String> grafo;
    private int origen;
    private int destino;
    private float[][] distancias;
    private int[][] siguiente;

    public Floyd(graphlablenodirect<String> grafo, int origen, int destino) {
        this.grafo = grafo;
        this.origen = origen;
        this.destino = destino;
        int n = grafo.nro_vertices();
        this.distancias = new float[n + 1][n + 1];
        this.siguiente = new int[n + 1][n + 1];
    }

    public String caminoCorto() throws Exception {
        int n = grafo.nro_vertices();

        // Inicialización de matrices
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (i == j) {
                    distancias[i][j] = 0;
                    siguiente[i][j] = -1;
                } else {
                    try {
                        float peso = grafo.getWeigth2(i, j);
                        if (Float.isNaN(peso) || peso <= 0) {
                            distancias[i][j] = Float.MAX_VALUE;
                            siguiente[i][j] = -1;
                        } else {
                            distancias[i][j] = peso;
                            siguiente[i][j] = j;
                        }
                    } catch (Exception e) {
                        distancias[i][j] = Float.MAX_VALUE;
                        siguiente[i][j] = -1;
                    }
                }
            }
        }

        // Algoritmo de Floyd-Warshall
        for (int k = 1; k <= n; k++) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    if (distancias[i][k] != Float.MAX_VALUE && distancias[k][j] != Float.MAX_VALUE &&
                        distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                        distancias[i][j] = distancias[i][k] + distancias[k][j];
                        siguiente[i][j] = siguiente[i][k];
                    }
                }
            }
        }

        return reconstruirCamino(origen, destino);
    }

    private String reconstruirCamino(int origen, int destino) {
        if (siguiente[origen][destino] == -1) {
            return "No hay camino";
        }

        StringBuilder camino = new StringBuilder();
        int actual = origen;
        float distanciaTotal = 0; 

        while (actual != destino) {
            if (siguiente[actual][destino] == -1) {
                return "Error: Camino interrumpido inesperadamente.";
            }
            // Agregar el ID del nodo al camino (en vez de su label)
            camino.append(actual).append(" -> ");
            distanciaTotal += distancias[actual][siguiente[actual][destino]];  // Sumar la distancia total
            actual = siguiente[actual][destino];
        }
        camino.append(destino);  // Agregar el destino al camino
        distanciaTotal += distancias[actual][destino];  // Sumar la última distancia

        // Imprimir la distancia total
        System.out.println("Distancia total recorrida: " + distanciaTotal);

        return "Camino: " + camino.toString() + "|" +  "Distancia total: " + distanciaTotal;
    }
}
