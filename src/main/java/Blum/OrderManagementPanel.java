package Blum;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderManagementPanel extends JPanel {
    private MainFrame mainFrame;
    private JTable orderTable;
    private DefaultTableModel tableModel;

    public OrderManagementPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        // 주문 테이블 초기화
        String[] columnNames = {"주문 ID", "고객 ID", "총 금액", "받는 사람 이름", "받는 사람 전화번호", "배송지 주소1", "배송지 주소2", "주문일", "주문 상태"};
        tableModel = new DefaultTableModel(columnNames, 0);
        orderTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);

        // 뒤로 가기 버튼
        JButton backButton = new JButton("←");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showMainPanel();
            }
        });
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.add(backButton);
        add(backButtonPanel, BorderLayout.NORTH);

        // 데이터베이스에서 주문 정보 가져오기
        loadOrders();
    }

    private void loadOrders() {
        Connection conn = DBConnection.getConnection();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM orders");
            while (rs.next()) {
                int orderId = rs.getInt("orderid");
                String memberId = rs.getString("memberid");
                int totalPrice = rs.getInt("total_price");
                String receiverName = rs.getString("receiver_name");
                String receiverPhone = rs.getString("receiver_phone");
                String deliveryLocation1 = rs.getString("deliverylocation1");
                String deliveryLocation2 = rs.getString("deliverylocation2");
                String orderDate = rs.getString("orderdate");
                String orderStatus = rs.getString("order_detail_status");
                Object[] rowData = {orderId, memberId, totalPrice, receiverName, receiverPhone, deliveryLocation1, deliveryLocation2, orderDate, orderStatus};
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }
}