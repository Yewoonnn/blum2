package Blum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setName("loginPanel");
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel idLabel = new JLabel("아이디: ");
        add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField idField = new JTextField(10);
        add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        JLabel passwordLabel = new JLabel("비밀번호: ");
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JPasswordField passwordField = new JPasswordField(10);
        add(passwordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton loginButton = new JButton("로그인");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String memberid = idField.getText();
                String memberpwd = new String(passwordField.getPassword());

                Connection conn = DBConnection.getConnection();
                String sql = "SELECT membername, memberid FROM members WHERE memberid = ? AND memberpwd = ?";
                try {
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, memberid);
                    stmt.setString(2, memberpwd);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        String memberName = rs.getString("membername");
                        String memberId = rs.getString("memberid");
                        JOptionPane.showMessageDialog(LoginPanel.this, "로그인 성공!", "로그인", JOptionPane.INFORMATION_MESSAGE);
                        if (memberId.equals("admin")) {
                            mainFrame.showMainPanel(memberName, true); // 관리자 아이디인 경우 isAdmin 플래그를 true로 설정
                        } else {
                            mainFrame.showMainPanel(memberName, false); // 일반 사용자인 경우 isAdmin 플래그를 false로 설정
                        }
                    } else {
                        JOptionPane.showMessageDialog(LoginPanel.this, "저장되지 않은 회원정보 입니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        buttonPanel.add(loginButton);

        JButton cancelButton = new JButton("취소");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showMainPanel();
            }
        });
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }
}