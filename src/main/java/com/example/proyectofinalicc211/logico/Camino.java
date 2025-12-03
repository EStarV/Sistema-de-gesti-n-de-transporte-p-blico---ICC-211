package com.example.proyectofinalicc211.logico;

import java.util.List;

public class Camino {
    private List<Parada> camino;
    private List<Ruta> rutas;
    private double distancia;

    public Camino(List<Parada> camino, double distancia, List<Ruta> rutas) {
        this.camino = camino;
        this.distancia = distancia;
        this.rutas = rutas;
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

    public List<Ruta> getRutas() {
        return rutas;
    }

    public void setRutas(List<Ruta> rutas) {
        this.rutas = rutas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof Camino))
            return false;
        if(((Camino) o).getCamino().size() != camino.size())
            return false;
        for(int i=0; i<camino.size(); i++){
            if(!(camino.get(i).getId().equals(((Camino) o).getCamino().get(i).getId())))
                return false;
        }
        return true;
    }
}
