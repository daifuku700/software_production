package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class Display extends JFrame {
    private JButton recordButton;
    private JButton sendButton;
    private JList<String> recordingList;
    private DefaultListModel<String> listModel;
    private boolean isRecording;

    private static final int PANEL_WIDTH = 200;

    public Display() {
        super("音声ソフトアプリ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);

        initComponents();

        setVisible(true);
    }

    private void initComponents() {
        isRecording = false;
        // リストモデルとリスト
        listModel = new DefaultListModel<>();
        recordingList = new JList<>(listModel);
        recordingList.setCellRenderer(new BubbleListCellRenderer(PANEL_WIDTH)); // 横幅を指定
        JScrollPane scrollPane = new JScrollPane(recordingList); // スクロールバー

        // ボタンパネル
        JPanel buttonPanel = new JPanel(new FlowLayout());
        recordButton = new JButton("録音");
        sendButton = new JButton("送信");
        sendButton.setEnabled(false);
        buttonPanel.add(recordButton);
        buttonPanel.add(sendButton);

        // メインパネル
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER); // 中央にリストを配置
        mainPanel.add(buttonPanel, BorderLayout.SOUTH); // ボタンパネルを下部に配置
        add(mainPanel);

        // ボタン動作
        recordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleRecordButton();
            }
        });

        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listModel.addElement("_audio.wav");
                sendButton.setEnabled(false);
                sendRecordedFile("_audio.wav");
            }
        });

        recordingList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedValue = recordingList.getSelectedValue();
                if (selectedValue != null) {
                    System.out.println("再生を開始しました: " + selectedValue);
                    ClientFunc.playWAV(selectedValue);
                }
            }
        });

        // チャットデータをロードしてリストに表示
        new Thread(() -> loadChatData()).start();
    }

    private void loadChatData() {
        try (Socket socket = new Socket("localhost", 8080);
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

            ArrayList<HashMap<String, String>> chatData = ClientFunc.getChat(dis, dos);
            SwingUtilities.invokeLater(() -> {
                for (HashMap<String, String> chat : chatData) {
                    String entry = "ID: " + chat.get("id") + ", Path: " + chat.get("path");
                    listModel.addElement(entry);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRecordButton() {
        if (!isRecording) {
            startRecording();
        }
    }

    private void startRecording() {
        isRecording = true;
        sendButton.setEnabled(false);
        recordButton.setText("録音中...");
        System.out.println("録音を開始しました");
        new Thread(new Runnable() {
            public void run() {
                ClientFunc.makeWAV("_audio.wav");
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        recordButton.setText("録音");
                        isRecording = false;
                        sendButton.setEnabled(true);
                        System.out.println("録音を終了しました");
                    }
                });
            }
        }).start();
    }

    private void sendRecordedFile(String filePath) {
        new Thread(() -> {
            try (Socket socket = new Socket("localhost", 8080);
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

                ClientFunc.sendFile(dis, dos, "usr", filePath);
                System.out.println("File sent: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Display();
            }
        });
    }
}
