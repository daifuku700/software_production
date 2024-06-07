package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class ClientFunc extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientFunc(Socket socket, BufferedReader in, PrintWriter out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    public void run() {
        try {
            // クライアントの処理
            makeMP3("sample.mp3");
            playMP3("sample.mp3");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void makeMP3(String fileName) {
        try {
            // mp3ファイル作成
            // ...以下に記載
            System.out.println(fileName + " created!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playMP3(String fileName) {
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
}