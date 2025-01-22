package models;

public class Parques{
    private Integer idParques;
    private String nombre;
    private String descripcion;
    private Double latitud;
    private Double longitud;
    

    public Integer getidParques() {
        return this.idParques;
    }

    public void setidParques(Integer idParques) {
        this.idParques = idParques;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getdescripcion() {
        return this.descripcion;
    }

    public void setdescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getLongitud() {
        return this.longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return this.latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Parques(Integer idParques, String nombre, String descripcion, Double longitud, Double latitud) {
        this.idParques = idParques;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Parques() {
    }



    
}



