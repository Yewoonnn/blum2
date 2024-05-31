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
    private JTextField nameField;
    private JTextField idField;
    private JPasswordField passwordField;
    private JPasswordField passwordConfirmField;
    private JTextField emailField;
    private JTextField phoneField;

    public SignUpPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        // 상단 패널
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("←");
        backButton.setPreferredSize(new Dimension(50, 30));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showMainPanel();
            }
        });
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        // 중앙 패널
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // 이름 레이블
        JLabel nameLabel = new JLabel("이름:");
        gbc.gridy = 0;
        centerPanel.add(nameLabel, gbc);

        // 이름 텍스트 필드
        nameField = new JTextField(20);
        nameField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(nameField, gbc);

        // 아이디 레이블
        JLabel idLabel = new JLabel("아이디:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        centerPanel.add(idLabel, gbc);

        // 아이디 텍스트 필드
        idField = new JTextField(20);
        idField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(idField, gbc);

        // 비밀번호 레이블
        JLabel passwordLabel = new JLabel("비밀번호:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        centerPanel.add(passwordLabel, gbc);

        // 비밀번호 텍스트 필드
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(passwordField, gbc);

        // 비밀번호 확인 레이블
        JLabel passwordConfirmLabel = new JLabel("비밀번호 확인:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        centerPanel.add(passwordConfirmLabel, gbc);

        // 비밀번호 확인 텍스트 필드
        passwordConfirmField = new JPasswordField(20);
        passwordConfirmField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(passwordConfirmField, gbc);

        // 이메일 레이블
        JLabel emailLabel = new JLabel("이메일:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        centerPanel.add(emailLabel, gbc);

        // 이메일 텍스트 필드
        emailField = new JTextField(20);
        emailField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(emailField, gbc);

        // 전화번호 레이블
        JLabel phoneLabel = new JLabel("전화번호:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        centerPanel.add(phoneLabel, gbc);

        // 전화번호 텍스트 필드
        phoneField = new JTextField(20);
        phoneField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(phoneField, gbc);
        // 회원가입 버튼
        JButton signUpButton = new JButton("회원가입");
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String membername = nameField.getText();
                String memberid = idField.getText();
                String memberpwd = new String(passwordField.getPassword());
                String memberpwdConfirm = new String(passwordConfirmField.getPassword());
                String email = emailField.getText();
                String phonenum = phoneField.getText();

                // 입력 필드 검증
                if (membername.isEmpty() || memberid.isEmpty() || memberpwd.isEmpty() ||
                        memberpwdConfirm.isEmpty() || email.isEmpty() || phonenum.isEmpty()) {
                    JOptionPane.showMessageDialog(SignUpPanel.this, "정보를 모두 입력하세요.", "회원가입 실패", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!email.contains("@")) {
                    JOptionPane.showMessageDialog(SignUpPanel.this, "잘못된 이메일 형식입니다.", "회원가입 실패", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!memberpwd.equals(memberpwdConfirm)) {
                    JOptionPane.showMessageDialog(SignUpPanel.this, "비밀번호가 일치하지 않습니다.", "회원가입 실패", JOptionPane.WARNING_MESSAGE);
                    return;
                }
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

                JOptionPane.showMessageDialog(SignUpPanel.this, "회원가입이 완료되었습니다.", "회원가입 성공", JOptionPane.INFORMATION_MESSAGE);
                mainFrame.showMainPanel();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(signUpButton, gbc);

        add(centerPanel, BorderLayout.CENTER);

    }
}