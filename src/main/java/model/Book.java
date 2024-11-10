package model;

import java.time.LocalDate;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private int donatedBy;
    private boolean isAvailable;
    private LocalDate donationDate;

    // Constructor
    public Book(int id, String title, String author, String isbn, int donatedBy) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.donatedBy = donatedBy;
        this.isAvailable = true;
        this.donationDate = LocalDate.now();
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public int getDonatedBy() { return donatedBy; }
    public void setDonatedBy(int donatedBy) { this.donatedBy = donatedBy; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public LocalDate getDonationDate() { return donationDate; }
    public void setDonationDate(LocalDate donationDate) { this.donationDate = donationDate; }



    public String toString() {
        return String.format("%s (Donated by: %s)", title, donatedBy);
    }
}