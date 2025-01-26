package controller.tda.graph.algoritmos;

import controller.tda.graph.graphlablenodirect;
import controller.tda.graph.Adyecencia;
import controller.tda.list.LinkedList;
import java.util.HashMap;
import java.util.Map;

public class BellmanFord {
    private graphlablenodirect<String> grafo;
    private int origen;
    private int destino;
    private Map<Integer, Float> distancias;
    private Map<Integer, Integer> predecesores;

    public BellmanFord(graphlablenodirect<String> grafo, int origen, int destino) {
        this.grafo = grafo;
        this.origen = origen;
        this.destino = destino;
        this.distancias = new HashMap<>();
        this.predecesores = new HashMap<>();
    }

    public String caminoCorto() throws Exception {
        int n = grafo.nro_vertices();

        // Inicialización de estructuras dinámicas
        for (int i = 1; i <= n; i++) {
            distancias.put(i, Float.MAX_VALUE);
            predecesores.put(i, -1);
        }
        distancias.put(origen, 0f);

        // Relajación de las aristas (n-1 veces)
        for (int i = 1; i < n; i++) {
            for (int u = 1; u <= n; u++) {
                LinkedList<Adyecencia> adyacencias = grafo.adyecencias(u);
                for (int j = 0; j < adyacencias.getSize(); j++) {
                    Adyecencia adyacencia = adyacencias.get(j);
                    int v = adyacencia.getdestination();
                    float peso = adyacencia.getweight();
                    
                    if (distancias.get(u) != Float.MAX_VALUE && distancias.get(u) + peso < distancias.get(v)) {
                        distancias.put(v, distancias.get(u) + peso);
                        predecesores.put(v, u);
                    }
                }
            }
        }

        // Verificación de ciclos negativos
        for (int u = 1; u <= n; u++) {
            LinkedList<Adyecencia> adyacencias = grafo.adyecencias(u);
            for (int j = 0; j < adyacencias.getSize(); j++) {
                Adyecencia adyacencia = adyacencias.get(j);
                int v = adyacencia.getdestination();
                float peso = adyacencia.getweight();
                if (distancias.get(u) != Float.MAX_VALUE && distancias.get(u) + peso < distancias.get(v)) {
                    return "El grafo tiene un ciclo negativo";
                }
            }
        }

        return reconstruirCamino(origen, destino);
    }

    private String reconstruirCamino(int origen, int destino) throws Exception {
        if (distancias.get(destino) == Float.MAX_VALUE) {
            return "No hay camino";
        }

        StringBuilder camino = new StringBuilder();
        int actual = destino;
        float distanciaTotal = 0;

        while (actual != -1) {
            if (predecesores.get(actual) != -1) {
                distanciaTotal += grafo.getWeigth2(predecesores.get(actual), actual);
            }
            camino.insert(0, actual + " -> ");
            actual = predecesores.get(actual);
        }
        camino.delete(camino.length() - 4, camino.length()); // Eliminar la última flecha

        System.out.println("Distancia total recorrida: " + distanciaTotal);
        return "Camino: " + camino.toString() + "\nDistancia total: " + distanciaTotal;
    }
}
