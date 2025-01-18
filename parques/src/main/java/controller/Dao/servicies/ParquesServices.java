package controller.Dao.servicies;


import controller.Dao.ParquesDao;
import controller.tda.list.LinkedList;
import models.Parques;
public class ParquesServices {
    private ParquesDao obj;
    public ParquesServices() {
        obj = new ParquesDao();
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public LinkedList<Parques> listAll(){
        return obj.getListAll();
    }

    public Parques getParques(){
        return obj.getParques();
    }

    public void setParques(Parques parques) {
        obj.setParques(parques);
    }

    public Parques get(Integer id) throws Exception {
        return obj.get(id);
    }

    public Boolean delete(Integer id) throws Exception {
        return obj.delete(id);
    }

    
    
}
