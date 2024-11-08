package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Products.Produit;

public class ProduitDAO {

    public List<Produit> getAllProduits() {
        List<Produit> produits = new ArrayList<>();
        String query = "SELECT id, Nom, Description, Type, Marque, Prix, Qt_Dispo, image_Path FROM produit";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Parcourir les résultats et créer des objets Produit
            while (resultSet.next()) {
            	int id = resultSet.getInt("id");
                String nom = resultSet.getString("Nom");
                String description = resultSet.getString("Description");
                String Type = resultSet.getString("Type");
                String marque = resultSet.getString("Marque");
                double prix = resultSet.getDouble("Prix");
                int QtDispo = resultSet.getInt("Qt_Dispo");
                String imagePath = resultSet.getString("image_Path");

                Produit produit = new Produit(id, nom, description, Type, marque, prix, QtDispo, imagePath);
                produits.add(produit);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produits;
    }
}