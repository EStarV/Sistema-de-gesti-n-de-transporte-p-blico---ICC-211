package com.example.proyectofinalicc211.model;

import java.util.UUID;

public class Parada {
    //Nodo
    private UUID id;
    private String nombre;
    private double posicionX; // Para el grafo visual
    private double posicionY; // Para el grafo visual

    public Parada(UUID id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.posicionX = Math.random() * 300;
        this.posicionY = Math.random() * 100;
    }

    public Parada(String nombre) {
        this.id = UUID.randomUUID();
        this.nombre = nombre;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPosicionX() {
        return posicionX;
    }

    public void setPosicionX(double posicionX) {
        this.posicionX = posicionX;
    }

    public double getPosicionY() {
        return posicionY;
    }

    public void setPosicionY(double posicionY) {
        this.posicionY = posicionY;
    }
}
