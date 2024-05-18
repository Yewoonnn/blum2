package Blum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUpPanel extends JPanel {
    private MainFrame mainFrame;

    public SignUpPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setName("signUpPanel");
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // 이름 입력란
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("이름: "), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField nameField = new JTextField(20);
        add(nameField, gbc);

        // 아이디 입력란
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("아이디: "), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField idField = new JTextField(20);
        add(idField, gbc);

        // 비밀번호 입력란
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("비밀번호: "), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPasswordField passwordField = new JPasswordField(20);
        add(passwordField, gbc);

        // 이메일 입력란
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("이메일: "), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField emailField = new JTextField(20);
        add(emailField, gbc);

        // 전화번호 입력란
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("전화번호: "), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField phoneField = new JTextField(20);
        add(phoneField, gbc);

        // 회원가입 버튼
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton signUpButton = new JButton("회원가입");
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 입력된 회원 정보 가져오기
                String membername = nameField.getText();
                String memberid = idField.getText();
                String memberpwd = new String(passwordField.getPassword());
                String email = emailField.getText();
                String phonenum = phoneField.getText();

                // 회원 정보 데이터베이스에 저장 (나중에 구현)
                // ...
                // 회원 정보 데이터베이스에 저장
                Connection conn = DBConnection.getConnection();
                String sql = "INSERT INTO members (membername, memberid, memberpwd, email, phonenum) VALUES (?, ?, ?, ?, ?)";
                try {
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, membername);
                    stmt.setString(2, memberid);
                    stmt.setString(3, memberpwd);
                    stmt.setString(4, email);
                    stmt.setString(5, phonenum);
                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    DBConnection.closeConnection();
                }

                // 회원가입 성공 메시지 출력
                JOptionPane.showMessageDialog(SignUpPanel.this, "회원가입이 완료되었습니다.", "회원가입 성공", JOptionPane.INFORMATION_MESSAGE);

                // 메인 패널로 전환
                mainFrame.showMainPanel();
            }
        });
        add(signUpButton, gbc);

        // 회원가입 버튼 클릭 이벤트 처리 (나중에 구현)
    }
}