package server;

import java.io.*;

public class ServerFunc extends Thread {
    public ServerFunc() {
        super();
    };

    public void sendFile(DataInputStream dis, DataOutputStream dos){
        try{
            dos.writeUTF("ready");

            int fileID = dis.readInt();

            File file = new File("server/test.wav"); //dbからIDで持ってくる
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
        }catch (IOException e) {
            System.err.println("File send error: " + e.getMessage());
        }
    }
}
