package com.example.proyectofinalicc211.logico;

import java.util.List;

public class Camino {
    private List<Parada> camino;
    private double distancia;

    public Camino(List<Parada> camino, double distancia) {
        this.camino = camino;
        this.distancia = distancia;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public List<Parada> getCamino() {
        return camino;
    }

    public void setCamino(List<Parada> camino) {
        this.camino = camino;
    }

    public void printCamino(){
        for(Parada parada : camino){
            System.out.print(parada.getNombre()+"-");
        }
        System.out.println(distancia);
    }
}
