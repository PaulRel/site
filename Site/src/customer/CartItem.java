package customer;

import products.Produit;

public class CartItem {
	private Produit product;
    private String size;
    private int quantity;

    public CartItem(Produit product, String size, int quantity) {
        this.product = product;
        this.size = size;
        this.quantity = quantity;
    }

	public Produit getProduct() {
		return product;
	}

	public void setProduct(Produit product) {
		this.product = product;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
