package Blum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyInfoEditPanel extends JPanel {
    private MainFrame mainFrame;
    private String memberId;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;

    public MyInfoEditPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        // 상단 패널
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("←");
        backButton.setPreferredSize(new Dimension(50, 30));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showMyInfoPanel(memberId);
            }
        });
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        // 중앙 패널
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // 이름 필드
        JLabel nameLabel = new JLabel("이름:");
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        centerPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        nameField = new JTextField(20);
        nameField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        centerPanel.add(nameField, gbc);

        // 이메일 필드
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel emailLabel = new JLabel("이메일:");
        emailLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        centerPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        centerPanel.add(emailField, gbc);

        // 전화번호 필드
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel phoneLabel = new JLabel("전화번호:");
        phoneLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        centerPanel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        phoneField = new JTextField(20);
        phoneField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        centerPanel.add(phoneField, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // 하단 패널
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("저장");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMemberInfo();
            }
        });
        bottomPanel.add(saveButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
        loadMemberInfo();
    }

    private void loadMemberInfo() {
        // 데이터베이스에서 회원 정보를 불러와서 필드에 설정하는 코드 작성
        // ...
    }

    private void updateMemberInfo() {
        // 수정된 회원 정보를 데이터베이스에 업데이트하는 코드 작성
        // ...
    }
}