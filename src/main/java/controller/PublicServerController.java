package controller;

import dao.PublicServerDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import model.PublicServer;

public class PublicServerController {

    @FXML private TextField txtIdNumber;
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtPersonalEmail;
    @FXML private TextField txtInstitutionalEmail;
    @FXML private TextField txtPhone;
    @FXML private TextField txtSearchIdNumber;

    @FXML private DatePicker dpBirthDate;

    @FXML private ComboBox<String> cbGender;
    @FXML private ComboBox<String> cbMaritalStatus;
    @FXML private ComboBox<String> cbBloodType;
    @FXML private ComboBox<String> cbEmploymentType;

    @FXML private TableView<PublicServer> table;
    @FXML private TableColumn<PublicServer, Long> colId;
    @FXML private TableColumn<PublicServer, String> colIdNumber;
    @FXML private TableColumn<PublicServer, String> colFirstName;
    @FXML private TableColumn<PublicServer, String> colLastName;
    @FXML private TableColumn<PublicServer, String> colPhone;

    private final PublicServerDAO dao = new PublicServerDAO();

    @FXML
    public void initialize() {

        cbGender.setItems(FXCollections.observableArrayList(
                "Masculino",
                "Femenino",
                "No binario",
                "Prefiero no decir"
        ));

        cbMaritalStatus.setItems(FXCollections.observableArrayList(
                "Soltero/a",
                "Casado/a",
                "Unión libre",
                "Divorciado/a",
                "Viudo/a"
        ));

        cbBloodType.setItems(FXCollections.observableArrayList(
                "A+",
                "A-",
                "B+",
                "B-",
                "AB+",
                "AB-",
                "O+",
                "O-"
        ));

        cbEmploymentType.setItems(FXCollections.observableArrayList(
                "Planta",
                "Encargo",
                "Provisional",
                "Libre nombramiento",
                "Carrera Administrativa"
        ));

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIdNumber.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

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

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
            if (selected != null) {
                fillForm(selected);
            }
        });

        reloadData();
    }

    @FXML
    public void save() {
        if (!validateFields()) {
            return;
        }

        PublicServer server = table.getSelectionModel().getSelectedItem();

        if (server == null) {
            server = new PublicServer();
        }

        server.setIdNumber(txtIdNumber.getText().trim());
        server.setFirstName(txtFirstName.getText().trim());
        server.setLastName(txtLastName.getText().trim());
        server.setBirthDate(dpBirthDate.getValue());
        server.setGender(cbGender.getValue());
        server.setMaritalStatus(cbMaritalStatus.getValue());
        server.setBloodType(cbBloodType.getValue());
        server.setPersonalEmail(txtPersonalEmail.getText().trim());
        server.setInstitutionalEmail(txtInstitutionalEmail.getText().trim());
        server.setPhone(txtPhone.getText().trim());
        server.setEmploymentType(cbEmploymentType.getValue());

        dao.save(server);
        reloadData();
        clearFields();

        showAlert(Alert.AlertType.INFORMATION,
                "Éxito",
                "Servidor público guardado correctamente.");
    }

    @FXML
    public void delete() {
        PublicServer selected = table.getSelectionModel().getSelectedItem();

        if (selected == null || selected.getId() == null) {
            showAlert(Alert.AlertType.WARNING,
                    "Advertencia",
                    "Selecciona un registro para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Eliminar a " + selected.getFirstName() + " " + selected.getLastName() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                dao.delete(selected.getId());
                reloadData();
                clearFields();

                showAlert(Alert.AlertType.INFORMATION,
                        "Éxito",
                        "Registro eliminado correctamente.");
            }
        });
    }

    @FXML
    public void reloadData() {
        table.setItems(FXCollections.observableArrayList(dao.findAll()));
        table.refresh();
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
            table.refresh();
        }
    }

    private void fillForm(PublicServer server) {
        txtIdNumber.setText(server.getIdNumber());
        txtFirstName.setText(server.getFirstName());
        txtLastName.setText(server.getLastName());
        dpBirthDate.setValue(server.getBirthDate());
        cbGender.setValue(server.getGender());
        cbMaritalStatus.setValue(server.getMaritalStatus());
        cbBloodType.setValue(server.getBloodType());
        txtPersonalEmail.setText(server.getPersonalEmail());
        txtInstitutionalEmail.setText(server.getInstitutionalEmail());
        txtPhone.setText(server.getPhone());
        cbEmploymentType.setValue(server.getEmploymentType());
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        if (txtIdNumber.getText() == null || txtIdNumber.getText().trim().isEmpty()) {
            errors.append("• Cédula es obligatoria.\n");
        }

        if (txtFirstName.getText() == null || txtFirstName.getText().trim().isEmpty()) {
            errors.append("• Nombres es obligatorio.\n");
        }

        if (txtLastName.getText() == null || txtLastName.getText().trim().isEmpty()) {
            errors.append("• Apellidos es obligatorio.\n");
        }

        if (dpBirthDate.getValue() == null) {
            errors.append("• Fecha de nacimiento es obligatoria.\n");
        }

        if (cbGender.getValue() == null) {
            errors.append("• Género es obligatorio.\n");
        }

        if (cbMaritalStatus.getValue() == null) {
            errors.append("• Estado civil es obligatorio.\n");
        }

        if (cbBloodType.getValue() == null) {
            errors.append("• Tipo de sangre es obligatorio.\n");
        }

        if (txtPhone.getText() == null || txtPhone.getText().trim().isEmpty()) {
            errors.append("• Teléfono es obligatorio.\n");
        }

        if (cbEmploymentType.getValue() == null) {
            errors.append("• Tipo de empleo es obligatorio.\n");
        }

        if (!errors.isEmpty()) {
            showAlert(Alert.AlertType.WARNING,
                    "Campos incompletos",
                    errors.toString());
            return false;
        }

        return true;
    }

    private void clearFields() {
        txtIdNumber.clear();
        txtFirstName.clear();
        txtLastName.clear();
        txtPersonalEmail.clear();
        txtInstitutionalEmail.clear();
        txtPhone.clear();
        txtSearchIdNumber.clear();

        dpBirthDate.setValue(null);

        cbGender.setValue(null);
        cbMaritalStatus.setValue(null);
        cbBloodType.setValue(null);
        cbEmploymentType.setValue(null);

        table.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}