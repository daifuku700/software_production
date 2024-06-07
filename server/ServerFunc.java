package server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerFunc extends Thread {
    public ServerFunc() {
        super();
    };

    public void moveFile(String fileName) {
        DataBase db = new DataBase();
        Path src = Paths.get(fileName);
        Path dest = Paths.get("./music/" + db.getNextId());
        try {
            Files.move(src, dest);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("cannot move file");
        }
        db.close();
    }
}
