package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Interface.MainView;
import javafx.scene.control.Alert.AlertType;
import products.Product;

public class SearchDAO {
	
	public String buildMultiKeywordQuery(String[] keywords) {
	    StringBuilder query = new StringBuilder(
	        "SELECT p.id FROM Product p " +
	        "LEFT JOIN CLothing v ON p.ID = v.product_ID " +
	        "LEFT JOIN Shoes c ON p.ID = c.product_ID WHERE "
	    );

	    // Ajouter des conditions LIKE pour chaque mot-clé
	    for (int i = 0; i < keywords.length; i++) {
	        if (i > 0) query.append(" AND "); // Ajouter AND entre chaque groupe de conditions
	        query.append("(LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ? OR LOWER(p.type) OR LOWER(p.brand) LIKE ? ");
	        query.append("OR LOWER(v.color) LIKE ? OR LOWER(c.surface) LIKE ? OR LOWER(c.color) LIKE ?)");
	    }
	    return query.toString();
	}
	
	public List<Product> search(String input) {
	    String[] words = input.split("\\s+");
	    String sql = buildMultiKeywordQuery(words);
        List<Product> results = new ArrayList<>();

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        // Remplir les paramètres dynamiquement pour chaque mot-clé
	        int index = 1;
	        for (String word : words) {
	            String searchTerm = "%" + word.toLowerCase() + "%";
	            for (int i = 0; i < 6; i++) { // 6 colonnes concernées par mot-clé
	                stmt.setString(index++, searchTerm);
	            }
	        }
	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	            	ProductDAO productDAO = new ProductDAO();
	                Product product = productDAO.getProductById(rs.getInt("id"));
	                results.add(product);
	            }
	        }
	    } catch (SQLException e) {
	    	MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
	    }
        return results;
	}
}
