package controller;

import dao.AdministrativeEventDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import model.AdministrativeEvent;

public class AdministrativeEventController {

    @FXML private TextField txtIdNumber;
    @FXML private TextField txtFullName;
    @FXML private TextField txtSearchIdNumber;

    @FXML private ComboBox<String> cbEventType;

    @FXML private DatePicker dpStartDate;
    @FXML private DatePicker dpEndDate;

    @FXML private TextArea taDescription;

    @FXML private TableView<AdministrativeEvent> table;
    @FXML private TableColumn<AdministrativeEvent, Long> colId;
    @FXML private TableColumn<AdministrativeEvent, String> colIdNumber;
    @FXML private TableColumn<AdministrativeEvent, String> colFullName;
    @FXML private TableColumn<AdministrativeEvent, String> colEventType;
    @FXML private TableColumn<AdministrativeEvent, String> colStartDate;
    @FXML private TableColumn<AdministrativeEvent, String> colEndDate;

    private final AdministrativeEventDAO dao = new AdministrativeEventDAO();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIdNumber.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colEventType.setCellValueFactory(new PropertyValueFactory<>("eventType"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        cbEventType.setItems(FXCollections.observableArrayList(
                "Vacaciones",
                "Licencia",
                "Permiso",
                "Incapacidad",
                "Traslado",
                "Encargo",
                "Otro"
        ));

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setMinHeight(300);
        table.setPrefHeight(600);
        table.setMaxHeight(Double.MAX_VALUE);
        table.setMaxWidth(Double.MAX_VALUE);

        Platform.runLater(() -> {
            if (table.getParent() instanceof Region parent) {
                table.prefHeightProperty().bind(parent.heightProperty().subtract(35));
                table.prefWidthProperty().bind(parent.widthProperty());
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            if (selected != null) {
                txtIdNumber.setText(selected.getIdNumber());
                txtFullName.setText(selected.getFullName());
                cbEventType.setValue(selected.getEventType());
                dpStartDate.setValue(selected.getStartDate());
                dpEndDate.setValue(selected.getEndDate());
                taDescription.setText(selected.getDescription());
            }
        });

        reloadData();
    }

    @FXML
    public void save() {
        AdministrativeEvent event = table.getSelectionModel().getSelectedItem();

        if (event == null) {
            event = new AdministrativeEvent();
        }

        event.setIdNumber(txtIdNumber.getText());
        event.setFullName(txtFullName.getText());
        event.setEventType(cbEventType.getValue());
        event.setStartDate(dpStartDate.getValue());
        event.setEndDate(dpEndDate.getValue());
        event.setDescription(taDescription.getText());

        dao.save(event);
        reloadData();
        clearForm();
    }

    @FXML
    public void delete() {
        AdministrativeEvent selected = table.getSelectionModel().getSelectedItem();

        if (selected != null && selected.getId() != null) {
            dao.delete(selected.getId());
            reloadData();
            clearForm();
        }
    }

    @FXML
    public void reloadData() {
        table.setItems(FXCollections.observableArrayList(dao.findAll()));
    }

    @FXML
    public void searchByIdNumber() {
        String idNumber = txtSearchIdNumber.getText();

        if (idNumber == null || idNumber.trim().isEmpty()) {
            reloadData();
        } else {
            table.setItems(FXCollections.observableArrayList(
                    dao.findByIdNumber(idNumber.trim())
            ));
        }
    }

    private void clearForm() {
        txtIdNumber.clear();
        txtFullName.clear();
        txtSearchIdNumber.clear();
        cbEventType.setValue(null);
        dpStartDate.setValue(null);
        dpEndDate.setValue(null);
        taDescription.clear();
        table.getSelectionModel().clearSelection();
    }
}