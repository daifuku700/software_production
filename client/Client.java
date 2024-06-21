
package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
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
            
            usr = login();
            System.out.println("login: " + usr);
            do {
                System.out.print("input next cmd: ");
                cmd = scan.next();

                switch (cmd) {
                    case "send":
                        sendFile(dis, dos, usr, "./client/audio.wav");
                        break;
                    case "get":
                        System.out.print("input ID: ");
                        int ID = scan.nextInt();
                        if (ID < 1) {
                            System.out.println("ID should be positive");
                            break;
                        }
                        getFile(dis, dos, ID);
                        break;
                    case "getChat":
                        ArrayList<HashMap<String, String>> chat = getChat(dis, dos);
                        for (HashMap<String, String> c : chat) {
                            System.out.println(c);
                        }
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
