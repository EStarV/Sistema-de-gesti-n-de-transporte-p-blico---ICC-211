package com.example.proyectofinalicc211.view.controllers;

import com.example.proyectofinalicc211.model.ListaAdyacencia;
import com.example.proyectofinalicc211.model.Parada;
import com.example.proyectofinalicc211.view.utilities.Constantes;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ModifParadaController {
    private Parada modifParada;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnGuardar;

    @FXML
    private AnchorPane scsModifPane;

    @FXML
    private TextField txtNombre;

    @FXML
    void cancelar(ActionEvent event) {
        if(!txtNombre.getText().isEmpty() && !txtNombre.getText().equalsIgnoreCase(modifParada.getNombre())){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cerrar sin modificar");
            alert.setContentText("¿Esta seguro que desea salir sin modificar?");
            if(alert.showAndWait().get() == ButtonType.YES){
                closeWindow();
            }
        }
        else {
            closeWindow();
        }
    }

    @FXML
    void guardar(ActionEvent event) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<Constantes.PARADA_NAME_LENGTH; i++) sb.append(" ");
        String campoVacio = sb.toString();
        if(txtNombre.getText().isEmpty() || campoVacio.contains(txtNombre.getText())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campo vacio");
            alert.setHeaderText(null);
            alert.setContentText("El nombre no puede estar vacio");
            alert.showAndWait();
            return;
        } else if (txtNombre.getText().length() > Constantes.PARADA_NAME_LENGTH) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nombre muy largo");
            alert.setHeaderText(null);
            alert.setContentText("El nombre es demasiado largo");
            alert.showAndWait();
            return;
        } else if (ListaAdyacencia.getInstancia().existeNombreParada(txtNombre.getText().toLowerCase()) &&
                !txtNombre.getText().equalsIgnoreCase(modifParada.getNombre())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nombre existente");
            alert.setHeaderText(null);
            alert.setContentText("Existe otra parada con este nombre");
            alert.showAndWait();
            return;
        } else{
            ListaAdyacencia.getInstancia().modificarParada(modifParada, txtNombre.getText());
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Modificacion exitosa");
            info.setHeaderText(null);
            info.setContentText("Parada modificada correctamente.");
            info.showAndWait();

            closeWindow();
        }

    }

    public void setModifParada(Parada modifParada) {
        this.modifParada = modifParada;
        txtNombre.setText(modifParada.getNombre());
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
}
