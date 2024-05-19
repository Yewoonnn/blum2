package Blum;

import java.time.LocalDateTime;

public class Product {
    private int productId;
    private int categoryId;
    private String empId;
    private String productName;
    private int price;
    private String content;
    private String image1;
    private String image2;
    private LocalDateTime productDate;

    public Product(int categoryId, String empId, String productName, int price, String content, String image1, String image2, LocalDateTime productDate) {
        this.categoryId = categoryId;
        this.empId = empId;
        this.productName = productName;
        this.price = price;
        this.content = content;
        this.image1 = image1;
        this.image2 = image2;
        this.productDate = productDate;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public LocalDateTime getProductDate() {
        return productDate;
    }

    public void setProductDate(LocalDateTime productDate) {
        this.productDate = productDate;
    }
}