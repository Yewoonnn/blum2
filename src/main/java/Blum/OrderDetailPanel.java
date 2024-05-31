package Blum;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
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
    private JTable orderInfoTable;
    private JTable orderProductTable;
    private DefaultTableModel orderInfoTableModel;
    private DefaultTableModel orderProductTableModel;

    public OrderDetailPanel(MainFrame mainFrame, OrderManagementPanel orderManagementPanel) {
        this.mainFrame = mainFrame;
        this.orderManagementPanel = orderManagementPanel;
        setLayout(new BorderLayout());

        // 상단 패널
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("←");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showPanel(orderManagementPanel, "orderManagementPanel");
            }
        });
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        // 중앙 패널
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JLabel titleLabel = new JLabel("주문 관리");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        centerPanel.add(titleLabel, gbc);

        gbc.gridy = 1;
        JLabel orderInfoLabel = new JLabel("주문자 정보");
        orderInfoLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        centerPanel.add(orderInfoLabel, gbc);

        gbc.gridy = 2;
        String[] orderInfoColumnNames = {"주문 ID", "회원 이름", "회원 휴대폰번호", "주문일", "회원 메일"};
        orderInfoTableModel = new DefaultTableModel(orderInfoColumnNames, 0);
        orderInfoTable = new JTable(orderInfoTableModel);
        orderInfoTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane orderInfoScrollPane = new JScrollPane(orderInfoTable);
        orderInfoScrollPane.setPreferredSize(new Dimension(600, 60));
        centerPanel.add(orderInfoScrollPane, gbc);

        gbc.gridy = 3;
        JLabel orderProductLabel = new JLabel("주문한 상품");
        orderProductLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        centerPanel.add(orderProductLabel, gbc);

        gbc.gridy = 4;
        String[] orderProductColumnNames = {"상품 이미지", "상품명", "상품 ID", "상품 가격", "구매 수량", "처리 상태"};
        orderProductTableModel = new DefaultTableModel(orderProductColumnNames, 0);
        orderProductTable = new JTable(orderProductTableModel);
        orderProductTable.setRowHeight(80);
        orderProductTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        orderProductTable.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());
        JScrollPane orderProductScrollPane = new JScrollPane(orderProductTable);
        orderProductScrollPane.setPreferredSize(new Dimension(600, 200));
        centerPanel.add(orderProductScrollPane, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    public void setOrderDetail(int orderId) {
        Connection conn = DBConnection.getConnection();
        try {
            // 주문자 정보 조회
            String sql = "SELECT o.orderid, m.membername, m.phonenum, o.orderdate, m.email " +
                    "FROM orders o " +
                    "JOIN members m ON o.memberid = m.memberid " +
                    "WHERE o.orderid = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            orderInfoTableModel.setRowCount(0);
            if (rs.next()) {
                Object[] rowData = {
                        rs.getInt("orderid"),
                        rs.getString("membername"),
                        rs.getString("phonenum"),
                        rs.getString("orderdate"),
                        rs.getString("email")
                };
                orderInfoTableModel.addRow(rowData);
            }
            rs.close();
            stmt.close();

            // 주문한 상품 정보 조회
            sql = "SELECT p.image1, p.product_name, od.productid, od.product_price, od.product_count, o.order_detail_status " +
                    "FROM order_detail od " +
                    "JOIN products p ON od.productid = p.productid " +
                    "JOIN orders o ON od.orderid = o.orderid " +
                    "WHERE od.orderid = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            rs = stmt.executeQuery();

            orderProductTableModel.setRowCount(0);
            while (rs.next()) {
                ImageIcon imageIcon = new ImageIcon(getClass().getResource(rs.getString("image1")));
                Image image = imageIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                Object[] rowData = {
                        new ImageIcon(image),
                        rs.getString("product_name"),
                        rs.getInt("productid"),
                        rs.getInt("product_price"),
                        rs.getInt("product_count"),
                        rs.getString("order_detail_status")
                };
                orderProductTableModel.addRow(rowData);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }

    private class ImageRenderer extends JLabel implements TableCellRenderer {
        public ImageRenderer() {
            setOpaque(true);
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof ImageIcon) {
                setIcon((ImageIcon) value);
            } else {
                setIcon(null);
                setText("");
            }
            return this;
        }
    }
}