package Blum;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ProductInfoPanel extends JPanel {
    private MainFrame mainFrame;
    private ProductDao productDao;
    private JLabel productImageLabel;
    private JLabel productNameLabel;
    private JLabel productPriceLabel;
    private JButton addToCartButton;
    private JButton buyNowButton;
    private JSpinner quantitySpinner;
    private int selectedQuantity;
    private int productId;
    private CartDao cartDao;
    private JLabel contentLabel;
    private Product product;

    public ProductInfoPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        productDao = new ProductDao();
        cartDao = new CartDao();
        setLayout(new BorderLayout());

        // 상품 정보 패널
        JPanel infoPanel = new JPanel(new BorderLayout());

        // 상품 이미지 패널
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        productImageLabel = new JLabel();
        imagePanel.add(productImageLabel);
        infoPanel.add(imagePanel, BorderLayout.CENTER);

        // 상품 정보 패널
        JPanel detailPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // 상품명 레이블
        productNameLabel = new JLabel();
        productNameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        detailPanel.add(productNameLabel, gbc);

        // 가격 레이블
        gbc.gridy = 1;
        productPriceLabel = new JLabel();
        productPriceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        detailPanel.add(productPriceLabel, gbc);

        // 수량 스피너
        gbc.gridy = 2;
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        quantitySpinner.addChangeListener(e -> selectedQuantity = (int) quantitySpinner.getValue());
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        quantityPanel.add(new JLabel("수량: "));
        quantityPanel.add(quantitySpinner);
        detailPanel.add(quantityPanel, gbc);

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addToCartButton = new JButton("장바구니에 담기");
        buyNowButton = new JButton("바로 구매하기");
        buttonPanel.add(addToCartButton);
        buttonPanel.add(buyNowButton);

        gbc.gridy = 3;
        detailPanel.add(buttonPanel, gbc);

        // 상품 내용 레이블
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        contentLabel = new JLabel();
        contentLabel.setVerticalAlignment(SwingConstants.TOP);
        contentLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        detailPanel.add(contentLabel, gbc);

        infoPanel.add(detailPanel, BorderLayout.SOUTH);

        // 스크롤 패널 생성
        JScrollPane scrollPane = new JScrollPane(infoPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        // 뒤로 가기 버튼
        JButton backButton = new JButton("←");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showMainPanel();
            }
        });
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.add(backButton);
        add(backButtonPanel, BorderLayout.NORTH);

        // 장바구니에 담기 버튼 액션 리스너 설정
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 로그인 여부 확인
                if (mainFrame.isLoggedIn()) {
                    // 장바구니에 상품 추가
                    String memberId = mainFrame.getMemberId();
                    cartDao.addCartItem(memberId, productId, selectedQuantity);

                    // 장바구니로 이동할지 확인하는 다이얼로그 표시
                    int option = JOptionPane.showConfirmDialog(ProductInfoPanel.this, "장바구니에 상품이 추가되었습니다.\n장바구니로 이동하시겠습니까?", "장바구니 이동", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        // "예" 버튼을 클릭한 경우 장바구니 패널로 이동
                        mainFrame.showCartPanel(memberId);
                    }
                } else {
                    JOptionPane.showMessageDialog(ProductInfoPanel.this, "로그인하세요.", "알림", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // 바로 구매하기 버튼 액션 리스너 설정
        buyNowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 로그인 여부 확인
                if (mainFrame.isLoggedIn()) {
                    // 선택한 상품 정보를 가져와서 주문 패널로 전달
                    int quantity = (int) quantitySpinner.getValue();
                    int totalPrice = product.getPrice() * quantity;
                    CartItem cartItem = new CartItem(0, product.getProductName(), product.getPrice(), "", quantity, product.getProductId());
                    List<CartItem> cartItems = new ArrayList<>();
                    cartItems.add(cartItem);
                    mainFrame.showOrderPanel(totalPrice, cartItems);
                } else {
                    JOptionPane.showMessageDialog(ProductInfoPanel.this, "로그인하세요.", "알림", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    public void setProductInfo(int productId) {
        this.productId = productId;
        product = productDao.getProductById(productId);
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

            // 상품 내용 설정
            String content = product.getContent().replace("\n", "<br>");
            contentLabel.setText("<html>" + content + "</html>");

            // 수량 초기화
            quantitySpinner.setValue(1);
            selectedQuantity = 1;
        }
    }
}