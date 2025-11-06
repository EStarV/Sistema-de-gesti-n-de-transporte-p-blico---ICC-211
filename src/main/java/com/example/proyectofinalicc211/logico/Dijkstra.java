package com.example.proyectofinalicc211.logico;

import java.util.*;

public class Dijkstra {
    private Map<UUID, List<Ruta>> grafo;
    private Map<UUID, Parada> paradas;

    public Dijkstra(Map<UUID, List<Ruta>> grafo, Map<UUID, Parada> paradas) {
        this.grafo = grafo;
        this.paradas = paradas;
    }

    public Camino calcCaminoById(UUID origen, UUID destino, String parametro) {
        Map<UUID, Double>distancias = new HashMap<>();
        Map<UUID, UUID> prev = new HashMap<>();
        Set<UUID> vertices = paradas.keySet();
        for (UUID nodo : vertices) {
            distancias.put(nodo, Double.MAX_VALUE);
        }
        distancias.put(origen, (double)0);
        PriorityQueue<Map.Entry<UUID, Double>> cola = new PriorityQueue<>(Map.Entry.comparingByValue());
        cola.add(new AbstractMap.SimpleEntry<>(origen, distancias.get(origen)));

        while(!cola.isEmpty()){
            Map.Entry<UUID, Double> entrada = cola.poll();
            UUID nodo = entrada.getKey();
            double peso = entrada.getValue();
            if (peso != distancias.getOrDefault(nodo, Double.MAX_VALUE)) continue;
            if (nodo.equals(destino) || peso==Double.MAX_VALUE) break;
            actualizarRutasVecinas(nodo, peso, distancias, prev, cola, parametro);
        }

        double total = distancias.getOrDefault(destino, Double.MAX_VALUE);
        return crearCamino(total, destino, prev);
    }

    private Camino crearCamino(double total, UUID destino, Map<UUID, UUID> prev){
        if(total == Double.MAX_VALUE){
            return new Camino(Collections.emptyList(), Double.MAX_VALUE);
        }
        LinkedList<Parada> camino = new LinkedList<>();
        UUID act = destino;
        while(act!=null){
            Parada previo = paradas.get(act);
            camino.addFirst(previo);
            act = prev.get(act);
        }

        return new Camino(camino, total);
    }

    private void actualizarRutasVecinas(UUID nodo, double peso, Map<UUID, Double>distancias, Map<UUID, UUID>prev, PriorityQueue<Map.Entry<UUID, Double>> cola, String parametro){
        List<Ruta> vecinos = grafo.getOrDefault(nodo, Collections.emptyList());
        for (Ruta r : vecinos) {
            UUID v = r.getId_destino();
            double p = getPesoRuta(r, parametro);
            if (p < 0) continue;

            double alt = peso + p;
            if (alt < distancias.getOrDefault(v, Double.MAX_VALUE)) {
                distancias.put(v, alt);
                prev.put(v, nodo);
                cola.add(new AbstractMap.SimpleEntry<>(v, alt));
            }
        }
    }


    public Camino calCamino(Parada origen, Parada destino, String peso) {
        return calcCaminoById(origen.getId(), destino.getId(), peso);
    }

    private double getPesoRuta(Ruta r, String peso) {
        if (r == null) return -1;
        if (peso.equalsIgnoreCase("distancia")) {
            return r.getDistancia();
        } else if (peso.equalsIgnoreCase("costo")) {
            return r.getCosto();
        } else {
            return r.getTiempo();
        }
    }

    public void setGrafo(Map<UUID, List<Ruta>> grafo) {
        this.grafo = grafo;
    }

    public Map<UUID, Parada> getParadas() {
        return paradas;
    }

    public void setParadas(Map<UUID, Parada> paradas) {
        this.paradas = paradas;
    }
}
