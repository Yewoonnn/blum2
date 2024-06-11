package Blum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;

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
    private JButton cartButton;
    private JButton customerManagementButton;
    private JButton orderManagementButton;
    private JButton myInfoButton;
    private MainFrame mainFrame;
    private boolean isAdmin;
    private ProductDao productDao;
    private JPanel centerPanel;
    private JTextField searchField;

    public MainPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        productDao = new ProductDao();
        setLayout(new BorderLayout());

        // 상단 패널 (검색창, 로그인, 회원가입 버튼)
        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("검색");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchField.getText();
                performSearch(keyword);
            }
        });
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        topPanel.add(searchPanel, BorderLayout.WEST);

        rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        loginButton = new JButton("로그인");
        signUpButton = new JButton("회원가입");
        rightPanel.add(loginButton);
        rightPanel.add(signUpButton);

        userLabel = new JLabel();
        rightPanel.add(userLabel);
        topPanel.add(rightPanel, BorderLayout.EAST);

        // 중앙 패널 (상품 버튼)
        centerPanel = new JPanel(new GridBagLayout());
        updateProductButtons();

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // 전체 레이아웃
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

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
                    rightPanel.remove(cartButton); // 장바구니 버튼 제거
                    rightPanel.remove(myInfoButton);
                    rightPanel.revalidate();
                    rightPanel.repaint();
                }
            }
        });

        // 제품 관리 버튼
        productManagementButton = new JButton("제품 관리");
        productManagementButton.addActionListener(e -> mainFrame.showProductManagementPanel());

        // 고객 관리 버튼
        customerManagementButton = new JButton("고객관리");
        customerManagementButton.addActionListener(e -> mainFrame.showCustomerManagementPanel());


        // 주문 관리 버튼
        orderManagementButton = new JButton("주문관리");
        orderManagementButton.addActionListener(e -> mainFrame.showOrderManagementPanel());

        cartButton = new JButton("장바구니");
        cartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String memberId = userLabel.getText().split("님")[0];
                mainFrame.showCartPanel(memberId);
                CartPanel cartPanel = (CartPanel) mainFrame.getCartPanel();
                cartPanel.loadCartItems();
            }
        });

        // 내 정보 버튼
        myInfoButton = new JButton("내 정보");
        myInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showMyInfoPanel(mainFrame.getMemberId());
            }
        });
    }

    public void setUserName(String name, boolean isAdmin) {
        userLabel.setText(name + "님");
        if (isAdmin) {
            rightPanel.removeAll();
            rightPanel.add(userLabel);
            rightPanel.add(logoutButton);
            rightPanel.add(productManagementButton);
            rightPanel.add(customerManagementButton);
            rightPanel.add(orderManagementButton);
        } else {
            rightPanel.removeAll();
            rightPanel.add(userLabel);
            rightPanel.add(cartButton);
            rightPanel.add(myInfoButton);
            rightPanel.add(logoutButton);
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
        rightPanel.remove(customerManagementButton);
        rightPanel.remove(orderManagementButton);
        rightPanel.add(loginButton);
        rightPanel.add(signUpButton);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    public void updateProductButtons() {
        centerPanel.removeAll();
        List<Product> products = productDao.getAllProducts();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(BUTTON_GAP, BUTTON_GAP, BUTTON_GAP, BUTTON_GAP);

        for (Product product : products) {
            String imagePath = product.getImage1();
            if (imagePath != null && !imagePath.isEmpty()) {
                URL imageUrl = getClass().getResource(imagePath);
                if (imageUrl != null) {
                    ImageIcon imageIcon = new ImageIcon(imageUrl);
                    Image image = imageIcon.getImage().getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH);
                    JButton productButton = new JButton(new ImageIcon(image));
                    productButton.addActionListener(new ProductButtonListener(product.getProductId()));
                    productButton.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
                    centerPanel.add(productButton, gbc);
                }
            }
            gbc.gridx++;
            if (gbc.gridx == 3) {
                gbc.gridx = 0;
                gbc.gridy++;
            }
        }

        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void performSearch(String keyword) {
        List<Product> searchResults = productDao.searchProducts(keyword);
        mainFrame.showSearchResultPanel(searchResults);
    }

    // 상품 버튼 클릭 이벤트 리스너
    private class ProductButtonListener implements ActionListener {
        private int productId;

        public ProductButtonListener(int productId) {
            this.productId = productId;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // 상품 정보 표시 패널로 전환
            mainFrame.showProductInfoPanel(productId);
        }
    }
}