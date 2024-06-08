package Blum;

public class CartItem {
    private int cartId;
    private String productName;
    private int price;
    private String image;
    private int quantity;
    private int productId;

    public CartItem(int cartId, String productName, int price, String image, int quantity, int productId) {
        this.cartId = cartId;
        this.productName = productName;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
        this.productId = productId;
    }

    public int getCartId() {
        return cartId;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public int getQuantity() {
        return quantity;
    }
    public int getProductId() {return productId;}
}
