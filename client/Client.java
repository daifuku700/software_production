
package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client extends ClientFunc {
    public static void main(String args[]) throws IOException {
        InetAddress addr = InetAddress.getByName("localhost");
        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, 8080);
        Scanner scan = new Scanner(System.in);
        try {
            System.out.println("socket = " + socket);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            String usr = "usr";

            String cmd;
            do {
                System.out.print("input next cmd: ");
                cmd = scan.nextLine();

                switch (cmd) {
                    case "send":
                        sendFile(dis, dos, usr, "./client/audio.wav");
                        break;
                    case "get":
                        receiveFile(dis, dos, 0);
                        break;
                    case "end":
                        dos.writeUTF("end");
                        break;
                    default:
                        System.out.println("please input correct cmd");
                }
            } while (!cmd.equals("end"));
        } finally {
            System.out.println("closing...");
            scan.close();
            socket.close();
        }
    }
}
