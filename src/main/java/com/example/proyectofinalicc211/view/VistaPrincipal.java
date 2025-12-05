package com.example.proyectofinalicc211.view;

import com.example.proyectofinalicc211.model.*;
import com.example.proyectofinalicc211.viewmodel.VistaModeloPrincipal;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.lang.Math;

public class VistaPrincipal {
    @FXML
    private ComboBox<Parada> cbOrigen;
    @FXML
    private ComboBox<Parada> cbDestino;
    @FXML
    private ChoiceBox<String> cbParametro;
    @FXML
    private ChoiceBox<String> cbAlgorithm;
    @FXML
    private Pane mapaPane;
    @FXML
    private Label lblEstado;

    private VistaModeloPrincipal viewModel = new VistaModeloPrincipal();
    private Map<UUID, Circle> nodosVisuales = new HashMap<>();

    @FXML
    private void initialize() {
        configurarCombos();

        cbOrigen.valueProperty().addListener((obs, oldVal, newVal) -> actualizarEstadoVisualNodos());
        cbDestino.valueProperty().addListener((obs, oldVal, newVal) -> actualizarEstadoVisualNodos());

        dibujarGrafo();
    }

    private void configurarCombos() {
        // Convertidor para mostrar nombres en los combos
        StringConverter<Parada> converter = new StringConverter<>() {
            @Override
            public String toString(Parada p) {
                return p != null ? p.getNombre() : "";
            }

            @Override
            public Parada fromString(String s) {
                return null;
            }
        };
        cbOrigen.setConverter(converter);
        cbDestino.setConverter(converter);

        cbOrigen.setItems(viewModel.getParadas());
        cbDestino.setItems(viewModel.getParadas());

        cbParametro.setItems(FXCollections.observableArrayList("tiempo", "distancia", "costo"));
        cbParametro.setValue("tiempo");

        cbAlgorithm.setItems(FXCollections.observableArrayList("dijkstra", "floyd"));
        cbAlgorithm.setValue("dijkstra");

        lblEstado.textProperty().bind(viewModel.mensajeResultadoProperty());
        lblEstado.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #d35400;"); // Naranja oscuro legible

        String estiloCombo = "-fx-font-size: 13px; -fx-font-weight: bold; -fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7;";
        cbOrigen.setStyle(estiloCombo);
        cbDestino.setStyle(estiloCombo);
    }

    @FXML
    private void calcularRuta() {
        Parada origen = cbOrigen.getValue();
        Parada destino = cbDestino.getValue();
        if (origen == null || destino == null) {
            lblEstado.setText("Seleccione origen y destino");
            return;
        }
        viewModel.calcularRuta(origen, destino, cbParametro.getValue(), cbAlgorithm.getValue());
        resaltarCaminos(viewModel.getUltimosCaminos());
    }

    // Visualizar el grafo

    private void dibujarGrafo() {
        mapaPane.getChildren().clear();
        nodosVisuales.clear();

        Map<UUID, List<Ruta>> grafo = ListaAdyacencia.getInstancia().getGrafo();
        Map<UUID, Parada> paradasMap = ListaAdyacencia.getInstancia().getParadas();

        boolean necesitaLayout = paradasMap.values().stream()
                .allMatch(p -> p.getPosicionX() == 0 && p.getPosicionY() == 0);

        double centerX = Math.max(mapaPane.getWidth() / 2, 400);
        double centerY = Math.max(mapaPane.getHeight() / 2, 300);
        double radio = Math.min(centerX, centerY) - 50;
        int contador = 0;

        for (Parada p : paradasMap.values()) {
            if (necesitaLayout || (p.getPosicionX() == 0 && p.getPosicionY() == 0)) {
                double angulo = 2 * Math.PI * contador / paradasMap.size();
                p.setPosicionX(centerX + radio * Math.cos(angulo));
                p.setPosicionY(centerY + radio * Math.sin(angulo));
                contador++;
            }

            Circle circle = new Circle(p.getPosicionX(), p.getPosicionY(), 20);
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(2);
            circle.setCursor(Cursor.HAND);

            configurarInteraccionNodo(circle, p);

            nodosVisuales.put(p.getId(), circle);
        }

        for (Map.Entry<UUID, List<Ruta>> entry : grafo.entrySet()) {
            Circle origenVisual = nodosVisuales.get(entry.getKey());
            if (origenVisual == null) continue;

            for (Ruta ruta : entry.getValue()) {
                Circle destinoVisual = nodosVisuales.get(ruta.getId_destino());
                if (destinoVisual != null) {
                    Line line = new Line();
                    line.startXProperty().bind(origenVisual.centerXProperty());
                    line.startYProperty().bind(origenVisual.centerYProperty());
                    line.endXProperty().bind(destinoVisual.centerXProperty());
                    line.endYProperty().bind(destinoVisual.centerYProperty());
                    line.setStroke(Color.LIGHTGRAY);
                    line.setStrokeWidth(2);

                    Polygon arrow = crearFlecha(origenVisual, destinoVisual);
                    Label pesoLabel = crearEtiquetaPeso(line, ruta);

                    mapaPane.getChildren().add(0, line);
                    mapaPane.getChildren().add(arrow);
                    mapaPane.getChildren().add(pesoLabel);
                }
            }
        }

        for (Map.Entry<UUID, Circle> entry : nodosVisuales.entrySet()) {
            Parada p = paradasMap.get(entry.getKey());
            Circle c = entry.getValue();

            Label label = new Label(p.getNombre());
            label.setStyle("-fx-background-color: white; -fx-border-color: #3498db; -fx-border-radius: 5; -fx-padding: 3px; -fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: black;");

            label.layoutXProperty().bind(c.centerXProperty().subtract(label.widthProperty().divide(2)));
            label.layoutYProperty().bind(c.centerYProperty().subtract(45));

            mapaPane.getChildren().addAll(c, label);
        }

        actualizarEstadoVisualNodos();
    }

    private void configurarInteraccionNodo(Circle circle, Parada p) {
        final class InteractionState {
            double startX, startY;
            boolean isDragging = false;
        }
        final InteractionState state = new InteractionState();

        circle.setOnMousePressed(e -> {
            state.startX = e.getSceneX();
            state.startY = e.getSceneY();
            state.isDragging = false;
            circle.getScene().setCursor(Cursor.MOVE);
        });

        circle.setOnMouseDragged(e -> {
            if (Math.abs(e.getSceneX() - state.startX) > 5 || Math.abs(e.getSceneY() - state.startY) > 5) {
                state.isDragging = true;

                double deltaX = e.getSceneX() - state.startX;
                double deltaY = e.getSceneY() - state.startY;

                double newX = circle.getCenterX() + deltaX;
                double newY = circle.getCenterY() + deltaY;

                if (newX > 10 && newX < mapaPane.getWidth() - 10) {
                    circle.setCenterX(newX);
                    p.setPosicionX(newX);
                }
                if (newY > 10 && newY < mapaPane.getHeight() - 10) {
                    circle.setCenterY(newY);
                    p.setPosicionY(newY);
                }

                state.startX = e.getSceneX();
                state.startY = e.getSceneY();
            }
        });

        circle.setOnMouseReleased(e -> {
            circle.getScene().setCursor(Cursor.HAND);
            if (!state.isDragging) {
                manejarClickNodo(p);
            }
        });
    }

    private void manejarClickNodo(Parada p) {
        Parada actualOrigen = cbOrigen.getValue();
        Parada actualDestino = cbDestino.getValue();
        UUID pId = p.getId();

        if (actualOrigen != null && actualOrigen.getId().equals(pId)) {
            cbOrigen.setValue(null);
        } else if (actualDestino != null && actualDestino.getId().equals(pId)) {
            cbDestino.setValue(null);
        } else if (actualOrigen == null) {
            cbOrigen.setValue(p);
        } else {
            cbDestino.setValue(p);
        }
    }

    private void actualizarEstadoVisualNodos() {
        Parada origen = cbOrigen.getValue();
        Parada destino = cbDestino.getValue();

        for (Map.Entry<UUID, Circle> entry : nodosVisuales.entrySet()) {
            UUID id = entry.getKey();
            Circle c = entry.getValue();

            if (origen != null && origen.getId().equals(id)) {
                c.setFill(Color.LIMEGREEN); // Verde
                c.setRadius(22);
            } else if (destino != null && destino.getId().equals(id)) {
                c.setFill(Color.TOMATO);    // Rojo
                c.setRadius(22);
            } else {
                c.setFill(Color.web("#3498db")); // Azul estándar
                c.setRadius(20);
            }
        }
    }

    private void resaltarCaminos(List<Camino> caminos) {
        mapaPane.getChildren().removeIf(node ->
                (node instanceof Line && (((Line) node).getStroke() == Color.RED || ((Line) node).getStroke() == Color.ORANGE))
        );

        if (caminos == null || caminos.isEmpty()) return;

        for (int index = 0; index < caminos.size(); index++) {
            Camino camino = caminos.get(index);
            List<Parada> paradas = camino.getCamino();
            Color colorRuta = (index == 0) ? Color.RED : Color.ORANGE;
            double grosor = (index == 0) ? 5 : 3;

            for (int i = 0; i < paradas.size() - 1; i++) {
                Parada p1 = paradas.get(i);
                Parada p2 = paradas.get(i + 1);

                Circle c1 = nodosVisuales.get(p1.getId());
                Circle c2 = nodosVisuales.get(p2.getId());

                if (c1 != null && c2 != null) {
                    Line line = new Line();
                    line.startXProperty().bind(c1.centerXProperty());
                    line.startYProperty().bind(c1.centerYProperty());
                    line.endXProperty().bind(c2.centerXProperty());
                    line.endYProperty().bind(c2.centerYProperty());
                    line.setStroke(colorRuta);
                    line.setStrokeWidth(grosor);
                    if (index > 0) line.setOpacity(0.7);

                    // Insertar detrás de los nodos pero delante de las grises
                    mapaPane.getChildren().add(mapaPane.getChildren().size() - nodosVisuales.size() * 2, line);
                }
            }
        }
    }

    private Polygon crearFlecha(Circle origen, Circle destino) {
        Polygon arrow = new Polygon();
        arrow.getPoints().addAll(0.0, 0.0, -10.0, -5.0, -10.0, 5.0);
        arrow.setFill(Color.GRAY);

        DoubleBinding angle = Bindings.createDoubleBinding(() -> {
            double dx = destino.getCenterX() - origen.getCenterX();
            double dy = destino.getCenterY() - origen.getCenterY();
            return Math.toDegrees(Math.atan2(dy, dx));
        }, origen.centerXProperty(), origen.centerYProperty(), destino.centerXProperty(), destino.centerYProperty());

        DoubleBinding xPos = Bindings.createDoubleBinding(() -> {
            double dx = destino.getCenterX() - origen.getCenterX();
            double dy = destino.getCenterY() - origen.getCenterY();
            double length = Math.sqrt(dx * dx + dy * dy);
            double ratio = (length - 25) / length;
            return origen.getCenterX() + dx * ratio;
        }, origen.centerXProperty(), destino.centerXProperty(), origen.centerYProperty(), destino.centerYProperty());

        DoubleBinding yPos = Bindings.createDoubleBinding(() -> {
            double dx = destino.getCenterX() - origen.getCenterX();
            double dy = destino.getCenterY() - origen.getCenterY();
            double length = Math.sqrt(dx * dx + dy * dy);
            double ratio = (length - 25) / length;
            return origen.getCenterY() + dy * ratio;
        }, origen.centerXProperty(), destino.centerXProperty(), origen.centerYProperty(), destino.centerYProperty());

        arrow.layoutXProperty().bind(xPos);
        arrow.layoutYProperty().bind(yPos);
        arrow.rotateProperty().bind(angle);
        return arrow;
    }

    private Label crearEtiquetaPeso(Line line, Ruta ruta) {
        String texto = String.format("T:%.0f D:%.0f C:%.0f",
                (double) ruta.getTiempo(), (double) ruta.getDistancia(), (double) ruta.getCosto());

        Label label = new Label(texto);
        label.setStyle("-fx-font-size: 10px; -fx-text-fill: #333; -fx-background-color: #fff9c4; -fx-border-color: #ccc; -fx-border-radius: 3; -fx-padding: 1px 3px;");
        label.layoutXProperty().bind(line.startXProperty().add(line.endXProperty()).divide(2).subtract(label.widthProperty().divide(2)));
        label.layoutYProperty().bind(line.startYProperty().add(line.endYProperty()).divide(2).subtract(label.heightProperty().divide(2)));
        return label;
    }

    // --- Métodos de Menú (Vinculados en FXML) ---
    @FXML
    private void abrirAddParada() {
        abrirVentanaYRefrescar("/AddParada.fxml");
    }

    @FXML
    private void abrirModifParada() {
        abrirVentanaYRefrescar("/SeleccParada.fxml");
    }

    @FXML
    private void abrirElimParada() {
        abrirVentanaYRefrescar("/SeleccParada.fxml");
    }

    @FXML
    private void abrirAddRuta() {
        abrirVentanaYRefrescar("/AddRuta.fxml");
    }

    @FXML
    private void abrirModifRuta() {
        abrirVentanaYRefrescar("/SeleccRuta.fxml");
    }

    @FXML
    private void abrirElimRuta() {
        abrirVentanaYRefrescar("/SeleccRuta.fxml");
    }

    @FXML
    private void abrirDijkstra() {
        abrirVentana("/Dijkstra.fxml");
    }

    @FXML
    private void abrirFloyd() {
        abrirVentana("/FloydWarshall.fxml");
    }

    @FXML
    private void mostrarAcercaDe() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("SUMA - Sistema Urbano de Movilidad Accesible");
        alert.setHeaderText("ICC-211");
        alert.setContentText("Desarrollado por: Mauricio Trejo y Estarlin Valerio.\nDiciembre 2025");
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
            e.printStackTrace();
        }
    }

    private void abrirVentanaYRefrescar(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
            viewModel = new VistaModeloPrincipal();
            initialize();
            dibujarGrafo();
            resaltarCaminos(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}