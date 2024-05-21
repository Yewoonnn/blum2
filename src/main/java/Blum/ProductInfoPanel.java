package Blum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductInfoPanel extends JPanel {
    private MainFrame mainFrame;
    private ProductDao productDao;
    private JLabel productNameLabel;
    private JLabel productPriceLabel;
    private JLabel productContentLabel;
    private JLabel productImageLabel;

    public ProductInfoPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        productDao = new ProductDao();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        productNameLabel = new JLabel();
        productPriceLabel = new JLabel();
        productContentLabel = new JLabel();
        productImageLabel = new JLabel();

        add(productNameLabel);
        add(productPriceLabel);
        add(productContentLabel);
        add(productImageLabel);

        JButton backButton = new JButton("뒤로 가기");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showMainPanel();
            }
        });
        add(backButton);
    }

    public void setProductInfo(int productId) {
        Product product = productDao.getProductById(productId);
        if (product != null) {
            productNameLabel.setText("상품명: " + product.getProductName());
            productPriceLabel.setText("가격: " + product.getPrice());
            productContentLabel.setText("내용: " + product.getContent());
            // 이미지 표시 로직 추가
            String imagePath = product.getImage1();
            if (imagePath != null && !imagePath.isEmpty()) {
                ImageIcon imageIcon = new ImageIcon(imagePath);
                Image image = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                productImageLabel.setIcon(new ImageIcon(image));
            } else {
                productImageLabel.setIcon(null);
            }
        }
    }
}