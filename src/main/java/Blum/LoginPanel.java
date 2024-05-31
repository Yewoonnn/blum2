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
    private JTextField idField;
    private JPasswordField passwordField;

    public LoginPanel(MainFrame mainFrame) {
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

        // 아이디 레이블
        JLabel idLabel = new JLabel("아이디:");
        gbc.gridy = 0;
        centerPanel.add(idLabel, gbc);

        // 아이디 텍스트 필드
        idField = new JTextField(15);
        idField.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(idField, gbc);

        // 비밀번호 레이블
        JLabel passwordLabel = new JLabel("비밀번호:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        centerPanel.add(passwordLabel, gbc);

        // 비밀번호 텍스트 필드
        passwordField = new JPasswordField(15);
        passwordField.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(passwordField, gbc);
        //로그인 버튼
        JButton loginButton = new JButton("로그인");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String memberid = idField.getText();
                String memberpwd = new String(passwordField.getPassword());

                Connection conn = DBConnection.getConnection();
                String sql = "SELECT membername, memberid, membertype FROM members WHERE memberid = ? AND memberpwd = ?";
                try {
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, memberid);
                    stmt.setString(2, memberpwd);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        String memberName = rs.getString("membername");
                        String memberId = rs.getString("memberid");
                        String memberType = rs.getString("membertype");

                        // 로그인 성공 알림창 표시
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

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(loginButton, gbc);

        add(centerPanel, BorderLayout.CENTER);

    }
}