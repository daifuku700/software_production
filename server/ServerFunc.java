package server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ServerFunc extends Thread {
    public ServerFunc() {
        super();
    };

    // ファイルを受信する関数
    public void receiveFile(Socket socket) { 
        String outputFilepath = "server/received_audio.wav"; // 受信したファイルの保存先
        byte[] buffer = new byte[512]; // ファイル受信時のバッファ

        try {
            InputStream inputStream = socket.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(outputFilepath);

            // ファイルをストリームで受信
            int fileLength;
            while ((fileLength = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, fileLength);
            }

            // 終了処理
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            System.out.println("File received successfully.");

        } catch (IOException e) {
            System.err.println("File receive error: "  + e.getMessage());
        }
    }
}
