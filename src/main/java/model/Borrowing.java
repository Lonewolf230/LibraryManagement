//package model;
//
//import java.time.LocalDate;
//
//public class Borrowing {
//    private int id;
//    private int bookId;
//    private int userId;
//    private LocalDate borrowDate;
//    private LocalDate dueDate;
//    private LocalDate returnDate;
//    private double fineAmount;
//
//    // Constructor
//    public Borrowing(int id, int bookId, int userId, LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate, double fineAmount) {
//        this.id = id;
//        this.bookId = bookId;
//        this.userId = userId;
//        this.borrowDate = borrowDate;
//        this.dueDate = dueDate;
//        this.returnDate = returnDate;
//        this.fineAmount = fineAmount;
//    }
//
//    // Getters and setters
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public int getBookId() {
//        return bookId;
//    }
//
//    public void setBookId(int bookId) {
//        this.bookId = bookId;
//    }
//
//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
//
//    public LocalDate getBorrowDate() {
//        return borrowDate;
//    }
//
//    public void setBorrowDate(LocalDate borrowDate) {
//        this.borrowDate = borrowDate;
//    }
//
//    public LocalDate getDueDate() {
//        return dueDate;
//    }
//
//    public void setDueDate(LocalDate dueDate) {
//        this.dueDate = dueDate;
//    }
//
//    public LocalDate getReturnDate() {
//        return returnDate;
//    }
//
//    public void setReturnDate(LocalDate returnDate) {
//        this.returnDate = returnDate;
//    }
//
//    public double getFineAmount() {
//        return fineAmount;
//    }
//
//    public void setFineAmount(double fineAmount) {
//        this.fineAmount = fineAmount;
//    }

package model;

import java.time.LocalDate;

public class Borrowing {
    private int id;
    private int bookId;
    private int userId;
    private String bookTitle;
    private String isbn;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double currentFine;

    // Constructors, getters, and setters

    public Borrowing() {
    }

    public Borrowing(int id, int bookId, int userId, String bookTitle, String isbn,
                     LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate, double currentFine) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.bookTitle = bookTitle;
        this.isbn = isbn;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.currentFine = currentFine;
    }

    public int getId() {
        return id;
    }

    public int getBookId() {
        return bookId;
    }

    public int getUserId() {
        return userId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getIsbn() {
        return isbn;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public double getCurrentFine() {
        return currentFine;
    }

    // Additional setters if needed
    public void setId(int id) {
        this.id = id;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setCurrentFine(double currentFine) {
        this.currentFine = currentFine;
    }


}
