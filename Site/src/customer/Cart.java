package customer;

import java.util.ArrayList;
import java.util.List;

import products.Produit;

public class Cart {
	private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void addProduct(Produit product, String size, int quantity) {
        // Vérifier si le produit-taille est déjà dans le Cart
        for (CartItem ligne : items) {
            if (ligne.getProduct().getId() == product.getId() && ligne.getSize().equals(size)) {
                ligne.setQuantity(ligne.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(product, size, quantity));
        System.out.println(this.toString());
    }

    public void removeProduct(int productId, String size) {
        items.removeIf(ligne -> ligne.getProduct().getId() == productId && ligne.getSize().equals(size));
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void clearCart() {
        items.clear();
    }
    
    @Override
    public String toString() {
    	String s = "";
    	for (CartItem ligne : items) {
    		s +=ligne.getProduct().getNom();
    	}
    	return s;
    }
}

