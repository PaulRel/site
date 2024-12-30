package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Interface.MainView;
import javafx.scene.control.Alert.AlertType;
import products.Chaussures;
import products.Product;
import products.Vetement;
import products.Vetement.TypeVetement;

public class ProductDAO {
	private String type, nom, description, marque, imagePath;
	int qtDispo;
	double prix;

    public List<Product> getAllProduits() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT id, Nom, Description, Type, Marque, Prix, Qt_Dispo, image_Path FROM produit";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Parcourir les résultats et créer des objets Produit
            while (resultSet.next()) {
            	int id = resultSet.getInt("id");
                nom = resultSet.getString("Nom");
                description = resultSet.getString("Description");
                type = resultSet.getString("Type");
                marque = resultSet.getString("Marque");
                prix = resultSet.getDouble("Prix");
                qtDispo = resultSet.getInt("Qt_Dispo");
                imagePath = resultSet.getString("image_Path");

                Product product = new Product(id, nom, description, type, marque, prix, qtDispo, imagePath);
                if (type.equalsIgnoreCase("chaussures")) {
                    product = getChaussuresDetails(id);
                } else if (type.equalsIgnoreCase("vetement")) {
                    product = getVetementsDetails(id);
                }
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    
    public Chaussures getChaussuresDetails(int id){
    	Chaussures chaussure = null;
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
    	    }

    	} catch (SQLException e) {
    	    e.printStackTrace();
    	}
    	return chaussure;
    }

    
    private HashMap<String, Integer> getTaillesStock(int produitId) throws SQLException {
        HashMap<String, Integer> taillesStock = new HashMap<>();
        String queryTailleStock = "SELECT * FROM Taillestock WHERE produit_id = ?";

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
    
    public Vetement getVetementsDetails(int id){
    	Vetement vetement = null;
    	String query = "SELECT * FROM Vetement WHERE produit_id = ?";

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
                 }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vetement;
    }
    
    public Product getProduitById(int id) {
        String query = "SELECT id, Nom, Description, Type, Marque, Prix, Qt_Dispo, image_Path FROM produit WHERE id = ?";
        Product product = null;  // Initialiser à null pour l'instant, si aucun produit n'est trouvé

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    nom = resultSet.getString("Nom");
                    description = resultSet.getString("Description");
                    type = resultSet.getString("Type");
                    marque = resultSet.getString("Marque");
                    prix = resultSet.getDouble("Prix");
                    qtDispo = resultSet.getInt("Qt_Dispo");
                    imagePath = resultSet.getString("image_Path");

                    product = new Product(id, nom, description, type, marque, prix, qtDispo, imagePath);
                    if (type.equalsIgnoreCase("chaussures")) {
                        product = getChaussuresDetails(id);
                    } else if (type.equalsIgnoreCase("vetement")) {
                        product = getVetementsDetails(id);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }
    
    public void insertProduct(Product product) {
    	String query = "INSERT INTO Produit (Nom, Description, Type, Marque, Prix, Qt_dispo, image_path) VALUES (?, ?, ?, ?, ?, ?, ?)";
    	try (Connection conn = DatabaseConnection.getConnection();
               PreparedStatement statement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

               // Remplace les paramètres de la requête par les valeurs de l'objet Customer
               statement.setString(1, product.getName());
               statement.setString(2, product.getDescription());
               statement.setString(3, product.getType());
               statement.setString(4, product.getBrand());            
               statement.setDouble(5, product.getPrice());
               statement.setInt(6, product.getQtDispo());
               statement.setString(7, product.getImagePath());

               int rowsInserted = statement.executeUpdate();
               if (rowsInserted > 0) {
                   System.out.println("Le produit a été ajouté avec succès !");

                   // Récupérer l'ID généré automatiquement
                   try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                       if (generatedKeys.next()) {
                           int generatedId = generatedKeys.getInt(1); // Récupérer l'ID généré
                           product.setId(generatedId); // Assigner l'ID généré à l'objet Product
                           System.out.println("ID du nouveau produit : " + generatedId);
                       } else {
                           throw new SQLException("Échec de récupération de l'ID généré.");
                       }
                   }
               }
           } catch (SQLException e) {
           	e.printStackTrace();
           }
    }
    
    public void deleteProduct(int id) {
    	String sql = "DELETE FROM Produit WHERE id = '"+ id+"'";
    	try (Connection conn = DatabaseConnection.getConnection();
    			PreparedStatement statement = conn.prepareStatement(sql)) {
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {MainView.showAlert("Succès", null, "Votre produit a été supprimé avec succès.", AlertType.INFORMATION);}
    	}catch (SQLException e) {
             MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
    	}
    }
}