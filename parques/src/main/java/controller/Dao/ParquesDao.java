package controller.Dao;

import controller.Dao.implement.AdapterDao;
import controller.tda.list.LinkedList;
import controller.tda.list.ListEmptyException;
import models.Parques;
public class ParquesDao extends AdapterDao<Parques> {

    private Parques parques;
    private LinkedList<Parques> listAll;

    public ParquesDao() {
        super(Parques.class);
    }
    
    public Parques getParques() {
        if (parques == null) {
            parques = new Parques();
        }
        return this.parques;
    }

    public void setParques(Parques parques) {
        this.parques = parques;
    }

    public LinkedList<Parques> getListAll() {
        if (this.listAll == null) {
            this.listAll = listAll(); 
        }
        return this.listAll;
    }

    public Boolean save() throws Exception {
        Integer id  =  getListAll().getSize() + 1;
        getParques().setidParques(id);
        persist(getParques());
        return true;
    }

    public Boolean update() throws Exception {
        this.merge(getParques(), getParques().getidParques() - 1);
        this.listAll = listAll();
        return true;
    }

    public Boolean delete(Integer id) throws Exception {
        for(int i = 0; i < getListAll().getSize(); i++) {
            Parques pro = getListAll().get(i);
            if(pro.getidParques().equals(id)) {
                getListAll().delete(i);
                return true;
            }
        }
        throw new Exception("Proyecto no encontrado con ID: " + id);
    }




}
