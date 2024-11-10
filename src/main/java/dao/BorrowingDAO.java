package dao;

import model.Borrowing;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowingDAO {
    public void borrowBook(String userEmail, int bookId) throws SQLException {
        String sql = "INSERT INTO borrowings (book_id, user_id, borrow_date, due_date) " +
                "SELECT ?, (SELECT id FROM users WHERE email = ?), SYSDATE, SYSDATE + 14 " +
                "FROM dual WHERE EXISTS (SELECT 1 FROM books WHERE id = ? AND is_available = 1)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            pstmt.setString(2, userEmail);
            pstmt.setInt(3, bookId);

            int affected = pstmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Book is not available for borrowing");
            }

            // Update book availability
            updateBookAvailability(conn, bookId, false);
        }
    }

    public double returnBook(int borrowingId) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            // Calculate fine
            String fineSql = "SELECT (SYSDATE - due_date) * 5 as fine " +
                    "FROM borrowings WHERE id = ? AND return_date IS NULL";
            double fine = 0;

            try (PreparedStatement pstmt = conn.prepareStatement(fineSql)) {
                pstmt.setInt(1, borrowingId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    fine = Math.max(0, rs.getDouble("fine"));
                }
            }

            // Update borrowing record
            String updateSql = "UPDATE borrowings SET return_date = SYSDATE, fine_amount = ? " +
                    "WHERE id = ? AND return_date IS NULL";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setDouble(1, fine);
                pstmt.setInt(2, borrowingId);
                pstmt.executeUpdate();
            }

            // Update user's total fines
            String userUpdateSql = "UPDATE users SET fines = fines + ? " +
                    "WHERE id = (SELECT user_id FROM borrowings WHERE id = ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(userUpdateSql)) {
                pstmt.setDouble(1, fine);
                pstmt.setInt(2, borrowingId);
                pstmt.executeUpdate();
            }

            // Update book availability
            String bookIdSql = "SELECT book_id FROM borrowings WHERE id = ?";
            int bookId;
            try (PreparedStatement pstmt = conn.prepareStatement(bookIdSql)) {
                pstmt.setInt(1, borrowingId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    bookId = rs.getInt("book_id");
                    updateBookAvailability(conn, bookId, true);
                }
            }

            conn.commit();
            return fine;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public boolean userExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if user exists
            }
        }
        return false; // User does not exist
    }


//    public double returnBook(int borrowingId) throws SQLException {
//        double fine = 0;
//        try (Connection conn = DatabaseConnection.getConnection()) {
//            conn.setAutoCommit(false);
//
//            String fineSql = "SELECT (SYSDATE - due_date) * 5 AS fine FROM borrowings WHERE id = ? AND return_date IS NULL";
//            try (PreparedStatement pstmt = conn.prepareStatement(fineSql)) {
//                pstmt.setInt(1, borrowingId);
//                try (ResultSet rs = pstmt.executeQuery()) {
//                    if (rs.next()) {
//                        fine = Math.max(0, rs.getDouble("fine"));
//                    }
//                }
//            }
//
//            String updateSql = "UPDATE borrowings SET return_date = SYSDATE, fine_amount = ? WHERE id = ? AND return_date IS NULL";
//            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
//                pstmt.setDouble(1, fine);
//                pstmt.setInt(2, borrowingId);
//                pstmt.executeUpdate();
//            }
//
//            String bookIdSql = "SELECT book_id FROM borrowings WHERE id = ?";
//            int bookId;
//            try (PreparedStatement pstmt = conn.prepareStatement(bookIdSql)) {
//                pstmt.setInt(1, borrowingId);
//                try (ResultSet rs = pstmt.executeQuery()) {
//                    if (rs.next()) {
//                        bookId = rs.getInt("book_id");
//                        updateBookAvailability(conn, bookId, true);
//                    }
//                }
//            }
//
//            conn.commit();
//        } catch (SQLException e) {
//            throw new SQLException("Error during book return operation: " + e.getMessage(), e);
//        }
//        return fine;
//    }


    public void payFine(String userEmail, double amount) throws SQLException {
        String sql = "UPDATE users SET fines = GREATEST(0, fines - ?) WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, amount);
            pstmt.setString(2, userEmail);

            int affected = pstmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("User not found");
            }
        }
    }

    private void updateBookAvailability(Connection conn, int bookId, boolean available) throws SQLException {
        String sql = "UPDATE books SET is_available = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, available ? 1 : 0);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
        }
    }

    public List<Borrowing> getBorrowingsByUserEmail(String email) throws SQLException {
        List<Borrowing> borrowings = new ArrayList<>();
        String sql = "SELECT b.id, bk.title, bk.isbn, b.borrow_date, b.due_date, b.return_date, b.fine_amount " +
                "FROM borrowings b " +
                "JOIN books bk ON b.book_id = bk.id " +
                "JOIN users u ON b.user_id = u.id " +
                "WHERE u.email = ? AND b.return_date IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Borrowing borrowing = new Borrowing();
                borrowing.setId(rs.getInt("id"));
                borrowing.setBookTitle(rs.getString("title"));
                borrowing.setIsbn(rs.getString("isbn"));
                borrowing.setBorrowDate(rs.getDate("borrow_date").toLocalDate());
                borrowing.setDueDate(rs.getDate("due_date").toLocalDate());
                borrowing.setCurrentFine(rs.getDouble("fine_amount"));
                borrowings.add(borrowing);
            }
        }
        return borrowings;
    }

}
