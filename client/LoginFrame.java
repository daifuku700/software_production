package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JButton loginButton;
    private String _username = "";

    LoginFrame() {
        // フレームの設定
        setTitle("voice board");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // パネルの作成
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        // ユーザーネームラベル
        JLabel usernameLabel = new JLabel("user name:");
        panel.add(usernameLabel);

        // ユーザーネームフィールド
        usernameField = new JTextField();
        panel.add(usernameField);

        // ログインボタン
        loginButton = new JButton("login");
        panel.add(loginButton);

        // ボタンのクリックイベント
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "please input user name", "error", JOptionPane.ERROR_MESSAGE);
                } 
                else {
                    synchronized (LoginFrame.this) {
                        _username = username;
                        LoginFrame.this.notifyAll();
                    }
                }
            }
        });

        // パネルをフレームに追加
        add(panel);
    }

    public String getUsername() {
        return _username;
    }
}
