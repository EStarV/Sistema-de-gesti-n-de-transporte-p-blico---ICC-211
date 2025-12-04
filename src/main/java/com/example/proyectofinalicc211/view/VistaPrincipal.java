package com.example.proyectofinalicc211.view;

import com.example.proyectofinalicc211.model.*;
import com.example.proyectofinalicc211.viewmodel.VistaModeloPrincipal;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class VistaPrincipal {

    @FXML private ComboBox<Parada> cbOrigen;
    @FXML private ComboBox<Parada> cbDestino;
    @FXML private ChoiceBox<String> cbParametro;
    @FXML private ChoiceBox<String> cbAlgorithm;
    @FXML private Pane mapaPane;
    @FXML private Label lblEstado;

    private VistaModeloPrincipal viewModel = new VistaModeloPrincipal();

    @FXML
    private void initialize() {
        // ComboBoxes
        cbOrigen.setItems(viewModel.getParadas());
        cbDestino.setItems(viewModel.getParadas());

        StringConverter<Parada> converter = new StringConverter<>() {
            @Override public String toString(Parada p) { return p != null ? p.getNombre() : ""; }
            @Override public Parada fromString(String s) { return null; }
        };
        cbOrigen.setConverter(converter);
        cbDestino.setConverter(converter);

        cbParametro.setItems(FXCollections.observableArrayList("tiempo", "distancia", "costo"));
        cbParametro.setValue("tiempo");
        cbAlgorithm.setItems(FXCollections.observableArrayList("dijkstra", "floyd"));
        cbAlgorithm.setValue("dijkstra");

        lblEstado.textProperty().bind(viewModel.mensajeResultadoProperty());

        dibujarGrafo();
    }

    @FXML
    private void calcularRuta() {
        Parada origen = cbOrigen.getValue();
        Parada destino = cbDestino.getValue();
        if (origen == null || destino == null) {
            lblEstado.setText("Seleccione origen y destino");
            return;
        }
        String param = cbParametro.getValue();
        String algo = cbAlgorithm.getValue();
        viewModel.calcularRuta(origen, destino, param, algo);
        resaltarCaminos(viewModel.getUltimosCaminos());
    }

    // ====== MENÚS ======
    @FXML private void abrirAddParada() { abrirVentanaYRefrescar("/AddParada.fxml"); }
    @FXML private void abrirModifParada() { abrirVentanaYRefrescar("/SeleccParada.fxml"); }
    @FXML private void abrirElimParada() { abrirVentanaYRefrescar("/SeleccParada.fxml"); }
    @FXML private void abrirAddRuta() { abrirVentanaYRefrescar("/AddRuta.fxml"); }
    @FXML private void abrirModifRuta() { abrirVentanaYRefrescar("/SeleccRuta.fxml"); }
    @FXML private void abrirElimRuta() { abrirVentanaYRefrescar("/SeleccRuta.fxml"); }
    @FXML private void abrirDijkstra() { abrirVentana("/Dijkstra.fxml"); }
    @FXML private void abrirFloyd() { abrirVentana("/FloydWarshall.fxml"); }

    @FXML
    private void mostrarAcercaDe() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sistema de Rutas de Transporte Público");
        alert.setHeaderText("ICC-211 - Algoritmos Clásicos y Estructuras de Datos");
        alert.setContentText("Desarrollado por: [TU NOMBRE]\nDiciembre 2025\n¡Proyecto completado con éxito!");
        alert.showAndWait();
    }

    private void abrirVentana(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("Búsqueda Avanzada");
            stage.show();
        } catch (Exception e) {
            lblEstado.setText("Error al abrir " + fxml);
        }
    }

    private void abrirVentanaYRefrescar(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
            // REFRESCAR TODO DESPUÉS DE CERRAR
            viewModel = new VistaModeloPrincipal(); // recarga datos
            initialize(); // recarga combos
            dibujarGrafo();
            resaltarCaminos(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ====== VISUALIZACIÓN DEL GRAFO ======
    private void dibujarGrafo() {
        mapaPane.getChildren().clear();
        Map<UUID, List<Ruta>> grafo = ListaAdyacencia.getInstancia().getGrafo();
        Map<UUID, Parada> paradasMap = ListaAdyacencia.getInstancia().getParadas();

        // Líneas (rutas)
        for (Map.Entry<UUID, List<Ruta>> entry : grafo.entrySet()) {
            Parada origen = paradasMap.get(entry.getKey());
            if (origen == null) continue;
            for (Ruta ruta : entry.getValue()) {
                Parada destino = paradasMap.get(ruta.getId_destino());
                if (destino != null) {
                    Line line = new Line(origen.getPosicionX(), origen.getPosicionY(),
                            destino.getPosicionX(), destino.getPosicionY());
                    line.setStroke(Color.GRAY);
                    line.setStrokeWidth(3);
                    mapaPane.getChildren().add(line);
                }
            }
        }

        // Nodos (paradas)
        for (Parada p : paradasMap.values()) {
            Circle circle = new Circle(p.getPosicionX(), p.getPosicionY(), 20, Color.web("#3498db"));
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(3);

            Label label = new Label(p.getNombre());
            label.setLayoutX(p.getPosicionX() - 20);
            label.setLayoutY(p.getPosicionY() - 40);
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

            // Click en parada → seleccionar en combo
            circle.setOnMouseClicked(e -> {
                if (cbOrigen.getValue() == null) cbOrigen.setValue(p);
                else cbDestino.setValue(p);
            });

            mapaPane.getChildren().addAll(circle, label);
        }
    }

    private void resaltarCaminos(List<Camino> caminos) {
        // Limpiar resaltado anterior
        mapaPane.getChildren().removeIf(node -> node instanceof Line && ((Line) node).getStroke() == Color.RED);

        if (caminos == null || caminos.isEmpty()) return;

        // Resaltar todas las rutas (principal + alternativas)
        for (Camino camino : caminos) {
            List<Parada> paradas = camino.getCamino();
            for (int i = 0; i < paradas.size() - 1; i++) {
                Parada p1 = paradas.get(i);
                Parada p2 = paradas.get(i + 1);
                Line line = new Line(p1.getPosicionX(), p1.getPosicionY(), p2.getPosicionX(), p2.getPosicionY());
                line.setStroke(Color.RED);
                line.setStrokeWidth(5);
                mapaPane.getChildren().add(line);
            }
        }
    }
}