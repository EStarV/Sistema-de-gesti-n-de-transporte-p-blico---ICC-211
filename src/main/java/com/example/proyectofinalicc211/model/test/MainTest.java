package com.example.proyectofinalicc211.model.test;

import com.example.proyectofinalicc211.model.*;

import java.util.ArrayList;
import java.util.List;

public class MainTest {
    public static void main(String[] args) {
        ListaAdyacencia instancia = ListaAdyacencia.getInstancia();

        System.out.println("Iniciando prueba local del sistema de rutas...");

        List<Parada> paradas = new ArrayList<>();
        String[] nombres = {"A", "B", "C", "D", "E", "F", "G", "H"};
        for (int i = 0; i < 8; i++) {
            Parada p = new Parada(nombres[i]);
            p.setPosicionX(100 + i * 90);
            p.setPosicionY(100 + (i % 4) * 120);
            paradas.add(p);
        }

        instancia.addParadasLista(paradas);
        System.out.println("8 paradas creadas y guardadas en Supabase");

        instancia.addRutasLista(List.of(
                new Ruta(paradas.get(0).getId(), paradas.get(3).getId(), 10, 35, 2.0, 1),
                new Ruta(paradas.get(0).getId(), paradas.get(2).getId(), 2, 20, 1.5, 1, "Metro"),
                new Ruta(paradas.get(0).getId(), paradas.get(1).getId(), 3, 20, 2.0, 2, "Metro"),
                new Ruta(paradas.get(1).getId(), paradas.get(3).getId(), 4, 30, 3.0, 2, "Monorriel"),
                new Ruta(paradas.get(1).getId(), paradas.get(4).getId(), 5, 30, 8.0, 3, "Metro"),
                new Ruta(paradas.get(2).getId(), paradas.get(5).getId(), 7, 20, 5.0, 1, "Metro"),
                new Ruta(paradas.get(2).getId(), paradas.get(7).getId(), 45, 35, 10.0, 1),
                new Ruta(paradas.get(3).getId(), paradas.get(5).getId(), 8, 35, 10.0, 2),
                new Ruta(paradas.get(3).getId(), paradas.get(6).getId(), 10, 35, 10.0, 1, "Monorriel"),
                new Ruta(paradas.get(4).getId(), paradas.get(6).getId(), 6, 25, 4.0, 2, "Tranvía"),
                new Ruta(paradas.get(4).getId(), paradas.get(7).getId(), 8, 30, 4.0, 1, "Tranvía"),
                new Ruta(paradas.get(5).getId(), paradas.get(4).getId(), 3, 35, 5.0, 3, "Metro"),
                new Ruta(paradas.get(5).getId(), paradas.get(6).getId(), 5, 35, 8.0, 1, "Monorriel"),
                new Ruta(paradas.get(6).getId(), paradas.get(7).getId(), 4, 20, 2.0, 2)
        ));

        System.out.println("14 rutas creadas y guardadas");

        Parada A = paradas.get(0);
        Parada H = paradas.get(7);

        System.out.println("\nRUTA MÁS RÁPIDA (TIEMPO):");
        Camino tiempo = instancia.dijkstra(A, H, "tiempo");
        if (tiempo != null) tiempo.printCamino();

        System.out.println("\nALTERNATIVAS (Dijkstra):");
        List<Camino> alternativas = instancia.rutaMasCortayAlterna(A, H, "tiempo");
        for (int i = 0; i < alternativas.size(); i++) {
            System.out.println("Alternativa " + (i + 1) + ": " + alternativas.get(i).getDistancia() + " min");
            alternativas.get(i).printCamino();
        }

        System.out.println("\nPrueba completada con éxito!");
    }
}