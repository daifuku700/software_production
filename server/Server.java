package server;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

class ServerThread extends ServerFunc {
    Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        DataBase db = new DataBase();
        try {
            db.createTable();

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            db.insertData("usr", "./music", sdf.format(date).toString(), "");
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
        System.out.println(getChatData());
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
