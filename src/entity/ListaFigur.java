/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author x
 */
public class ListaFigur {
    private int liczbaFigur;
    private List<Figura> listaFigur;

    public ListaFigur() {
        liczbaFigur = 0;
        listaFigur = new ArrayList<Figura>();
    }

    public int getLiczbaFigur() {
        return liczbaFigur;
    }

    public void setLiczbaFigur(int liczbaFigur) {
        this.liczbaFigur = liczbaFigur;
    }

    public List<Figura> getListaFigur() {
        return listaFigur;
    }

    public void setListaFigur(List<Figura> listaFigur) {
        this.listaFigur = listaFigur;
    }
    
    public void addFigura(Figura figura){
        this.listaFigur.add(figura);
    }
    
}
