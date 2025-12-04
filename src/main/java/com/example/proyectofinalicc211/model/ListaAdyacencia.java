package com.example.proyectofinalicc211.model;

import com.example.proyectofinalicc211.data.ParadaDAO;
import com.example.proyectofinalicc211.data.RutaDAO;
import java.util.*;

public class ListaAdyacencia {
    private Map<UUID, List<Ruta>> grafo = new HashMap<>();
    private Map<UUID, Parada> paradas = new HashMap<>();
    private Map<String, UUID> nombres = new HashMap<>();
    private Map<UUID, List<Ruta>> entrantes = new HashMap<>();

    private static ListaAdyacencia instancia = null;

    private ListaAdyacencia() {
        cargarDesdeBD();
    }

    public static ListaAdyacencia getInstancia() {
        if (instancia == null) {
            instancia = new ListaAdyacencia();
        }
        return instancia;
    }

    private void cargarDesdeBD() {
        ParadaDAO pdao = new ParadaDAO();
        RutaDAO rdao = new RutaDAO();
        List<Parada> paradasList = pdao.obtenerTodas();
        addParadasLista(paradasList);
        List<Ruta> rutasList = rdao.obtenerTodas();
        addRutasLista(rutasList);
    }

    public void addParadasLista(List<Parada> lista) {
        for (Parada p : lista) addParada(p);
    }

    public void addRutasLista(List<Ruta> lista) {
        for (Ruta r : lista) addRuta(r);
    }

    public boolean addParada(Parada parada) {
        if (parada == null || parada.getId() == null) return false;
        if (paradas.containsKey(parada.getId())) return false;

        paradas.put(parada.getId(), parada);
        grafo.put(parada.getId(), new LinkedList<>());
        entrantes.put(parada.getId(), new LinkedList<>());
        nombres.put(parada.getNombre().toLowerCase(), parada.getId());

        new ParadaDAO().guardar(parada);
        return true;
    }

    public boolean existeNombreParada(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) return false;
        return nombres.containsKey(nombre.toLowerCase().trim());
    }

    public boolean addRuta(Ruta ruta) {
        if (ruta == null || !grafo.containsKey(ruta.getId_origen()) || !grafo.containsKey(ruta.getId_destino())) {
            return false;
        }
        if (existeRutaEntreNodos(ruta.getId_origen(), ruta.getId_destino(), ruta.getMedio())) {
            return false;
        }

        grafo.get(ruta.getId_origen()).add(ruta);
        entrantes.get(ruta.getId_destino()).add(ruta);
        new RutaDAO().guardar(ruta);
        return true;
    }

    public boolean existeRutaEntreNodos(UUID origen, UUID destino, String medio) {
        List<Ruta> rutas = grafo.getOrDefault(origen, Collections.emptyList());
        for (Ruta r : rutas) {
            if (r.getId_destino().equals(destino) &&
                    (medio == null || r.getMedio().equalsIgnoreCase(medio))) {
                return true;
            }
        }
        return false;
    }

    // ==== FUNCIÓN QUE TE FALTABA ====
    public boolean deleteParada(Parada parada) {
        if (parada == null || parada.getId() == null) return false;

        UUID id = parada.getId();

        // Eliminar todas las rutas que salen o entran a esta parada
        List<Ruta> salientes = grafo.getOrDefault(id, Collections.emptyList());
        List<Ruta> entrantesParada = this.entrantes.getOrDefault(id, Collections.emptyList());

        // Eliminar de la base de datos y de las listas
        for (Ruta r : salientes) {
            new RutaDAO().eliminar(r.getId());
        }
        for (Ruta r : entrantesParada) {
            new RutaDAO().eliminar(r.getId());
        }

        // Eliminar de las estructuras
        grafo.remove(id);
        entrantes.remove(id);
        paradas.remove(id);
        nombres.remove(parada.getNombre().toLowerCase());

        // Eliminar también de otras listas (rutas que apuntaban aquí)
        for (List<Ruta> lista : grafo.values()) {
            lista.removeIf(r -> r.getId_destino().equals(id));
        }
        for (List<Ruta> lista : entrantes.values()) {
            lista.removeIf(r -> r.getId_origen().equals(id));
        }

        // Eliminar de la base de datos
        new ParadaDAO().eliminar(id);

        return true;
    }
    // =================================

    public boolean deleteRuta(Ruta ruta) {
        if (ruta == null) return false;
        boolean removed = grafo.getOrDefault(ruta.getId_origen(), new ArrayList<>()).remove(ruta);
        entrantes.getOrDefault(ruta.getId_destino(), new ArrayList<>()).remove(ruta);
        if (removed) new RutaDAO().eliminar(ruta.getId());
        return removed;
    }

    public Map<UUID, List<Ruta>> getEntrantes() { return entrantes; }

    public List<Ruta> rutasEntreNodos(UUID origen, UUID destino) {
        return grafo.getOrDefault(origen, Collections.emptyList())
                .stream()
                .filter(r -> r.getId_destino().equals(destino))
                .toList();
    }

    public Ruta buscarRutaConMedio(UUID origen, UUID destino, String medio) {
        return grafo.getOrDefault(origen, Collections.emptyList())
                .stream()
                .filter(r -> r.getId_destino().equals(destino) && r.getMedio().equalsIgnoreCase(medio))
                .findFirst()
                .orElse(null);
    }

    public Parada getParada(UUID id) {
        return paradas.get(id);
    }

    public boolean modificarParada(Parada parada, String nuevoNombre) {
        if (parada == null || nuevoNombre == null || nuevoNombre.trim().isEmpty()) return false;
        if (existeNombreParada(nuevoNombre) && !parada.getNombre().equalsIgnoreCase(nuevoNombre)) return false;

        nombres.remove(parada.getNombre().toLowerCase());
        parada.setNombre(nuevoNombre);
        nombres.put(nuevoNombre.toLowerCase(), parada.getId());
        new ParadaDAO().guardar(parada);
        return true;
    }

    public boolean modifNodosRuta(Parada newOrigen, Parada newDestino, Ruta ruta) {
        if (ruta == null || newOrigen == null || newDestino == null) return false;

        grafo.getOrDefault(ruta.getId_origen(), new ArrayList<>()).remove(ruta);
        entrantes.getOrDefault(ruta.getId_destino(), new ArrayList<>()).remove(ruta);

        ruta.setId_origen(newOrigen.getId());
        ruta.setId_destino(newDestino.getId());

        grafo.computeIfAbsent(newOrigen.getId(), k -> new LinkedList<>()).add(ruta);
        entrantes.computeIfAbsent(newDestino.getId(), k -> new LinkedList<>()).add(ruta);

        new RutaDAO().guardar(ruta);
        return true;
    }

    public boolean modificarRuta(Ruta ruta, int tiempo, double distancia, int costo, int transbordos, String medio) {
        if (ruta == null) return false;
        ruta.setTiempo(tiempo);
        ruta.setDistancia(distancia);
        ruta.setCosto(costo);
        ruta.setTransbordos(transbordos);
        ruta.setMedio(medio);
        new RutaDAO().guardar(ruta);
        return true;
    }

    public Camino dijkstra(Parada origen, Parada destino, String parametro) {
        Dijkstra d = new Dijkstra(grafo, paradas);
        return d.calcCamino(origen, destino, parametro.toLowerCase());
    }

    public Camino floydMarshall(Parada origen, Parada destino, String parametro) {
        FloydMarshall fm = new FloydMarshall(grafo, paradas);
        return fm.calcCamino(origen, destino, parametro.toLowerCase());
    }

    public List<Camino> rutaMasCortayAlterna(Parada origen, Parada destino, String parametro, String algoritmo) {
        algoritmo = algoritmo == null ? "dijkstra" : algoritmo.toLowerCase();
        if ("dijkstra".equals(algoritmo)) {
            Dijkstra d = new Dijkstra(grafo, paradas);
            return d.caminosAlternos(origen, destino, parametro.toLowerCase());
        } else {
            FloydMarshall fm = new FloydMarshall(grafo, paradas);
            Camino principal = fm.calcCamino(origen, destino, parametro.toLowerCase());
            List<Camino> lista = new ArrayList<>();
            if (principal.getDistancia() == Double.MAX_VALUE) return lista;
            lista.add(principal);

            for (int i = 1; i < principal.getCamino().size() - 1; i++) {
                UUID evitar = principal.getCamino().get(i).getId();
                Dijkstra aux = new Dijkstra(grafo, paradas);
                Camino alt = aux.calcCaminoById(origen.getId(), destino.getId(), parametro, evitar);
                if (alt.getDistancia() != Double.MAX_VALUE && !lista.contains(alt)) {
                    lista.add(alt);
                }
            }
            return lista;
        }
    }

    public List<Camino> rutaMasCortayAlterna(Parada origen, Parada destino, String parametro) {
        return rutaMasCortayAlterna(origen, destino, parametro, "dijkstra");
    }

    public Map<UUID, List<Ruta>> getGrafo() { return grafo; }
    public Map<UUID, Parada> getParadas() { return paradas; }
}