package com.example.proyectofinalicc211.visual.controllers;
import com.example.proyectofinalicc211.logico.ListaAdyacencia;
import com.example.proyectofinalicc211.logico.Parada;
import com.example.proyectofinalicc211.logico.Ruta;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class ModifRutaController implements Initializable {
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
    }

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnGuardar;

    @FXML
    private ChoiceBox<String> cbxMedio;

    @FXML
    private ChoiceBox<String> cbxParadaDestino;

    @FXML
    private ChoiceBox<String> cbxParadaOrigen;

    @FXML
    private AnchorPane scsModifPane;

    @FXML
    private Spinner<Integer> spnCosto;

    @FXML
    private Spinner<Double> spnDistancia;

    @FXML
    private Spinner<Integer> spnTiempo;

    private Ruta modifRuta;

    @FXML
    void cancelar(ActionEvent event) {
        if(!camposVacios()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cerrar sin modificar");
            alert.setContentText("¿Esta seguro que desea salir sin guardar cambios?");
            if(alert.showAndWait().get() == ButtonType.YES){
                closeWindow();
            }
        }
        else {
            closeWindow();
        }
    }

    @FXML
    void modificar(ActionEvent event) {
        int indOrigen = cbxParadaOrigen.getSelectionModel().getSelectedIndex();
        int indDestino = cbxParadaDestino.getSelectionModel().getSelectedIndex();
        Parada origen = paradas.get(indOrigen);
        Parada destino = paradas.get(indDestino);
        String medio = cbxMedio.getValue();
        boolean existe = ListaAdyacencia.getInstancia().existeRutaEntreNodos(origen.getId(), destino.getId(),medio);
        boolean esIgual = origen.getId().equals(modifRuta.getId_origen()) &&
                destino.getId().equals(modifRuta.getId_destino())
                && medio.equalsIgnoreCase(modifRuta.getMedio());
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
        } else if (existe && !esIgual) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Esta ruta existe");
            alert.setHeaderText(null);
            alert.setContentText("Ya existe una ruta entre estas paradas y con este medio");
            alert.showAndWait();
        } else{
            if(!(origen.getId().equals(modifRuta.getId_origen()) && destino.getId().equals(modifRuta.getId_destino()))){
                ListaAdyacencia.getInstancia().modifNodosRuta(origen, destino, modifRuta);
                modifRuta.setId_origen(origen.getId());
                modifRuta.setId_destino(destino.getId());
            }
            double distancia = spnDistancia.getValue();
            int tiempo = spnTiempo.getValue();
            int costo = spnCosto.getValue();
            ListaAdyacencia.getInstancia().modificarRuta(modifRuta, tiempo, distancia, costo, 1, medio);

            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Ruta modificada");
            info.setHeaderText(null);
            info.setContentText("La ruta ha sido modificada con exito.");
            info.showAndWait();
            closeWindow();
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

    private void closeWindow() {
        try {
            Stage stage = null;
            if (scsModifPane != null && scsModifPane.getScene() != null) {
                stage = (Stage) scsModifPane.getScene().getWindow();
            } else if (btnCancelar != null && btnCancelar.getScene() != null) {
                stage = (Stage) btnCancelar.getScene().getWindow();
            } else if (btnGuardar != null && btnGuardar.getScene() != null) {
                stage = (Stage) btnGuardar.getScene().getWindow();
            }
            if (stage != null) {
                stage.close();
            } else {
                Platform.runLater(() -> {
                    if (scsModifPane != null && scsModifPane.getScene() != null) {
                        ((Stage) scsModifPane.getScene().getWindow()).close();
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setModifRuta(Ruta modifRuta) {
        this.modifRuta = modifRuta;
        cbxMedio.setValue(modifRuta.getMedio());

        Parada origen = ListaAdyacencia.getInstancia().getParada(modifRuta.getId_origen());
        Parada destino = ListaAdyacencia.getInstancia().getParada(modifRuta.getId_destino());
        int indOrigen = paradas.indexOf(origen);
        int indDestino = paradas.indexOf(destino);

        cbxParadaOrigen.getSelectionModel().select(indOrigen);
        cbxParadaDestino.getSelectionModel().select(indDestino);

        SpinnerValueFactory<Integer> factoryTime = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 720);
        factoryTime.setValue(modifRuta.getTiempo());
        spnTiempo.setEditable(true);
        spnTiempo.setValueFactory(factoryTime);

        SpinnerValueFactory<Integer> factoryCosto = new SpinnerValueFactory.IntegerSpinnerValueFactory(15, 1500);
        factoryCosto.setValue(modifRuta.getCosto());
        spnCosto.setEditable(true);
        spnCosto.setValueFactory(factoryCosto);

        SpinnerValueFactory<Double> factoryDistancia = new SpinnerValueFactory.DoubleSpinnerValueFactory(1.0, 300.0);
        factoryDistancia.setValue(modifRuta.getDistancia());
        spnDistancia.setEditable(true);
        spnDistancia.setValueFactory(factoryDistancia);
    }
}