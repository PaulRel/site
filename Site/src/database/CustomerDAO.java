package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import customer.Customer;

public class CustomerDAO {
	public void signUpCustomer(Customer customer) {
        String query = "INSERT INTO Customer (FirstName, LastName, Civility, Email, Passwords, Address) VALUES (?, ?, ?, ?, ?, ?)";

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
}