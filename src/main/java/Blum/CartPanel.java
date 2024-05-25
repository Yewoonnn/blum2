package Blum;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CartPanel extends JPanel {
    private MainFrame mainFrame;
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JLabel totalPriceLabel;
    private CartDao cartDao;
    private String memberId;

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
        JScrollPane scrollPane = new JScrollPane(cartTable);
        add(scrollPane, BorderLayout.CENTER);

        // 총 금액 레이블
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPriceLabel = new JLabel("총 금액: 0원");
        totalPriceLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        totalPanel.add(totalPriceLabel);
        add(totalPanel, BorderLayout.SOUTH);

        // 상단 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("←");
        JButton selectAllButton = new JButton("전체선택");
        JButton deleteSelectedButton = new JButton("선택삭제");
        JButton orderButton = new JButton("주문하기");
        buttonPanel.add(backButton);
        buttonPanel.add(selectAllButton);
        buttonPanel.add(deleteSelectedButton);
        buttonPanel.add(orderButton);
        add(buttonPanel, BorderLayout.NORTH);

        // 버튼 액션 리스너 등록

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showMainPanel();
            }
        });
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.add(backButton);
        add(backButtonPanel, BorderLayout.NORTH);
        selectAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectAllItems();
            }
        });

        deleteSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedItems();
            }
        });

        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 주문하기 로직 구현
                JOptionPane.showMessageDialog(CartPanel.this, "주문이 완료되었습니다.");
                cartDao.clearCartByMember(memberId);
                loadCartItems();
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

            // 같은 상품이 이미 장바구니에 있는지 확인
            boolean isExistingItem = false;
            int existingItemRow = -1;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String productName = (String) tableModel.getValueAt(i, 2);
                if (productName.equals(cartItem.getProductName())) {
                    isExistingItem = true;
                    existingItemRow = i;
                    break;
                }
            }

            if (isExistingItem) {
                // 같은 상품이 이미 장바구니에 있는 경우 수량 증가
                int currentQuantity = (int) tableModel.getValueAt(existingItemRow, 4);
                int newQuantity = currentQuantity + cartItem.getQuantity();
                tableModel.setValueAt(newQuantity, existingItemRow, 4);
                tableModel.setValueAt(cartItem.getPrice() * newQuantity, existingItemRow, 5);
            } else {
                // 새로운 상품인 경우 장바구니에 추가
                Object[] rowData = {false, new ImageIcon(image), cartItem.getProductName(), cartItem.getPrice(), cartItem.getQuantity(), cartItem.getPrice() * cartItem.getQuantity(), cartItem.getCartId()};
                tableModel.addRow(rowData);
            }
        }

        // 장바구니 테이블의 행 높이 설정
        cartTable.setRowHeight(100);

        updateTotalPrice();
    }

    private void selectAllItems() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(true, i, 0);
        }
    }

    private void deleteSelectedItems() {
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            boolean isSelected = (boolean) tableModel.getValueAt(i, 0);
            if (isSelected) {
                int cartId = (int) tableModel.getValueAt(i, 6);
                cartDao.removeCartItem(cartId);
                tableModel.removeRow(i);
            }
        }
        updateTotalPrice();
    }


    private void updateTotalPrice() {
        int totalPrice = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int subtotal = (int) tableModel.getValueAt(i, 4);
            totalPrice += subtotal;
        }
        totalPriceLabel.setText("총 금액: " + totalPrice + "원");
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