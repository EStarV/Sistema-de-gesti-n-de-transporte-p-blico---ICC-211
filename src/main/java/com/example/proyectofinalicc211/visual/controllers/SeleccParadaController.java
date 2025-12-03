package com.example.proyectofinalicc211.visual.controllers;

import com.example.proyectofinalicc211.logico.ListaAdyacencia;
import com.example.proyectofinalicc211.logico.Parada;
import com.example.proyectofinalicc211.visual.utilities.Constantes;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ResourceBundle;

public class SeleccParadaController implements Initializable {
    private List<Parada> paradas;
    private Parada paradaSelec;

    @FXML
    private AnchorPane scsSelParada;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paradas = new ArrayList<>(new LinkedHashSet<>(ListaAdyacencia.getInstancia().getParadas().values()));
        String[] paradasAux = new String[paradas.size()];
        for (int i = 0; i < paradas.size(); i++) {
            paradasAux[i] = paradas.get(i).getNombre()+" - Rutas: " +
                    ListaAdyacencia.getInstancia().getGrafo().get(paradas.get(i).getId()).size();
        }
        lstParada.getItems().setAll(paradasAux);
        btnEliminar.setDisable(true);
        btnModificar.setDisable(true);

        lstParada.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                paradaSelec = paradas.get(lstParada.getSelectionModel().getSelectedIndex());
                btnEliminar.setDisable(false);
                btnModificar.setDisable(false);
            }
        });
    }

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnModificar;

    @FXML
    private ListView<String> lstParada;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    void cancelar(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Saliendo");
        alert.setHeaderText(null);
        alert.setContentText("Saliendo de seleccion de parada");
        alert.showAndWait();
        Stage stage = (Stage)scsSelParada.getScene().getWindow();
        stage.close();
    }

    @FXML
    void eliminar(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar");
        alert.setHeaderText("Se eliminara esta parada");
        alert.setContentText("¿Está seguro de querer eliminar esta parada?\nTambién seran eliminadas las rutas que se conecten con ésta");
        if(alert.showAndWait().get() == ButtonType.OK){
            ListaAdyacencia.getInstancia().deleteParada(paradaSelec);
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Parada eliminada");
            alert2.setHeaderText(null);
            alert2.setContentText(String.format("Parada %s eliminada con exito",  paradaSelec.getNombre()));
            alert2.showAndWait();
            lstParada.getItems().remove(paradas.indexOf(paradaSelec));
            paradaSelec = null;
            btnEliminar.setDisable(true);
            btnModificar.setDisable(true);
        }
    }

    @FXML
    void modificar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Constantes.MODIFICAR_PARADA));
        root = loader.load();

        ModifParadaController controller = loader.getController();
        controller.setModifParada(paradaSelec);

        Stage dialog = new Stage();
        dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialog.setTitle("Modificar Parada");
        Image logo = new Image(Constantes.LOGO);
        dialog.getIcons().add(logo);
        dialog.setScene(new Scene(root));
        dialog.showAndWait();
        updateList();
    }

    public void updateList(){
        int ind = paradas.indexOf(paradaSelec);
        String newName = ListaAdyacencia.getInstancia().getParadas().get(paradaSelec.getId()).getNombre()+
                " - Rutas: "+ListaAdyacencia.getInstancia().getGrafo().get(paradaSelec.getId()).size();
        lstParada.getItems().set(ind,newName);
    }

}
