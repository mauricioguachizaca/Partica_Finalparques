package controller.tda.graph;
public class graphlablenodirect<E> extends graphlabledirect<E> {
    public graphlablenodirect(Integer nro_vertices, Class<E> clazz) {
        super(nro_vertices, clazz);
    }	

    public void insertEdgeL(E v1, E v2, Float wiegth) throws Exception {
        if (isLabelsGraph()) {
            add_edge(getVerticeL(v1), getVerticeL(v2), wiegth);
            add_edge(getVerticeL(v2), getVerticeL(v1), wiegth);

        } else {
            throw new Exception("Grafo no etiquetado");
        }
    }


    
}