package Blum;

// CategoryDao.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    private static final String INSERT_CATEGORY = "INSERT INTO category (categoryname) VALUES (?)";
    private static final String SELECT_ALL_CATEGORIES = "SELECT * FROM category";
    private static final String UPDATE_CATEGORY = "UPDATE category SET categoryname = ? WHERE categoryid = ?";
    private static final String DELETE_CATEGORY = "DELETE FROM category WHERE categoryid = ?";

    public int getNextCategoryId() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        int nextId = 1;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT MAX(categoryid) FROM category");
            if (rs.next()) {
                nextId = rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
        return nextId;
    }
    public void addCategory(String categoryName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(INSERT_CATEGORY);
            stmt.setString(1, categoryName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SELECT_ALL_CATEGORIES);
            while (rs.next()) {
                int categoryId = rs.getInt("categoryid");
                String categoryName = rs.getString("categoryname");
                Category category = new Category(categoryId, categoryName);
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
        return categories;
    }

    public void updateCategory(int categoryId, String categoryName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(UPDATE_CATEGORY);
            stmt.setString(1, categoryName);
            stmt.setInt(2, categoryId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }

    public void deleteCategory(int categoryId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(DELETE_CATEGORY);
            stmt.setInt(1, categoryId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }
}