package dao;

import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (name, email, phone, fines,is_deleted) VALUES (?, ?, ?, ?,0)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.setDouble(4, user.getFines());

            pstmt.executeUpdate();
        }
    }

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET name=?, email=?, phone=?, fines=? WHERE id=? AND (is_deleted = 0 OR is_deleted IS NULL)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.setDouble(4, user.getFines());
            pstmt.setInt(5, user.getId());

            pstmt.executeUpdate();
        }
    }

    public boolean hasOutstandingFines(int userId) throws SQLException {
        String sql = "SELECT fines FROM users WHERE id = ? AND (is_deleted = 0 OR is_deleted IS NULL)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("fines") > 0;
            }
        }
        return false;
    }


    public void deleteUser(int userId) throws SQLException {
        // First check if user exists and is not already deleted
        String checkSql = "SELECT COUNT(*) FROM users WHERE id = ? AND (is_deleted = 0 OR is_deleted IS NULL)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, userId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                throw new SQLException("User not found or already deleted");
            }
        }

        // If user exists and is not deleted, proceed with soft delete
        String sql = "UPDATE users SET is_deleted = 1 WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }



    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE is_deleted = 0 OR is_deleted IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
                user.setFines(rs.getDouble("fines"));
                users.add(user);
            }
        }
        return users;
    }
    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ? AND (is_deleted = 0 OR is_deleted IS NULL)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getDouble("fines") // Assuming fines is also part of the User model
                );
            } else {
                return null; // No user found with the given ID
            }
        }
    }
}