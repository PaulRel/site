package customer;

public class CartManager {
    private static Cart tempCart = new Cart();

    public static Cart getTempCart() {
        return tempCart;
    }

    public static void clearTempCart() {
        tempCart.clearCart();
    }
}