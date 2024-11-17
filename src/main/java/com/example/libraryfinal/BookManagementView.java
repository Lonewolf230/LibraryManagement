package com.example.libraryfinal;

import dao.UserDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import model.Book;
import dao.BookDAO;
import model.User;

import java.sql.SQLException;

public class BookManagementView extends VBox {
    private TextField titleField = new TextField();
    private TextField authorField = new TextField();
    private TextField isbnField = new TextField();
//    private TextField donorField = new TextField();
    private TableView<Book> bookTable = new TableView<>();
    private BookDAO bookDAO = new BookDAO();
    private ComboBox<User> donorComboBox = new ComboBox<>();
    private UserDAO userDAO = new UserDAO(); // Add this line



    public BookManagementView() {
        setPadding(new Insets(10));
        setSpacing(10);

        // Form for adding/editing books
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.addRow(0, new Label("Title:"), titleField);
        form.addRow(1, new Label("Author:"), authorField);
        form.addRow(2, new Label("ISBN:"), isbnField);
        form.addRow(3, new Label("Donated By:"), donorComboBox);

        HBox buttonBox = new HBox(10);
        Button addButton = new Button("Add Book");
        addButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");

        Button updateButton=new Button("Update Book");
        updateButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");

        Button deleteButton=new Button("Delete Book");
        deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");

        Button refreshButton=new Button("Refresh");

        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, refreshButton);


        addButton.setOnAction(e->{
            addBook();
            refreshDonorComboBox();
        });
        updateButton.setOnAction(e->updateBook());
        deleteButton.setOnAction(e->deleteBook());
        refreshButton.setOnAction(e->{
                refreshTable();
                refreshDonorComboBox();
        });

        // Setup table
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAuthor()));

        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIsbn()));

        TableColumn<Book, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().isAvailable() ? "Available" : "Borrowed"));

        TableColumn<Book, String> donorNameCol = new TableColumn<>("Donor");
        donorNameCol.setCellValueFactory(data -> {
            int donorId = data.getValue().getDonatedBy();
            try {
                return new SimpleStringProperty(bookDAO.getDonorNameById(donorId));
            } catch (SQLException e) {
                e.printStackTrace();
                return new SimpleStringProperty("Unknown");
            }
        });

        bookTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    fillFormWithSelectedBook(newValue);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        bookTable.getColumns().addAll(titleCol, authorCol,isbnCol,statusCol,donorNameCol);

        getChildren().addAll(form,buttonBox, bookTable);
        setupDonorComboBox();
        refreshTable();
    }

    private void fillFormWithSelectedBook(Book book) throws SQLException {
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        isbnField.setText(book.getIsbn());
        donorComboBox.setValue(userDAO.getUserById(book.getDonatedBy())); // Set selected donor by ID
    }


    private void populateDonorComboBox() {
        try {
            UserDAO userDAO = new UserDAO();
            donorComboBox.getItems().setAll(userDAO.getAllUsers());
            donorComboBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(User user) {
                    return user != null ? user.getName() : "";
                }
                @Override
                public User fromString(String name) {
                    return donorComboBox.getItems().stream()
                            .filter(user -> user.getName().equals(name))
                            .findFirst().orElse(null);
                }
            });
        } catch (SQLException e) {
            showError("Error loading users: " + e.getMessage());
        }
    }

    private void setupDonorComboBox() {
        // Set up the converter once
        donorComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(User user) {
                return user != null ? user.getName() : "";
            }

            @Override
            public User fromString(String name) {
                return donorComboBox.getItems().stream()
                        .filter(user -> user.getName().equals(name))
                        .findFirst().orElse(null);
            }
        });
        refreshDonorComboBox(); // Initial population
    }


    public void addBook() {
        User selectedDonor = donorComboBox.getValue();
        if (selectedDonor == null) {
            showError("Please select a donor.");
            return;
        }
        int donorId = selectedDonor.getId(); // Use donor ID
        Book newBook = new Book(0, titleField.getText(), authorField.getText(), isbnField.getText(), donorId);

        try {
            boolean success = bookDAO.addBook(newBook);
            if (success) {
                showSuccess("Book added successfully.");
                refreshTable();
                clearForm();
            } else {
                showError("Failed to add book.");
            }
        } catch (SQLException e) {
            showError("Error adding book: " + e.getMessage());
        }
    }


    private void updateBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showError("Please select a book to update.");
            return;
        }
        try {
            User selectedDonor = donorComboBox.getValue();
            if (selectedDonor == null) {
                showError("Please select a donor.");
                return;
            }
            int donorId = selectedDonor.getId();

            // Create an updated Book object with the donorId
            Book updatedBook = new Book(selectedBook.getId(), titleField.getText(), authorField.getText(),
                    isbnField.getText(), donorId);

            // Update the book in the database
            bookDAO.updateBook(updatedBook);
            refreshTable();
            clearForm();
            showSuccess("Book updated successfully.");
        } catch (Exception e) {
            showError("Error updating book: " + e.getMessage());
        }
    }


    private void deleteBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showError("Please select a book to delete.");
            return;
        }
        try {
            String isbn = selectedBook.getIsbn(); // Use the selected book's ISBN
            bookDAO.deleteBook(isbn);
            refreshTable();
            clearForm();
            showSuccess("Book deleted successfully.");
        } catch (Exception e) {
            showError("Error deleting book: " + e.getMessage());
        }
    }

    private void refreshTable() {
        try {
            bookTable.getItems().setAll(bookDAO.getAllBooks());
        } catch (Exception e) {
            showError("Error loading books: " + e.getMessage());
        }
    }

    private void refreshDonorComboBox() {
        try {
            // Store the currently selected user
            User selectedUser = donorComboBox.getValue();

            // Clear and reload all users
            donorComboBox.getItems().clear();
            donorComboBox.getItems().addAll(userDAO.getAllUsers());

            // Restore the selection if the user still exists
            if (selectedUser != null) {
                donorComboBox.getItems().stream()
                        .filter(user -> user.getId() == selectedUser.getId())
                        .findFirst()
                        .ifPresent(user -> donorComboBox.setValue(user));
            }
        } catch (SQLException e) {
            showError("Error loading users: " + e.getMessage());
        }
    }

    private void clearForm() {
        titleField.clear();
        authorField.clear();
        isbnField.clear();
        donorComboBox.setValue(null);
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