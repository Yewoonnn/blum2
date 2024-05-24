package Blum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private MainFrame mainFrame;
    private boolean isAdmin;
    private ProductDao productDao;
    private JPanel centerPanel;

    public MainPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        productDao = new ProductDao();
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
                }
            }
        });

        // 제품 관리 버튼
        productManagementButton = new JButton("제품 관리");
        productManagementButton.addActionListener(e -> mainFrame.showProductManagementPanel());

        cartButton = new JButton("장바구니");
        cartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showCartPanel(userLabel.getText());
            }
        });
    }

    public void setUserName(String name, boolean isAdmin) {
        userLabel.setText(name + "님");
        rightPanel.add(cartButton);
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

    public void updateProductButtons() {
        centerPanel.removeAll();
        List<Product> products = productDao.getAllProducts();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(BUTTON_GAP, BUTTON_GAP, BUTTON_GAP, BUTTON_GAP);

        for (Product product : products) {
            JButton productButton = new JButton(product.getProductName());
            productButton.addActionListener(new ProductButtonListener(product.getProductId()));
            productButton.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
            centerPanel.add(productButton, gbc);

            gbc.gridx++;
            if (gbc.gridx == 3) {
                gbc.gridx = 0;
                gbc.gridy++;
            }
        }

        centerPanel.revalidate();
        centerPanel.repaint();
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