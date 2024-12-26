package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import products.Product;

public class SearchDAO {
	
	public String buildMultiKeywordQuery(String[] keywords) {
	    StringBuilder query = new StringBuilder(
	        "SELECT p.id FROM produit p " +
	        "LEFT JOIN vetement v ON p.ID = v.Produit_ID " +
	        "LEFT JOIN chaussures c ON p.ID = c.Produit_ID WHERE "
	    );

	    // Ajouter des conditions LIKE pour chaque mot-clé
	    for (int i = 0; i < keywords.length; i++) {
	        if (i > 0) query.append(" AND "); // Ajouter AND entre chaque groupe de conditions
	        query.append("(LOWER(p.Nom) LIKE ? OR LOWER(p.Description) LIKE ? OR LOWER(p.Type) OR LOWER(p.Marque) LIKE ? ");
	        query.append("OR LOWER(v.Couleur) LIKE ? OR LOWER(c.Surface) LIKE ? OR LOWER(c.Couleur) LIKE ?)");
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
	                Product produit = productDAO.getProduitById(rs.getInt("id"));
	                results.add(produit);
	            }
	        }
	    } catch (SQLException e) {
            e.printStackTrace();
	    }
        return results;
	}
}
