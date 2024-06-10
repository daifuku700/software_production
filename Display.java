import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Display extends JFrame {
    private JPanel mainPanel;
    private JButton recordButton, playButton;
    private JTextArea logArea;

    public Display() {
        super("音声ソフトアプリ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        initComponents();

        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());

        // ボタンパネル
        JPanel buttonPanel = new JPanel(new FlowLayout());
        recordButton = new JButton("録音");
        playButton = new JButton("再生");
        buttonPanel.add(recordButton);
        buttonPanel.add(playButton);

        // ログエリア
        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        // ボタンの動作を設定
        recordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logArea.append("録音を開始しました\n");
                // 録音処理を実装
            }
        });

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logArea.append("再生を開始しました\n");
                // 再生処理を実装
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