package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/ma_base_de_donnees";
    private static final String USER = "root";
    private static final String PASSWORD = "25Standup*";

    public static Connection getConnection() throws SQLException {
    	// Connect to the Database
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
