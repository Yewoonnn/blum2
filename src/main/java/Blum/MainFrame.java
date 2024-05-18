package Blum;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    public MainFrame() {
        setTitle("쇼핑몰 애플리케이션");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);


        // 회원가입 패널 추가 (나중에 구현)
        LoginPanel loginPanel = new LoginPanel(this);
        cardPanel.add(loginPanel, "loginPanel");

        SignUpPanel signUpPanel = new SignUpPanel(this);
        cardPanel.add(signUpPanel, "signUpPanel");
        // 메인 패널 추가
        MainPanel mainPanel = new MainPanel(this);
        cardPanel.add(mainPanel, "mainPanel");

        add(cardPanel, BorderLayout.CENTER);


    }


    public void showMainPanel() {
        cardLayout.show(cardPanel, "mainPanel");
    }

    // MainFrame.java
    public void showLoginPanel() {
        cardLayout.show(cardPanel, "loginPanel");
    }
    public void showSignUpPanel() {
        cardLayout.show(cardPanel, "signUpPanel");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.showMainPanel(); // 메인 패널 표시
            mainFrame.setVisible(true);
        });
    }
}
