package server;

import java.io.File;
import java.sql.*;

public class DataBase {
    private final String DB_NAME = "database.db";

    private Connection c = null;

    private Statement stmt = null;

    public DataBase() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection("jdbc:sqlite:" + this.DB_NAME);

            this.stmt = this.c.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        try {
            this.stmt.executeQuery("CREATE TABLE IF NOT EXISTS chat " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "usr TEXT NOT NULL, path TEXT NOT NULL," +
                    "date TEXT NOT NULL, description TEXT);");
            this.c.commit();
            System.out.println("created table");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("cannot create table");
        }
    }

    public void insertData(String usr, String path, String date, String description) {
        try {
            this.stmt.executeUpdate("INSERT INTO chat (usr, path, date, description) " +
                    "VALUES ('" + usr + "', '" + path + "', '" + date + "', '" + description + "');");
            System.out.println("inserted data");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("cannot insert data");
        }
    }

    public String getPath(int id) {
        String path = null;
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM chat WHERE id = " + id);
            while (rs.next()) {
                path = rs.getString("path");
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("cannot get path");
        }
        return path;
    }
}
