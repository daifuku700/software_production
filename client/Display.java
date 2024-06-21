package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        // 左パネル(データベースから取得した音声データを表示するパネル)
        JPanel leftPanel = new JPanel(new BorderLayout());
        JList<String> leftList = new JList<>(new DefaultListModel<>());
        leftList.setCellRenderer(new BubbleListCellRenderer(PANEL_WIDTH)); // 横幅を指定
        JScrollPane leftScrollPane = new JScrollPane(leftList); // スクロールバー
        leftPanel.add(leftScrollPane, BorderLayout.CENTER);
        leftPanel.setPreferredSize(new Dimension(PANEL_WIDTH, getHeight()));

        // メインパネル
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.EAST); // 自身の録音を右側に配置
        mainPanel.add(leftPanel, BorderLayout.WEST); // データベース上の録音を左側に配置
        mainPanel.add(buttonPanel, BorderLayout.SOUTH); // ボタンパネルを下部に配置
        add(mainPanel);

        // ボタン動作
        recordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleRecordButton();
                // 録音処理を実装
            }
        });
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listModel.addElement("録音 " + (listModel.getSize() + 1));
                sendButton.setEnabled(false);
                // 送信処理を実装
                isRecording = false;
            }
        });

        recordingList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedValue = recordingList.getSelectedValue();
                if (selectedValue != null) {
                    System.out.println("再生を開始しました: " + selectedValue);
                    // 再生処理を実装
                }
            }
        });
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
        // 録音処理を実装
        System.out.println("録音を開始しました");
        // ClientFuncを呼び出す
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Display();
            }
        });
    }
}
