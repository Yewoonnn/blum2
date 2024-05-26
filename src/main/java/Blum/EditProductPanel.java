package Blum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class EditProductPanel extends JPanel {
    private MainFrame mainFrame;
    private ProductManagementPanel productManagementPanel;
    private JTextField categoryIdField;
    private JTextField empIdField;
    private JTextField productNameField;
    private JTextField priceField;
    private JTextArea contentArea;
    private JTextField image1Field;
    private JTextField image2Field;
    private Product product;

    public EditProductPanel(MainFrame mainFrame, ProductManagementPanel productManagementPanel) {
        this.mainFrame = mainFrame;
        this.productManagementPanel = productManagementPanel;
        setLayout(new BorderLayout());

        // 제품 수정 폼
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        categoryIdField = new JTextField(10);
        empIdField = new JTextField(10);
        productNameField = new JTextField(20);
        priceField = new JTextField(10);
        contentArea = new JTextArea(5, 20);
        image1Field = new JTextField(20);
        image2Field = new JTextField(20);

        formPanel.add(new JLabel("카테고리 ID:"));
        formPanel.add(categoryIdField);
        formPanel.add(new JLabel("직원 ID:"));
        formPanel.add(empIdField);
        formPanel.add(new JLabel("제품명:"));
        formPanel.add(productNameField);
        formPanel.add(new JLabel("가격:"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("내용:"));
        formPanel.add(new JScrollPane(contentArea));
        formPanel.add(new JLabel("이미지1:"));
        formPanel.add(image1Field);
        formPanel.add(new JLabel("이미지2:"));
        formPanel.add(image2Field);

        add(formPanel, BorderLayout.CENTER);

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        JButton updateButton = new JButton("수정");
        JButton cancelButton = new JButton("취소");

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProduct();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showPanel(productManagementPanel, "productPanel");
            }
        });

        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateProduct() {
        try {
            int categoryId = Integer.parseInt(categoryIdField.getText());
            String empId = empIdField.getText();
            String productName = productNameField.getText();
            int price = Integer.parseInt(priceField.getText());
            String content = contentArea.getText();
            String image1 = image1Field.getText();
            String image2 = image2Field.getText();

            product.setCategoryId(categoryId);
            product.setEmpId(empId);
            product.setProductName(productName);
            product.setPrice(price);
            product.setContent(content);
            product.setImage1(image1);
            product.setImage2(image2);
            product.setProductDate(LocalDateTime.now());

            ProductDao productDao = new ProductDao();
            productDao.updateProduct(product);
            productManagementPanel.refreshProductTable();
            mainFrame.showPanel(productManagementPanel, "productPanel");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(EditProductPanel.this, "잘못된 입력 값입니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            categoryIdField.setText(String.valueOf(product.getCategoryId()));
            empIdField.setText(product.getEmpId());
            productNameField.setText(product.getProductName());
            priceField.setText(String.valueOf(product.getPrice()));
            contentArea.setText(product.getContent());
            image1Field.setText(product.getImage1());
            image2Field.setText(product.getImage2());
        }
    }
}