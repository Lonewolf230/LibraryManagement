package dao;

import model.Book;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
//    public void addBook(Book book) throws SQLException {
//        String sql = "INSERT INTO books (title, author, isbn, donated_by, is_available, donation_date) " +
//                "VALUES (?, ?, ?, ?, ?, ?)";
//
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setString(1, book.getTitle());
//            pstmt.setString(2, book.getAuthor());
//            pstmt.setString(3, book.getIsbn());
//            pstmt.setString(4, book.getDonatedBy());
//            pstmt.setBoolean(5, book.isAvailable());
//            pstmt.setDate(6, Date.valueOf(book.getDonationDate()));
//
//            pstmt.executeUpdate();
//        }
//    }

    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("phone"));
                user.setFines(rs.getDouble("fines"));
                return user;
            }
        }
        return null;
    }

public boolean addBook(Book book) throws SQLException {
    String sql = "INSERT INTO books (title, author, isbn, donated_by, is_available) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, book.getTitle());
        pstmt.setString(2, book.getAuthor());
        pstmt.setString(3, book.getIsbn());
        pstmt.setInt(4, book.getDonatedBy()); // donor ID
        pstmt.setBoolean(5, book.isAvailable());

        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLIntegrityConstraintViolationException e) {
        throw new SQLException("The specified donor does not exist.");
    }
}

//    public void updateBook(Book book) throws SQLException {
//        String checkSql = "SELECT COUNT(*) FROM books WHERE id=?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
//
//            checkStmt.setInt(1, book.getId());
//            ResultSet rs = checkStmt.executeQuery();
//            if (rs.next() && rs.getInt(1) == 0) {
//                throw new SQLException("Book with ID " + book.getId() + " does not exist.");
//            }
//        }
//
//        String sql = "UPDATE books SET title=?, author=?, isbn=?, donated_by=?, is_available=? WHERE id=?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setString(1, book.getTitle());
//            pstmt.setString(2, book.getAuthor());
//            pstmt.setString(3, book.getIsbn());
//            pstmt.setString(4, book.getDonatedBy());
//            pstmt.setBoolean(5, book.isAvailable());
//            pstmt.setInt(6, book.getId());
//
//            pstmt.executeUpdate();
//        }
//    }

    public void updateBook(Book book) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM books WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, book.getId());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                throw new SQLException("Book with ID " + book.getId() + " does not exist.");
            }
        }

        String sql = "UPDATE books SET title=?, author=?, isbn=?, donated_by=?, is_available=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setInt(4, book.getDonatedBy()); // Use int here
            pstmt.setBoolean(5, book.isAvailable());
            pstmt.setInt(6, book.getId());

            pstmt.executeUpdate();
        }
    }


//    public String getDonorNameById(int userId) throws SQLException {
//        String sql = "SELECT name FROM users WHERE id = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, userId);
//            ResultSet rs = pstmt.executeQuery();
//            if (rs.next()) {
//                return rs.getString("name");
//            }
//        }
//        return null; // or throw an exception if user is not found
//    }

    public String getDonorNameById(int id) throws SQLException {
        String sql = "SELECT name FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        }
        return "Unknown";
    }

    public void deleteBook(String isbn) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM books WHERE isbn=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, isbn);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                throw new SQLException("Book with ISBN " + isbn + " does not exist.");
            }
        }

        String sql = "DELETE FROM books WHERE isbn=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);
            pstmt.executeUpdate();
        }
    }

//    public void deleteBook(String isbn) throws SQLException {
//        String checkSql = "SELECT COUNT(*) FROM books WHERE isbn=?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
//
//            checkStmt.setString(1, isbn);
//            ResultSet rs = checkStmt.executeQuery();
//            if (rs.next() && rs.getInt(1) == 0) {
//                throw new SQLException("Book with ISBN " + isbn + " does not exist.");
//            }
//        }
//
//        String sql = "MODIFY books set is_deleted=1 WHERE isbn=?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setString(1, isbn);
//            pstmt.executeUpdate();
//        }
//    }


//    public List<Book> getAvailableBooks() throws SQLException {
//        List<Book> books = new ArrayList<>();
//        String sql = "SELECT * FROM books WHERE is_available = 1 ORDER BY title";
//
//        try (Connection conn = DatabaseConnection.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//
//            while (rs.next()) {
//                Book book = new Book(
//                        rs.getInt("id"),
//                        rs.getString("title"),
//                        rs.getString("author"),
//                        rs.getString("isbn"),
//                        rs.getString("donated_by")
//                );
//                book.setAvailable(rs.getBoolean("is_available"));
//                if (rs.getDate("donation_date") != null) {
//                    book.setDonationDate(rs.getDate("donation_date").toLocalDate());
//                }
//                books.add(book);
//            }
//        }
//        return books;
//
//    }
//
//    public List<Book> getAllBooks() throws SQLException {
//        List<Book> books = new ArrayList<>();
//        String sql = "SELECT * FROM books";
//
//        try (Connection conn = DatabaseConnection.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//
//            while (rs.next()) {
//                Book book = new Book(
//                        rs.getInt("id"),
//                        rs.getString("title"),
//                        rs.getString("author"),
//                        rs.getString("isbn"),
//                        rs.getString("donated_by")
//                );
//                book.setAvailable(rs.getBoolean("is_available"));
//                books.add(book);
//            }
//        }
//        return books;
//    }

    public List<Book> getAvailableBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE is_available = 1 ORDER BY title";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getInt("donated_by") // Change to int
                );
                book.setAvailable(rs.getBoolean("is_available"));
                if (rs.getDate("donation_date") != null) {
                    book.setDonationDate(rs.getDate("donation_date").toLocalDate());
                }
                books.add(book);
            }
        }
        return books;
    }

    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getInt("donated_by") // Change to int
                );
                book.setAvailable(rs.getBoolean("is_available"));
                books.add(book);
            }
        }
        return books;
    }


    public boolean isBookAvailable(int bookId) throws SQLException {
        String sql = "SELECT is_available FROM books WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();

            return rs.next() && rs.getBoolean("is_available");
        }
    }

    public boolean checkIsbnExists(String isbn) throws SQLException {
        String sql = "SELECT 1 FROM books WHERE isbn = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();
        }
    }
}




//package dao;
//
//import model.Book;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class BookDAO {
//    // ... (keep your existing methods) ...
//
//    public List<Book> getAvailableBooks() throws SQLException {
//        List<Book> books = new ArrayList<>();
//        String sql = "SELECT * FROM books WHERE is_available = 1 ORDER BY title";
//
//        try (Connection conn = DatabaseConnection.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//
//            while (rs.next()) {
//                Book book = new Book(
//                        rs.getInt("id"),
//                        rs.getString("title"),
//                        rs.getString("author"),
//                        rs.getString("isbn"),
//                        rs.getString("donated_by")
//                );
//                book.setAvailable(rs.getBoolean("is_available"));
//                if (rs.getDate("donation_date") != null) {
//                    book.setDonationDate(rs.getDate("donation_date").toLocalDate());
//                }
//                books.add(book);
//            }
//        }
//        return books;
//    }
//
//    public boolean isBookAvailable(int bookId) throws SQLException {
//        String sql = "SELECT is_available FROM books WHERE id = ?";
//
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setInt(1, bookId);
//            ResultSet rs = pstmt.executeQuery();
//
//            return rs.next() && rs.getBoolean("is_available");
//        }
//    }
//
//    public boolean checkIsbnExists(String isbn) throws SQLException {
//        String sql = "SELECT 1 FROM books WHERE isbn = ?";
//
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setString(1, isbn);
//            ResultSet rs = pstmt.executeQuery();
//
//            return rs.next();
//        }
//    }
//}
