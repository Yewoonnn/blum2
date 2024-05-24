package Blum;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CartDao {
    private static final String INSERT_CART_ITEM = "INSERT INTO cart (memberid, quantity, productid, cartdate) VALUES (?, ?, ?, ?)";
    private static final String SELECT_CART_ITEMS_BY_MEMBER = "SELECT c.cartid, c.quantity, p.product_name, p.price, p.image1 FROM cart c JOIN products p ON c.productid = p.productid WHERE c.memberid = ?";
    private static final String DELETE_CART_ITEM = "DELETE FROM cart WHERE cartid = ?";
    private static final String CLEAR_CART_BY_MEMBER = "DELETE FROM cart WHERE memberid = ?";

    public void addCartItem(String memberId, int productId, int quantity) {
        Connection conn = DBConnection.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(INSERT_CART_ITEM);
            stmt.setString(1, memberId);
            stmt.setInt(2, quantity);
            stmt.setInt(3, productId);
            stmt.setDate(4, Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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

    public void removeCartItem(int cartId) {
        Connection conn = DBConnection.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(DELETE_CART_ITEM);
            stmt.setInt(1, cartId);
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