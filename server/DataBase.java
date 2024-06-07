package server;

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
            System.err.println("Error: " + e.getMessage());
            System.err.println("cannot connect to database");
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
            System.err.println("table already exists");
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

    public int getNextId() {
        int id = 0;
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM chat ORDER BY id DESC LIMIT 1");
            while (rs.next()) {
                id = rs.getInt("id");
            }
            rs.close();
            System.out.println("got id");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("cannot get id");
        }
        return id + 1;
    }

    public String getPath(int id) {
        String path = null;
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM chat WHERE id = " + id);
            while (rs.next()) {
                path = rs.getString("path");
            }
            rs.close();
            System.out.println("got path");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("cannot get path");
        }
        return path;
    }

    public void close() {
        try {
            if (this.c != null) {
                this.c.close();
            }
            System.out.println("closed connection");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("cannot close connection");
        }
    }
}
