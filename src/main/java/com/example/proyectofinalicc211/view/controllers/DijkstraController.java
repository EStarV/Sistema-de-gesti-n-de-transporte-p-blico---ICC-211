package com.example.proyectofinalicc211.view.controllers;

import com.example.proyectofinalicc211.model.Camino;
import com.example.proyectofinalicc211.model.ListaAdyacencia;
import com.example.proyectofinalicc211.model.Parada;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import java.util.List;

public class DijkstraController extends CaminosCortosController{
    @FXML
    public void calcular(ActionEvent event) {
        Parada origen = paradas.get(cbxOrigen.getSelectionModel().getSelectedIndex());
        Parada destino = paradas.get(cbxDestino.getSelectionModel().getSelectedIndex());
        String parametro = cbxParametro.getSelectionModel().getSelectedItem();
        List<Camino> rutas = ListaAdyacencia.getInstancia().rutaMasCortayAlterna(origen, destino, parametro);
        if(rutas == null || rutas.isEmpty() || rutas.get(0).getDistancia()==Double.MAX_VALUE){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Camino no encontrado");
            alert.setHeaderText(null);
            alert.setContentText("No se ha encontrado un camino que una ambas paradas");
            alert.showAndWait();
            return;
        }
        /// Esto es provisional
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("El camino mas corto");
        StringBuilder camino = new StringBuilder();
        for(int i = 0; i < rutas.get(0).getCamino().size(); i++)
            camino.append(rutas.get(0).getCamino().get(i).getNombre());
        camino.append("-").append(rutas.get(0).getDistancia());
        alert.setHeaderText(String.format("El camino mas corto entre: %s y %s es:",
                origen.getNombre(), destino.getNombre()));
        alert.setContentText(camino.toString());
        alert.showAndWait();
        /// Aqui se le debe de pasar las rutas mas cortas al visualizador del grafo
        closeWindow();
    }
}