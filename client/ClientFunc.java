package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class ClientFunc {
    public static Display display;

    public ClientFunc() {
    }

    public static void makeWAV(String fileName) {
        AudioFormat format = new AudioFormat(44100, 16, 2, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            System.err.println("Line not supported");
            return;
        }

        try (TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info)) {
            line.open(format);
            line.start();

            AudioInputStream ais = new AudioInputStream(line);
            File file = new File(fileName);

            // 録音時間を5秒に制限
            long recordTime = 5000; // 5秒

            // 録音データを読み取り、WAVファイルに書き込む
            try (AudioInputStream shortAis = new AudioInputStream(ais, format,
                    (long) (format.getFrameRate() * recordTime / 1000.0))) {
                AudioSystem.write(shortAis, AudioFileFormat.Type.WAVE, file);
            }

            System.out.println(fileName + " created!");

        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void playWAV(String fileName) {
        try {
            File file = new File(fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioStream);
            clip.start();

            // 再生が終了するまで待機
            while (!clip.isRunning())
                Thread.sleep(10);
            while (clip.isRunning())
                Thread.sleep(10);

            clip.close();
            audioStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getFile(DataInputStream dis, DataOutputStream dos, int ID) {
        try {
            dos.writeUTF("get");

            String response = dis.readUTF();
            if (!response.equals("ready")) {
                System.err.println("ERR: cannot get response from server");
                return;
            }

            dos.writeInt(ID);

            response = dis.readUTF();
            if (!response.equals("ID OK")) {
                System.err.println("ERR: cannot find id");
                return;
            }

            String fileName = "./client/music/" + ID + ".wav";

            Path folder = Paths.get("./client/music");
            if (!Files.exists(folder)) {
                try {
                    Files.createDirectory(folder);
                } catch (IOException e) {
                    System.err.println("ERR: " + e.getMessage());
                    System.err.println("cannot create directory");
                    fileName = "./client" + ID + ".wav";
                }
            }

            long fileSize = dis.readLong();
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                byte[] buffer = new byte[512];
                int read = 0;
                long remaining = fileSize;
                while ((read = dis.read(buffer, 0, Math.min(buffer.length, (int) remaining))) > 0) {
                    remaining -= read;
                    fos.write(buffer, 0, read);
                }
                System.out.println("File " + fileName + " received.");
            }
            response = dis.readUTF();
            if (!response.equals("finish")) {
                System.err.println("ERR: cannot get response from server");
                return;
            }
        } catch (IOException e) {
            System.err.println("File receive error: " + e.getMessage());
        }
    }

    // 音声ファイルの送信
    public static void sendFile(DataInputStream dis, DataOutputStream dos, String usr, String filePath) {
        try {
            // ファイル送信の意思表示
            dos.writeUTF("send");

            // サーバーからの応答を確認した後ファイルを送信
            String response = dis.readUTF();
            if (!response.equals("ready")) {
                System.err.println("ERR: cannot get response from server");
                return;
            }

            File file = new File(filePath);

            if (!file.exists()) {
                System.out.println("ERR: cannot find audio file");
                return;
            }

            dos.writeLong(file.length());

            dos.writeUTF(usr);

            byte[] buffer = new byte[512]; // ファイル送信時のバッファ

            try (FileInputStream fis = new FileInputStream(file)) {
                // ファイルをストリームで送信
                int read = 0;
                while ((read = fis.read(buffer)) > 0) {
                    dos.write(buffer, 0, read);
                }

                System.out.println("file send successfully");
                notifyServer();
            } catch (IOException e) {
                System.err.println("File send error: " + e.getMessage());
            }

            response = dis.readUTF();
            if (!response.equals("finish")) {
                System.err.println("ERR: cannot get response from server");
                return;
            }
        } catch (IOException e) {
            System.err.println("File send error: " + e.getMessage());
        }
    }

    public static ArrayList<HashMap<String, String>> getChat(DataInputStream dis, DataOutputStream dos) {
        ArrayList<HashMap<String, String>> chat = new ArrayList<HashMap<String, String>>();
        try {
            dos.writeUTF("getChat");

            String response = dis.readUTF();
            if (!response.equals("ready")) {
                System.err.println("ERR: cannot get response from server");
                return chat;
            }

            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                HashMap<String, String> tmp = new HashMap<String, String>();
                tmp.put("id", dis.readUTF());
                tmp.put("usr", dis.readUTF());
                tmp.put("path", dis.readUTF());
                tmp.put("date", dis.readUTF());
                tmp.put("description", dis.readUTF());
                chat.add(tmp);
            }
            response = dis.readUTF();
            System.out.println(response);
            if (!response.equals("finish")) {
                System.err.println("ERR: cannot get response from server");
                return chat;
            }
        } catch (IOException e) {
            System.err.println("File send error: " + e.getMessage());
        }
        return chat;
    }

    public static String login() {
        LoginFrame frame = new LoginFrame();
        frame.setVisible(true);
        synchronized (frame) {
            while (frame.getUsername().isEmpty()) {
                try {
                    frame.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        frame.setVisible(false);
        return frame.getUsername();
    }

    public static void notifyServer() {
        try {
            InetAddress addr = InetAddress.getByName("localhost");
            Socket notifySocket = new Socket(addr, 8081);
            DataOutputStream notifyDos = new DataOutputStream(notifySocket.getOutputStream());
            notifyDos.writeUTF("notify");
            notifyDos.close();
            notifySocket.close();
        } catch (IOException e) {
            System.err.println("Failed to notify server: " + e.getMessage());
        }
    }

    // クライアントがサーバーからの通知を受信するためのハンドラー
    public static class NotificationHandler implements Runnable {
        private Socket socket;
        private Display display;

        public NotificationHandler(Socket socket, Display display) {
            this.socket = socket;
            this.display = display;
        }

        @Override
        public void run() {
            try (DataInputStream dis = new DataInputStream(socket.getInputStream())) {
                System.out.println("Notification handler started.");
                while (!socket.isClosed()) { // 変更箇所: ソケットが閉じられたかどうかを確認
                    try {
                        String message = dis.readUTF();
                        System.out.println("Notification received: " + message);
                        display.reloadComponents();
                    } catch (IOException e) {
                        if (socket.isClosed()) {
                            System.out.println("Notification socket closed.");
                        } else {
                            System.out.println("Error reading from notification socket: " + e.getMessage());
                        }
                        break; // エラーが発生したらループを抜ける
                    }
                }
                System.out.println("Notification handler finished.");
            } catch (IOException e) {
                System.out.println("Error in notification handler setup: " + e.getMessage());
            }
        }
    }
}
