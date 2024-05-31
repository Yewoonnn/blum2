package Blum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class AddProductPanel extends JPanel {
    private MainFrame mainFrame;
    private ProductManagementPanel productManagementPanel;
    private JTextField categoryIdField;
    private JTextField empIdField;
    private JTextField productNameField;
    private JTextField priceField;
    private JTextArea contentArea;
    private JTextField image1Field;
    private JTextField image2Field;

    public AddProductPanel(MainFrame mainFrame, ProductManagementPanel productManagementPanel) {
        this.mainFrame = mainFrame;
        this.productManagementPanel = productManagementPanel;
        setLayout(new BorderLayout());

        // 제품 추가 폼
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        categoryIdField = new JTextField(10);
        productNameField = new JTextField(20);
        priceField = new JTextField(10);
        contentArea = new JTextArea(5, 20);
        image1Field = new JTextField(20);
        image2Field = new JTextField(20);

        formPanel.add(new JLabel("카테고리 ID:"));

        formPanel.add(categoryIdField);
        formPanel.add(new JLabel("직원 ID:"));
        formPanel.add(new JLabel("admin")); // 텍스트 필드 대신 레이블 추가
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
        JButton addButton = new JButton("추가");
        JButton cancelButton = new JButton("취소");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showPanel(productManagementPanel, "productPanel");
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addProduct() {
        try {
            int categoryId = Integer.parseInt(categoryIdField.getText());
            String empId = "admin"; // 직원 ID를 "admin"으로 고정
            String productName = productNameField.getText();
            int price = Integer.parseInt(priceField.getText());
            String content = contentArea.getText();
            String image1 = image1Field.getText();
            String image2 = image2Field.getText();
            LocalDateTime productDate = LocalDateTime.now();

            Product product = new Product(categoryId, empId, productName, price, content, image1, image2, productDate);
            ProductDao productDao = new ProductDao();
            productDao.addProduct(product);
            productManagementPanel.refreshProductTable();
            mainFrame.showPanel(productManagementPanel, "productPanel");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(AddProductPanel.this, "잘못된 입력 값입니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}