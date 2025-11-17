package com.example.proyectofinalicc211.logico;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Parada> paradas = new ArrayList<>();
        for(int i=0; i<8; i++){
             paradas.add(new Parada(String.format("%c", 'A'+i)));
        }
        ListaAdyacencia lista = new ListaAdyacencia();
        lista.addParadasLista(paradas);
        ArrayList<Ruta> rutas = new ArrayList<>();
        rutas.add(new Ruta(paradas.get(0).getId(), paradas.get(3).getId(), 10, 35, 2, 1));
        rutas.add(new Ruta(paradas.get(0).getId(), paradas.get(2).getId(), 2, 20, 1.5, 1, "Metro"));
        rutas.add(new Ruta(paradas.get(0).getId(), paradas.get(1).getId(), 3, 20, 2, 2, "Metro"));
        rutas.add(new Ruta(paradas.get(1).getId(), paradas.get(3).getId(), 4, 30, 3, 2, "Monorriel"));
        rutas.add(new Ruta(paradas.get(1).getId(), paradas.get(4).getId(), 5, 30, 8, 3, "Metro"));
        rutas.add(new Ruta(paradas.get(2).getId(), paradas.get(5).getId(), 7, 20, 5, 1, "Metro"));
        rutas.add(new Ruta(paradas.get(2).getId(), paradas.get(7).getId(), 45, 35, 10, 1));
        rutas.add(new Ruta(paradas.get(3).getId(), paradas.get(5).getId(), 8, 35, 10, 2 ));
        rutas.add(new Ruta(paradas.get(3).getId(), paradas.get(6).getId(), 10, 35, 10, 1, "Monorriel"));
        rutas.add(new Ruta(paradas.get(4).getId(), paradas.get(6).getId(), 6, 25, 4, 2, "Tranvia"));
        rutas.add(new Ruta(paradas.get(4).getId(), paradas.get(7).getId(), 8, 30, 4, 1, "Tranvia"));
        rutas.add(new Ruta(paradas.get(5).getId(), paradas.get(4).getId(), 3, 35, 5, 3, "Metro"));
        rutas.add(new Ruta(paradas.get(5).getId(), paradas.get(6).getId(), 5, 35, 8, 1, "Monorriel"));
        rutas.add(new Ruta(paradas.get(6).getId(), paradas.get(7).getId(), 4, 20, 2, 2));

        lista.addRutasLista(rutas);
        Camino caminoCorto1 = lista.dijkstra(paradas.get(0), paradas.get(7), "tiempo");
        Camino caminoCorto2 = lista.dijkstra(paradas.get(0), paradas.get(7), "distancia");
        Camino caminoCorto3 = lista.dijkstra(paradas.get(0), paradas.get(7), "Costo");
        caminoCorto1.printCamino();
        caminoCorto2.printCamino();
        caminoCorto3.printCamino();
        Camino caminoFloyd1 = lista.floydMarshall(paradas.get(0), paradas.get(7), "tiempo");
        Camino caminoFloyd2 = lista.floydMarshall(paradas.get(0), paradas.get(7), "distancia");
        Camino caminoFloyd3 = lista.floydMarshall(paradas.get(0), paradas.get(7), "Costo");
        caminoFloyd1.printCamino();
        caminoFloyd2.printCamino();
        caminoFloyd3.printCamino();
    }
}