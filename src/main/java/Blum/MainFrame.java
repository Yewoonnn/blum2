package Blum;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private MainPanel mainPanel;

    private boolean isLoggedIn;

    public MainFrame() {
        setTitle("쇼핑몰 애플리케이션");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        isLoggedIn = false;
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
        mainPanel = new MainPanel(this);
        cardPanel.add(mainPanel, "mainPanel");

        // 제품 관리 패널 추가
        ProductManagementPanel productPanel = new ProductManagementPanel(this);
        cardPanel.add(productPanel, "productPanel");

        // 상품 정보 표시 패널 추가
        ProductInfoPanel productInfoPanel = new ProductInfoPanel(this);
        cardPanel.add(productInfoPanel, "productInfoPanel");

        // 장바구니 패널 추가
        CartPanel cartPanel = new CartPanel(this, "");
        cardPanel.add(cartPanel, "cartPanel");

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
        mainPanel.updateProductButtons(); // 상품 버튼 업데이트
        cardLayout.show(cardPanel, "mainPanel");
    }

    public void showMainPanel(String memberName, boolean isAdmin) {
        MainPanel mainPanel = (MainPanel) cardPanel.getComponent(2);
        mainPanel.setUserName(memberName, isAdmin);
        mainPanel.removeLoginButtons();
        showMainPanel(); // 메인 패널 표시
        isLoggedIn = true;
    }
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void showProductManagementPanel() {
        cardLayout.show(cardPanel, "productPanel");
    }

    public void showProductInfoPanel(int productId) {
        ProductInfoPanel productInfoPanel = (ProductInfoPanel) cardPanel.getComponent(4);
        productInfoPanel.setProductInfo(productId);
        cardLayout.show(cardPanel, "productInfoPanel");
    }

    public void showCartPanel(String memberId) {
        CartPanel cartPanel = (CartPanel) cardPanel.getComponent(5);
        cartPanel.setMemberId(memberId);
        cartPanel.loadCartItems();
        cardLayout.show(cardPanel, "cartPanel");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }


}