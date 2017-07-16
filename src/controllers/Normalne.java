/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.ArrayList;
import java.util.List;

import entity.Wierzcholek;

/**
 *
 * @author x
 */
public class Normalne {

    static private List<Double> calcNormal(List<Double> v) {
        //v = x y z | x y z | x y z
        List<Double> v1 = new ArrayList<Double>();
        List<Double> v2 = new ArrayList<Double>();
        List<Double> out = new ArrayList<Double>();
        int x = 0, y = 1, z = 2;

        v1.add(v.get(0) - v.get(3));
        v1.add(v.get(1) - v.get(4));
        v1.add(v.get(2) - v.get(5));

        v2.add(v.get(3) - v.get(6));
        v2.add(v.get(4) - v.get(7));
        v2.add(v.get(5) - v.get(8));

        out.add((v1.get(1) * v2.get(2)) - (v1.get(2) * v2.get(1)));
        out.add((v1.get(2) * v2.get(0)) - (v1.get(0) * v2.get(2)));
        out.add((v1.get(0) * v2.get(1)) - (v1.get(1) * v2.get(0)));

        Double length = Math.sqrt((out.get(0) * out.get(0)) + (out.get(1) * out.get(1)) + (out.get(2) * out.get(2)));
        if (length == 0.0) {
            length = 1.0;
        }

        out.set(0, (out.get(0) / length));
        out.set(1, (out.get(1) / length));
        out.set(2, (out.get(2) / length));

        return out;
    }

    static public List<Double> calcNormal(Wierzcholek w0, Wierzcholek w1, Wierzcholek w2) {
        List<Double> v = new ArrayList<Double>();
        v.add(w0.getX());
        v.add(w0.getY());
        v.add(0.0);
        v.add(w1.getX());
        v.add(w1.getY());
        v.add(0.0);
        v.add(w2.getX());
        v.add(w2.getY());
        v.add(0.0);
        return calcNormal(v);
    }

    static public List<Double> calcNormal(Wierzcholek w0, double z0, Wierzcholek w1, double z1, Wierzcholek w2, double z2) {
        List<Double> v = new ArrayList<Double>();
        v.add(w0.getX());
        v.add(w0.getY());
        v.add(z0);
        v.add(w1.getX());
        v.add(w1.getY());
        v.add(z1);
        v.add(w2.getX());
        v.add(w2.getY());
        v.add(z2);
        return calcNormal(v);
    }
}
