package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileInputStream;

public class ServerFunc extends Thread {
    public ServerFunc() {
        super();
    };

    public void sendFile(DataInputStream dis, DataOutputStream dos) {
        try {
            dos.writeUTF("ready");

            int fileID = dis.readInt();

            DataBase db = new DataBase();
            HashMap<String, String> chat = db.get(fileID);
            if (!chat.containsKey("path")) {
                System.err.println("ERR: cannot find id");
                dos.writeUTF("ID ERR");
                return;
            } else {
                dos.writeUTF("ID OK");
            }
            File file = new File(chat.get("path")); // dbからIDで持ってくる
            dos.writeLong(file.length());

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[512];
                int read = 0;
                while ((read = fis.read(buffer)) > 0) {
                    dos.write(buffer, 0, read);
                }
                System.out.println("File " + file.getName() + " sent.");
            }
            dos.writeUTF("finish");
            db.close();
        } catch (IOException e) {
            System.err.println("File send error: " + e.getMessage());
        }
    }

    // ファイルを受信する関数
    public void receiveFile(DataInputStream dis, DataOutputStream dos) {
        try {
            dos.writeUTF("ready");

            String outputFilepath = "server/audio.wav"; // 受信したファイルの保存先
            long fileSize = dis.readLong();
            String usr = dis.readUTF();

            byte[] buffer = new byte[512]; // ファイル受信時のバッファ

            try (FileOutputStream fos = new FileOutputStream(outputFilepath)) {
                int read = 0;
                long remaining = fileSize;
                while ((read = dis.read(buffer, 0, (int) Math.min(buffer.length, (int) remaining))) > 0) {
                    remaining -= read;
                    fos.write(buffer, 0, read);
                }
            }

            dos.writeUTF("finish");

            System.out.println("file received successfully");
            moveFile(outputFilepath);

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DataBase db = new DataBase();
            db.insertData(usr, "./server/music/" + (db.getMaxId() + 1) + ".wav", sdf.format(date).toString(), "");
        } catch (IOException e) {
            System.err.println("File receive error: " + e.getMessage());
        }
    }

    /**
     * this is a function for moving a file when you get a file from the client
     *
     * @param fileName name of the file that you want to move to the music directory
     */
    public void moveFile(String fileName) {
        DataBase db = new DataBase();
        Path folder = Paths.get("./server/music");
        if (!Files.exists(folder)) {
            try {
                Files.createDirectory(folder);
            } catch (IOException e) {
                System.err.println("ERR: " + e.getMessage());
                System.err.println("cannot create directory");
            }
        }
        Path src = Paths.get(fileName);
        Path dest = Paths.get("./server/music/" + (db.getMaxId() + 1) + ".wav");
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
     *
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
