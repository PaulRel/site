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

import Interface.MainView;
import customer.Invoice;
import customer.Order;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert.AlertType;
import products.Product;

public class InvoiceDAO {
	
	// RECUPERER TOUTES LES FACTURES
	public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String query = "SELECT * FROM invoice";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            // Parcourir les résultats et créer des objets Produit
            while (rs.next()) {
            	int invoiceId = rs.getInt("invoice_id");
            	int order_id = rs.getInt("order_id");
                String billingAddress = rs.getString("billing_address");
                String shippingAddress = rs.getString("shipping_address");
                String shippingMethod = rs.getString("shipping_method");
                double shippingPrice = rs.getDouble("shipping_price");
                String paymentMethod = rs.getString("payment_method");
                LocalDate invoiceDate = rs.getDate("invoice_date").toLocalDate();
                
                OrderDAO orderDAO = new OrderDAO();
                Invoice invoice = new Invoice(orderDAO.getOrderById(order_id));
                invoice.setInvoiceId(invoiceId);
                invoice.setOrder(orderDAO.getOrderById(order_id));
                invoice.setBillingAddress(billingAddress);
                invoice.setShippingAddress(shippingAddress);
                invoice.setShippingMethod(shippingMethod);
                invoice.setShippingPrice(shippingPrice);
                invoice.setPaymentMethod(paymentMethod);
                invoice.setInvoiceDate(invoiceDate);
                
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }
	
	
	// INSERTION
	public void insertInvoice(Invoice invoice) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			String sql = "INSERT INTO invoice (order_id, billing_address, shipping_address, shipping_method, shipping_price, payment_method, invoice_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
			pstmt.setInt(1, invoice.getOrder().getOrderId());
            pstmt.setString(2, invoice.getBillingAddress());
            pstmt.setString(3, invoice.getShippingAddress());
            pstmt.setString(4, invoice.getShippingMethod());
            pstmt.setDouble(5, invoice.getShippingPrice());
            pstmt.setString(6, invoice.getPaymentMethod());
            pstmt.setDate(7, Date.valueOf(invoice.getInvoiceDate()));
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int invoiceId = rs.getInt(1);
                invoice.setInvoiceId(invoiceId);
            }
        } catch (SQLException e) {
        	MainView.showAlert("Erreur", null, "Une erreur est survenue lors de l'insertion de la facture : " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }
	
	
	// UPDATE
	public void updateInvoice(String billingAddress, String shippingAddress, String shippingMethod, String paymentMethod, int invoiceId) {
		String sql = "UPDATE Invoice SET billing_address = ?, shipping_address = ?, shipping_method = ?, shipping_price = ?, payment_method = ? WHERE invoice_id = ?";     
		double shippingCost = 0.0;
		if (shippingMethod != null) {
		    if (shippingMethod.contains("UPS Domicile")) {
		        shippingCost = 9.00;
		    } else if (shippingMethod.contains("Colissimo mon domicile")) {
		        shippingCost = 4.00;
		    } else if (shippingMethod.contains("Chronopost")) {
		        shippingCost = 15.00;
		    } else if (shippingMethod.contains("Retrait en magasin TennisShop")) {
		        shippingCost = 0.00;
		    }
		}
		
        try(Connection conn = DatabaseConnection.getConnection();
        	PreparedStatement updateStmt = conn.prepareStatement(sql)){
        		updateStmt.setString(1, billingAddress);
                updateStmt.setString(2, shippingAddress);
                updateStmt.setString(3, shippingMethod);
                updateStmt.setDouble(4, shippingCost);
                updateStmt.setString(5, paymentMethod);
                updateStmt.setInt(6, invoiceId);
                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected > 0) {
                	MainView.showAlert("Information Message", null, "Modifier avec succès", AlertType.INFORMATION);                    
                }
          	}catch(Exception e){
          		MainView.showAlert("Erreur", null, "Une erreur est survenue lors de la mise à jour de la facture : " + e.getMessage(), AlertType.ERROR);
          		e.printStackTrace();
          	}
	}
	
	
	// RECUPERATION UNE FACTURE A PARTIR DE SON ID
	public Invoice getInvoiceById(Order order) {
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
                    double shippingPrice = rs.getDouble("shipping_price");
                    String paymentMethod = rs.getString("payment_method");
                    LocalDate invoiceDate = rs.getDate("invoice_date").toLocalDate();
                        
                    Invoice invoice = new Invoice(order);
                    invoice.setInvoiceId(invoiceId);
                    invoice.setBillingAddress(billingAddress);
                    invoice.setShippingAddress(shippingAddress);
                    invoice.setShippingMethod(shippingMethod);
                    invoice.setShippingPrice(shippingPrice);
                    invoice.setPaymentMethod(paymentMethod);
                    invoice.setInvoiceDate(invoiceDate);
                    return invoice;
                }
            }
           
        } catch (SQLException e) {
        	MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
        }
        return null; // Retourne null si aucune facture n'est trouvée
    }
	
	
	// SUPPRESSION
	public void deleteInvoice(Invoice invoice) {
    	String sql = "DELETE FROM Invoice WHERE invoice_id = '"+ invoice.getInvoiceId()+"'";
    	try (Connection conn = DatabaseConnection.getConnection();
    			PreparedStatement statement = conn.prepareStatement(sql)) {
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {MainView.showAlert("Succès", null, "La facture a été supprimée avec succès.", AlertType.INFORMATION);}
    	}catch (SQLException e) {
             MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
    	}
	}	
}
