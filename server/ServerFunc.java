package server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

public class ServerFunc extends Thread {
    public ServerFunc() {
        super();
    };

    /**
     * this is a function for moving a file when you get a file from the client
     * @param fileName name of the file that you want to move to the music directory
     */
    public void moveFile(String fileName) {
        DataBase db = new DataBase();
        Path src = Paths.get("./server/" + fileName);
        Path dest = Paths.get("./server/music/" + db.getMaxId() + ".wav");
        try {
            Files.move(src, dest);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("cannot move file");
        }
        db.close();
    }

    /**
     * this is a function for getting chat data from the database
     * @return chat from the database [{usr, path, date, description}, ...]
     */
    public ArrayList<Map<String, String>> getChatData() {
        ArrayList<Map<String, String>> chatData = new ArrayList<Map<String, String>>();
        DataBase db = new DataBase();
        int maxId = db.getMaxId();
        for (int i = 1; i <= maxId; i++) {
            chatData.add(db.get(i));
        }
        db.close();
        return chatData;
    }
}
