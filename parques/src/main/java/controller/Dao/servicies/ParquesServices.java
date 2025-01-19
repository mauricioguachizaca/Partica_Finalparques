package controller.Dao.servicies;

import controller.Dao.ParquesDao;
import controller.tda.list.LinkedList;
import models.Parques;

public class ParquesServices {
    private ParquesDao obj;

    // Constructor
    public ParquesServices() {
        obj = new ParquesDao();
    }

    // Método para guardar un parque
    public Boolean save() throws Exception {
        return obj.save();
    }

    // Método para actualizar un parque
    public Boolean update() throws Exception {
        return obj.update();
    }

    // Método para listar todos los parques
    public LinkedList<Parques> listAll() {
        return obj.getListAll();
    }

    // Método para obtener un parque
    public Parques getParques() {
        return obj.getParques();
    }

    // Método para establecer un parque
    public void setParques(Parques parques) {
        obj.setParques(parques);
    }

    // Método para obtener un parque por ID
    public Parques get(Integer id) throws Exception {
        return obj.get(id);
    }

    // Método para eliminar un parque
    public Boolean delete(Integer id) throws Exception {
        return obj.delete(id);
    }

    // Método para calcular el camino corto entre dos parques usando el algoritmo seleccionado
    public String calcularCaminoCorto(int origen, int destino, int algoritmo) throws Exception {
        return obj.caminoCorto(origen, destino, algoritmo);
    }
}
