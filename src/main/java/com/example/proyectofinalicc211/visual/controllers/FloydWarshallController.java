package com.example.proyectofinalicc211.visual.controllers;

import com.example.proyectofinalicc211.logico.Camino;
import com.example.proyectofinalicc211.logico.ListaAdyacencia;
import com.example.proyectofinalicc211.logico.Parada;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class FloydWarshallController extends CaminosCortosController{

    @FXML
    public void calcular(){
        Parada origen = paradas.get(cbxOrigen.getSelectionModel().getSelectedIndex());
        Parada destino = paradas.get(cbxDestino.getSelectionModel().getSelectedIndex());
        String parametro = cbxParametro.getSelectionModel().getSelectedItem();
        Camino camino = ListaAdyacencia.getInstancia().floydMarshall(origen, destino, parametro);

        if(camino == null || camino.getDistancia()==Double.MAX_VALUE || camino.getRutas().isEmpty()){
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
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < camino.getCamino().size(); i++)
            sb.append(camino.getCamino().get(i).getNombre());
        sb.append("-").append(camino.getDistancia());
        alert.setHeaderText(String.format("El camino mas corto entre: %s y %s es:",
                origen.getNombre(), destino.getNombre()));
        alert.setContentText(sb.toString());
        alert.showAndWait();
        /// Aqui se le debe de pasar la ruta mas cortas al visualizador del grafo
        closeWindow();
    }
}
