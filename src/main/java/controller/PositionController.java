package controller;

import dao.PositionDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import model.Position;

public class PositionController {

    @FXML private TextField txtIdNumber;
    @FXML private TextField txtLastName;
    @FXML private TextField txtName;
    @FXML private TextField txtSearchIdNumber;

    @FXML private ComboBox<String> cbDependency;
    @FXML private ComboBox<String> cbSalaryCategory;
    @FXML private ComboBox<String> cbEmploymentType;

    @FXML private TableView<Position> table;
    @FXML private TableColumn<Position, Long> colId;
    @FXML private TableColumn<Position, String> colIdNumber;
    @FXML private TableColumn<Position, String> colLastName;
    @FXML private TableColumn<Position, String> colName;
    @FXML private TableColumn<Position, String> colDependency;
    @FXML private TableColumn<Position, String> colEmploymentType;

    private final PositionDAO dao = new PositionDAO();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIdNumber.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDependency.setCellValueFactory(new PropertyValueFactory<>("dependency"));
        colEmploymentType.setCellValueFactory(new PropertyValueFactory<>("employmentType"));

        cbDependency.setItems(FXCollections.observableArrayList(
                "Administrativa",
                "Financiera",
                "Talento Humano",
                "Planeación",
                "Jurídica",
                "Sistemas",
                "Otra"
        ));

        cbSalaryCategory.setItems(FXCollections.observableArrayList(
                "Nivel 1",
                "Nivel 2",
                "Nivel 3",
                "Nivel 4",
                "Nivel 5"
        ));

        cbEmploymentType.setItems(FXCollections.observableArrayList(
                "Carrera Administrativa",
                "Libre Nombramiento",
                "Provisional",
                "Contratista",
                "Encargo"
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
                txtLastName.setText(selected.getLastName());
                txtName.setText(selected.getName());
                cbDependency.setValue(selected.getDependency());
                cbSalaryCategory.setValue(selected.getSalaryCategory());
                cbEmploymentType.setValue(selected.getEmploymentType());
            }
        });

        reloadData();
    }

    @FXML
    public void save() {
        Position position = table.getSelectionModel().getSelectedItem();

        if (position == null) {
            position = new Position();
        }

        position.setIdNumber(txtIdNumber.getText());
        position.setLastName(txtLastName.getText());
        position.setName(txtName.getText());
        position.setDependency(cbDependency.getValue());
        position.setSalaryCategory(cbSalaryCategory.getValue());
        position.setEmploymentType(cbEmploymentType.getValue());

        dao.save(position);
        reloadData();
        clearForm();
    }

    @FXML
    public void delete() {
        Position selected = table.getSelectionModel().getSelectedItem();

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
        txtLastName.clear();
        txtName.clear();
        txtSearchIdNumber.clear();

        cbDependency.setValue(null);
        cbSalaryCategory.setValue(null);
        cbEmploymentType.setValue(null);

        table.getSelectionModel().clearSelection();
    }
}