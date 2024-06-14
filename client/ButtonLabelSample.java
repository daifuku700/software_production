package test;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class ButtonLabelSample implements ActionListener {
    JLabel l1, l2;
    JButton b1, b2;

    private void createAndShowGUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(10, 10, 500, 250);
        frame.setTitle("ButtonLabelSample");

        JPanel p = new JPanel();
        GridLayout layout = new GridLayout(2, 2);
        p.setLayout(layout);

        // SwingConstants.CENTER で中央揃え
        l1 = new JLabel("label1", SwingConstants.CENTER);
        l2 = new JLabel("label2", SwingConstants.CENTER);
        // 枠線をつける
        l1.setBorder(new LineBorder(Color.BLACK, 2));
        l2.setBorder(new LineBorder(Color.BLACK, 2));
        // 背景色をつける
        l1.setBackground(Color.LIGHT_GRAY);
        l2.setBackground(Color.LIGHT_GRAY);
        l1.setOpaque(true);
        l2.setOpaque(true);

        b1 = new JButton("button1");
        b2 = new JButton("button2");
        // ボタンクリック時のリスナーを追加
        b1.addActionListener(this);
        b2.addActionListener(this);

        p.add(l1);
        p.add(b1);
        p.add(l2);
        p.add(b2);

        frame.getContentPane().add(p);
        frame.setVisible(true);
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
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ButtonLabelSample().createAndShowGUI();
            }
        });
    }
}