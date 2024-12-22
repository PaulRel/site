package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import Interface.MainView;
import customer.Invoice;
import customer.Order;
import javafx.scene.control.Alert.AlertType;

public class InvoiceDAO {
	public void insertInvoice(Invoice invoice) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			String sql = "INSERT INTO invoice (order_id, billing_address, shipping_address, payment_method, invoice_date) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
			pstmt.setInt(1, invoice.getOrder().getOrderId());
            pstmt.setString(2, invoice.getBillingAddress());
            pstmt.setString(3, invoice.getShippingAddress());
            pstmt.setString(4, invoice.getPaymentMethod());
            pstmt.setDate(5, Date.valueOf(invoice.getInvoiceDate()));

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int orderId = rs.getInt(1);
                invoice.setInvoiceId(orderId);
            }
        } catch (SQLException e) {
        	MainView.showAlert("Erreur", null, "Une erreur est survenue lors de l'insertion de la facture : " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }
	
	public Invoice getInvoiceByOrderId(Order order) {
        String query = "SELECT * FROM invoice WHERE order_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
        		PreparedStatement pstmt = connection.prepareStatement(query);){
            pstmt.setInt(1, order.getOrderId());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                        int invoiceId = rs.getInt("invoice_id"); 
                        String billingAddress = rs.getString("billing_address");
                        String shippingAddress = rs.getString("shipping_address");
                        String shippingMethod = rs.getString("shipping_method");
                        String paymentMethod = rs.getString("payment_method");
                        LocalDate invoiceDate = rs.getDate("invoice_date").toLocalDate();
                        
                        Invoice invoice = new Invoice(order);
                        invoice.setInvoiceId(invoiceId);
                        invoice.setBillingAddress(billingAddress);
                        invoice.setShippingAddress(shippingAddress);
                        invoice.setShippingMethod(shippingMethod);
                        invoice.setPaymentMethod(paymentMethod);
                        invoice.setInvoiceDate(invoiceDate);
                        return invoice;
                }
            }
           
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la facture : " + e.getMessage());
        }
        return null; // Retourne null si aucune facture n'est trouvée
    }
}
