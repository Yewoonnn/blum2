package Blum;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDetailPanel extends JPanel {
    private MainFrame mainFrame;
    private OrderManagementPanel orderManagementPanel;
    private JTable orderDetailTable;
    private DefaultTableModel tableModel;
    private JLabel memberInfoLabel;

    public OrderDetailPanel(MainFrame mainFrame, OrderManagementPanel orderManagementPanel) {
        this.mainFrame = mainFrame;
        this.orderManagementPanel = orderManagementPanel;
        setLayout(new BorderLayout());

        // 주문 상세 테이블 초기화
        String[] columnNames = {"주문 상세 ID", "주문 ID", "제품 ID", "제품명", "수량", "가격"};
        tableModel = new DefaultTableModel(columnNames, 0);
        orderDetailTable = new JTable(tableModel);
        orderDetailTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(orderDetailTable);
        add(scrollPane, BorderLayout.CENTER);

        // 회원 정보 레이블
        memberInfoLabel = new JLabel();
        memberInfoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        memberInfoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(memberInfoLabel, BorderLayout.NORTH);

        // 뒤로 가기 버튼
        JButton backButton = new JButton("←");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showPanel(orderManagementPanel, "orderManagementPanel");
            }
        });
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.add(backButton);
        add(backButtonPanel, BorderLayout.SOUTH);
    }

    public void setOrderDetail(int orderId) {
        // 데이터베이스에서 주문 상세 정보 가져오기
        loadOrderDetails(orderId);

        // 데이터베이스에서 회원 정보 가져오기
        loadMemberInfo(orderId);
    }

    private void loadOrderDetails(int orderId) {
        tableModel.setRowCount(0);
        Connection conn = DBConnection.getConnection();
        try {
            String sql = "SELECT od.order_detail_id, od.orderid, od.productid, p.product_name, od.product_count, od.product_price " +
                    "FROM order_detail od " +
                    "JOIN products p ON od.productid = p.productid " +
                    "WHERE od.orderid = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int orderDetailId = rs.getInt("order_detail_id");
                int productId = rs.getInt("productid");
                String productName = rs.getString("product_name");
                int productCount = rs.getInt("product_count");
                int productPrice = rs.getInt("product_price");
                Object[] rowData = {orderDetailId, orderId, productId, productName, productCount, productPrice};
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }

    private void loadMemberInfo(int orderId) {
        Connection conn = DBConnection.getConnection();
        try {
            String sql = "SELECT m.membername, m.email, m.phonenum " +
                    "FROM orders o " +
                    "JOIN members m ON o.memberid = m.memberid " +
                    "WHERE o.orderid = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String memberName = rs.getString("membername");
                String email = rs.getString("email");
                String phoneNum = rs.getString("phonenum");
                String memberInfo = "<html><b>회원 정보</b><br>" +
                        "이름: " + memberName + "<br>" +
                        "이메일: " + email + "<br>" +
                        "전화번호: " + phoneNum + "</html>";
                memberInfoLabel.setText(memberInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }
}