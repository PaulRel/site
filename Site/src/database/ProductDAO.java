package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Interface.MainView;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert.AlertType;
import products.Chaussures;
import products.Product;
import products.Vetement;
import products.Vetement.TypeVetement;

public class ProductDAO {
	private String type, name, description, brand, imagePath;
	double price;
	
	// RECUPERATION DE TOUS LES PRODUITS

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT id, name, Description, Type, brand, price, image_Path FROM product";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Parcourir les résultats et créer des objets Produit
            while (resultSet.next()) {
            	int id = resultSet.getInt("id");
                name = resultSet.getString("name");
                description = resultSet.getString("Description");
                type = resultSet.getString("Type");
                brand = resultSet.getString("brand");
                price = resultSet.getDouble("price");
                imagePath = resultSet.getString("image_Path");

                Product product = new Product(id, name, description, type, brand, price, imagePath);
                if (type.equalsIgnoreCase("chaussures")) {
                    product = getShoesDetails(id);
                } else if (type.equalsIgnoreCase("vetement")) {
                    product = getVetementsDetails(id);
                }
                products.add(product);
            }

        } catch (SQLException e) {
        	MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
        return products;
    }
    
    public Chaussures getShoesDetails(int id){
    	Chaussures shoes = null;
    	String query = "SELECT * FROM Shoes WHERE product_id = ?";

    	try (Connection connection = DatabaseConnection.getConnection();
    	    PreparedStatement statement = connection.prepareStatement(query)) {
    	    statement.setInt(1, id);
    	    ResultSet resultSet = statement.executeQuery();
    	    
    	    if (resultSet.next()) {
    	        String surface = resultSet.getString("surface");
    	        String gender = resultSet.getString("gender");
    	        String color = resultSet.getString("color");
                shoes = new Chaussures(id, name, description, type, brand, price, imagePath, surface, gender, color);
                shoes.setSizeStock(getSizeStock(id));
    	    }

    	} catch (SQLException e) {
    		MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
    	    e.printStackTrace();
    	}
    	return shoes;
    }

    
    private HashMap<String, Integer> getSizeStock(int productId) throws SQLException {
        HashMap<String, Integer> sizeStock = new HashMap<>();
        String querySizeStock = "SELECT * FROM size_stock WHERE product_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
        	PreparedStatement ps = connection.prepareStatement(querySizeStock)) {
        	ps.setInt(1, productId);
        	ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String size = rs.getString("size");
                int qtDispo = rs.getInt("stock");
                sizeStock.put(size, qtDispo); // Ajouter la taille et quantité dans la HashMap
            }
        }
        return sizeStock;
    }
    
    public Vetement getVetementsDetails(int id){
    	Vetement vetement = null;
    	String query = "SELECT * FROM Clothing WHERE product_id = ?";

    	try (Connection connection = DatabaseConnection.getConnection();
        	    PreparedStatement statement = connection.prepareStatement(query)) {
        	    statement.setInt(1, id);
        	    ResultSet resultSet = statement.executeQuery();
        	    
        	    if (resultSet.next()) {
                     String gender = resultSet.getString("gender");
                     String color = resultSet.getString("color"); 
                     String stringTypeVetement = resultSet.getString("type"); 
                     TypeVetement typeVetement = TypeVetement.valueOf(stringTypeVetement.toUpperCase());
                     
                     vetement = new Vetement(id, name, description, type, brand, price, imagePath, typeVetement, gender, color);
                     vetement.setSizeStock(getSizeStock(id));
                 }

        } catch (SQLException e) {
        	MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
        return vetement;
    }
    
    public Product getProductById(int id) {
        String query = "SELECT id, name, Description, Type, brand, price, image_Path FROM product WHERE id = ?";
        Product product = null;  // Initialiser à null pour l'instant, si aucun produit n'est trouvé

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    name = resultSet.getString("name");
                    description = resultSet.getString("Description");
                    type = resultSet.getString("Type");
                    brand = resultSet.getString("brand");
                    price = resultSet.getDouble("price");
                    imagePath = resultSet.getString("image_Path");

                    product = new Product(id, name, description, type, brand, price, imagePath);
                    if (type.equalsIgnoreCase("Shoes")) {
                        product = getShoesDetails(id);
                    } else if (type.equalsIgnoreCase("vetement")) {
                        product = getVetementsDetails(id);
                    }
                }
            }
        } catch (SQLException e) {
        	MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
        return product;
    }
    
    
    // INSERTION
    
    public int insertProduct(Product product) {
    	String query = "INSERT INTO product (name, Description, Type, brand, price, image_path) VALUES (?, ?, ?, ?, ?, ?)";
    	try (Connection conn = DatabaseConnection.getConnection();
               PreparedStatement statement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

               // Remplace les paramètres de la requête par les valeurs de l'objet Customer
               statement.setString(1, product.getName());
               statement.setString(2, product.getDescription());
               statement.setString(3, product.getType());
               statement.setString(4, product.getBrand());            
               statement.setDouble(5, product.getPrice());
               statement.setString(6, product.getImagePath());

               int rowsInserted = statement.executeUpdate();
               if (rowsInserted > 0) {
                   // Récupérer l'ID généré automatiquement
                   try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                       if (generatedKeys.next()) {
                           int generatedId = generatedKeys.getInt(1); // Récupérer l'ID généré
                           product.setId(generatedId); // Assigner l'ID généré à l'objet Product
                           return generatedId;
                       } else {
                           throw new SQLException("Échec de récupération de l'ID généré.");
                       }
                   }
               }
           } catch (SQLException e) {
        	   MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
        	   e.printStackTrace();
           }
    	return 0;
    }
    
    public void insertShoes(int id, String surface, String gender, String color, String size, int qt) {
    	String query = "INSERT INTO Shoes (product_id, surface, gender, color) VALUES (?, ?, ?, ?)";
    	try (Connection conn = DatabaseConnection.getConnection();
    			PreparedStatement pstmt = conn.prepareStatement(query)) {
    		pstmt.setInt(1, id);
            pstmt.setString(2, surface);
            pstmt.setString(3, gender);
            pstmt.setString(4, color);

            // Exécuter la requête
            pstmt.executeUpdate();
            
            insertSizeStock(id, size, qt);
            
            MainView.showAlert("Information Message", "ID : "+ id, "Chaussures ajoutées avec succès", AlertType.INFORMATION);
        } catch (SQLException e) {
        	MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
    	}
    }
    
    
    public void insertVetement(int id, String type, String gender, String color, String size, int qt) {
    	String query = "INSERT INTO Clothing (product_id, type, gender, color) VALUES (?, ?, ?, ?)";
    	try (Connection conn = DatabaseConnection.getConnection();
    			PreparedStatement pstmt = conn.prepareStatement(query)) {
    		pstmt.setInt(1, id);
            pstmt.setString(2, type);
            pstmt.setString(3, gender);
            pstmt.setString(4, color);

            // Exécuter la requête
            pstmt.executeUpdate();
            
            insertSizeStock(id, size, qt);
            
            MainView.showAlert("Information Message", null, "Vetement ajouté avec succès", AlertType.INFORMATION);
        } catch (SQLException e) {
        	MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
    	}
    }
    
    private void insertSizeStock(int id, String size, int qt) {
    	String query = "INSERT INTO size_stock (product_id, size, qt_dispo) VALUES (?, ?, ?)";
    	try (Connection conn = DatabaseConnection.getConnection();
    			PreparedStatement pstmt = conn.prepareStatement(query)) {
    		pstmt.setInt(1, id);
            pstmt.setString(2, size);
            pstmt.setInt(3, qt);

            // Exécuter la requête
            pstmt.executeUpdate();
        } catch (SQLException e) {
        	MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
    	}
    }
    
    // UPDATE
    public void updateProduct(Product product) {
    	String sql = "UPDATE product SET name = ?, Description = ?, Type = ?, brand = ?, price = ?, image_path = ? WHERE id = ?";
        
        try(Connection conn = DatabaseConnection.getConnection();
        	PreparedStatement updateStmt = conn.prepareStatement(sql)){
        		updateStmt.setString(1, product.getName());
                updateStmt.setString(2, product.getDescription());
                updateStmt.setString(3, product.getType());
                updateStmt.setString(4, product.getBrand());
                updateStmt.setDouble(5, product.getPrice());
                updateStmt.setString(6, product.getImagePath());
                updateStmt.setInt(7, product.getId());
                updateStmt.executeUpdate();
                //if (rowsAffected > 0) {
                	//MainView.showAlert("Information Message", null, "Modifier avec succès", AlertType.INFORMATION);                    
                //}
          	}catch(Exception e){e.printStackTrace(); MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);}
    }
    
    public void updateShoes(int id, String surface, String gender, String color, String size, int qt) {
    	String sql = "UPDATE Shoes SET Surface = ?, gender = ?, color = ? WHERE product_ID = ?";
        
        try(Connection conn = DatabaseConnection.getConnection();
        	PreparedStatement updateStmt = conn.prepareStatement(sql)){
        		updateStmt.setString(1, surface);
        		updateStmt.setString(2, gender);
        		updateStmt.setString(3, color);
        		updateStmt.setInt(4, id);
                int rowsAffected = updateStmt.executeUpdate();
                updateSizeStock(id, size, qt);
                if (rowsAffected > 0) {
                	MainView.showAlert("Information Message", null, "Modifier avec succès", AlertType.INFORMATION);                    
                }
          	}catch(Exception e){e.printStackTrace(); MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);}
    }
    
    public void updateVetement(int id, String type, String gender, String color, String size, int qt) {
    	String sql = "UPDATE Clothing SET Type = ?, gender = ?, color = ? WHERE product_ID = ?";
        
        try(Connection conn = DatabaseConnection.getConnection();
        	PreparedStatement updateStmt = conn.prepareStatement(sql)){
        		updateStmt.setString(1, type);
        		updateStmt.setString(2, gender);
        		updateStmt.setString(3, color);
        		updateStmt.setInt(4, id);
                int rowsAffected = updateStmt.executeUpdate();
                updateSizeStock(id, size, qt);
                if (rowsAffected > 0) {
                	MainView.showAlert("Information Message", null, "Modifier avec succès", AlertType.INFORMATION);                   
                }
          	}catch(Exception e){e.printStackTrace(); MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);}
    }
    
    private void updateSizeStock(int id, String size, int qt) {
    	String sql = "UPDATE size_stock SET qt_dispo = ? WHERE product_ID = ? AND size = ?";
    	try (Connection conn = DatabaseConnection.getConnection();
    			PreparedStatement updateStmt = conn.prepareStatement(sql)) {		
    		updateStmt.setInt(1, qt);
    		updateStmt.setInt(2, id);
    		updateStmt.setString(3, size);

            // Exécuter la requête
    		updateStmt.executeUpdate();
    		//MainView.showAlert("Information Message", null, "Modifier avec succès", AlertType.INFORMATION);
        } catch (SQLException e) {
        	MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
    	}
    }
    
    
    
    // SUPPRESSION
    
    public void deleteProduct(int id) {
    	String sql = "DELETE FROM product WHERE id = '"+ id+"'";
    	try (Connection conn = DatabaseConnection.getConnection();
    			PreparedStatement statement = conn.prepareStatement(sql)) {
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {MainView.showAlert("Succès", null, "Votre produit a été supprimé avec succès.", AlertType.INFORMATION);}
    	}catch (SQLException e) {
             MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
             e.printStackTrace();
    	}
    }
}