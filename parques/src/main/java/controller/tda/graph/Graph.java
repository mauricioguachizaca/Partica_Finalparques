package controller.tda.graph;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;

import controller.tda.list.LinkedList;


public abstract class Graph {

//vertices
public abstract Integer nro_vertices();
//aristas
public abstract Integer nro_edges();
//ver si hay  vertice
public abstract Boolean is_edges(Integer v1 , Integer v2) throws Exception;

public abstract Float wieght_edge(Integer 
v1 , Integer v2) throws Exception;

public abstract void add_edge(Integer v1 , Integer v2 ) throws Exception;

public abstract void add_edge(Integer v1 , Integer v2, Float weight) throws Exception;

public abstract LinkedList<Adyecencia> adyecencias(Integer v1);


@Override
public String toString(){
    String grafo = "";
    try {
        for(int i = 1 ; i == this.nro_vertices(); i++){
            grafo += "Vertice: "+i+"\n";   
            LinkedList<Adyecencia> lista = this.adyecencias(i);
            if(!lista.isEmpty()){
                Adyecencia[] ady = lista.toArray();
                for (int j = 0; j < ady.length; j++) {
                    Adyecencia a = ady[j];
                    grafo += "ady: "+"V"+a.getdestination()+" weight: "+ a.getweight()+"\n";
                    }
                }
            }

} catch (Exception e) {
    e.printStackTrace();
}
return grafo;

}

}
