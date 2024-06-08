package Blum;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CartPanel extends JPanel {
    private MainFrame mainFrame;
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JLabel totalPriceLabel;
    private CartDao cartDao;
    private String memberId;
    private JCheckBox selectAllCheckBox;

    public CartPanel(MainFrame mainFrame, String memberId) {
        this.mainFrame = mainFrame;
        this.memberId = memberId;
        cartDao = new CartDao();
        setLayout(new BorderLayout());

        // 장바구니 테이블
        String[] columnNames = {"선택", "상품 이미지", "상품명", "가격", "수량", "합계"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };
        cartTable = new JTable(tableModel);
        cartTable.setRowHeight(100);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        cartTable.getColumnModel().getColumn(4).setCellEditor(new SpinnerEditor(1, 100, 1)); // 수량 열에 SpinnerEditor 설정
        cartTable.getColumnModel().getColumn(1).setCellRenderer(new ImageRenderer()); // 이미지 렌더러 설정
        JScrollPane scrollPane = new JScrollPane(cartTable);
        add(scrollPane, BorderLayout.CENTER);

        // 상단 패널
        JPanel topPanel = new JPanel(new BorderLayout());

        // 뒤로 가기 버튼
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("←");
        backButton.setPreferredSize(new Dimension(50, 30)); // 버튼 크기 설정
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showMainPanel();
            }
        });
        backButtonPanel.add(backButton);
        topPanel.add(backButtonPanel, BorderLayout.WEST);

        // 상단 버튼 패널
        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        selectAllCheckBox = new JCheckBox("전체선택");
        JButton deleteSelectedButton = new JButton("선택삭제");
        topButtonPanel.add(selectAllCheckBox);
        topButtonPanel.add(deleteSelectedButton);
        topPanel.add(topButtonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // 하단 패널
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // 총 금액 레이블
        totalPriceLabel = new JLabel("총 금액: 0원");
        totalPriceLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        bottomPanel.add(totalPriceLabel, BorderLayout.WEST);

        // 주문하기 버튼
        JButton orderButton = new JButton("주문하기");
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int totalPrice = calculateTotalPrice();
                List<CartItem> cartItems = getCartItems();
                mainFrame.showOrderPanel(totalPrice, cartItems);
            }
        });
        bottomPanel.add(orderButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // 버튼 액션 리스너 등록
        selectAllCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectAllItems(selectAllCheckBox.isSelected());
            }
        });

        deleteSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedItems();
            }
        });
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void loadCartItems() {
        List<CartItem> cartItems = cartDao.getCartItemsByMember(memberId);
        tableModel.setRowCount(0);
        for (CartItem cartItem : cartItems) {
            ImageIcon imageIcon = new ImageIcon(getClass().getResource(cartItem.getImage()));
            Image image = imageIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            Object[] rowData = {false, new ImageIcon(image), cartItem.getProductName(), cartItem.getPrice(), cartItem.getQuantity(), cartItem.getPrice() * cartItem.getQuantity(), cartItem.getCartId()};
            tableModel.addRow(rowData);
        }
        // 장바구니 테이블의 행 높이 설정
        cartTable.setRowHeight(100);

        updateTotalPrice();
    }

    private void selectAllItems(boolean isSelected) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(isSelected, i, 0);
        }
    }

    private void deleteSelectedItems() {
        List<String> productNames = new ArrayList<>();
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            boolean isSelected = (boolean) tableModel.getValueAt(row, 0);
            if (isSelected) {
                String productName = (String) tableModel.getValueAt(row, 2);
                productNames.add(productName);
            }
        }
        if (!productNames.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(CartPanel.this, "선택한 상품을 삭제하시겠습니까?", "상품 삭제", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                cartDao.removeCartItemsByProductNames(productNames, memberId);
                loadCartItems();
                updateTotalPrice();
            }
        } else {
            JOptionPane.showMessageDialog(CartPanel.this, "삭제할 상품을 선택하세요.", "알림", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateTotalPrice() {
        int totalPrice = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int subtotal = (int) tableModel.getValueAt(i, 5);
            totalPrice += subtotal;
        }
        totalPriceLabel.setText("총 금액: " + totalPrice + "원");
    }

    private int calculateTotalPrice() {
        int totalPrice = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int subtotal = (int) tableModel.getValueAt(i, 5);
            totalPrice += subtotal;
        }
        return totalPrice;
    }

    private List<CartItem> getCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String productName = (String) tableModel.getValueAt(i, 2);
            int price = (int) tableModel.getValueAt(i, 3);
            int quantity = (int) tableModel.getValueAt(i, 4);
            int totalPrice = (int) tableModel.getValueAt(i, 5);
            int cartId = 0;
            if (tableModel.getColumnCount() > 6) {
                cartId = (int) tableModel.getValueAt(i, 6);
            }
            int productId = cartDao.getProductIdByCartId(cartId);
            CartItem cartItem = new CartItem(cartId, productName, price, "", quantity, productId);
            cartItems.add(cartItem);
        }
        return cartItems;
    }

    private class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof ImageIcon) {
                ImageIcon imageIcon = (ImageIcon) value;
                Image image = imageIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                return new JLabel(new ImageIcon(image));
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    private class SpinnerEditor extends DefaultCellEditor {
        private JSpinner spinner;

        public SpinnerEditor(int min, int max, int step) {
            super(new JTextField());
            spinner = new JSpinner(new SpinnerNumberModel(1, min, max, step));
            spinner.addChangeListener(e -> {
                int row = cartTable.getSelectedRow();
                if (row != -1) {
                    int quantity = (int) spinner.getValue();
                    int price = (int) tableModel.getValueAt(row, 3);
                    int subtotal = price * quantity;
                    tableModel.setValueAt(quantity, row, 4);
                    tableModel.setValueAt(subtotal, row, 5);
                    updateTotalPrice();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            spinner.setValue(value);
            return spinner;
        }

        @Override
        public Object getCellEditorValue() {
            return spinner.getValue();
        }
    }
}
