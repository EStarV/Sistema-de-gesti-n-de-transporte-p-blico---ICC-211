package com.example.proyectofinalicc211.viewmodel;

import com.example.proyectofinalicc211.model.Camino;
import com.example.proyectofinalicc211.model.ListaAdyacencia;
import com.example.proyectofinalicc211.model.Parada;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class VistaModeloPrincipal {
    private ListaAdyacencia modelo = ListaAdyacencia.getInstancia();
    private ObservableList<Parada> paradasObservables = FXCollections.observableArrayList();
    private StringProperty mensajeResultado = new SimpleStringProperty("Bienvenido");
    private List<Camino> ultimosCaminos; // Para resaltar en la view

    public VistaModeloPrincipal() {
        paradasObservables.addAll(modelo.getParadas().values());
    }

    public void calcularRuta(Parada origen, Parada destino, String parametro, String algoritmo) {
        ultimosCaminos = modelo.rutaMasCortayAlterna(origen, destino, parametro, algoritmo);
        if (ultimosCaminos.isEmpty() || ultimosCaminos.get(0).getDistancia() == Double.MAX_VALUE) {
            mensajeResultado.set("No hay ruta disponible");
        } else {
            StringBuilder sb = new StringBuilder("Ruta principal (" + algoritmo + "): ");
            ultimosCaminos.get(0).printCamino(); // Para consola, opcional
            sb.append(ultimosCaminos.get(0).getDistancia()).append("\nAlternativas:\n");
            for (int i = 1; i < ultimosCaminos.size(); i++) {
                sb.append("Alt ").append(i).append(": ").append(ultimosCaminos.get(i).getDistancia()).append("\n");
            }
            mensajeResultado.set(sb.toString());
        }
    }

    public ObservableList<Parada> getParadas() {
        return paradasObservables;
    }

    public StringProperty mensajeResultadoProperty() {
        return mensajeResultado;
    }

    public List<Camino> getUltimosCaminos() {
        return ultimosCaminos;
    }
}