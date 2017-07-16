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
public class Figura {
    private int liczbaWierzcholkow;
    private List<Wierzcholek> listaWierzcholkow;

    public Figura() {
        liczbaWierzcholkow = 0;
        listaWierzcholkow = new ArrayList<Wierzcholek>();
    }

    public int getLiczbaWierzcholkow() {
        return liczbaWierzcholkow;
    }

    public void setLiczbaWierzcholkow(int liczbaWierzcholkow) {
        this.liczbaWierzcholkow = liczbaWierzcholkow;
    }

    public List<Wierzcholek> getListaWierzcholkow() {
        return listaWierzcholkow;
    }

    public void setListaWierzcholkow(List<Wierzcholek> listaWierzcholkow) {
        this.listaWierzcholkow = listaWierzcholkow;
    }
    
    public void addWierzcholek(Wierzcholek wierzcholek){
        this.listaWierzcholkow.add(wierzcholek);
    }
    
}
