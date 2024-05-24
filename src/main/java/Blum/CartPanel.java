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
        String[] columnNames = {"상품 이미지", "상품명", "가격", "수량", "합계"};
        tableModel = new DefaultTableModel(columnNames, 0);
        cartTable = new JTable(tableModel);
        cartTable.setRowHeight(100);
        cartTable.getColumnModel().getColumn(0).setPreferredWidth(100);
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
        JButton backButton = new JButton("뒤로가기");
        JButton removeButton = new JButton("상품 삭제");
        JButton clearButton = new JButton("장바구니 비우기");
        JButton orderButton = new JButton("주문하기");
        buttonPanel.add(backButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(orderButton);
        add(buttonPanel, BorderLayout.NORTH);

        // 버튼 액션 리스너 등록

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showMainPanel();
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = cartTable.getSelectedRow();
                if (selectedRow != -1) {
                    int cartId = (int) tableModel.getValueAt(selectedRow, 5);
                    cartDao.removeCartItem(cartId);
                    loadCartItems();
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cartDao.clearCartByMember(memberId);
                loadCartItems();
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
            Object[] rowData = {new ImageIcon(image), cartItem.getProductName(), cartItem.getPrice(), cartItem.getQuantity(), cartItem.getPrice() * cartItem.getQuantity(), cartItem.getCartId()};
            tableModel.addRow(rowData);
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
}