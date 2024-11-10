package com.example.libraryfinal;

import dao.BorrowingDAO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Borrowing;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class BookReturnView extends VBox {
    private TextField emailField = new TextField();
    private TableView<Borrowing> borrowedBooksTable = new TableView<>();
    private BorrowingDAO borrowingDAO = new BorrowingDAO();
    private TextField finePaymentField = new TextField();

    public BookReturnView() {
        setPadding(new Insets(10));
        setSpacing(10);

        // Email input
        HBox emailBox = new HBox(10);
        emailBox.getChildren().addAll(new Label("Enter Email:"), emailField);
        Button searchButton = new Button("Search Books");
        emailBox.getChildren().add(searchButton);

        // Borrowed books table
        setupBorrowedBooksTable();

        // Fine payment section
        GridPane finePane = new GridPane();
        finePane.setHgap(10);
        finePane.setVgap(10);
        finePane.add(new Label("Pay Fine Amount:"), 0, 0);
        finePane.add(finePaymentField, 1, 0);
        Button payButton = new Button("Pay Fine");

        searchButton.setOnAction(e -> loadBorrowedBooks());
        payButton.setOnAction(e -> payFine());

        getChildren().addAll(emailBox, borrowedBooksTable, finePane, payButton);
    }

    private void setupBorrowedBooksTable() {
        TableColumn<Borrowing, String> titleCol = new TableColumn<>("Book Title");
        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookTitle()));

        TableColumn<Borrowing, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIsbn()));

        TableColumn<Borrowing, LocalDate> dueDateCol = new TableColumn<>("Due Date");
        dueDateCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDueDate()));

        TableColumn<Borrowing, Number> fineCol = new TableColumn<>("Current Fine");
        fineCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getCurrentFine()));

        TableColumn<Borrowing, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button returnButton = new Button("Return");

            {
                returnButton.setOnAction(event -> {
                    Borrowing borrowing = getTableView().getItems().get(getIndex());
                    returnBook(borrowing);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(returnButton);
                }
            }
        });

        borrowedBooksTable.getColumns().addAll(titleCol, isbnCol, dueDateCol, fineCol, actionCol);
    }

//    private void loadBorrowedBooks() {
//        try {
//            String email = emailField.getText();
//            if (email.isEmpty()) {
//                showError("Please enter email address");
//                return;
//            }
//            List<Borrowing> borrowings = borrowingDAO.getBorrowingsByUserEmail(email);
//            borrowedBooksTable.getItems().setAll(borrowings);
//        } catch (SQLException e) {
//            showError("Error loading borrowed books: " + e.getMessage());
//        }
//    }
private void loadBorrowedBooks() {
    try {
        String email = emailField.getText();
        if (email.isEmpty()) {
            showError("Please enter email address");
            return;
        }

        // Check if user exists
        if (!borrowingDAO.userExists(email)) {
            showError("User does not exist with the provided email.");
            return;
        }

        List<Borrowing> borrowings = borrowingDAO.getBorrowingsByUserEmail(email);
        borrowedBooksTable.getItems().setAll(borrowings);
    } catch (SQLException e) {
        showError("Error loading borrowed books: " + e.getMessage());
    }
}


    private void returnBook(Borrowing borrowing) {
        try {
            double fine = borrowingDAO.returnBook(borrowing.getId());
            showSuccess(String.format("Book '%s' (ISBN: %s) returned successfully.\nCurrent fine: ₹%.2f",
                    borrowing.getBookTitle(), borrowing.getIsbn(), fine));
            loadBorrowedBooks();
        } catch (SQLException e) {
            showError("Error returning book: " + e.getMessage());
        }
    }

    private void payFine() {
        try {
            String email = emailField.getText();
            if (email.isEmpty()) {
                showError("Please enter email address");
                return;
            }

            double amount = Double.parseDouble(finePaymentField.getText());
            borrowingDAO.payFine(email, amount);
            showSuccess(String.format("Payment of ₹%.2f processed successfully", amount));
            loadBorrowedBooks();
            finePaymentField.clear();
        } catch (NumberFormatException e) {
            showError("Please enter a valid amount");
        } catch (SQLException e) {
            showError("Error processing payment: " + e.getMessage());
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
