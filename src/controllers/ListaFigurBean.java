/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import entity.Figura;
import entity.ListaFigur;
import entity.Wierzcholek;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author x
 */
public class ListaFigurBean {
    private static ListaFigur pobranieZPliku(String nazwa) throws IOException {
        ListaFigur listaFigur = new ListaFigur();
        Figura figura;
        Wierzcholek wierzcholek;
        
        FileReader file = new FileReader(nazwa);
        BufferedReader buffer = new BufferedReader(file);
        int index;
        String temp;
        
        try {
            String line = buffer.readLine();
            if (line != null) {
                listaFigur.setLiczbaFigur(Integer.parseInt(line));              //przeksztalcenie na int iloœci figur w pliku
                for(int i = 0; i< listaFigur.getLiczbaFigur(); i++){
                    line = buffer.readLine();                                       //odczytanie kolejnej linii
                    if (line != null){
                        figura = new Figura();                                   //stworzenie nowej figury
                        index = 0;
                        temp = "";
                        while(index < line.length() && line.charAt(index) != ';'){      //odczytanie liczby wierzcholkow
                            temp = temp + line.charAt(index);
                            index++;
                        }
                        index++;
                        figura.setLiczbaWierzcholkow(Integer.parseInt(temp));
                        temp = "";
                        
                        if (figura.getLiczbaWierzcholkow() > 0){
                            for (int j = 0; j < figura.getLiczbaWierzcholkow(); j++){
                                wierzcholek = new Wierzcholek();                        //stworzenie nowego wierzcholka
                                
                                while(index < line.length() && line.charAt(index) != ','){      //pobranie wspolrzednej X
                                    temp = temp + line.charAt(index);
                                    index++;
                                }
                                if(temp != null){
                                    wierzcholek.setX(Double.parseDouble(temp));        //wpisanie X'a
                                    temp = "";
                                    index++;                                        //pominiecie ,
                                }
                                
                                while(index < line.length() && line.charAt(index) != ';'){          //pobranie wspolrzednej Y
                                    temp = temp + line.charAt(index);
                                    index++;
                                }
                                if(temp != null){
                                    wierzcholek.setY(Double.parseDouble(temp));            //wpisanie Y'a
                                    temp = "";
                                    index++;                                                //pominiecie ;
                                }
                                figura.addWierzcholek(wierzcholek);
                            }
                            listaFigur.addFigura(figura);
                        }
                    }
                }
            }
            return listaFigur;
        } catch (Exception e) {
            System.out.printf("ERROR while pobranieZPliku");
            System.out.printf(e.toString());
            return null;
        }
    }
    
    public static ListaFigur pobierzDane(String nazwaPliku){
        try {
            return pobranieZPliku(nazwaPliku);
        } catch (Exception e) {
            System.out.printf("ERROR while pobierzDane");
            System.out.printf(e.toString());
            return null;
        }
    }
    
}
