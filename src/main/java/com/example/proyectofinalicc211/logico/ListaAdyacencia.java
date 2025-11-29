package com.example.proyectofinalicc211.logico;

import java.util.*;

public class ListaAdyacencia {
    private Map<UUID, List<Ruta>> grafo = new HashMap<>();
    private Map<UUID, Parada> paradas = new HashMap<>();

    public ListaAdyacencia() {}

    public void addParada(Parada parada){
        if(parada == null || parada.getId() == null) return;
        paradas.put(parada.getId(), parada);
        grafo.put(parada.getId(), new LinkedList<>());
        grafo.put(parada.getId(), grafo.get(parada.getId()));
    }

    public boolean addRuta(Ruta ruta){
        if(ruta == null || ruta.getId() == null || ruta.getId_origen() == null || ruta.getId_destino() == null) return false;
        if(existeRutaEntreNodos(ruta.getId_origen(), ruta.getId_destino(), ruta.getMedio())) return false;
        if(grafo.containsKey(ruta.getId_origen())){
            grafo.get(ruta.getId_origen()).add(ruta);
            return true;
        }
        return false;
    }

    public boolean deleteParada(Parada parada){
        if(parada == null || parada.getId() == null) return false;
        return deleteParadaById(parada.getId());
    }

    public boolean deleteParadaById(UUID id){
        if(paradas.containsKey(id)){
            grafo.remove(id);
            for(List<Ruta> rutas : grafo.values()){
                rutas.removeIf(r -> r.getId_destino().equals(id));
            }
            paradas.remove(id);
            return true;
        }
        return false;
    }

    public boolean deleteRuta(Ruta ruta) {
        if(ruta == null || ruta.getId() == null || ruta.getId_origen() == null || ruta.getId_destino() == null) return false;

        UUID id_Origen = ruta.getId_origen();
        List<Ruta> rutas = grafo.get(id_Origen);
        if(rutas == null) return false;
        return rutas.removeIf(r -> r.getId().equals(ruta.getId()));
    }

    public Map<UUID, Parada> getParadas(){
        return paradas;
    }

    public void setParadas(Map<UUID, Parada> paradas){
        this.paradas = paradas;
    }

    public void addRutasLista(List<Ruta> rutas){
        for(Ruta ruta : rutas){
            addRuta(ruta);
        }
    }

    public void addParadasLista(List<Parada> paradas){
        for(Parada parada : paradas){
            if(parada == null) continue;
            addParada(parada);
        }
    }

    public Map<UUID, List<Ruta>> getGrafo(){
        return grafo;
    }

    public void setGrafo(Map<UUID, List<Ruta>> grafo){
        this.grafo = grafo;
    }

    public Parada getParada(UUID id){
        if(grafo.containsKey(id)){
            return paradas.get(id);
        }
        return null;
    }

    public Camino dijkstra(Parada origen, Parada destino, String parametro){
        Dijkstra dijkstra = new Dijkstra(grafo, paradas);
        return dijkstra.calcCamino(origen, destino, parametro);
    }

    public Camino floydMarshall(Parada origen, Parada destino, String parametro){
        FloydMarshall floyd = new FloydMarshall(grafo, paradas);
        return floyd.calcCamino(origen, destino, parametro);
    }

    public Camino floydMarshall(UUID origen, UUID destino, String parametro){
        FloydMarshall floyd = new FloydMarshall(grafo, paradas);
        return floyd.calcularCamino(origen, destino, parametro);
    }

    public Camino dijkstraID(UUID origen, UUID destino, String parametro){
        Dijkstra dijkstra = new Dijkstra(grafo, paradas);
        return dijkstra.calcCaminoById(origen, destino, parametro, null);
    }

    public ArrayList<Camino> rutaMasCortayAlterna(Parada origen, Parada destino, String parametro){
        Dijkstra dijkstra = new Dijkstra(grafo, paradas);
        return (ArrayList<Camino>) dijkstra.caminosAlternos(origen, destino, parametro);
    }

    public boolean modificarParada(Parada parada, String new_nombre){
        if(parada == null || parada.getId() == null) return false;
        if(!paradas.containsKey(parada.getId())) return false;
        paradas.get(parada.getId()).setNombre(new_nombre);
        return true;
    }

    public boolean modificarRuta(Ruta ruta, int new_tiempo, double new_distancia, int new_costo, int new_transbordos, String new_medio){
        if(ruta == null || ruta.getId() == null) return false;

        List<Ruta>rutas = grafo.get(ruta.getId_origen());
        Ruta r = getRuta(ruta.getId_origen(), ruta.getId());
        if(r == null) return false;
        r.setTiempo(new_tiempo);
        r.setDistancia(new_distancia);
        r.setCosto(new_costo);
        r.setTransbordos(new_transbordos);
        if(new_medio != null){
            r.setMedio(new_medio);
        }
        return true;
    }

    public boolean modifNodosRuta(Parada new_origen, Parada new_destino, Ruta ruta){
        if(new_origen == null || new_destino == null) return false;
        if(!paradas.containsKey(new_origen.getId()) || !paradas.containsKey(new_destino.getId())) return false;
        if(existeRutaEntreNodos(new_origen.getId(), new_destino.getId(), ruta.getMedio())) return false;

        Ruta r = getRuta(ruta.getId_origen(), ruta.getId());
        if(r == null) return false;
        grafo.get(ruta.getId_origen()).remove(r);
        r.setId_origen(new_origen.getId());
        r.setId_destino(new_destino.getId());
        grafo.get(new_origen.getId()).add(ruta);
        return true;
    }

    public Ruta getRuta(UUID origen, UUID id){
        if(grafo.containsKey(origen)){
            List<Ruta>rutas = grafo.get(origen);
            for(Ruta r : rutas){
                if(r.getId().equals(id)){
                    return r;
                }
            }
        }
        return null;
    }

    public boolean existeRutaEntreNodos(UUID origen, UUID destino, String medio){
        List<Ruta>rutas = grafo.get(origen);
        for(Ruta r : rutas){
            if(r.getId_destino().equals(destino) && r.getMedio().equals(medio)){
                return true;
            }
        }
        return false;
    }

}
