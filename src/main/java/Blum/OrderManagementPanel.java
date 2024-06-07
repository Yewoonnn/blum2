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
        orderTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);

        // 상단 패널
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

// 뒤로 가기 버튼
        JButton backButton = new JButton("←");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showMainPanel();
            }
        });
        topPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);

// 하단 패널
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

// 주문 상태 콤보박스
        String[] statusOptions = {"주문완료", "배송중", "배송완료"};
        JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);
        statusComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = orderTable.getSelectedRow();
                if (selectedRow != -1) {
                    int orderId = (int) tableModel.getValueAt(selectedRow, 0);
                    String newStatus = (String) statusComboBox.getSelectedItem();
                    updateOrderStatus(orderId, newStatus);
                    tableModel.setValueAt(newStatus, selectedRow, 8);
                }
            }
        });
        bottomPanel.add(statusComboBox);

        add(bottomPanel, BorderLayout.SOUTH);

        // 데이터베이스에서 주문 정보 가져오기
        loadOrders();
    }

    private void loadOrders() {
        Connection conn = DBConnection.getConnection();
        try {
            String sql = "SELECT * FROM orders";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

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
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }

    private void updateOrderStatus(int orderId, String newStatus) {
        Connection conn = DBConnection.getConnection();
        try {
            String sql = "UPDATE orders SET order_detail_status = ? WHERE orderid = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newStatus);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
            stmt.close();
            refreshOrders(); // 주문 상태 변경 후 주문 정보 새로 로드
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }
    public void refreshOrders() {
        tableModel.setRowCount(0); // 기존 테이블 데이터 초기화
        loadOrders(); // 주문 정보 새로 로드
    }
}