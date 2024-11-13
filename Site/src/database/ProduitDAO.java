package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import products.Chaussures;
import products.Produit;
import products.Vetement;
import products.Vetement.TypeVetement;

public class ProduitDAO {
	private String type, nom, description, marque, imagePath;
	int id, qtDispo;
	double prix;

    public List<Produit> getAllProduits() {
        List<Produit> produits = new ArrayList<>();
        String query = "SELECT id, Nom, Description, Type, Marque, Prix, Qt_Dispo, image_Path FROM produit";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Parcourir les résultats et créer des objets Produit
            while (resultSet.next()) {
            	id = resultSet.getInt("id");
                nom = resultSet.getString("Nom");
                description = resultSet.getString("Description");
                type = resultSet.getString("Type");
                marque = resultSet.getString("Marque");
                prix = resultSet.getDouble("Prix");
                qtDispo = resultSet.getInt("Qt_Dispo");
                imagePath = resultSet.getString("image_Path");

                Produit produit = new Produit(id, nom, description, type, marque, prix, qtDispo, imagePath);
                if (type.equalsIgnoreCase("chaussures")) {
                    produit = getChaussuresDetails();
                } else if (type.equalsIgnoreCase("vetement")) {
                    produit = getVetementsDetails();
                }
                produits.add(produit);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }
    
    public Chaussures getChaussuresDetails(){
    	Chaussures chaussure = null;
    	List<Chaussures> chaussures = new ArrayList<>();
    	String query = "SELECT * FROM Chaussures WHERE produit_id = ?";

    	try (Connection connection = DatabaseConnection.getConnection();
    	    PreparedStatement statement = connection.prepareStatement(query)) {
    	    statement.setInt(1, id);
    	    ResultSet resultSet = statement.executeQuery();
    	    
    	    if (resultSet.next()) {
    	        String surface = resultSet.getString("surface");
    	        String genre = resultSet.getString("genre");
    	        String couleur = resultSet.getString("couleur");
                    
                chaussure = new Chaussures(id, nom, description, type, marque, prix, qtDispo, imagePath, surface, genre, couleur);
                chaussure.setTailleStock(getTaillesStock(id));
                chaussures.add(chaussure);    	        
    	    }

    	} catch (SQLException e) {
    	    e.printStackTrace();
    	}
    	return chaussure;
    }

    
    private HashMap<String, Integer> getTaillesStock(int produitId) throws SQLException {
        HashMap<String, Integer> taillesStock = new HashMap<>();
        String queryTailleStock = "SELECT * FROM Taillestock WHERE Produit_ID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
        	PreparedStatement ps = connection.prepareStatement(queryTailleStock)) {
        	ps.setInt(1, produitId);
        	ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String taille = rs.getString("taille");
                int qtDispo = rs.getInt("qt_dispo");
                taillesStock.put(taille, qtDispo); // Ajouter la taille et quantité dans la HashMap
            }
        }
        return taillesStock;
    }
    
    public Vetement getVetementsDetails(){
    	Vetement vetement = null;
    	List<Vetement> vetements = new ArrayList<>();
    	String query = "SELECT * FROM Vetement WHERE Produit_ID = ?";

    	try (Connection connection = DatabaseConnection.getConnection();
        	    PreparedStatement statement = connection.prepareStatement(query)) {
        	    statement.setInt(1, id);
        	    ResultSet resultSet = statement.executeQuery();
        	    
        	    if (resultSet.next()) {
                     String genre = resultSet.getString("Genre");
                     String couleur = resultSet.getString("Couleur"); 
                     String stringTypeVetement = resultSet.getString("type"); 
                     TypeVetement typeVetement = TypeVetement.valueOf(stringTypeVetement.toUpperCase());
                     
                     vetement = new Vetement(id, nom, description, type, marque, prix, qtDispo, imagePath, typeVetement, genre, couleur);
                     vetement.setTailleStock(getTaillesStock(id));
                     vetements.add(vetement);
                 }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vetement;
    }
}