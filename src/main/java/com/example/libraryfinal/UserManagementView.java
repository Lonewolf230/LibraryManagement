

package com.example.libraryfinal;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.User;
import dao.UserDAO;

import java.sql.SQLException;

public class UserManagementView extends VBox {
    private TextField nameField = new TextField();
    private TextField emailField = new TextField();
    private TextField phoneField = new TextField();
    private TextField idField = new TextField();  // User ID field
    private TableView<User> userTable = new TableView<>();
    private UserDAO userDAO = new UserDAO();

    public UserManagementView() {
        setPadding(new Insets(10));
        setSpacing(10);

        // Form for adding/editing users
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.addRow(1, new Label("Name:"), nameField);
        form.addRow(2, new Label("Email:"), emailField);
        form.addRow(3, new Label("Phone:"), phoneField);
        form.addRow(0, new Label("ID:"), idField);  // Adding ID field in the form


        idField.setPromptText("Enter id only while deleting or updating");

        // Buttons for actions
        HBox buttonBox = new HBox(10);
        Button addButton = new Button("Add User");
        addButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");

        Button updateButton = new Button("Update User");
        updateButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");

        Button deleteButton = new Button("Delete User");
        deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");

        Button viewButton = new Button("Refresh");

        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, viewButton);

        addButton.setOnAction(e -> addUser());
        updateButton.setOnAction(e -> updateUser());
        deleteButton.setOnAction(e -> deleteUser());
        viewButton.setOnAction(e -> refreshTable());

        // Setup table
        setupTable();

        getChildren().addAll(form, buttonBox, userTable);
    }

    private void setupTable() {

        TableColumn<User, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getId()));

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<User, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));

        TableColumn<User, Number> finesCol = new TableColumn<>("Outstanding Fines");
        finesCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getFines()));

        userTable.getColumns().addAll(idCol,nameCol, emailCol, phoneCol, finesCol);

        // Set item selection listener to populate fields
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                idField.setText(String.valueOf(newSelection.getId()));
                nameField.setText(newSelection.getName());
                emailField.setText(newSelection.getEmail());
                phoneField.setText(newSelection.getPhone());
            }
        });
    }

    private void addUser() {
        try {
            User user = new User(0, nameField.getText(), emailField.getText(), phoneField.getText());
            userDAO.addUser(user);
            showSuccess("User Added Successfully!");
            clearForm();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1) { // ORA-00001: Unique constraint violation
                showError("Email already exists!");
            } else {
                showError("Error adding user: " + e.getMessage());
            }
        }
    }

    private void updateUser() {
        try {
            int id = Integer.parseInt(idField.getText());
            User user = new User(id, nameField.getText(), emailField.getText(), phoneField.getText());
            userDAO.updateUser(user);
            showSuccess("User Updated Successfully!");
            clearForm();
        } catch (SQLException e) {
            showError("Error updating user: " + e.getMessage());
        } catch (NumberFormatException e) {
            showError("Please enter a valid ID!");
        }
    }


    public void deleteUser() {
        int userId = Integer.parseInt(idField.getText()); // assuming a selected user ID is in a text field

        try {
            if (userDAO.hasOutstandingFines(userId)) {
                showError("Cannot delete user with outstanding fines.");
                return;
            }

            userDAO.deleteUser(userId);

                showSuccess("User deleted successfully.");
                refreshTable();

        } catch (SQLException e) {
            showError("Error deleting user: " + e.getMessage());
        }
    }

    private void refreshTable() {
        try {
            userTable.getItems().setAll(userDAO.getAllUsers());
            userTable.setVisible(true);
        } catch (Exception e) {
            showError("Error loading users: " + e.getMessage());
        }
    }

    private void clearForm() {
        idField.clear();
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        userTable.getSelectionModel().clearSelection();
    }

    private void showIdField(boolean visible) {
        idField.setVisible(visible);
        if (!visible) {
            idField.clear(); // Clear ID field when hiding
        }
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}

