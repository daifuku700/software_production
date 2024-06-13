package server;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DataBase {
    private final String DB_NAME = "database.db";

    private Connection c = null;

    private Statement stmt = null;

    public DataBase() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection("jdbc:sqlite:" + this.DB_NAME);
            this.stmt = this.c.createStatement();
            createTable();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("cannot connect to database");
        }
    }

    /**
     * this is a function for creating a table in the database
     */
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

    /**
     * this is a function for inserting data to the database
     * @param usr user name
     * @param path path of the voice data
     * @param date date of the voice data
     * @param description description of the voice data
     */
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

    /**
     * this is a function for getting the max id of the dataset
     * @return max id of the dataset
     */
    public int getMaxId() {
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
        return id;
    }

    /**
     * this is a function for getting chat data from the database
     * @param id id of the chat
     * @return chat from the database {usr, path, date, description}
     */
    public HashMap<String, String> get(int id) {
        HashMap<String, String> data = new HashMap<String, String>();
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM chat WHERE id = " + id);
            while (rs.next()) {
                data.put("usr", rs.getString("usr"));
                data.put("path", rs.getString("path"));
                data.put("date", rs.getString("date"));
                data.put("description", rs.getString("description"));
            }
            rs.close();
            System.out.println("got data");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("cannot get data");
        }
        return data;
    }

    /**
     * this is a function for getting the path of the voice data
     * @param id id of the chat
     * @return path of the voice data
     */
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

    /**
     * this is a function to close the connection to the database
     * ! you should call this function when you finish using the database
     */
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
