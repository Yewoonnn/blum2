// ProductManagementPanel.java
package Blum;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductManagementPanel extends JPanel {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private ProductDao productDao;
    private MainFrame mainFrame;

    public ProductManagementPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
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
        JButton backButton = new JButton("←");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 버튼 액션 리스너 추가
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddProductPanel();
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEditProductPanel();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedProduct();
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showMainPanel();
            }
        });
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.add(backButton);
        add(backButtonPanel, BorderLayout.NORTH);

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

    private void showAddProductPanel() {
        AddProductPanel addProductPanel = new AddProductPanel(mainFrame, this);
        mainFrame.showPanel(addProductPanel, "addProductPanel");
    }
    private void showEditProductPanel() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            int productId = (int) productTable.getValueAt(selectedRow, 0);
            Product product = productDao.getProductById(productId);

            // EditProductPanel의 인덱스를 확인하고 해당 인덱스를 사용하여 가져옵니다.
            int editProductPanelIndex = 7; // EditProductPanel의 인덱스를 직접 지정
            EditProductPanel editProductPanel = (EditProductPanel) mainFrame.cardPanel.getComponent(editProductPanelIndex);

            editProductPanel.setProduct(product);
            mainFrame.showPanel(editProductPanel, "editProductPanel");
        } else {
            JOptionPane.showMessageDialog(ProductManagementPanel.this, "수정할 제품을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void deleteSelectedProduct() {
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


    public void refreshProductTable() {
        loadProducts();
    }
}