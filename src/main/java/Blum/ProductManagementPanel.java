package Blum;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;

public class ProductManagementPanel extends JPanel {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private ProductDao productDao;

    public ProductManagementPanel(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        // 제품 테이블 초기화
        String[] columnNames = {"제품 ID", "카테고리 ID", "직원 ID", "제품명", "가격", "내용", "이미지1", "이미지2", "등록일"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        // 제품 관리 버튼
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("제품 추가");
        JButton editButton = new JButton("제품 수정");
        JButton deleteButton = new JButton("제품 삭제");
        JButton cancelButton = new JButton("취소");
        buttonPanel.add(cancelButton);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 버튼 액션 리스너 추가
        addButton.addActionListener(new AddProductButtonListener());
        editButton.addActionListener(new EditProductButtonListener());
        deleteButton.addActionListener(new DeleteProductButtonListener());
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showMainPanel();
            }
        });

        // 데이터베이스에서 제품 정보 가져오기
        productDao = new ProductDao();
        loadProducts();
    }

    private void loadProducts() {
        List<Product> products = productDao.getAllProducts();
        tableModel.setRowCount(0); // 기존 행 제거

        for (Product product : products) {
            Object[] rowData = {product.getProductId(), product.getCategoryId(), product.getEmpId(), product.getProductName(),
                    product.getPrice(), product.getContent(), product.getImage1(), product.getImage2(), product.getProductDate()};
            tableModel.addRow(rowData);
        }
    }

    private class AddProductButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 제품 추가 다이얼로그 표시
            JTextField categoryIdField = new JTextField(10);
            JTextField empIdField = new JTextField(10);
            JTextField productNameField = new JTextField(20);
            JTextField priceField = new JTextField(10);
            JTextArea contentArea = new JTextArea(5, 20);
            JTextField image1Field = new JTextField(20);
            JTextField image2Field = new JTextField(20);

            JPanel inputPanel = new JPanel(new GridLayout(7, 2, 5, 5));
            inputPanel.add(new JLabel("카테고리 ID:"));
            inputPanel.add(categoryIdField);
            inputPanel.add(new JLabel("직원 ID:"));
            inputPanel.add(empIdField);
            inputPanel.add(new JLabel("제품명:"));
            inputPanel.add(productNameField);
            inputPanel.add(new JLabel("가격:"));
            inputPanel.add(priceField);
            inputPanel.add(new JLabel("내용:"));
            inputPanel.add(new JScrollPane(contentArea));
            inputPanel.add(new JLabel("이미지1:"));
            inputPanel.add(image1Field);
            inputPanel.add(new JLabel("이미지2:"));
            inputPanel.add(image2Field);

            int result = JOptionPane.showConfirmDialog(ProductManagementPanel.this, inputPanel, "제품 추가", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int categoryId = Integer.parseInt(categoryIdField.getText());
                    String empId = empIdField.getText();
                    String productName = productNameField.getText();
                    int price = Integer.parseInt(priceField.getText());
                    String content = contentArea.getText();
                    String image1 = image1Field.getText();
                    String image2 = image2Field.getText();
                    LocalDateTime productDate = LocalDateTime.now();

                    Product product = new Product(categoryId, empId, productName, price, content, image1, image2, productDate);
                    productDao.addProduct(product);
                    loadProducts();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ProductManagementPanel.this, "잘못된 입력 값입니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class EditProductButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 선택된 제품 정보 수정
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                int productId = (int) productTable.getValueAt(selectedRow, 0);
                int categoryId = (int) productTable.getValueAt(selectedRow, 1);
                String empId = (String) productTable.getValueAt(selectedRow, 2);
                String productName = (String) productTable.getValueAt(selectedRow, 3);
                int price = (int) productTable.getValueAt(selectedRow, 4);
                String content = (String) productTable.getValueAt(selectedRow, 5);
                String image1 = (String) productTable.getValueAt(selectedRow, 6);
                String image2 = (String) productTable.getValueAt(selectedRow, 7);
                LocalDateTime productDate = (LocalDateTime) productTable.getValueAt(selectedRow, 8);

                JTextField categoryIdField = new JTextField(10);
                categoryIdField.setText(String.valueOf(categoryId));
                JTextField empIdField = new JTextField(10);
                empIdField.setText(empId);
                JTextField productNameField = new JTextField(20);
                productNameField.setText(productName);
                JTextField priceField = new JTextField(10);
                priceField.setText(String.valueOf(price));
                JTextArea contentArea = new JTextArea(5, 20);
                contentArea.setText(content);
                JTextField image1Field = new JTextField(20);
                image1Field.setText(image1);
                JTextField image2Field = new JTextField(20);
                image2Field.setText(image2);

                JPanel inputPanel = new JPanel(new GridLayout(7, 2, 5, 5));
                inputPanel.add(new JLabel("카테고리 ID:"));
                inputPanel.add(categoryIdField);
                inputPanel.add(new JLabel("직원 ID:"));
                inputPanel.add(empIdField);
                inputPanel.add(new JLabel("제품명:"));
                inputPanel.add(productNameField);
                inputPanel.add(new JLabel("가격:"));
                inputPanel.add(priceField);
                inputPanel.add(new JLabel("내용:"));
                inputPanel.add(new JScrollPane(contentArea));
                inputPanel.add(new JLabel("이미지1:"));
                inputPanel.add(image1Field);
                inputPanel.add(new JLabel("이미지2:"));
                inputPanel.add(image2Field);

                int result = JOptionPane.showConfirmDialog(ProductManagementPanel.this, inputPanel, "제품 수정", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        categoryId = Integer.parseInt(categoryIdField.getText());
                        empId = empIdField.getText();
                        productName = productNameField.getText();
                        price = Integer.parseInt(priceField.getText());
                        content = contentArea.getText();
                        image1 = image1Field.getText();
                        image2 = image2Field.getText();

                        Product product = new Product(categoryId, empId, productName, price, content, image1, image2, productDate);
                        product.setProductId(productId);
                        productDao.updateProduct(product);
                        loadProducts();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(ProductManagementPanel.this, "잘못된 입력 값입니다.", "오류", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(ProductManagementPanel.this, "수정할 제품을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class DeleteProductButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 선택된 제품 정보 삭제
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                int productId = (int) productTable.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(ProductManagementPanel.this, "선택한 제품을 삭제하시겠습니까?", "제품 삭제", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    productDao.deleteProduct(productId);
                    loadProducts();
                }
            } else {
                JOptionPane.showMessageDialog(ProductManagementPanel.this, "삭제할 제품을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}