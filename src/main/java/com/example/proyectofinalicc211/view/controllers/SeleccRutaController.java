package com.example.proyectofinalicc211.view.controllers;

import com.example.proyectofinalicc211.model.ListaAdyacencia;
import com.example.proyectofinalicc211.model.Parada;
import com.example.proyectofinalicc211.model.Ruta;
import com.example.proyectofinalicc211.view.utilities.Constantes;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SeleccRutaController implements Initializable {

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnModificar;

    @FXML
    private ComboBox<String> cbxDestino;

    @FXML
    private ChoiceBox<String> cbxMedio;

    @FXML
    private ComboBox<String> cbxOrigen;

    @FXML
    private AnchorPane scsSelRuta;

    @FXML
    void cancelar(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Saliendo");
        alert.setHeaderText(null);
        alert.setContentText("Saliendo de seleccion de Ruta");
        alert.showAndWait();
        Stage stage = (Stage)scsSelRuta.getScene().getWindow();
        stage.close();
    }

    @FXML
    void delete(ActionEvent event) {
        Ruta rutaSel = ListaAdyacencia.getInstancia().buscarRutaConMedio(origen.getId(),
                destino.getId(), cbxMedio.getValue());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar");
        alert.setHeaderText("Se eliminara esta ruta");
        alert.setContentText("¿Está seguro de querer eliminar esta ruta?");
        if(alert.showAndWait().get() == ButtonType.OK){
            ListaAdyacencia.getInstancia().deleteRuta(rutaSel);
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Ruta eliminada");
            alert2.setHeaderText(null);
            alert2.setContentText("Se ha eliminado esta ruta con exito");
            alert2.showAndWait();
            reset(event);
            btnEliminar.setDisable(true);
            btnModificar.setDisable(true);
        }
    }

    @FXML
    void modificar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Constantes.MODIFICAR_RUTA));
        Parent root = loader.load();

        ModifRutaController controller = loader.getController();
        Ruta ruta = ListaAdyacencia.getInstancia().buscarRutaConMedio(origen.getId(), destino.getId(), cbxMedio.getValue());
        controller.setModifRuta(ruta);

        Stage dialog = new Stage();
        dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialog.setTitle("Modificar Parada");
        Image logo = new Image(Constantes.LOGO);
        dialog.getIcons().add(logo);
        dialog.setScene(new Scene(root));
        dialog.showAndWait();
        reset(event);
    }

    @FXML
    void reset(ActionEvent event) {
        origenAux = new ArrayList<>(paradas);
        destinoAux = new ArrayList<>(paradas);
        String[] paradasAux = new String[paradas.size()];
        for(int i = 0; i < paradas.size(); i++){
            paradasAux[i] = paradas.get(i).getNombre();
        }
        destino = null;
        origen = null;

        cbxOrigen.getItems().setAll(paradasAux);
        cbxDestino.getItems().setAll(paradasAux);
        cbxDestino.getSelectionModel().clearSelection();
        cbxOrigen.getSelectionModel().clearSelection();
        cbxMedio.setDisable(true);
        cbxMedio.getItems().clear();
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    private List<Parada> paradas;
    private List<Parada> origenAux;
    private List<Parada> destinoAux;
    private List<Ruta> rutas;
    private Parada origen = null;
    private Parada destino = null;

    private boolean updating = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paradas = new ArrayList<>(new LinkedHashSet<>(ListaAdyacencia.getInstancia().getParadas().values()));
        String[] paradasAux = new String[paradas.size()];
        for(int i = 0; i < paradas.size(); i++){
            paradasAux[i] = paradas.get(i).getNombre();
        }
        origenAux = new ArrayList<>(paradas);
        destinoAux = new ArrayList<>(paradas);

        cbxOrigen.getItems().setAll(paradasAux);
        cbxDestino.getItems().setAll(paradasAux);
        cbxMedio.setDisable(true);

        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);

        cbxOrigen.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(updating) return;
                updating = true;
                try {
                    int ind = cbxOrigen.getSelectionModel().getSelectedIndex();

                    if (ind >= 0) {
                        origen = origenAux.get(ind);
                        List<Ruta> salientes = ListaAdyacencia.getInstancia().getGrafo().get(origen.getId());
                        if (salientes == null || salientes.isEmpty()) {
                            destinoAux = new ArrayList<>();
                            cbxDestino.getItems().clear();
                            cbxDestino.getSelectionModel().clearSelection();
                            cbxMedio.getItems().clear();
                            cbxMedio.setDisable(true);
                            btnModificar.setDisable(true);
                            btnEliminar.setDisable(true);
                            return;
                        }
                        LinkedHashSet<Parada> paradasAux = new LinkedHashSet<>();
                        ArrayList<String> names = new ArrayList<>();
                        for (Ruta r : salientes) {
                            Parada p = ListaAdyacencia.getInstancia().getParadas().get(r.getId_destino());
                            if (p != null && paradasAux.add(p)) {
                                names.add(p.getNombre());
                            }
                        }
                        destinoAux = new ArrayList<>(paradasAux);

                        if(sameItems(cbxDestino.getItems(), names)){
                            int prevIndex = -1;
                            if (destino != null) {
                                for (int i = 0; i < destinoAux.size(); i++) {
                                    if (destinoAux.get(i).getId().equals(destino.getId())) { prevIndex = i; break; }
                                }
                            }
                            if (prevIndex >= 0) {
                                final int sel = prevIndex;
                                Platform.runLater(() -> cbxDestino.getSelectionModel().select(sel));
                            } else {
                                destino = null;
                                cbxMedio.getItems().clear();
                                cbxMedio.setDisable(true);
                                btnModificar.setDisable(true);
                                btnEliminar.setDisable(true);
                                cbxDestino.getSelectionModel().clearSelection();
                            }
                        }else {
                            cbxDestino.getItems().setAll(names);
                            int prevIndex = -1;
                            if (destino != null) {
                                for (int i = 0; i < destinoAux.size(); i++) {
                                    if (destinoAux.get(i).getId().equals(destino.getId())) { prevIndex = i; break; }
                                }
                            }
                            if (prevIndex >= 0) {
                                final int sel = prevIndex;
                                Platform.runLater(() -> cbxDestino.getSelectionModel().select(sel));
                            } else {
                                destino = null;
                                cbxMedio.getItems().clear();
                                cbxMedio.setDisable(true);
                                btnModificar.setDisable(true);
                                btnEliminar.setDisable(true);
                            }
                        }

                    } else {
                        return;
                    }
                    Platform.runLater(() -> initMedio());
                } finally {
                    updating = false;
                }
            }
        }
        );

        cbxDestino.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (updating) return;
                updating = true;
                try {
                    int ind = cbxDestino.getSelectionModel().getSelectedIndex();
                    if (ind >= 0) {
                        destino = destinoAux.get(ind);
                        List<Ruta> entrantes = ListaAdyacencia.getInstancia().getEntrantes().get(destino.getId());
                        if (entrantes == null || entrantes.isEmpty()) {
                            origenAux = new ArrayList<>();
                            cbxOrigen.getItems().clear();
                            cbxOrigen.getSelectionModel().clearSelection();
                            cbxMedio.getItems().clear();
                            cbxMedio.setDisable(true);
                            btnModificar.setDisable(true);
                            btnEliminar.setDisable(true);
                            return;
                        }
                        LinkedHashSet<Parada> paradasAux = new LinkedHashSet<>();
                        ArrayList<String> names = new ArrayList<>();
                        for (Ruta r : entrantes) {
                            Parada p = ListaAdyacencia.getInstancia().getParadas().get(r.getId_origen());
                            if(p != null && paradasAux.add(p)){
                                names.add(p.getNombre());
                            }
                        }
                        origenAux = new ArrayList<>(paradasAux);
                        if (sameItems(cbxOrigen.getItems(), names)) {
                            int prevIndex = -1;
                            if (origen != null) {
                                for (int i = 0; i < origenAux.size(); i++) {
                                    if (origenAux.get(i).getId().equals(origen.getId())) { prevIndex = i; break; }
                                }
                            }
                            if (prevIndex >= 0) {
                                final int sel = prevIndex;
                                Platform.runLater(() -> cbxOrigen.getSelectionModel().select(sel));
                            } else {
                                origen = null;
                                cbxMedio.getItems().clear();
                                cbxMedio.setDisable(true);
                                btnEliminar.setDisable(true);
                                btnModificar.setDisable(true);
                                cbxOrigen.getSelectionModel().clearSelection();
                            }
                        } else {
                            cbxOrigen.getItems().setAll(names);
                            int prevIndex = -1;
                            if (origen != null) {
                                for (int i = 0; i < origenAux.size(); i++) {
                                    if (origenAux.get(i).getId().equals(origen.getId())) { prevIndex = i; break; }
                                }
                            }
                            if (prevIndex >= 0) {
                                final int sel = prevIndex;
                                Platform.runLater(() -> cbxOrigen.getSelectionModel().select(sel));
                            } else {
                                origen = null;
                                cbxMedio.getItems().clear();
                                cbxMedio.setDisable(true);
                                btnEliminar.setDisable(true);
                                btnModificar.setDisable(true);
                            }
                        }
                    }
                    Platform.runLater(() -> initMedio());
                } finally {
                    updating = false;
                }
            }
        });

        cbxMedio.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                btnModificar.setDisable(true);
                btnEliminar.setDisable(true);
            } else {
                btnModificar.setDisable(false);
                btnEliminar.setDisable(false);
            }
        });

    }

    private void initMedio(){
        if(origen == null || destino == null){
            cbxMedio.getItems().clear();
            cbxMedio.setDisable(true);
            btnModificar.setDisable(true);
            btnEliminar.setDisable(true);
            return;
        }

        rutas = ListaAdyacencia.getInstancia().rutasEntreNodos(origen.getId(), destino.getId());
        if(rutas == null || rutas.isEmpty()){
            cbxMedio.getItems().clear();
            cbxMedio.setDisable(true);
            btnModificar.setDisable(true);
            btnEliminar.setDisable(true);
            return;
        }

        String[] medios = new String[rutas.size()];
        for(int i = 0; i < rutas.size(); i++){
            medios[i] = rutas.get(i).getMedio();
        }
        cbxMedio.getItems().clear();
        cbxMedio.getItems().setAll(medios);
        cbxMedio.setDisable(false);
    }

    private boolean sameItems(javafx.collections.ObservableList<String> existing, List<String> nue) {
        if (existing == null) return (nue == null || nue.isEmpty());
        if (nue == null) return existing.isEmpty();
        if (existing.size() != nue.size()) return false;
        for (int i = 0; i < nue.size(); i++) {
            if (!Objects.equals(existing.get(i), nue.get(i))) return false;
        }
        return true;
    }
}