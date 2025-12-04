package com.example.proyectofinalicc211.model;

import java.util.*;

public abstract class AlgoritmoRutaCorta {
    protected Map<UUID, List<Ruta>> grafo;

    public AlgoritmoRutaCorta(Map<UUID, List<Ruta>> grafo) {
        this.grafo = grafo;
    }

    public abstract Camino calcCamino(Parada origen, Parada destino, String parametro);

    public double getPesoRuta(Ruta r, String peso) {
        if (r == null) return -1;
        if (peso.equalsIgnoreCase("distancia")) {
            return r.getDistancia();
        } else if (peso.equalsIgnoreCase("costo")) {
            return r.getCosto();
        } else {
            return r.getTiempo();
        }
    }

    public LinkedList<Ruta> reconstruirRutas(List<Parada> camino, String parametro) {
        LinkedList<Ruta> rutasAux = new LinkedList<>();
        for(int i = 0; i < camino.size()-1; i++){
            List<Ruta> rutas = grafo.get(camino.get(i).getId());
            Parada siguiente = camino.get(i+1);

            Ruta mejor = null;
            double mejorPeso = Double.MAX_VALUE;
            for(Ruta r : rutas){
                if(r == null) continue;
                if(r.getId_destino().equals(siguiente.getId())){
                    double peso = getPesoRuta(r, parametro);
                    if(mejor == null || peso < mejorPeso){
                        mejor = r;
                        mejorPeso = peso;
                    }
                }
            }
            rutasAux.add(mejor);
        }
        return rutasAux;
    }
}
