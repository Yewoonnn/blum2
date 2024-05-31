package Blum;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;

    private CardLayout cardLayout;
    JPanel cardPanel;
    private MainPanel mainPanel;
    private OrderPanel orderPanel;

    private boolean isLoggedIn;
    private String memberId;
    private ProductManagementPanel productManagementPanel;

    public MainFrame() {
        setTitle("쇼핑몰 애플리케이션");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        isLoggedIn = false;
        memberId = "";
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

        // 제품 추가 패널 추가
        AddProductPanel addProductPanel = new AddProductPanel(this, productPanel);
        cardPanel.add(addProductPanel, "addProductPanel");

        // 제품 수정 패널 추가
        EditProductPanel editProductPanel = new EditProductPanel(this, productPanel);
        cardPanel.add(editProductPanel, "editProductPanel");

        // 주문 패널 추가
        orderPanel = new OrderPanel(this);
        cardPanel.add(orderPanel, "orderPanel");

        // 고객 관리 패널 추가
        CustomerManagementPanel customerManagementPanel = new CustomerManagementPanel(this);
        cardPanel.add(customerManagementPanel, "customerManagementPanel");

        // 주문 관리 패널 추가
        OrderManagementPanel orderManagementPanel = new OrderManagementPanel(this);
        cardPanel.add(orderManagementPanel, "orderManagementPanel");

        // 주문 상세 패널 추가
        OrderDetailPanel orderDetailPanel = new OrderDetailPanel(this, orderManagementPanel);
        cardPanel.add(orderDetailPanel, "orderDetailPanel");

        // 카테고리 패널 추가
        CategoryPanel categoryPanel = new CategoryPanel(this, productManagementPanel);
        cardPanel.add(categoryPanel, "categoryPanel");

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
        memberId = memberName;
    }
    public void showCustomerManagementPanel() {
        cardLayout.show(cardPanel, "customerManagementPanel");
    }
    public String getMemberId() {
        return memberId;
    }
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void showProductManagementPanel() {
        cardLayout.show(cardPanel, "productPanel");
    }
    public void showOrderManagementPanel() {
        cardLayout.show(cardPanel, "orderManagementPanel");
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
    public void showPanel(JPanel panel, String panelName) {
        cardLayout.show(cardPanel, panelName);
    }

    public void showOrderPanel(int totalPrice, List<CartItem> cartItems) {
        orderPanel.setTotalPrice(totalPrice);
        orderPanel.setProductInfo(cartItems); // 상품 정보 설정
        cardLayout.show(cardPanel, "orderPanel");
    }
    public void showCategoryPanel() {
        cardLayout.show(cardPanel, "categoryPanel");
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
    public JPanel getCartPanel() {
        return (JPanel) cardPanel.getComponent(5);
    }


}