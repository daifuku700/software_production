
package server;

import java.io.*;
import java.net.*;

class ServerThread extends ServerFunc {
    Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            // db.insertData("usr", "./music", sdf.format(date).toString(), "");

            String line;
            do {
                // メッセージを受け取るフェーズ
                line = dis.readUTF();
                switch (line) {
                    case "send":
                        receiveFile(dis, dos);
                        break;
                    case "get":
                        sendFile(dis, dos);
                        break;
                    case "end":
                        System.out.println("end");
                        break;
                }
            } while (!line.equals("end"));

        } catch (NumberFormatException | NullPointerException e) { // clientが接続を切った場合
            System.out.println(Thread.currentThread().getName() + "が切断されました");
        } catch (SocketException e) { // java.net.SocketException: Connection reset
            System.out.println(Thread.currentThread().getName() + "が切断されました");
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println(e);
            } finally {
                System.out.println("closing...");
            }
        }
    }
}

public class Server {
    static int PORT = 8080;

    public static void main(String args[]) throws IOException {
        ServerSocket soc = new ServerSocket(PORT);
        try {
            while (true) {
                Socket s = soc.accept();
                ServerThread st = new ServerThread(s);
                st.start();
            }
        } finally {
            soc.close();
        }
    }
}
