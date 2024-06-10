package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String args[]) throws IOException {
        InetAddress addr = InetAddress.getByName("localhost");
        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, 8080);
        try {
            System.out.println("socket = " + socket);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),
                    true);
          
            // ファイル送信の意思表示
            Send(out);

            // サーバーからの応答を確認した後ファイルを送信
            String response = in.readLine();
            if ("Ready".equals(response)) {
                SendFile(socket, "./client/audio.wav"); // ファイル名を指定
            }
        } finally {
            System.out.println("closing...");
            socket.close();
        }
    }

    // ファイル送信の意思表示
    private static void Send(PrintWriter out) {
        out.println("Send");
    }

    // 音声ファイルの送信
    private static void SendFile(Socket socket, String filePath) {
        byte[] buffer = new byte[512]; // ファイル送信時のバッファ

        try (InputStream inputStream = new FileInputStream(filePath);
                OutputStream outputStream = socket.getOutputStream()) {
            // ファイルをストリームで送信
            int fileLength;
            while ((fileLength = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, fileLength);
            }

            // 終了処理
            outputStream.flush();
            System.out.println("File sent successfully.");

        } catch (IOException e) {
            System.err.println("File send error: " + e.getMessage());
        }
    }
}
