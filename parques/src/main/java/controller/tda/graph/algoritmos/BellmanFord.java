package controller.tda.graph.algoritmos;
import controller.tda.list.LinkedList;

import java.util.Arrays;

import controller.tda.graph.Adyecencia;
import controller.tda.graph.graphlablenodirect;


public class BellmanFord {
    private graphlablenodirect<String> grafo;
    private int origen;
    private float[] distancias;
    private int[] predecesores;

    public BellmanFord(graphlablenodirect<String> grafo, int origen, int destino) {
        this.grafo = grafo;
        this.origen = origen;
        int n = grafo.nro_vertices();
        this.distancias = new float[n + 1];
        this.predecesores = new int[n + 1];
    }

    public String caminoCorto(int destino) throws Exception {
        int n = grafo.nro_vertices();

        // Inicialización
        Arrays.fill(distancias, Float.MAX_VALUE);
        distancias[origen] = 0;
        Arrays.fill(predecesores, -1);

        // Relajación de las aristas (n-1 veces)
        for (int i = 1; i < n; i++) {
            for (int u = 1; u <= n; u++) {
                LinkedList<Adyecencia> adyacencias = grafo.adyecencias(u);
                for (int j = 0; j < adyacencias.getSize(); j++) {
                    Adyecencia adyacencia = adyacencias.get(j);
                    int v = adyacencia.getdestination();
                    float peso = adyacencia.getweight();
                    if (distancias[u] != Float.MAX_VALUE && distancias[u] + peso < distancias[v]) {
                        distancias[v] = distancias[u] + peso;
                        predecesores[v] = u;
        
                        // DEBUG: Ver qué se está asignando
                        System.out.println("Predecesor de " + v + " es " + u);
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
                if (distancias[u] != Float.MAX_VALUE && distancias[u] + peso < distancias[v]) {
                    return "El grafo tiene un ciclo negativo";
                }
            }
        }

        // Reconstruir el camino
        return reconstruirCamino(origen, destino);
    }

    private String reconstruirCamino(int origen, int destino) throws Exception {
        if (distancias[destino] == Float.MAX_VALUE) {
            return "No hay camino";
        }
        
        StringBuilder camino = new StringBuilder();
        int actual = destino;
    
        // Recorrer los predecesores hasta llegar al origen
        while (actual != -1) {
            String nombre = grafo.getLabelL(actual);
            camino.insert(0, nombre + "(" + actual + ")");
            
            if (predecesores[actual] != -1) {
                camino.insert(0, " <- ");  // Solo agregar " <- " si no es el origen
            }
            
            // Depuración: Ver cómo se actualizan los predecesores
            System.out.println("Nodo actual: " + actual + " (predecesor: " + predecesores[actual] + ")");
            actual = predecesores[actual];
        }
        
        System.out.println("en otra vida total: " +  camino.toString());
        return camino.toString();

    }
     
    
}
