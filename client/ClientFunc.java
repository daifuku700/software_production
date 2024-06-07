package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

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
            makeWAV("sample.wav");
            playWAV("sample.wav");

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

    public void makeWAV(String fileName) {
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
            long endTime = System.currentTimeMillis() + recordTime;

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

    public void playWAV(String fileName) {
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
