package com.example.proyectofinalicc211.view.applications;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainUI extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Esta línea funciona sin importar dónde esté el FXML, siempre que esté en resources
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Vista.fxml")));

        Scene scene = new Scene(root);
        stage.setTitle("Sistema de Rutas de Transporte Público - ICC211");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}