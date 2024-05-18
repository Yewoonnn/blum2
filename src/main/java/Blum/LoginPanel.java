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
                String sql = "SELECT membername FROM members WHERE memberid = ? AND memberpwd = ?";
                try {
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, memberid);
                    stmt.setString(2, memberpwd);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        String memberName = rs.getString("membername");
                        JOptionPane.showMessageDialog(LoginPanel.this, "로그인 성공!", "로그인", JOptionPane.INFORMATION_MESSAGE);
                        mainFrame.showMainPanel(memberName);
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