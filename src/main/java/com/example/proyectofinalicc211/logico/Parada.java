package com.example.proyectofinalicc211.logico;

import java.util.UUID;

public class Parada {
    //Nodo
    private UUID id;
    private String nombre;

    public Parada(UUID id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Parada(String nombre){
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
}
