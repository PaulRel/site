package customer;

import products.Product;

public class CartItem {
	private Product product;
    private String size;
    private int quantity;

    public CartItem(Product product, String size, int quantity) {
        this.product = product;
        this.size = size;
        this.quantity = quantity;
    }

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
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
	
	@Override
	public String toString() {
		return "Nom du produit : " + this.product.getName() +
				" Taille : "+ this.size +
				" Quantité :"+ this.quantity +
				" Prix : " + this.product.getPrice();
	}

}
