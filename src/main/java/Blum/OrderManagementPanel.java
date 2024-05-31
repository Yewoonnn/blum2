package Blum;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
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
        String[] columnNames = {"주문 ID", "고객 ID", "총 금액", "받는 사람 이름", "받는 사람 전화번호", "배송지 주소1", "배송지 주소2", "주문일", "주문 상태", "상세보기"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 9; // 상세보기 열만 편집 가능하도록 설정
            }
        };
        orderTable = new JTable(tableModel);
        orderTable.setRowHeight(30);
        orderTable.getColumnModel().getColumn(9).setCellRenderer(new ButtonRenderer());
        orderTable.getColumnModel().getColumn(9).setCellEditor(new ButtonEditor(new JCheckBox()));
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
                Object[] rowData = {orderId, memberId, totalPrice, receiverName, receiverPhone, deliveryLocation1, deliveryLocation2, orderDate, orderStatus, "상세보기"};
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String orderId;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = orderTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int orderId = (int) orderTable.getValueAt(selectedRow, 0);
                        OrderDetailPanel orderDetailPanel = new OrderDetailPanel(mainFrame, OrderManagementPanel.this);
                        orderDetailPanel.setOrderDetail(orderId);

                        // 주문상세 패널로 전환
                        mainFrame.showPanel(orderDetailPanel, "orderDetailPanel");
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            orderId = table.getValueAt(row, 0).toString();
            button.setText(value.toString());
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }
}


