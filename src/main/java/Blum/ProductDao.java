package Blum;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {
    private static final String SELECT_ALL_PRODUCTS = "SELECT * FROM products";
    private static final String INSERT_PRODUCT = "INSERT INTO products (categoryId, empId, product_name, price, content, image1, image2, product_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_PRODUCT = "UPDATE products SET categoryId = ?, empId = ?, product_name = ?, price = ?, content = ?, image1 = ?, image2 = ?, product_date = ? WHERE productId = ?";
    private static final String DELETE_PRODUCT = "DELETE FROM products WHERE productId = ?";
    private static final String SELECT_PRODUCT_BY_ID = "SELECT * FROM products WHERE productId = ?";

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_ALL_PRODUCTS);
            while (rs.next()) {
                int productId = rs.getInt("productId");
                int categoryId = rs.getInt("categoryId");
                String empId = rs.getString("empId");
                String productName = rs.getString("product_name");
                int price = rs.getInt("price");
                String content = rs.getString("content");
                String image1 = rs.getString("image1");
                String image2 = rs.getString("image2");
                LocalDateTime productDate = rs.getTimestamp("product_date").toLocalDateTime();
                Product product = new Product(categoryId, empId, productName, price, content, image1, image2, productDate);
                product.setProductId(productId);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
        return products;
    }

    public void addProduct(Product product) {
        Connection conn = DBConnection.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(INSERT_PRODUCT);
            stmt.setInt(1, product.getCategoryId());
            stmt.setString(2, product.getEmpId());
            stmt.setString(3, product.getProductName());
            stmt.setInt(4, product.getPrice());
            stmt.setString(5, product.getContent());
            stmt.setString(6, product.getImage1());
            stmt.setString(7, product.getImage2());
            stmt.setTimestamp(8, Timestamp.valueOf(product.getProductDate()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }

    public void updateProduct(Product product) {
        Connection conn = DBConnection.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_PRODUCT);
            stmt.setInt(1, product.getCategoryId());
            stmt.setString(2, product.getEmpId());
            stmt.setString(3, product.getProductName());
            stmt.setInt(4, product.getPrice());
            stmt.setString(5, product.getContent());
            stmt.setString(6, product.getImage1());
            stmt.setString(7, product.getImage2());
            stmt.setTimestamp(8, Timestamp.valueOf(product.getProductDate()));
            stmt.setInt(9, product.getProductId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }

    public void deleteProduct(int productId) {
        Connection conn = DBConnection.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(DELETE_PRODUCT);
            stmt.setInt(1, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }

    public Product getProductById(int productId) {
        Connection conn = DBConnection.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(SELECT_PRODUCT_BY_ID);
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int categoryId = rs.getInt("categoryId");
                String empId = rs.getString("empId");
                String productName = rs.getString("product_name");
                int price = rs.getInt("price");
                String content = rs.getString("content");
                String image1 = rs.getString("image1");
                String image2 = rs.getString("image2");
                LocalDateTime productDate = rs.getTimestamp("product_date").toLocalDateTime();
                Product product = new Product(categoryId, empId, productName, price, content, image1, image2, productDate);
                product.setProductId(productId);
                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
        return null;
    }
}