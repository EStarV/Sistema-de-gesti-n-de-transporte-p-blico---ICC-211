package com.example.proyectofinalicc211.view.controllers;

import com.example.proyectofinalicc211.model.ListaAdyacencia;
import com.example.proyectofinalicc211.model.Parada;
import com.example.proyectofinalicc211.model.Ruta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class AddRutaController implements Initializable {
    private String[] medios= {"Metro", "Monorriel", "Autobus", "Carro", "Bicicleta", "Tranvia", "Teleferico"};
    List<Parada> paradas;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbxMedio.getItems().setAll(medios);
        paradas = new ArrayList<>(new LinkedHashSet<>(ListaAdyacencia.getInstancia().getParadas().values()));
        String[] paradasNames = new String[paradas.size()];
        for (int i = 0; i < paradas.size(); i++) {
            paradasNames[i] = paradas.get(i).getNombre();
        }
        cbxParadaOrigen.getItems().setAll(paradasNames);
        cbxParadaDestino.getItems().setAll(paradasNames);

        SpinnerValueFactory<Integer> factoryTime = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 720);
        factoryTime.setValue(1);
        spnTiempo.setEditable(true);
        spnTiempo.setValueFactory(factoryTime);

        SpinnerValueFactory<Integer> factoryCosto = new SpinnerValueFactory.IntegerSpinnerValueFactory(15, 1500);
        factoryCosto.setValue(15);
        spnCosto.setEditable(true);
        spnCosto.setValueFactory(factoryCosto);

        SpinnerValueFactory<Double> factoryDistancia = new SpinnerValueFactory.DoubleSpinnerValueFactory(1.0, 300.0);
        factoryDistancia.setValue(1.0);
        spnDistancia.setEditable(true);
        spnDistancia.setValueFactory(factoryDistancia);
    }

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnGuardar;

    @FXML
    private AnchorPane addScene;

    @FXML
    private ChoiceBox<String> cbxMedio;

    @FXML
    private ChoiceBox<String> cbxParadaDestino;

    @FXML
    private ChoiceBox<String> cbxParadaOrigen;

    @FXML
    private Label lblParadaOrigen;

    @FXML
    private Spinner<Integer> spnCosto;

    @FXML
    private Spinner<Double> spnDistancia;

    @FXML
    private Spinner<Integer> spnTiempo;

    @FXML
    private void add(ActionEvent event) {
        int indOrigen = cbxParadaOrigen.getSelectionModel().getSelectedIndex();
        int indDestino = cbxParadaDestino.getSelectionModel().getSelectedIndex();
        UUID origen = paradas.get(indOrigen).getId();
        UUID destino = paradas.get(indDestino).getId();
        if(camposVacios()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos vacios");
            alert.setHeaderText(null);
            alert.setContentText("No pueden haber campos vacios para guardar");
            alert.showAndWait();
        }
        else if(indOrigen == indDestino){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Paradas identicas");
            alert.setHeaderText(null);
            alert.setContentText("Las paradas de origen y destino son iguales");
            alert.showAndWait();
        } else if (ListaAdyacencia.getInstancia().existeRutaEntreNodos(origen, destino, cbxMedio.getValue())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Esta ruta existe");
            alert.setHeaderText(null);
            alert.setContentText("Ya existe una ruta entre estas paradas y con este medio");
            alert.showAndWait();
        } else {
            double distancia = spnDistancia.getValue();
            int tiempo = spnTiempo.getValue();
            int costo = spnCosto.getValue();
            Ruta aux = new Ruta(origen, destino, tiempo, costo, distancia, 0, cbxMedio.getValue());

            ListaAdyacencia.getInstancia().addRuta(aux);
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Registro");
            info.setHeaderText(null);
            info.setContentText("Parada registrada correctamente.");
            info.showAndWait();

            Stage stage = (Stage) addScene.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        if(!camposVacios()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cerrar sin registrar");
            alert.setContentText("¿Esta seguro que desea salir sin registrar?");
            if(alert.showAndWait().get() == ButtonType.YES){
                Stage stage = (Stage)addScene.getScene().getWindow();
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Saliendo sin guardar");
                alert2.showAndWait();
                stage.close();
            }
        }
        else {
            Stage stage = (Stage)addScene.getScene().getWindow();
            stage.close();
        }
    }

    private boolean camposVacios(){
        if(spnCosto.getValue() == null || spnTiempo.getValue() == null || spnDistancia.getValue() == null){
            return true;
        }
        if(cbxMedio.getValue() == null || cbxParadaDestino.getValue() == null || cbxParadaOrigen.getValue() == null){
            return true;
        }
        return false;
    }

}
