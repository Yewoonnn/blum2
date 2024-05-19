package Blum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JPanel {
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;
    private static final int BUTTON_SIZE = 150;
    private static final int BUTTON_GAP = 20;

    private JButton loginButton;
    private JButton signUpButton;
    private JLabel userLabel;
    private JPanel rightPanel;
    private JButton logoutButton;
    private JButton productManagementButton;

    private MainFrame mainFrame;
    private boolean isAdmin;

    public MainPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        // 상단 패널 (검색창, 로그인, 회원가입 버튼)
        JPanel topPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField(20);
        topPanel.add(searchField, BorderLayout.WEST);

        rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        loginButton = new JButton("로그인");
        signUpButton = new JButton("회원가입");
        rightPanel.add(loginButton);
        rightPanel.add(signUpButton);

        userLabel = new JLabel();
        rightPanel.add(userLabel);
        topPanel.add(rightPanel, BorderLayout.EAST);

        // 중앙 패널 (상품 버튼)
        JPanel centerPanel = new JPanel(new GridLayout(3, 3, BUTTON_GAP, BUTTON_GAP));
        for (int i = 1; i <= 9; i++) {
            JButton productButton = new JButton("상품 " + i);
            productButton.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
            centerPanel.add(productButton);
        }

        // 전체 레이아웃
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // 로그인 버튼 클릭 시 로그인 패널 표시
        loginButton.addActionListener(e -> mainFrame.showLoginPanel());

        // 회원가입 버튼 클릭 시 회원가입 패널 표시
        signUpButton.addActionListener(e -> mainFrame.showSignUpPanel());

        // 로그아웃 버튼
        logoutButton = new JButton("로그아웃");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(MainPanel.this, "로그아웃하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    showLoginButtons();
                    userLabel.setText("");
                    isAdmin = false;
                }
            }
        });

        // 제품 관리 버튼
        productManagementButton = new JButton("제품 관리");
        productManagementButton.addActionListener(e -> mainFrame.showProductManagementPanel());
    }

    public void setUserName(String name, boolean isAdmin) {
        userLabel.setText(name + "님");
        rightPanel.add(logoutButton);
        this.isAdmin = isAdmin;
        if (isAdmin) {
            rightPanel.add(productManagementButton);
        }
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    public void removeLoginButtons() {
        rightPanel.remove(loginButton);
        rightPanel.remove(signUpButton);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    public void showLoginButtons() {
        rightPanel.remove(logoutButton);
        rightPanel.remove(productManagementButton);
        rightPanel.add(loginButton);
        rightPanel.add(signUpButton);
        rightPanel.revalidate();
        rightPanel.repaint();
    }
}