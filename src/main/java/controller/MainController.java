package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;

public class MainController extends Stage {

    @FXML private Button btnHome;
    @FXML private Button btnServers;
    @FXML private Button btnPositions;
    @FXML private Button btnEvents;

    @FXML private Label lblModuloTitulo;

    @FXML private Label icon1, icon2, icon3, icon4;

    @FXML private Label lblCard1Title, lblCard2Title, lblCard3Title, lblCard4Title;
    @FXML private Label lblCard1Value, lblCard2Value, lblCard3Value, lblCard4Value;
    @FXML private Label lblCard1Sub, lblCard2Sub, lblCard3Sub, lblCard4Sub;

    @FXML private StackPane contentArea;
    @FXML private StackPane homePanel;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/talento_humano";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private Timeline autoRefresh;
    private String moduloActivo = "home";

    public MainController() {

        try {

            URL fxmlUrl = getClass().getResource("/view/MainWindow.fxml");
            if (fxmlUrl == null) {
                throw new RuntimeException("No se encontró MainWindow.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            loader.setController(this);

            Scene scene = new Scene(loader.load(), 980, 660);
            scene.getStylesheets().add(
                    getClass().getResource("/style.css").toExternalForm()
            );

            setTitle("Gestión de Talento Humano");
            setScene(scene);
            setMinWidth(800);
            setMinHeight(560);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {

        activarModulo("home");

        autoRefresh = new Timeline(
                new KeyFrame(Duration.seconds(15), e -> actualizarEstadisticas(moduloActivo))
        );

        autoRefresh.setCycleCount(Timeline.INDEFINITE);
        autoRefresh.play();

        setOnCloseRequest(e -> autoRefresh.stop());
    }

    @FXML private void onHome() { activarModulo("home"); }
    @FXML private void onServers() { activarModulo("servers"); }
    @FXML private void onPositions() { activarModulo("positions"); }
    @FXML private void onEvents() { activarModulo("events"); }

    private void activarModulo(String modulo) {

        moduloActivo = modulo;

        marcarBotonActivo(modulo);
        actualizarTitulo(modulo);
        actualizarEstadisticas(modulo);
        cargarContenido(modulo);
    }

    private void marcarBotonActivo(String modulo) {

        String activo = "-fx-background-color: #2d7a3a; -fx-text-fill: white; -fx-font-size: 13px; -fx-background-radius: 6; -fx-cursor: hand; -fx-font-family: 'Georgia'; -fx-alignment: CENTER_LEFT; -fx-padding: 0 0 0 14;";
        String inactivo = "-fx-background-color: transparent; -fx-text-fill: #c8e6c9; -fx-font-size: 13px; -fx-background-radius: 6; -fx-cursor: hand; -fx-font-family: 'Georgia'; -fx-alignment: CENTER_LEFT; -fx-padding: 0 0 0 14;";

        btnHome.setStyle(inactivo);
        btnServers.setStyle(inactivo);
        btnPositions.setStyle(inactivo);
        btnEvents.setStyle(inactivo);

        switch (modulo) {
            case "home" -> btnHome.setStyle(activo);
            case "servers" -> btnServers.setStyle(activo);
            case "positions" -> btnPositions.setStyle(activo);
            case "events" -> btnEvents.setStyle(activo);
        }
    }

    private void actualizarTitulo(String modulo) {

        lblModuloTitulo.setText(
                switch (modulo) {
                    case "home" -> "Inicio — Panel General";
                    case "servers" -> "Servidores Públicos";
                    case "positions" -> "Gestión de Cargos";
                    case "events" -> "Novedades Administrativas";
                    default -> "";
                }
        );
    }

    private void actualizarEstadisticas(String modulo) {

        switch (modulo) {
            case "home" -> statsHome();
            case "servers" -> statsServidores();
            case "positions" -> statsCargos();
            case "events" -> statsNovedades();
        }
    }

    private void statsHome() {

        configurarTarjeta(1, "👥", "Servidores", "#1a5c28", "registrados", "SELECT COUNT(*) FROM publicserver");
        configurarTarjeta(2, "🏛️", "Dependencias", "#2d7a3a", "registradas", "SELECT COUNT(DISTINCT dependency) FROM position");
        configurarTarjeta(3, "📋", "Cargos", "#e67e22", "registrados", "SELECT COUNT(*) FROM position");
        configurarTarjeta(4, "📄", "Novedades", "#aa0106", "registradas", "SELECT COUNT(*) FROM administrativeevent");
    }

    private void statsServidores() {

        configurarTarjeta(1, "👥", "Total", "#1a5c28", "servidores", "SELECT COUNT(*) FROM publicserver");
        configurarTarjeta(2, "🏛️", "Dependencias", "#2d7a3a", "en uso", "SELECT COUNT(DISTINCT dependency) FROM position");
        configurarTarjeta(3, "📅", "Promedio Edad", "#e67e22", "años promedio", "SELECT IFNULL(ROUND(AVG(TIMESTAMPDIFF(YEAR, birthDate, CURDATE()))),0) FROM publicserver");
        configurarTarjeta(4, "📋", "Tipos de Empleo", "#aa0106", "registrados", "SELECT COUNT(DISTINCT employmentType) FROM publicserver");
    }

    private void statsCargos() {

        configurarTarjeta(1, "📋", "Total", "#1a5c28", "cargos", "SELECT COUNT(*) FROM position");
        configurarTarjeta(2, "✅", "Ocupados", "#2d7a3a", "asignados", "SELECT COUNT(DISTINCT id) FROM position WHERE id IN (SELECT position_id FROM publicserver)");
        configurarTarjeta(3, "⭕", "Vacantes", "#e67e22", "sin asignar", "SELECT COUNT(*) FROM position WHERE id NOT IN (SELECT position_id FROM publicserver)");
        configurarTarjeta(4, "🏷️", "Categorías", "#aa0106", "salariales", "SELECT COUNT(DISTINCT salaryCategory) FROM position");
    }

    private void statsNovedades() {

        configurarTarjeta(1, "📄", "Total", "#1a5c28", "novedades", "SELECT COUNT(*) FROM administrativeevent");
        configurarTarjeta(2, "🏖️", "Vacaciones", "#2d7a3a", "activas", "SELECT COUNT(*) FROM administrativeevent WHERE administrativeeventtype_id = 1");
        configurarTarjeta(3, "📌", "Permisos", "#e67e22", "activos", "SELECT COUNT(*) FROM administrativeevent WHERE administrativeeventtype_id = 2");
        configurarTarjeta(4, "📊", "Tipos", "#aa0106", "existentes", "SELECT COUNT(DISTINCT administrativeeventtype_id) FROM administrativeevent");
    }

    private void configurarTarjeta(int num, String ico, String titulo, String color, String sub, String sql) {

        Label iconLbl = getIcon(num);
        Label titleLbl = getTitleLabel(num);
        Label valueLbl = getValueLabel(num);
        Label subLbl = getSubLabel(num);

        iconLbl.setText(ico);
        titleLbl.setText(titulo);
        subLbl.setText(sub);

        valueLbl.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " + color + "; -fx-font-family: 'Georgia';");
        valueLbl.setText("...");

        new Thread(() -> {

            String resultado = consultarValor(sql);

            Platform.runLater(() -> valueLbl.setText(resultado));

        }).start();
    }

    private String consultarValor(String sql) {

        try (
                Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)
        ) {

            if (rs.next()) {
                return rs.getString(1);
            }

            return "0";

        } catch (SQLException e) {
            e.printStackTrace();
            return "N/D";
        }
    }

    private void cargarContenido(String modulo) {

        contentArea.getChildren().removeIf(n -> n != homePanel);

        if (modulo.equals("home")) {
            homePanel.setVisible(true);
            return;
        }

        homePanel.setVisible(false);

        String fxml = switch (modulo) {
            case "servers" -> "/view/PublicServerWindow.fxml";
            case "positions" -> "/view/PositionWindow.fxml";
            case "events" -> "/view/AdministrativeEventWindow.fxml";
            default -> null;
        };

        if (fxml == null) return;

        try {
            URL url = getClass().getResource(fxml);
            if (url == null) return;

            Node contenido = FXMLLoader.load(url);
            contentArea.getChildren().add(contenido);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Label getIcon(int n) {
        return switch (n) {
            case 1 -> icon1;
            case 2 -> icon2;
            case 3 -> icon3;
            default -> icon4;
        };
    }

    private Label getTitleLabel(int n) {
        return switch (n) {
            case 1 -> lblCard1Title;
            case 2 -> lblCard2Title;
            case 3 -> lblCard3Title;
            default -> lblCard4Title;
        };
    }

    private Label getValueLabel(int n) {
        return switch (n) {
            case 1 -> lblCard1Value;
            case 2 -> lblCard2Value;
            case 3 -> lblCard3Value;
            default -> lblCard4Value;
        };
    }

    private Label getSubLabel(int n) {
        return switch (n) {
            case 1 -> lblCard1Sub;
            case 2 -> lblCard2Sub;
            case 3 -> lblCard3Sub;
            default -> lblCard4Sub;
        };
    }
}