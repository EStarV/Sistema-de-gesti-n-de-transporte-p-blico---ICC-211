package com.example.proyectofinalicc211.visual.controllers;

import com.example.proyectofinalicc211.logico.ListaAdyacencia;
import com.example.proyectofinalicc211.logico.Parada;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ResourceBundle;

public class CaminosCortosController implements Initializable {
    protected List<Parada> paradas;
    protected String[] parametros = {"Tiempo","Distancia","Costo"};
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbxParametro.getItems().setAll(parametros);
        paradas = new ArrayList<>(new LinkedHashSet<>(ListaAdyacencia.getInstancia().getParadas().values()));
        String[] paradasNames = new String[paradas.size()];
        for (int i = 0; i < paradas.size(); i++) {
            paradasNames[i] = paradas.get(i).getNombre();
        }
        cbxOrigen.getItems().setAll(paradasNames);
        cbxDestino.getItems().setAll(paradasNames);
        btnCalcular.setDisable(true);

        cbxOrigen.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateCalcular();
            }
        });

        cbxDestino.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                updateCalcular();
            }
        });

        cbxParametro.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                updateCalcular();
            }
        });
    }

    protected void updateCalcular(){
        if(cbxOrigen.getSelectionModel().getSelectedItem()!=null &&
                cbxDestino.getSelectionModel().getSelectedItem()!=null &&
                cbxParametro.getSelectionModel().getSelectedItem()!=null){
            btnCalcular.setDisable(false);
        }
        else{
            btnCalcular.setDisable(true);
        }
    }

    @FXML
    protected Button btnCalcular;

    @FXML
    protected Button btnCancelar;

    @FXML
    protected ComboBox<String> cbxDestino;

    @FXML
    protected ComboBox<String> cbxOrigen;

    @FXML
    protected ChoiceBox<String> cbxParametro;

    @FXML
    protected AnchorPane scsCalcPane;

    @FXML
    public void cancelar(ActionEvent event) {
        closeWindow();
    }

    protected void closeWindow() {
        try {
            Stage stage = null;
            if (scsCalcPane != null && scsCalcPane.getScene() != null) {
                stage = (Stage) scsCalcPane.getScene().getWindow();
            } else if (btnCancelar != null && btnCancelar.getScene() != null) {
                stage = (Stage) btnCancelar.getScene().getWindow();
            } else if (btnCalcular != null && btnCalcular.getScene() != null) {
                stage = (Stage) btnCalcular.getScene().getWindow();
            }
            if (stage != null) {
                stage.close();
            } else {
                Platform.runLater(() -> {
                    if (scsCalcPane != null && scsCalcPane.getScene() != null) {
                        ((Stage) scsCalcPane.getScene().getWindow()).close();
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
