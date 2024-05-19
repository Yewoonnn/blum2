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

        initComponents();
    }

    private void initComponents() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // 회원가입 패널 추가
        SignUpPanel signUpPanel = new SignUpPanel(this);
        cardPanel.add(signUpPanel, "signUpPanel");

        // 로그인 패널 추가
        LoginPanel loginPanel = new LoginPanel(this);
        cardPanel.add(loginPanel, "loginPanel");

        // 메인 패널 추가
        MainPanel mainPanel = new MainPanel(this);
        cardPanel.add(mainPanel, "mainPanel");

        // 제품 관리 패널 추가
        ProductManagementPanel productPanel = new ProductManagementPanel(this);
        cardPanel.add(productPanel, "productPanel");

        add(cardPanel, BorderLayout.CENTER);

        // 초기 패널 설정
        showMainPanel();
    }

    public void showSignUpPanel() {
        cardLayout.show(cardPanel, "signUpPanel");
    }

    public void showLoginPanel() {
        cardLayout.show(cardPanel, "loginPanel");
    }

    public void showMainPanel() {
        cardLayout.show(cardPanel, "mainPanel");
    }

    public void showMainPanel(String memberName, boolean isAdmin) {
        MainPanel mainPanel = (MainPanel) cardPanel.getComponent(2);
        mainPanel.setUserName(memberName, isAdmin);
        mainPanel.removeLoginButtons();
        cardLayout.show(cardPanel, "mainPanel");
    }

    public void showProductManagementPanel() {
        cardLayout.show(cardPanel, "productPanel");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}