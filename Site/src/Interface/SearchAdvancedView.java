package Interface;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SearchAdvancedView {
	
	private AnchorPane rootPane;
	private VBox criteriaContainer;
	
	public SearchAdvancedView(MainView mainView) {
		rootPane = new AnchorPane();
        
        Scene accountScene = new Scene(rootPane, 1350, 670);
        createAdvancedSearchSection();
        
        HeaderView v = new HeaderView(mainView);      
        rootPane.getChildren().addAll(v.getHeader());
        String css = this.getClass().getResource("/style.css").toExternalForm();        
        accountScene.getStylesheets().add(css);
        mainView.getPrimaryStage().setScene(accountScene);	
	}
	
	private void createAdvancedSearchSection() {
		
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
        searchButton.setOnAction(e -> addSearchCriterion());

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
        
        VBox root = new VBox(20, scrollPane);
        AnchorPane.setTopAnchor(root, 116.0);
        root.setPrefSize(1350, 550);
        root.setStyle("-fx-background-color: derive(#ececec,26.4%)");
        root.setAlignment(Pos.CENTER); //Centrer verticalement
        rootPane.getChildren().addAll(root);
    }

    private void addSearchCriterion() {
        // ComboBox pour les critères
        ComboBox<String> criteriaBox = new ComboBox<>();
        criteriaBox.getItems().addAll("Tous critères", "Nom", "Description", "Type", "Marque", "Couleur", "Genre");
        criteriaBox.setValue("Tous critères");

        // TextField pour la recherche
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher dans le catalogue ...");
        searchField.setPrefSize(500, 150);

        // ComboBox pour les connecteurs logiques (ET/OU)
        ComboBox<String> logicalConnector = new ComboBox<>();
        logicalConnector.getItems().addAll("ET", "OU");
        logicalConnector.setValue("ET");

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
}
