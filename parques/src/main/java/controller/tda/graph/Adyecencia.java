package controller.tda.graph;

public class Adyecencia {
    private Integer destination;
    private Float weight;

    //tienes que construir el constructor por defecto 
    public Adyecencia(Integer destination, Float weight) {
        this.destination = destination;
        this.weight = weight;
        
    }

    public Integer getdestination() {
        return this.destination;
    }

    public void setdestination(Integer destination) {
        this.destination = destination;
    }

    public Float getweight() {
        return this.weight;
    }

    public void setweight(Float weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Adyecencia{" +
                "destination=" + destination +
                ", weight=" + weight +
                '}';
    }
    
}
