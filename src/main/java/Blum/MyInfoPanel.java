package Blum;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyInfoPanel extends JPanel {
    private MainFrame mainFrame;
    private String memberId;
    private JLabel nameLabel;
    private JLabel idLabel;
    private JLabel emailLabel;
    private JLabel phoneLabel;
    private DefaultTableModel orderTableModel;

    public MyInfoPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        // 상단 패널
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("←");
        backButton.setPreferredSize(new Dimension(50, 30));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showMainPanel();
            }
        });
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        // 중앙 패널
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setPreferredSize(new Dimension(600, 400));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // 회원 정보 레이블
        JLabel nameTextLabel = new JLabel("이름:");
        nameTextLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        centerPanel.add(nameTextLabel, gbc);

        gbc.gridx = 1;
        nameLabel = new JLabel();
        nameLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        centerPanel.add(nameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel idTextLabel = new JLabel("아이디:");
        idTextLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        centerPanel.add(idTextLabel, gbc);

        gbc.gridx = 1;
        idLabel = new JLabel();
        idLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        centerPanel.add(idLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel emailTextLabel = new JLabel("이메일:");
        emailTextLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        centerPanel.add(emailTextLabel, gbc);

        gbc.gridx = 1;
        emailLabel = new JLabel();
        emailLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        centerPanel.add(emailLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel phoneTextLabel = new JLabel("전화번호:");
        phoneTextLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        centerPanel.add(phoneTextLabel, gbc);

        gbc.gridx = 1;
        phoneLabel = new JLabel();
        phoneLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        centerPanel.add(phoneLabel, gbc);




        add(centerPanel, BorderLayout.CENTER);

        // 하단 패널
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton editButton = new JButton("정보 수정");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = JOptionPane.showInputDialog(MyInfoPanel.this, "비밀번호를 입력하세요:", "비밀번호 확인", JOptionPane.PLAIN_MESSAGE);
                if (password != null) {
                    checkPassword(password);
                }
            }
        });
        bottomPanel.add(editButton);

        JButton withdrawButton = new JButton("회원탈퇴");
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(MyInfoPanel.this, "정말로 회원탈퇴 하시겠습니까?", "회원탈퇴", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    withdrawMember();
                }
            }
        });
        bottomPanel.add(withdrawButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
        loadMemberInfo();

    }

    private void loadMemberInfo() {
        Connection conn = DBConnection.getConnection();
        try {
            String sql = "SELECT membername, memberid, email, phonenum FROM members WHERE memberid = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, memberId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("membername");
                String id = rs.getString("memberid");
                String email = rs.getString("email");
                String phone = rs.getString("phonenum");

                nameLabel.setText(name);
                idLabel.setText(id);
                emailLabel.setText(email);
                phoneLabel.setText(phone);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }


    private void checkPassword(String password) {
        Connection conn = DBConnection.getConnection();
        try {
            String sql = "SELECT memberpwd FROM members WHERE memberid = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, memberId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("memberpwd");
                if (password.equals(storedPassword)) {
                    mainFrame.showMyInfoEditPanel(memberId);
                } else {
                    JOptionPane.showMessageDialog(MyInfoPanel.this, "비밀번호가 일치하지 않습니다.", "비밀번호 오류", JOptionPane.ERROR_MESSAGE);
                }
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }

    private void withdrawMember() {
        Connection conn = DBConnection.getConnection();
        try {
            // 주문 상세 정보 삭제
            String deleteOrderDetailSql = "DELETE od FROM order_detail od " +
                    "JOIN orders o ON od.orderid = o.orderid " +
                    "WHERE o.memberid = ?";
            PreparedStatement deleteOrderDetailStmt = conn.prepareStatement(deleteOrderDetailSql);
            deleteOrderDetailStmt.setString(1, memberId);
            deleteOrderDetailStmt.executeUpdate();
            deleteOrderDetailStmt.close();

            // 주문 정보 삭제
            String deleteOrderSql = "DELETE FROM orders WHERE memberid = ?";
            PreparedStatement deleteOrderStmt = conn.prepareStatement(deleteOrderSql);
            deleteOrderStmt.setString(1, memberId);
            deleteOrderStmt.executeUpdate();
            deleteOrderStmt.close();

            // 장바구니 정보 삭제
            String deleteCartSql = "DELETE FROM cart WHERE memberid = ?";
            PreparedStatement deleteCartStmt = conn.prepareStatement(deleteCartSql);
            deleteCartStmt.setString(1, memberId);
            deleteCartStmt.executeUpdate();
            deleteCartStmt.close();

            // 회원 정보 삭제
            String deleteMemberSql = "DELETE FROM members WHERE memberid = ?";
            PreparedStatement deleteMemberStmt = conn.prepareStatement(deleteMemberSql);
            deleteMemberStmt.setString(1, memberId);
            deleteMemberStmt.executeUpdate();
            deleteMemberStmt.close();

            JOptionPane.showMessageDialog(MyInfoPanel.this, "회원탈퇴가 완료되었습니다.", "회원탈퇴", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.logout(); // 로그아웃 처리
            mainFrame.showLoginPanel();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }

    private class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof ImageIcon) {
                JLabel label = new JLabel((ImageIcon) value);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
}