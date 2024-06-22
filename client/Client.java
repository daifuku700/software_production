
package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends ClientFunc {
    public static void main(String args[]) throws IOException {
        InetAddress addr = InetAddress.getByName("localhost");
        System.out.println("addr = " + addr);
        Socket mainSocket = new Socket(addr, 8080);

        // 即時通信用のスレッドを開始
        Socket notifySocket = new Socket(addr, 8081);
        Thread notificationThread = new Thread(new ClientFunc.NotificationHandler(notifySocket));
        notificationThread.start();

        System.out.println("main socket = " + mainSocket);
        DataInputStream dis = new DataInputStream(mainSocket.getInputStream());
        DataOutputStream dos = new DataOutputStream(mainSocket.getOutputStream());

        String usr = "usr";

        usr = login();
        System.out.println("login: " + usr);

        new Display(mainSocket, notifySocket, usr, dis, dos);
    }
}
