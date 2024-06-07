package client;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String args[]) throws IOException {
        InetAddress addr = InetAddress.getByName("localhost");
        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, 8080);
        try {
            System.out.println("socket = " + socket);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        } finally {
            System.out.println("closing...");
            socket.close();
        }
    }
}
