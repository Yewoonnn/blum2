package Blum;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CustomerManagementPanel extends JPanel {
    private MainFrame mainFrame;
    private JTable customerTable;
    private DefaultTableModel tableModel;

    public CustomerManagementPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        // 고객 테이블 초기화
        String[] columnNames = {"고객 ID", "이름", "이메일", "전화번호"};
        tableModel = new DefaultTableModel(columnNames, 0);
        customerTable = new JTable(tableModel);
        customerTable.setRowHeight(30); // 행 높이 설정
        customerTable.getColumnModel().getColumn(0).setPreferredWidth(100); // 고객 ID 열 너비 설정
        customerTable.getColumnModel().getColumn(1).setPreferredWidth(100); // 이름 열 너비 설정
        customerTable.getColumnModel().getColumn(2).setPreferredWidth(200); // 이메일 열 너비 설정
        customerTable.getColumnModel().getColumn(3).setPreferredWidth(150); // 전화번호 열 너비 설정
        JScrollPane scrollPane = new JScrollPane(customerTable);
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

        // 데이터베이스에서 고객 정보 가져오기
        loadCustomers();
    }

    private void loadCustomers() {
        Connection conn = DBConnection.getConnection();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM members");
            while (rs.next()) {
                String memberId = rs.getString("memberid");
                String memberName = rs.getString("membername");
                String email = rs.getString("email");
                String phoneNum = rs.getString("phonenum");
                Object[] rowData = {memberId, memberName, email, phoneNum};
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }
    private void updateCustomer(String customerId, String name, String email, String phone) {
        Connection conn = DBConnection.getConnection();
        try {
            String sql = "UPDATE members SET membername = ?, email = ?, phonenum = ? WHERE memberid = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, customerId);
            stmt.executeUpdate();
            stmt.close();
            refreshCustomers(); // 고객 정보 수정 후 고객 정보 새로 로드
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }

    private void deleteCustomer(String customerId) {
        Connection conn = DBConnection.getConnection();
        try {
            String sql = "DELETE FROM members WHERE memberid = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, customerId);
            stmt.executeUpdate();
            stmt.close();
            refreshCustomers(); // 고객 정보 삭제 후 고객 정보 새로 로드
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }
    public void refreshCustomers() {
        tableModel.setRowCount(0); // 기존 테이블 데이터 초기화
        loadCustomers(); // 고객 정보 새로 로드
    }
}