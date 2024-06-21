
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
        Socket mainSocket = new Socket(addr, 8080);
        Scanner scan = new Scanner(System.in);

        // 即時通信用のスレッドを開始
        Socket notifySocket = new Socket(addr, 8081);
        Thread notificationThread = new Thread(new ClientFunc.NotificationHandler(notifySocket));
        notificationThread.start();

        try {
            System.out.println("main socket = " + mainSocket);
            DataInputStream dis = new DataInputStream(mainSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(mainSocket.getOutputStream());

            String usr = "usr";

            String cmd;
            do {
                System.out.print("input next cmd: ");
                cmd = scan.next();

                switch (cmd) {
                    case "send":
                        sendFile(dis, dos, usr, "./client/audio.wav");
                        notifyServer(); // 二つ目のサーバーに通知
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
            mainSocket.close();
            notifySocket.close();
        }
    }
}
