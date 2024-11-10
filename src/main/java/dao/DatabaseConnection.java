package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
//    private static final String URL = "jdbc:oracle:thin:@localhost:1521/xe";
    private static final String URL="jdbc:oracle:thin:@//localhost:1521/XE";

    private static final String USER = "System";
    private static final String PASSWORD = "manish";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}