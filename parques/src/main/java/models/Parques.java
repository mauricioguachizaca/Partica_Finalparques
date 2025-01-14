package models;

public class Parques{
    private Integer idParques;
    private String nombre;
    private String direccion;
    private Float longitud;
    private Float latitud;

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

    public String getDireccion() {
        return this.direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Float getLongitud() {
        return this.longitud;
    }

    public void setLongitud(Float longitud) {
        this.longitud = longitud;
    }

    public Float getLatitud() {
        return this.latitud;
    }

    public void setLatitud(Float latitud) {
        this.latitud = latitud;
    }

    public Parques(Integer idParques, String nombre, String direccion, Float longitud, Float latitud) {
        this.idParques = idParques;
        this.nombre = nombre;
        this.direccion = direccion;
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public Parques() {
    }

    
}



