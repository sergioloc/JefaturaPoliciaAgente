package com.cdi.practica.jefaturapoliciaagente.Objetos;

/**
 * Created by Sergio on 16/04/2018.
 */

public class Item {
    String texto;
    int imagen;

    public Item(String texto, int imagen){
        this.texto=texto;
        this.imagen=imagen;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

}
