package customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;

public class Order {
	private int orderId;
    private Customer customer;
    private List<CartItem> products;
    private String status; // "Pending", "Validated", "Delivered" / "En cours", "Validée", "Livrée"
    private LocalDate orderDate;
    
    public Order(Customer customer, List<CartItem> products) {
        this.customer = customer;
        this.products = new ArrayList<>(products);
        this.status = "Pending";
        this.orderDate = LocalDate.now();
    }

    public void validateOrder() {
        this.status = "Validated";
    }

    public void deliverOrder() {
        this.status = "Delivered";
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


}
