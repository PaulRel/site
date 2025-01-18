package customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import products.Product;

	public class Order {
		private int orderId;
	    private Customer customer;
	    private List<CartItem> products;
	    private String status;
	    private LocalDate orderDate;
	    private double totalPrice;

	    public Order(Customer customer) {
	        this.customer = customer;
	        this.products = new ArrayList<>();
	        this.status = "En cours";
	        this.orderDate = LocalDate.now();
	        this.totalPrice = 0.0;
	    }

	    public void addProduct(Product product, String size, int quantity) {
	        CartItem detail = new CartItem(product, size, quantity);
	        products.add(detail);
	        calculateTotalPrice();
	    }

	    public void validateOrder() {
	        this.status = "Validée";
	        updateOrderStatusInDatabase();
	    }

	    public void deliverOrder() {
	        this.status = "Livrée";
	        updateOrderStatusInDatabase();
	    }

	    // Calcul du prix total des produits
	    public void calculateTotalPrice() {
	        this.totalPrice = products.stream()
	                .mapToDouble(product -> product.getProduct().getPrice() * product.getQuantity())
	                .sum();
	    }
    
    public void decrementStock(int productId, String size, int quantity) {
        String query = "UPDATE taillestock SET qt_dispo = qt_dispo - ? WHERE produit_id = ? AND taille = ?";
        try (Connection connection = DatabaseConnection.getConnection();
        		PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, productId);
            pstmt.setString(3, size);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void updateOrderStatusInDatabase(){
        String query = "UPDATE Orders SET status = ? WHERE order_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
        		PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, this.status); // Mise à jour avec le statut actuel
            pstmt.setInt(2, this.orderId); // Identifiant de la commande
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public String toString() {
    	return "L'identifiant de la commande est "+this.getOrderId() + 
    			".\nLe client est " + this.customer.getFirstName() +
    			".\nLes produits commandés sont :"+this.getProducts();
    }
    
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<CartItem> getProducts() {
		return products;
	}

	public void setProducts(List<CartItem> products) {
		this.products = products;
		calculateTotalPrice();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}


}
