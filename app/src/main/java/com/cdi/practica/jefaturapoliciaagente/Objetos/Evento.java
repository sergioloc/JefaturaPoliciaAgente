package com.cdi.practica.jefaturapoliciaagente.Objetos;

/**
 * Created by Sergio López on 22/06/2018.
 */

public class Evento {
    private String nombre;
    private String ubicacion;
    private String hora;
    private int numAgentes;

    public Evento(){}

    public Evento(String nombre, String ubicacion, String hora, int numAgentes) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.hora = hora;
        this.numAgentes = numAgentes;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getNumAgentes() {
        return numAgentes;
    }

    public void setNumAgentes(int numAgentes) {
        this.numAgentes = numAgentes;
    }
}
