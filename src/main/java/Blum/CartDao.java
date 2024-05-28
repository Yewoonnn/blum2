package Blum;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CartDao {
    private static final String INSERT_CART_ITEM = "INSERT INTO cart (memberid, quantity, productid, cartdate) VALUES (?, ?, ?, ?)";
    private static final String SELECT_CART_ITEMS_BY_MEMBER = "SELECT c.cartid, c.quantity, p.product_name, p.price, p.image1 FROM cart c JOIN products p ON c.productid = p.productid WHERE c.memberid = ?";
    private static final String DELETE_CART_ITEM = "DELETE FROM cart WHERE cartid = ?";
    private static final String CLEAR_CART_BY_MEMBER = "DELETE FROM cart WHERE memberid = ?";

    public void addCartItem(String memberId, int productId, int quantity) {
        Connection conn = DBConnection.getConnection();
        try {
            // 장바구니에 이미 상품이 있는지 확인
            String selectSql = "SELECT * FROM cart WHERE memberid = ? AND productid = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            selectStmt.setString(1, memberId);
            selectStmt.setInt(2, productId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                // 이미 존재하는 상품인 경우 수량 증가
                int cartId = rs.getInt("cartid");
                int currentQuantity = rs.getInt("quantity");
                int newQuantity = currentQuantity + quantity;

                String updateSql = "UPDATE cart SET quantity = ? WHERE cartid = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, newQuantity);
                updateStmt.setInt(2, cartId);
                updateStmt.executeUpdate();
            } else {
                // 새로운 상품인 경우 장바구니에 추가
                String insertSql = "INSERT INTO cart (memberid, quantity, productid, cartdate) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setString(1, memberId);
                insertStmt.setInt(2, quantity);
                insertStmt.setInt(3, productId);
                insertStmt.setDate(4, Date.valueOf(LocalDate.now()));
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }

    public List<CartItem> getCartItemsByMember(String memberId) {
        List<CartItem> cartItems = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(SELECT_CART_ITEMS_BY_MEMBER);
            stmt.setString(1, memberId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int cartId = rs.getInt("cartid");
                int quantity = rs.getInt("quantity");
                String productName = rs.getString("product_name");
                int price = rs.getInt("price");
                String image = rs.getString("image1");
                CartItem cartItem = new CartItem(cartId, productName, price, image, quantity);
                cartItems.add(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
        return cartItems;
    }

    public void removeCartItems(List<Integer> cartIds) {
        Connection conn = DBConnection.getConnection();
        try {
            String sql = "DELETE FROM cart WHERE cartid IN (" + String.join(",", Collections.nCopies(cartIds.size(), "?")) + ")";
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (int i = 0; i < cartIds.size(); i++) {
                stmt.setInt(i + 1, cartIds.get(i));
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }
    public void removeCartItemsByProductNames(List<String> productNames, String memberId) {
        Connection conn = DBConnection.getConnection();
        try {
            String productNamesString = String.join(",", Collections.nCopies(productNames.size(), "?"));
            String sql = "DELETE FROM cart WHERE productid IN (SELECT productid FROM products WHERE product_name IN (" + productNamesString + ")) AND memberid = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (int i = 0; i < productNames.size(); i++) {
                stmt.setString(i + 1, productNames.get(i));
            }
            stmt.setString(productNames.size() + 1, memberId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }

    public void clearCartByMember(String memberId) {
        Connection conn = DBConnection.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(CLEAR_CART_BY_MEMBER);
            stmt.setString(1, memberId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }
}