package Blum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductInfoPanel extends JPanel {
    private MainFrame mainFrame;
    private ProductDao productDao;
    private JLabel productImageLabel;
    private JLabel productNameLabel;
    private JLabel productPriceLabel;
    private JButton addToCartButton;
    private JButton buyNowButton;

    public ProductInfoPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        productDao = new ProductDao();
        setLayout(new BorderLayout());

        // 상품 이미지 레이블
        productImageLabel = new JLabel();
        productImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(productImageLabel, BorderLayout.CENTER);

        // 상품 정보 패널
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // 상품명 레이블
        productNameLabel = new JLabel();
        productNameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        infoPanel.add(productNameLabel, gbc);

        // 가격 레이블
        gbc.gridy = 1;
        productPriceLabel = new JLabel();
        productPriceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        infoPanel.add(productPriceLabel, gbc);

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addToCartButton = new JButton("장바구니에 담기");
        buyNowButton = new JButton("바로 구매하기");
        buttonPanel.add(addToCartButton);
        buttonPanel.add(buyNowButton);

        gbc.gridy = 2;
        infoPanel.add(buttonPanel, gbc);

        add(infoPanel, BorderLayout.SOUTH);

        // 뒤로 가기 버튼
        JButton backButton = new JButton("뒤로 가기");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showMainPanel();
            }
        });
        add(backButton, BorderLayout.NORTH);


    }

    public void setProductInfo(int productId) {
        Product product = productDao.getProductById(productId);
        if (product != null) {
            // 상품 이미지 설정
            String imagePath = product.getImage1();
            if (imagePath != null && !imagePath.isEmpty()) {
                ImageIcon imageIcon = new ImageIcon(getClass().getResource(imagePath));
                Image image = imageIcon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
                productImageLabel.setIcon(new ImageIcon(image));
            } else {
                productImageLabel.setIcon(null);
            }

            // 상품명 설정
            productNameLabel.setText(product.getProductName());

            // 가격 설정
            productPriceLabel.setText(product.getPrice() + "원");

            // 장바구니에 담기 버튼 액션 리스너 설정
            addToCartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 로그인 여부 확인
                    if (mainFrame.isLoggedIn()) {
                        // 장바구니에 상품 추가 로직 구현
                        JOptionPane.showMessageDialog(ProductInfoPanel.this, "장바구니에 상품이 추가되었습니다.");
                    } else {
                        JOptionPane.showMessageDialog(ProductInfoPanel.this, "로그인하세요.", "알림", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });

            // 바로 구매하기 버튼 액션 리스너 설정
            buyNowButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 바로 구매하기 로직 구현
                    JOptionPane.showMessageDialog(ProductInfoPanel.this, "구매 페이지로 이동합니다.");
                }
            });
        }
    }
}