package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import customer.CartItem;
import customer.Customer;
import customer.Order;
import products.Product;

public class OrderDAO {
	
	public List<Order> getOrdersByCustomer(Customer customer) {
        List<Order> orders = new ArrayList<>();
        
        String query = "SELECT * FROM Orders WHERE customer_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
        		PreparedStatement statement = connection.prepareStatement(query);){
        	statement.setInt(1, customer.getId());
        	
            try (ResultSet rs = statement.executeQuery()) {
            	while (rs.next()) {
            		int orderId = rs.getInt("order_id");
            		LocalDate orderDate = rs.getDate("order_date").toLocalDate();
            		String status = rs.getString("status");

            		Order order = new Order(customer);
            		order.setOrderId(orderId);
            		order.setOrderDate(orderDate);
            		order.setStatus(status);

            		addOrderDetails(order, connection);
                    
            		orders.add(order);
            	}
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        return orders;
    }
	
	private void addOrderDetails(Order order, Connection connection) {
        String query = "SELECT * FROM OrderDetails WHERE order_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, order.getOrderId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int productId = rs.getInt("product_id");
                    String size = rs.getString("size");
                    int quantity = rs.getInt("quantity");
                    ProduitDAO produitDAO = new ProduitDAO();
                    Product product = produitDAO.getProduitById(productId);
                    order.addProduct(product, size, quantity);
                }
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
    }
	
    public void insertOrder(Order order) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sqlOrder = "INSERT INTO Orders (customer_id, order_date, status) VALUES (?, ?, ?)";
            PreparedStatement psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);

            psOrder.setInt(1, order.getCustomer().getId());
            psOrder.setDate(2, Date.valueOf(order.getOrderDate()));
            psOrder.setString(3, order.getStatus());
            psOrder.executeUpdate();

            ResultSet rs = psOrder.getGeneratedKeys();
            if (rs.next()) {
                int orderId = rs.getInt(1);
                order.setOrderId(orderId);
                insertOrderDetails(conn, orderId, order.getProducts());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertOrderDetails(Connection conn, int orderId, List<CartItem> products) throws SQLException {
        String sqlDetail = "INSERT INTO OrderDetails (order_id, product_id, quantity, size) VALUES (?, ?, ?, ?)";
        PreparedStatement psDetail = conn.prepareStatement(sqlDetail);

        for (CartItem product : products) {
            psDetail.setInt(1, orderId);
            psDetail.setInt(2, product.getProduct().getId());
            psDetail.setInt(3, product.getQuantity());
            psDetail.setString(4, product.getSize());
            psDetail.addBatch();
        }
        psDetail.executeBatch();
    }
}
