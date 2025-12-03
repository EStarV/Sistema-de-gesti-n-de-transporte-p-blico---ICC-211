package com.example.proyectofinalicc211.logico;

import java.util.*;

public class ListaAdyacencia {
    private Map<UUID, List<Ruta>> grafo = new HashMap<>();
    private Map<UUID, Parada> paradas = new HashMap<>();

    //Mapas para poder hacer comprobaciones
    private Map<String, UUID> nombres = new HashMap<>();
    private Map<UUID, List<Ruta>> entrantes = new HashMap<>();

    public Map<UUID, List<Ruta>> getEntrantes(){
        return entrantes;
    }

    public ListaAdyacencia() {}

    private static ListaAdyacencia listaAdyacencia = null;
    public static ListaAdyacencia getInstancia() {
        if (listaAdyacencia == null) {
            listaAdyacencia = new ListaAdyacencia();
        }
        return listaAdyacencia;
    }

    public void addParada(Parada parada){
        if(parada == null || parada.getId() == null) return;
        paradas.put(parada.getId(), parada);
        grafo.put(parada.getId(), new LinkedList<>());
        //Esto puede ser temporal para obtener comprobaciones en O(1)
        nombres.put(parada.getNombre().toLowerCase(), parada.getId());
        entrantes.put(parada.getId(), new LinkedList<>());
    }

    public boolean addRuta(Ruta ruta){
        if(ruta == null || ruta.getId() == null || ruta.getId_origen() == null || ruta.getId_destino() == null) return false;
        if(existeRutaEntreNodos(ruta.getId_origen(), ruta.getId_destino(), ruta.getMedio())) return false;
        if(grafo.containsKey(ruta.getId_origen())){
            grafo.get(ruta.getId_origen()).add(ruta);
            entrantes.get(ruta.getId_destino()).add(ruta);
            return true;
        }
        return false;
    }

    public boolean deleteParada(Parada parada){
        if(parada == null || parada.getId() == null) return false;
        return deleteParadaById(parada.getId(), parada.getNombre().toLowerCase());
    }

    public boolean deleteParadaById(UUID idParada, String nombre){
        if(idParada == null) return false;
        if(paradas.containsKey(idParada)){
            List<Ruta> salen = grafo.get(idParada);
            List<Ruta> entran = entrantes.get(idParada);
            //Eliminar en grafo salientes con destino al eliminado
            if(entran != null && !entran.isEmpty()){
                for(Ruta ruta : entran){
                    grafo.get(ruta.getId_origen()).remove(ruta);
                }
            }
            //Elimina las rutas que llegan al nodo eliminado
            if(!(salen == null || salen.isEmpty())){
                for(Ruta ruta : salen){
                    entrantes.get(ruta.getId_destino()).remove(ruta);
                }
            }
            grafo.remove(idParada);
            entrantes.remove(idParada);
            paradas.remove(idParada);
            if(nombre != null && !nombre.isEmpty()){
                nombres.remove(nombre.toLowerCase());
            }
            return true;
        }
        return false;
    }

    public boolean deleteRuta(Ruta ruta) {
        if(ruta == null || ruta.getId() == null || ruta.getId_origen() == null || ruta.getId_destino() == null) return false;

        UUID id_Origen = ruta.getId_origen();
        UUID id_Destino = ruta.getId_destino();

        List<Ruta> rutas = grafo.get(id_Origen);
        List<Ruta> entran = entrantes.get(id_Destino);

        boolean eliminarSal = false;
        boolean eliminarEnt = false;
        if(rutas != null && !rutas.isEmpty()){
            eliminarSal = rutas.removeIf(r -> r.getId().equals(ruta.getId()));
        }
        if(entran != null && !entran.isEmpty()){
            eliminarEnt = entran.removeIf(r -> r.getId().equals(ruta.getId()));
        }
        return eliminarSal || eliminarEnt;
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

    public Ruta buscarRutaConMedio(UUID origen, UUID destino, String medio){
        List<Ruta> rutas = grafo.get(origen);
        Ruta ruta = null;
        if(rutas != null && !rutas.isEmpty()){
            for(Ruta r: rutas){
                if(r.getId_destino().equals(destino) && r.getMedio().equalsIgnoreCase(medio)){
                    ruta = r;
                    break;
                }
            }
        }
        return ruta;

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
        List<Camino> camino = dijkstra.caminosAlternos(origen, destino, parametro);
        if(camino == null || camino.isEmpty())
            return null;
        return (ArrayList<Camino>) camino;
    }

    public boolean modificarParada(Parada parada, String new_nombre){
        if(parada == null || parada.getId() == null) return false;
        if(!paradas.containsKey(parada.getId())) return false;
        if(new_nombre == null || new_nombre.isEmpty()) return false;
        String nombreViejo = parada.getNombre();
        paradas.get(parada.getId()).setNombre(new_nombre);
        nombres.remove(nombreViejo);
        nombres.put(new_nombre, parada.getId());
        return true;
    }

    public boolean existeNombreParada(String nombre){
        if(nombres.containsKey(nombre.toLowerCase())) return true;
        return false;
    }

    //Modificar los atributos de la ruta
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

    //Modificar los nodos de una ruta
    public boolean modifNodosRuta(Parada new_origen, Parada new_destino, Ruta ruta){
        if(new_origen == null || new_destino == null) return false;
        if(!paradas.containsKey(new_origen.getId()) || !paradas.containsKey(new_destino.getId())) return false;
        if(existeRutaEntreNodos(new_origen.getId(), new_destino.getId(), ruta.getMedio())) return false;

        Ruta r = getRuta(ruta.getId_origen(), ruta.getId());
        if(r == null) return false;
        List<Ruta> salViejo = grafo.get(r.getId_origen());
        if (salViejo != null) {
            salViejo.removeIf(x -> x.getId().equals(r.getId()));
        }
        List<Ruta> entViejo = entrantes.get(r.getId_destino());
        if (entViejo != null) {
            entViejo.removeIf(x -> x.getId().equals(r.getId()));
        }

        grafo.get(r.getId_origen()).remove(r);
        entrantes.get(r.getId_destino()).remove(r);

        r.setId_origen(new_origen.getId());
        r.setId_destino(new_destino.getId());

        grafo.get(new_origen.getId()).add(ruta);
        entrantes.get(new_destino.getId()).add(ruta);
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
        if(rutas == null) return false;
        for(Ruta r : rutas){
            if(r.getId_destino().equals(destino) && r.getMedio().equals(medio)){
                return true;
            }
        }
        return false;
    }

    public List<Ruta> rutasEntreNodos(UUID origen, UUID destino){
        List<Ruta>rutas = grafo.get(origen);
        List<Ruta>rutasAux = new ArrayList<>();
        if(rutas == null) return rutasAux;
        for(Ruta r : rutas){
            if(r.getId_destino().equals(destino)){
                rutasAux.add(r);
            }
        }
        return rutasAux;
    }
}
