package Blum;

// CategoryPanel.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CategoryPanel extends JPanel {
    private MainFrame mainFrame;
    private ProductManagementPanel productManagementPanel;
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private JTextField categoryNameField;
    private CategoryDao categoryDao;

    public CategoryPanel(MainFrame mainFrame, ProductManagementPanel productManagementPanel) {
        this.mainFrame = mainFrame;
        this.productManagementPanel = productManagementPanel;
        this.categoryDao = new CategoryDao();
        setLayout(new BorderLayout());

        // 카테고리 테이블 설정
        String[] columnNames = {"카테고리 ID", "카테고리 이름"};
        tableModel = new DefaultTableModel(columnNames, 0);
        categoryTable = new JTable(tableModel);
        categoryTable.setRowHeight(30); // 행 높이 조정
        categoryTable.getColumnModel().getColumn(0).setPreferredWidth(100); // 첫 번째 열 너비 조정
        categoryTable.getColumnModel().getColumn(1).setPreferredWidth(200); // 두 번째 열 너비 조정
        JScrollPane scrollPane = new JScrollPane(categoryTable);
        scrollPane.setPreferredSize(new Dimension(400, 300)); // 스크롤 패널 크기 조정
        add(scrollPane, BorderLayout.CENTER);

        // 카테고리 추가 패널
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryNameField = new JTextField(20);
        JButton addButton = new JButton("추가");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCategory();
            }
        });
        JButton updateButton = new JButton("수정");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCategory();
            }
        });
        JButton deleteButton = new JButton("삭제");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCategory();
            }
        });
        addPanel.add(new JLabel("카테고리 이름:"));
        addPanel.add(categoryNameField);
        addPanel.add(addButton);
        addPanel.add(updateButton);
        addPanel.add(deleteButton);

        // 뒤로 가기 버튼
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("←");
        backButton.setPreferredSize(new Dimension(50, 30)); // 버튼 크기 조정
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showPanel(productManagementPanel, "productPanel");
            }
        });
        backButtonPanel.add(backButton);

        // 상단 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backButtonPanel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        // 하단 패널
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(addPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // 데이터베이스에서 카테고리 목록 로드
        loadCategories();
    }

    private void loadCategories() {
        List<Category> categories = categoryDao.getAllCategories();
        tableModel.setRowCount(0);
        for (Category category : categories) {
            Object[] rowData = {category.getCategoryId(), category.getCategoryName()};
            tableModel.addRow(rowData);
        }
    }

    private void addCategory() {
        String categoryName = categoryNameField.getText().trim();
        if (!categoryName.isEmpty()) {
            categoryDao.addCategory(categoryName);
            categoryNameField.setText("");
            loadCategories();
        }
    }

    private int getNextCategoryId() {
        int maxId = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int id = (int) tableModel.getValueAt(i, 0);
            if (id > maxId) {
                maxId = id;
            }
        }
        return maxId + 1;
    }

    private void updateCategory() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow != -1) {
            int categoryId = (int) tableModel.getValueAt(selectedRow, 0);
            String categoryName = (String) tableModel.getValueAt(selectedRow, 1);
            String newCategoryName = JOptionPane.showInputDialog(this, "새로운 카테고리 이름을 입력하세요:", categoryName);
            if (newCategoryName != null && !newCategoryName.trim().isEmpty()) {
                categoryDao.updateCategory(categoryId, newCategoryName.trim());
                loadCategories();
            }
        }
    }

    private void deleteCategory() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow != -1) {
            int categoryId = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "선택한 카테고리를 삭제하시겠습니까?", "카테고리 삭제", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                categoryDao.deleteCategory(categoryId);
                loadCategories();
            }
        }
    }
}