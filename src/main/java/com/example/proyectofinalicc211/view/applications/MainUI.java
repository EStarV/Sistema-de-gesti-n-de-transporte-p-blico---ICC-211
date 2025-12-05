package com.example.proyectofinalicc211.view.applications;

import com.example.proyectofinalicc211.view.utilities.Constantes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainUI extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Vista.fxml")));

        Scene scene = new Scene(root);
        stage.setTitle("Sistema Urbano de Transporte Accesible - SUMA");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.setMinWidth(400);
        stage.setMinHeight(600);
        Image logo = new Image(getClass().getResource(Constantes.LOGO).toExternalForm());
        stage.getIcons().add(logo);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}