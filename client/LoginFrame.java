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
        // setTitle("voice board");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // パネルの作成
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(230, 255, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 30, 30, 30);

        // ヘッダーラベル
        JLabel headerLabel = new JLabel("Login to Voice Board", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(headerLabel, gbc);

        // ユーザーネームラベル
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(usernameLabel, gbc);

        // ユーザーネームフィールド
        usernameField = new JTextField(15);
        usernameField.setPreferredSize(new Dimension(100, 40));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(usernameField, gbc);

        // ログインボタン
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(100, 40));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        // ボタンのクリックイベント
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "please input user name", "error", JOptionPane.ERROR_MESSAGE);
                } 
                else if (username.length() > 8) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Username must be 8 characters or less", "error", JOptionPane.ERROR_MESSAGE);
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
