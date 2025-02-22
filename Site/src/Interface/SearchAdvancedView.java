package Interface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import database.ProductDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import products.Product;

/**
 * Représente la vue de recherche avancée permettant de rechercher des produits
 * en utilisant différents critères.
 */
public class SearchAdvancedView {
	
	private VBox criteriaContainer;
	
	/**
     * Constructeur pour initialiser la vue de recherche avancée.
     * Cette méthode crée la scène avec tous les éléments nécessaires.
     *
     * @param mainView La vue principale du programme qui contient la scène actuelle.
     */
	public SearchAdvancedView(MainView mainView) {
		AnchorPane rootPane = new AnchorPane();
        HeaderView v = new HeaderView(mainView); 
        
        rootPane.getChildren().addAll(v.getHeader(), createAdvancedSearchSection(mainView));
        
        Scene searchScene = new Scene(rootPane, 1368, 690);
        searchScene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
        mainView.getPrimaryStage().setScene(searchScene);
	}
	
	/**
     * Crée la section de recherche avancée avec les champs nécessaires 
     * pour effectuer la recherche des produits.
     *
     * @param mainView La vue principale contenant la scène.
     * @return VBox Le conteneur de la section de recherche avancée.
     */
	private VBox createAdvancedSearchSection(MainView mainView) {
		
		Label mainLabel = new Label("Recherche Avancée");

        // Conteneur des critères
        criteriaContainer = new VBox(10);
        criteriaContainer.setPadding(new Insets(10));

        // Ajouter le premier critère initial
        addSearchCriterion();

        // Bouton pour ajouter un nouveau critère
        Button addCriterionButton = new Button(" + Ajouter un critère de recherche");
        addCriterionButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
        addCriterionButton.setOnAction(e -> addSearchCriterion());
        
        Button searchButton = new Button("Rechercher");
        searchButton.setOnAction(e -> {
        	List<Product> products = executeSearch();
        	mainView.showProductView(Product.class, products);
        });

        // Mise en page
        VBox main = new VBox(10, mainLabel, criteriaContainer, addCriterionButton, searchButton);
        main.setPadding(new Insets(10));
        main.setMaxSize(700, 400);
        main.setAlignment(Pos.CENTER);
        main.setStyle("-fx-background-color: white");
             
        StackPane centeredPane = new StackPane(main); // Création StackPane pour centrer main dans le ScrollPane
        centeredPane.setAlignment(Pos.CENTER);
        centeredPane.setStyle("-fx-background-color: derive(#ececec,26.4%)");
        
        ScrollPane scrollPane = MainView.createScrollPane(centeredPane);
        scrollPane.setPrefViewportHeight(0);
        
        VBox root = new VBox(20, scrollPane);
        root.setStyle("-fx-background-color: derive(#ececec,26.4%)");
        root.setAlignment(Pos.CENTER); //Centrer verticalement
        root.setPrefSize(1368, 690);
        AnchorPane.setTopAnchor(root, 118.0);
        AnchorPane.setBottomAnchor(root, 0.0);

        return root;
    }

	/**
     * Ajoute un nouveau critère de recherche dans la section des critères.
     * Un critère est composé d'un ComboBox pour le type de critère, d'un champ de texte pour la recherche
     * et d'un bouton pour supprimer ce critère.
     */
    private void addSearchCriterion() {
        // ComboBox pour les critères
        ComboBox<String> criteriaBox = new ComboBox<>();
        criteriaBox.getItems().addAll("Tous critères", "Nom", "Description", "Type", "Marque", "Couleur", "Genre", "Surface");
        criteriaBox.setValue("Tous critères");

        // TextField pour la recherche
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher dans le catalogue ...");
        searchField.setPrefSize(500, 150);

        // ComboBox pour les connecteurs logiques (ET/OU)
        ComboBox<String> logicalConnector = new ComboBox<>();
        logicalConnector.getItems().addAll("AND", "OR");
        logicalConnector.setValue("AND");

        // Bouton pour supprimer un critère
        Button removeButton = new Button("X");
        removeButton.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #007bff; -fx-font-weight: bold;");
        removeButton.setOnAction(e -> criteriaContainer.getChildren().remove(removeButton.getParent()));

        // Mise en page d'une ligne de critère
        HBox criterionRow = new HBox(10, logicalConnector, criteriaBox, searchField, removeButton);
        criterionRow.setPadding(new Insets(5));
        criterionRow.setAlignment(Pos.CENTER);

        // Ajouter la ligne au conteneur principal
        criteriaContainer.getChildren().add(criterionRow);

        // Désactiver le connecteur logique pour le premier critère
        if (criteriaContainer.getChildren().size() == 1) {
            logicalConnector.setDisable(true);
        }
	}
    
    /**
     * Exécute la recherche sur la base de données en fonction des critères sélectionnés et
     * retourne la liste des produits correspondants.
     *
     * @return Liste des produits correspondant à la recherche.
     */
    @SuppressWarnings("unchecked")
	private List<Product> executeSearch() {
        StringBuilder query = new StringBuilder("SELECT product_id FROM vueproduits WHERE ");
        List<String> parameters = new ArrayList<>();
        List<Product> results = new ArrayList<>();

        for (int i = 0; i < criteriaContainer.getChildren().size(); i++) {
            HBox criterionRow = (HBox) criteriaContainer.getChildren().get(i);

            ComboBox<String> logicalConnector = (ComboBox<String>) criterionRow.getChildren().get(0);
            ComboBox<String> criteriaBox = (ComboBox<String>) criterionRow.getChildren().get(1);
            TextField searchField = (TextField) criterionRow.getChildren().get(2);

            String selectedCriteria = criteriaBox.getValue();
            String searchValue = searchField.getText();

            if (searchValue != null && !searchValue.isEmpty()) {
                if (i > 0) {
                    query.append(" ").append(logicalConnector.getValue()).append(" ");
                }

                if ("Tous critères".equals(selectedCriteria)) {
                    query.append("(LOWER(name) LIKE ? OR LOWER(description) LIKE ? OR LOWER(type) LIKE ? OR LOWER(brand) LIKE ? "
                    		+ "OR LOWER(color) LIKE ? OR LOWER(surface) LIKE ? OR LOWER(gender) LIKE ? OR LOWER(TypeVetements) LIKE ?)");
                    
                    for (int j = 0; j < 8; j++) {
                        parameters.add("%" + searchValue.toLowerCase() + "%");
                    }
                } else {
                	// Adapter le critère sélectionné à la colonne correspondante en base de données
                    String columnName = getColumnName(selectedCriteria);
                    query.append(columnName).append(" LIKE ?");
                    parameters.add("%" + searchValue.toLowerCase() + "%");
                }
            }
        }

        //System.out.println("Requête SQL générée : " + query);
        //System.out.println("Paramètres : " + parameters);

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setString(i + 1, parameters.get(i));
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ProductDAO productDAO = new ProductDAO();
                Product product = productDAO.getProductById(resultSet.getInt("Product_id"));
                results.add(product);
            }

        } catch (SQLException e) {
        	MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
        return results;
    }
    
    /**
     * Mappe les noms de critères affichés en français avec les colonnes correspondantes dans la base de données.
     *
     * @param selectedValue Le nom du critère sélectionné par l'utilisateur.
     * @return Le nom de la colonne correspondant à ce critère dans la base de données.
     */
    private String getColumnName(String selectedValue) {
        return switch (selectedValue) {
            case "Nom" -> "name";
            case "Description" -> "description";
            case "Type" -> "type";
            case "Marque" -> "brand";
            case "Couleur" -> "color";
            case "Genre" -> "gender";
            case "Surface" -> "surface";
            default -> "name"; // Valeur par défaut pour éviter les erreurs
        };
    }
}
