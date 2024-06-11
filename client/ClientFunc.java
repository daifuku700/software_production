package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class ClientFunc {
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
}
