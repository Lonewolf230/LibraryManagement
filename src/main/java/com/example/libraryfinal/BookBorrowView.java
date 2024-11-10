package com.example.libraryfinal;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.Book;
import dao.BookDAO;
import dao.BorrowingDAO;


import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class BookBorrowView extends VBox {
    private TextField emailField = new TextField();
    private ComboBox<Book> bookComboBox = new ComboBox<>();
    private BookDAO bookDAO = new BookDAO();
    private BorrowingDAO borrowingDAO = new BorrowingDAO();


    public BookBorrowView() {
        setPadding(new Insets(10));
        setSpacing(10);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("User Email:"), 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(new Label("Select Book:"), 0, 1);
        grid.add(bookComboBox, 1, 1);


        Button borrowButton = new Button("Borrow Book");
        borrowButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");

        Button refreshButton=new Button("Refresh Books");
        refreshButton.setStyle("-fx-background-color:#000000 ; -fx-text-fill: white;");


        borrowButton.setOnAction(e -> borrowBook());
        refreshButton.setOnAction(e->loadAvailableBooks());

        getChildren().addAll(grid, borrowButton,refreshButton);

        // Load available books
        loadAvailableBooks();
    }

    private void loadAvailableBooks() {
        try {
            bookComboBox.getItems().clear();
            bookComboBox.getItems().addAll(bookDAO.getAvailableBooks());
        } catch (SQLException e) {
            showError("Error loading books: " + e.getMessage());
        }
    }

//    private void borrowBook() {
//        try {
//            Book selectedBook = bookComboBox.getValue();
//            String userEmail = emailField.getText();
//
//            if (selectedBook == null || userEmail.isEmpty()) {
//                showError("Please select a book and enter user email");
//                return;
//            }
//
//            borrowingDAO.borrowBook(userEmail, selectedBook.getId());
//            showSuccess("Book borrowed successfully! Due date: " +
//                    LocalDate.now().plusDays(14).format(DateTimeFormatter.ISO_LOCAL_DATE));
//            loadAvailableBooks();
//        } catch (SQLException e) {
//            showError("Error borrowing book: " + e.getMessage());
//        }
//    }

    private void borrowBook() {
        try {
            Book selectedBook = bookComboBox.getValue();
            String userEmail = emailField.getText();

            if (selectedBook == null || userEmail.isEmpty()) {
                showError("Please select a book and enter user email");
                return;
            }

            // Check if user exists
            if (!borrowingDAO.userExists(userEmail)) {
                showError("User does not exist with the provided email.");
                return;
            }

            borrowingDAO.borrowBook(userEmail, selectedBook.getId());
            showSuccess("Book borrowed successfully! Due date: " +
                    LocalDate.now().plusDays(14).format(DateTimeFormatter.ISO_LOCAL_DATE));
            loadAvailableBooks();
        } catch (SQLException e) {
            showError("Error borrowing book: " + e.getMessage());
        }
    }


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }
}


//package com.example.libraryfinal;
//
//import javafx.geometry.Insets;
//import javafx.scene.control.*;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.VBox;
//import javafx.beans.property.SimpleStringProperty;
//import model.Book;
//import dao.BookDAO;
//import dao.BorrowingDAO;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//
//public class BookBorrowView extends VBox {
//    private TextField emailField = new TextField();
//    private TableView<Book> availableBooksTable = new TableView<>();
//    private BookDAO bookDAO = new BookDAO();
//    private BorrowingDAO borrowingDAO = new BorrowingDAO();
//
//    public BookBorrowView() {
//        setPadding(new Insets(10));
//        setSpacing(10);
//
//        // Email input section
//        GridPane emailGrid = new GridPane();
//        emailGrid.setHgap(10);
//        emailGrid.setVgap(10);
//        emailGrid.add(new Label("User Email:"), 0, 0);
//        emailGrid.add(emailField, 1, 0);
//
//        // Available books table
//        setupBooksTable();
//
//        // Refresh button
//        Button refreshButton = new Button("Refresh Available Books");
//        refreshButton.setOnAction(e -> loadAvailableBooks());
//
//        getChildren().addAll(emailGrid, refreshButton, availableBooksTable);
//        loadAvailableBooks();
//    }
//
//    private void setupBooksTable() {
//        // Setup columns
//        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
//        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
//
//        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
//        authorCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAuthor()));
//
//        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
//        isbnCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIsbn()));
//
//        // Action column for borrow button
//        TableColumn<Book, Void> actionCol = new TableColumn<>("Action");
//        actionCol.setCellFactory(param -> new TableCell<>() {
//            private final Button borrowButton = new Button("Borrow");
//
//            {
//                borrowButton.setOnAction(event -> {
//                    Book book = getTableView().getItems().get(getIndex());
//                    borrowBook(book);
//                });
//            }
//
//            @Override
//            protected void updateItem(Void item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty) {
//                    setGraphic(null);
//                } else {
//                    setGraphic(borrowButton);
//                }
//            }
//        });
//
//        availableBooksTable.getColumns().addAll(titleCol, authorCol, isbnCol, actionCol);
//    }
//
//    private void loadAvailableBooks() {
//        try {
//            availableBooksTable.getItems().clear();
//            availableBooksTable.getItems().addAll(bookDAO.getAvailableBooks());
//        } catch (SQLException e) {
//            showError("Error loading available books: " + e.getMessage());
//        }
//    }
//
//    private void borrowBook(Book book) {
//        String email = emailField.getText().trim();
//
//        if (email.isEmpty()) {
//            showError("Please enter your email address");
//            return;
//        }
//
//        try {
//            borrowingDAO.borrowBook(email, book.getId());
//            showSuccess("Book '" + book.getTitle() + "' borrowed successfully!\n" +
//                    "Due date: " + LocalDate.now().plusDays(14).format(DateTimeFormatter.ISO_LOCAL_DATE));
//            loadAvailableBooks(); // Refresh the table
//        } catch (SQLException e) {
//            showError("Error borrowing book: " + e.getMessage());
//        }
//    }
//
//    private void showError(String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Error");
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    private void showSuccess(String message) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Success");
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//}
