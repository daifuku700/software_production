package server;

import java.io.*;
import java.net.*;

class ServerThread extends ServerFunc {
    Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        DataBase db = new DataBase();
        db.createTable();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),
                    true);

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
        db.close();
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
