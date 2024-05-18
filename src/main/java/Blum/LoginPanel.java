package Blum;

import Blum.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setName("loginPanel");
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        add(new JLabel("아이디: "), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField idField = new JTextField(20);
        add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        add(new JLabel("비밀번호: "), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JPasswordField passwordField = new JPasswordField(20);
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("로그인");
        add(loginButton, gbc);

        JButton signUpButton = new JButton("회원가입");

        add(signUpButton, gbc);

        // 로그인 버튼 클릭 이벤트 처리
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                char[] password = passwordField.getPassword();
                if (id.equals("admin") && new String(password).equals("password")) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "로그인 성공!", "로그인", JOptionPane.INFORMATION_MESSAGE);
                    mainFrame.showMainPanel(); // 메인 패널로 전환
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this, "잘못된 아이디 또는 비밀번호입니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}