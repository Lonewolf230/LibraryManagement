package model;

public class User {
    private int id;
    private String name;
    private String email;
    private String phone;
    private double fines;

    // Constructor
    public User(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.fines = 0.0;
    }

    public User(int id, String name, String email, String phone, double fines) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.fines = fines; // Set to actual fines from the database
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public double getFines() { return fines; }
    public void setFines(double fines) { this.fines = fines; }
}
