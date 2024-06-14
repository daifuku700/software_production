package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

public class Display extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private JButton recordButton;
    private JList<String> recordingList;
    private DefaultListModel<String> listModel;
    private JLabel l1, l2;
    private JButton b1, b2;

    public Display() {
        super("音声ソフトアプリ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500); // サイズを変更
        setLocationRelativeTo(null);

        initComponents();

        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());

        // リストモデルとリスト
        listModel = new DefaultListModel<>();
        recordingList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(recordingList);

        // ボタンパネル
        JPanel buttonPanel = new JPanel(new FlowLayout());
        recordButton = new JButton("録音");
        buttonPanel.add(recordButton);

        // ButtonLabelSampleのコンポーネントを作成
        JPanel buttonLabelPanel = new JPanel();
        GridLayout layout = new GridLayout(2, 2);
        buttonLabelPanel.setLayout(layout);

        l1 = new JLabel("label1", SwingConstants.CENTER);
        l2 = new JLabel("label2", SwingConstants.CENTER);
        l1.setBorder(new LineBorder(Color.BLACK, 2));
        l2.setBorder(new LineBorder(Color.BLACK, 2));
        l1.setBackground(Color.LIGHT_GRAY);
        l2.setBackground(Color.LIGHT_GRAY);
        l1.setOpaque(true);
        l2.setOpaque(true);

        b1 = new JButton("button1");
        b2 = new JButton("button2");
        b1.addActionListener(this);
        b2.addActionListener(this);

        buttonLabelPanel.add(l1);
        buttonLabelPanel.add(b1);
        buttonLabelPanel.add(l2);
        buttonLabelPanel.add(b2);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(buttonLabelPanel, BorderLayout.NORTH);

        add(mainPanel);

        // ボタンの動作を設定
        recordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listModel.addElement("録音 " + (listModel.getSize() + 1));
                // 録音処理を実装
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

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if ("button1".equals(command)) {
            String text = l1.getText();
            if ("label1".equals(text)) {
                l1.setText(text.toUpperCase());
            } else {
                l1.setText(text.toLowerCase());
            }
        } else if ("button2".equals(command)) {
            if (l2.getBackground().equals(Color.LIGHT_GRAY)) {
                l2.setBackground(Color.RED);
                l2.setOpaque(true);
            } else {
                l2.setBackground(Color.LIGHT_GRAY);
                l2.setOpaque(true);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Display();
            }
        });
    }
}
