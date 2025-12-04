package com.example.proyectofinalicc211.view.controllers;
import com.example.proyectofinalicc211.view.utilities.Constantes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class MainController {

    @FXML
    private MenuBar menu;

    @FXML
    private void addParada(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Constantes.ADD_PARADA));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Agregar Parada");
        openSeleccion(stage, root);
    }

    private void openSeleccion(Stage stage, Parent root) {
        stage.initModality(Modality.WINDOW_MODAL);

        Window owner = menu.getScene().getWindow();
        stage.initOwner(owner);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        Image logo = new Image(Constantes.LOGO);
        stage.getIcons().add(logo);
        stage.showAndWait();
    }

    @FXML
    private void addRuta(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Constantes.ADD_RUTA));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Agregar Ruta");
        openSeleccion(stage, root);

    }

    @FXML
    private void dijkstra(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Constantes.DIJKSTRA));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Calcular ruta mas corta");
        openSeleccion(stage, root);
    }

    @FXML
    private void floyd(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Constantes.FLOYD));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Calcular ruta mas corta");
        openSeleccion(stage, root);
    }

    @FXML
    private void seleccionParada(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Constantes.SELECC_PARADA));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Seleccione Parada");
        openSeleccion(stage, root);
    }

    @FXML
    private void seleccionRuta(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Constantes.SELECC_RUTA));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Seleccione ruta");
        openSeleccion(stage, root);
    }

}