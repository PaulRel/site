package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Interface.MainView;
import customer.Customer;
import customer.Customer.Civility;
import customer.Customer.Role;
import javafx.scene.control.Alert.AlertType;

public class CustomerDAO {
	
	// RECUPERATION DE TOUS LES CLIENTS

	public List<Customer> getAllCustomers() {
	    List<Customer> customers = new ArrayList<>();
	    String query = "SELECT * FROM customer";

	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement statement = connection.prepareStatement(query);
	         ResultSet resultSet = statement.executeQuery()) {

	        // Parcourir les résultats et créer des objets Customer
	        while (resultSet.next()) {
	            int customerID = resultSet.getInt("CustomerID");
	            String firstName = resultSet.getString("FirstName");
	            String lastName = resultSet.getString("LastName");
	            Civility civility = Civility.valueOf(resultSet.getString("Civility"));
	            String email = resultSet.getString("Email");
	            String phoneNumber = resultSet.getString("PhoneNumber");
	            String password = resultSet.getString("Password");
	            Role role = Role.valueOf(resultSet.getString("Role").toUpperCase());
	            String address = resultSet.getString("Address");

	            // Créer un objet Customer et l'ajouter à la liste
	            Customer customer = new Customer(customerID, firstName, lastName, civility, email, phoneNumber, password, role, address);
	            customers.add(customer);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return customers;
	}

	
    // INSERTION
	
	public void signUpCustomer(Customer customer) {
        String query = "INSERT INTO Customer (FirstName, LastName, Civility, Email, Password, Address) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Remplace les paramètres de la requête par les valeurs de l'objet Customer
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getCivility().name());
            statement.setString(4, customer.getEmail());            
            statement.setString(5, customer.getPassword());
            statement.setString(6, customer.getAddress());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Le client a été ajouté avec succès !");

                // Récupérer l'ID généré automatiquement
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1); // Récupérer l'ID généré
                        customer.setId(generatedId); // Assigner l'ID généré à l'objet Customer
                        System.out.println("ID du nouveau client : " + generatedId);
                    } else {
                        throw new SQLException("Échec de récupération de l'ID généré.");
                    }
                }
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
    }
	
	
	// SUPPRESSION
	
	public void deleteCustomer(Customer customer) {
		String query = "DELETE FROM customer WHERE CustomerID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
        	PreparedStatement statement = conn.prepareStatement(query)) {

            // On définit l'ID du client à supprimer dans la requête
            statement.setInt(1, customer.getId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                MainView.showAlert("Succès", null, "Le compte a été supprimé avec succès.", AlertType.INFORMATION);
            }
        }
        catch (SQLException e) {
            MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }
	
	
	// UPDATE
	
	public void updateCustomer(Customer customer) {
		try (Connection conn = DatabaseConnection.getConnection()){
	        String updateQuery = "UPDATE Customer SET FirstName = ?, LastName = ?, Email = ?, PhoneNumber = ?, Address = ? WHERE CustomerID = ?";
	        PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
	        updateStmt.setString(1, customer.getFirstName());
	        updateStmt.setString(2, customer.getLastName());
	        updateStmt.setString(3, customer.getEmail());
	        updateStmt.setString(4, customer.getPhoneNumber());
	        updateStmt.setString(5, customer.getAddress());
	        updateStmt.setInt(6, customer.getId());
	        int rowsAffected = updateStmt.executeUpdate();

	        if (rowsAffected > 0) {
	        	MainView.showAlert("Succès", null, "Vos informations ont été mis à jour avec succès.", AlertType.INFORMATION);
	        }    	        
	    } catch (SQLException e) {
	    	MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
	        e.printStackTrace();
	    }
	}
	
	
	// RECUPERATION D'UN CLIENT A PARTIR DE SON ADRESSSE MAIL

	public Customer authenticate(String email, String password) {
        String query = "SELECT * FROM Customer WHERE Email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)) {       	
        	statement.setString(1, email); // Paramètre l'email dans la requête       	
        	ResultSet rs = statement.executeQuery();
                       
        	// Check if a user exists with this email
            while (rs.next()) {
            	String storedPassword = rs.getString("Password");
                if (password.equals(storedPassword))  {
                	 // Create and return a Customer object
                	int id = rs.getInt("CustomerID");
                    String firstName = rs.getString("FirstName");
                    String lastName = rs.getString("LastName");
                    String phoneNumber = rs.getString("PhoneNumber");
                    String address = rs.getString("Address");
                    String civilityString = rs.getString("Civility");
                    String roleString = rs.getString("Role");
                    
                    // Map civility and role enums
                    Customer.Civility civility = Customer.Civility.valueOf(civilityString);
                    Customer.Role role = Customer.Role.valueOf(roleString.toUpperCase());

                    return new Customer(id, firstName, lastName, civility, email, phoneNumber, storedPassword, role, address);
                }
            }
        } catch (SQLException e) {
        	MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
        	e.printStackTrace();
        }
        
        return null; // Authentification échouée
    }
	
	
	// RECUPERATION  D'UN CLIENT A PARTIR DE SON ID
	
	public Customer getCustomerById(int id) {
        String query = "SELECT * FROM Customer WHERE CustomerID = ?";
        Customer customer = null;  // Initialiser à null pour l'instant, si aucun produit n'est trouvé

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                	String firstName = resultSet.getString("FirstName");
                	String lastName = resultSet.getString("LastName");
                	Civility civility = Civility.valueOf(resultSet.getString("Civility"));
                	
                	String email = resultSet.getString("Email");
                	String phoneNumber = resultSet.getString("PhoneNumber");

                	String password = resultSet.getString("Password");
                	Role role = Role.valueOf(resultSet.getString("Role").toUpperCase());
                	String address = resultSet.getString("Address");

                    customer = new Customer(id, firstName, lastName, civility, email, phoneNumber, password, role, address);       
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }
}