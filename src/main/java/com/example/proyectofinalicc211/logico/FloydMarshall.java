package com.example.proyectofinalicc211.logico;

import java.util.*;

public class FloydMarshall {

    private Map<UUID, List<Ruta>> grafo;
    private Map<UUID, Parada> paradas;
    private ArrayList<Parada> paradasAux;
    private Map<UUID, Integer> indices;
    private Map<Integer, UUID> ids;

    public FloydMarshall(Map<UUID, List<Ruta>> grafo, Map<UUID, Parada> paradas) {
        this.grafo = grafo;
        this.paradas = paradas;
        this.paradasAux = new ArrayList<>(paradas.values());
        this.indices = new HashMap<>(paradasAux.size());
        this.ids = new HashMap<>(paradasAux.size());
        for(int i = 0; i < paradasAux.size(); i++){
            indices.put(paradasAux.get(i).getId(), i);
            ids.put(i, paradasAux.get(i).getId());
        }
    }

    public Camino calcularCamino(Parada origen, Parada destino, String parametro){
        return calcularCamino(origen.getId(), destino.getId(), parametro);
    }

    public Camino calcularCamino(UUID origen, UUID dest, String parametro) {
        int n = paradasAux.size();
        double[][] pesos = new double[n][n];
        //ArrayList<ArrayList<Parada>> recorridos = new ArrayList<>(n);
        int[][] recorridos = new int[n][n];

        initDistancias(pesos, parametro);
        initRecorridos(recorridos, pesos);

        for(int ind = 0; ind < n; ind++){
            for(int j = 0; j < n; j++){
                if(j == ind || pesos[ind][j] == Double.MAX_VALUE) continue;
                for(int i = 0; i < n; i++){
                    if(i == ind || pesos[i][ind] == Double.MAX_VALUE) continue;
                    double sum = pesos[ind][j] + pesos[i][ind];
                    if(sum < pesos[i][j]){
                        pesos[i][j] = sum;
                        recorridos[i][j] = recorridos[i][ind];
                    }
                }
            }
        }

        return crearCamino(pesos, recorridos, origen, dest);
    }

    private Camino crearCamino(double[][] pesos, int[][] recorridos, UUID origen, UUID destino){
        int indOrigen = indices.get(origen);
        int indDestino = indices.get(destino);
        if(pesos[indOrigen][indDestino] == Double.MAX_VALUE){
            return new Camino(Collections.emptyList(), Double.MAX_VALUE);
        }
        LinkedList<Parada>camino = new LinkedList<>();
        int act = indOrigen;
        camino.add(paradasAux.get(act));
        int j = indDestino;
        while(act != j){
            int sig = recorridos[act][j];
            if(sig == -1){
                return new Camino(Collections.emptyList(), Double.MAX_VALUE);
            }
            act = sig;
            camino.add(paradasAux.get(act));
        }
        return new Camino(camino, pesos[indOrigen][indDestino]);
    }

    private void initRecorridos(int[][] recorridos, double[][] pesos){
        for(int j = 0; j < recorridos.length; j++){
            for(int i = 0; i < recorridos.length; i++){
                if(pesos[i][j] == Double.MAX_VALUE)recorridos[i][j] = -1;
                else recorridos[i][j] = j;
            }
        }
    }

    private void initDistancias(double[][] pesos, String parametro) {
        for(int i = 0; i < paradasAux.size(); i++){
            for(int j = 0; j < paradasAux.size(); j++){
                if(i==j) pesos[i][j] = 0;
                else pesos[i][j] = Double.MAX_VALUE;
            }
        }
        for(int i = 0; i < paradasAux.size(); i++){
            List<Ruta>rutas = grafo.getOrDefault(paradasAux.get(i).getId(), Collections.emptyList());
            for(Ruta ruta : rutas){
                if(ruta==null) continue;
                int j = indices.get(ruta.getId_destino());
                double peso = getPesoRuta(ruta, parametro);
                if(peso < pesos[i][j]){
                    pesos[i][j] = peso;
                }
            }
        }
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
}
