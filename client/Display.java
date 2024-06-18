package client;

import java.awt.BorderLayout;
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

    public Display() {
        super("音声ソフトアプリ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);

        initComponents();

        setVisible(true);
    }

    private void initComponents() {
        // リストモデルとリスト
        listModel = new DefaultListModel<>();
        recordingList = new JList<>(listModel);
        recordingList.setCellRenderer(new BubbleListCellRenderer()); // カスタムレンダラー
        JScrollPane scrollPane = new JScrollPane(recordingList); // スクロールバー

        // ボタンパネル
        JPanel buttonPanel = new JPanel(new FlowLayout());
        recordButton = new JButton("録音");
        buttonPanel.add(recordButton);
        sendButton = new JButton("送信");
        buttonPanel.add(sendButton);

        // メインパネル
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.EAST); // 自身の録音を右側に配置
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // ボタン動作
        recordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listModel.addElement("録音 " + (listModel.getSize() + 1));
                // 録音処理を実装

            }
        });
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 送信処理を実装
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Display();
            }
        });
    }
}
