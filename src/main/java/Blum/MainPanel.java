package Blum;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;
    private static final int BUTTON_SIZE = 150;
    private static final int BUTTON_GAP = 20;

    private JButton loginButton;
    private JButton signUpButton;
    private JLabel userLabel;
    private JPanel rightPanel;

    private MainFrame mainFrame;

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
    }

    public void setUserName(String name) {
        userLabel.setText(name + "님");
    }

    public void removeLoginButtons() {
        rightPanel.remove(loginButton);
        rightPanel.remove(signUpButton);
        rightPanel.revalidate();
        rightPanel.repaint();
    }
}