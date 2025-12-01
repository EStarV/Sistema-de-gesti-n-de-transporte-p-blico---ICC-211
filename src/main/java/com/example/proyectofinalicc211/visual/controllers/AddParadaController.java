package com.example.proyectofinalicc211.visual.controllers;

import com.example.proyectofinalicc211.logico.ListaAdyacencia;
import com.example.proyectofinalicc211.logico.Parada;
import com.example.proyectofinalicc211.visual.utilities.Constantes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class AddParadaController implements Initializable{

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnGuardar;

    @FXML
    private AnchorPane addScene;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNombre;

    @FXML
    private void add(ActionEvent event) {
        if(txtNombre.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campo vacio");
            alert.setHeaderText(null);
            alert.setContentText("El campo nombre no puede estar vacio");
            alert.showAndWait();
        } else if (ListaAdyacencia.getInstancia().existeNombreParada(txtNombre.getText().toLowerCase())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nombre existente");
            alert.setHeaderText(null);
            alert.setContentText("Existe otra parada con este nombre");
            alert.showAndWait();
        } else if (txtNombre.getText().length() > Constantes.PARADA_NAME_LENGTH) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nombre muy largo");
            alert.setHeaderText(null);
            alert.setContentText("El nombre es demasiado largo");
            alert.showAndWait();
        }else{
            Parada aux =  new Parada(UUID.fromString(txtId.getText()), txtNombre.getText());
            ListaAdyacencia.getInstancia().addParada(aux);
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
    private void close(ActionEvent event) {
        if(!txtNombre.getText().isEmpty()){
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtId.setText(UUID.randomUUID().toString());
    }
}