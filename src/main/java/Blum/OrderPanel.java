package Blum;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField address1Field;
    private JTextField address2Field;
    private JLabel totalPriceLabel;
    private JTable productTable;
    private DefaultTableModel tableModel;

    public OrderPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        // 상단 패널
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("←");
        backButton.addActionListener(e -> mainFrame.showCartPanel(mainFrame.getMemberId()));
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        // 중앙 패널
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 배송 정보 패널
        JPanel deliveryPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel deliveryLabel = new JLabel("배송 정보");
        deliveryLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        deliveryPanel.add(deliveryLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel nameLabel = new JLabel("받는 사람 이름:");
        deliveryPanel.add(nameLabel, gbc);
        gbc.gridy = 2;
        JLabel phoneLabel = new JLabel("받는 사람 전화번호:");
        deliveryPanel.add(phoneLabel, gbc);
        gbc.gridy = 3;
        JLabel address1Label = new JLabel("배송지 주소1:");
        deliveryPanel.add(address1Label, gbc);
        gbc.gridy = 4;
        JLabel address2Label = new JLabel("배송지 주소2:");
        deliveryPanel.add(address2Label, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nameField = new JTextField(20);
        deliveryPanel.add(nameField, gbc);
        gbc.gridy = 2;
        phoneField = new JTextField(20);
        deliveryPanel.add(phoneField, gbc);
        gbc.gridy = 3;
        address1Field = new JTextField(20);
        deliveryPanel.add(address1Field, gbc);
        gbc.gridy = 4;
        address2Field = new JTextField(20);
        deliveryPanel.add(address2Field, gbc);

        centerPanel.add(deliveryPanel, BorderLayout.NORTH);

        // 상품 정보 패널
        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel productLabel = new JLabel("상품 정보");
        productLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        productPanel.add(productLabel, BorderLayout.NORTH);

        String[] columnNames = {"상품명", "가격", "수량", "합계"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        productTable.setRowHeight(30);
        productTable.setShowGrid(false);
        productTable.setIntercellSpacing(new Dimension(0, 0));
        JScrollPane scrollPane = new JScrollPane(productTable);
        productPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.add(new JLabel("총 금액:"));
        totalPriceLabel = new JLabel();
        totalPanel.add(totalPriceLabel);
        productPanel.add(totalPanel, BorderLayout.SOUTH);

        centerPanel.add(productPanel, BorderLayout.CENTER);

        JScrollPane mainScrollPane = new JScrollPane(centerPanel);
        add(mainScrollPane, BorderLayout.CENTER);

        // 하단 패널
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton orderButton = new JButton("주문하기");
        orderButton.addActionListener(e -> processOrder());
        bottomPanel.add(orderButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    private void processOrder() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String address1 = address1Field.getText();
        String address2 = address2Field.getText();
        int totalPrice = Integer.parseInt(totalPriceLabel.getText().replace("원", "").trim());
        String memberId = mainFrame.getMemberId();

        // 주문 정보를 데이터베이스에 저장
        Connection conn = DBConnection.getConnection();
        String sql = "INSERT INTO orders (memberid, total_price, receiver_name, receiver_phone, deliverylocation1, deliverylocation2, orderdate, order_detail_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, memberId);
            stmt.setInt(2, totalPrice);
            stmt.setString(3, name);
            stmt.setString(4, phone);
            stmt.setString(5, address1);
            stmt.setString(6, address2);
            stmt.setDate(7, java.sql.Date.valueOf(java.time.LocalDate.now()));
            stmt.setString(8, "주문완료");
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }

        JOptionPane.showMessageDialog(this, "주문이 완료되었습니다.", "주문 완료", JOptionPane.INFORMATION_MESSAGE);
        mainFrame.showMainPanel();
    }

    public void setTotalPrice(int totalPrice) {
        totalPriceLabel.setText(totalPrice + "원");
    }

    public void setProductInfo(List<CartItem> cartItems) {
        tableModel.setRowCount(0);
        for (CartItem cartItem : cartItems) {
            Object[] rowData = {cartItem.getProductName(), cartItem.getPrice(), cartItem.getQuantity(), cartItem.getPrice() * cartItem.getQuantity()};
            tableModel.addRow(rowData);
        }
    }


}