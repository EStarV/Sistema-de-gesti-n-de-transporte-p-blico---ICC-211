package com.example.proyectofinalicc211.logico;

import java.util.*;

public class Dijkstra extends AlgoritmoRutaCorta {
    private Map<UUID, Parada> paradas;

    public Dijkstra(Map<UUID, List<Ruta>> grafo, Map<UUID, Parada> paradas) {
        super(grafo);
        this.paradas = paradas;
    }

    public Camino calcCaminoById(UUID origen, UUID destino, String parametro, UUID evitar) {
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
            if (evitar != null) {
                if (evitar.equals(origen)) {
                    return new Camino(Collections.emptyList(), Double.MAX_VALUE, Collections.emptyList());
                }
                if (evitar.equals(destino)) {
                    return new Camino(Collections.emptyList(), Double.MAX_VALUE, Collections.emptyList());
                }
            }
            Map.Entry<UUID, Double> entrada = cola.poll();
            UUID nodo = entrada.getKey();
            if(nodo.equals(evitar)) continue;
            double peso = entrada.getValue();
            if (peso != distancias.getOrDefault(nodo, Double.MAX_VALUE)) continue;
            if (nodo.equals(destino) || peso==Double.MAX_VALUE) break;
            actualizarRutasVecinas(nodo, peso, distancias, prev, cola, parametro, evitar);
        }

        double total = distancias.getOrDefault(destino, Double.MAX_VALUE);
        return crearCamino(total, destino, prev, parametro);
    }

    private Camino crearCamino(double total, UUID destino, Map<UUID, UUID> prev, String parametro) {
        if(total == Double.MAX_VALUE){
            return new Camino(Collections.emptyList(), total, Collections.emptyList());
        }
        LinkedList<Parada> camino = new LinkedList<>();
        UUID act = destino;
        while(act!=null){
            Parada previo = paradas.get(act);
            camino.addFirst(previo);
            act = prev.get(act);
        }

        LinkedList<Ruta> rutasAux = reconstruirRutas(camino, parametro);

        return new Camino(camino, total, rutasAux);
    }

    private void actualizarRutasVecinas(UUID nodo, double peso, Map<UUID, Double>distancias, Map<UUID, UUID>prev, PriorityQueue<Map.Entry<UUID, Double>> cola, String parametro, UUID evitar) {
        List<Ruta> vecinos = grafo.getOrDefault(nodo, Collections.emptyList());
        for (Ruta r : vecinos) {
            if(r == null) continue;
            UUID v = r.getId_destino();
            if(evitar != null && v.equals(evitar)) continue;
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

    @Override
    public Camino calcCamino(Parada origen, Parada destino, String peso) {
        return calcCaminoById(origen.getId(), destino.getId(), peso, null);
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

    public List<Camino> caminosAlternos(Parada origen, Parada destino, String parametro) {
        Camino principal = calcCaminoById(origen.getId(), destino.getId(), parametro, null);
        int cant = 3;
        ArrayList<Camino> candidatos = new ArrayList<>(cant);
        if(principal == null || principal.getRutas().isEmpty())
            return Collections.emptyList();
        candidatos.add(principal);

        PriorityQueue<Camino> cola = new PriorityQueue<>(Comparator.comparingDouble(Camino::getDistancia));
        for(int i=1; i<principal.getCamino().size()-1; i++){
            UUID evitar = principal.getCamino().get(i).getId();
            Camino camino = calcCaminoById(origen.getId(), destino.getId(), parametro, evitar);
            if(camino.getDistancia()==Double.MAX_VALUE)continue;
            if(camino.equals(principal)) continue;
            cola.add(camino);
        }
        int i = 0;
        while(!cola.isEmpty() && i < cant){
            Camino camino = cola.poll();
            if(camino.getDistancia() == Double.MAX_VALUE) break;
            if(camino == null) continue;
            if(candidatos.contains(camino)) continue;
            candidatos.add(camino);
            i++;
        }
        return candidatos;
    }
}
