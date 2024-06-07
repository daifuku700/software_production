package server;

import java.io.File;
import java.sql.*;

public class ServerFunc extends Thread {
    public ServerFunc() {
        super();
    };

    public static void createTable(File db) {
        String cmd = "CREATE TABLE IF NOT EXISTS chat " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "usr TEXT NOT NULL, path TEXT NOT NULL," +
                "date TEXT NOT NULL, description TEXT);";

    }
}
