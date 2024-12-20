package Interface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import customer.CartItem;
import customer.Order;
import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import products.Produit;

public class AccountView {
	private AnchorPane rootPane;
	private VBox mainContent;

    public AccountView(MainView mainView) {        
        rootPane = new AnchorPane();
        createLeftMenu(mainView);
        createMainSection();
        
        Scene accountScene = new Scene(rootPane, 1350, 670);
        
        HeaderView v = new HeaderView(mainView);      
        rootPane.getChildren().addAll(v.getHeader());
        String css = this.getClass().getResource("/style.css").toExternalForm();        
        accountScene.getStylesheets().add(css);
        mainView.getPrimaryStage().setScene(accountScene);
    }
    
    public void createLeftMenu(MainView mainView) {

        // Menu de gauche
        VBox menuBox = new VBox(15);
        AnchorPane.setTopAnchor(menuBox, 116.0);
        menuBox.setPadding(new Insets(20));
        menuBox.setStyle("-fx-background-color: #F8F8F8;");

        // Section utilisateur
        VBox userBox = new VBox(10);
        userBox.setAlignment(Pos.CENTER);
        userBox.setPadding(new Insets(10));
        ImageView profileIcon = new ImageView("https://via.placeholder.com/100"); // Placeholder pour l'image de profil
        profileIcon.setFitWidth(60);
        profileIcon.setFitHeight(60);
        Label userNameLabel = new Label("Bonjour,\n" + MainView.getCurrentCustomer().getFirstName() + " " + MainView.getCurrentCustomer().getLastName());
        userNameLabel.setStyle("-fx-font-weight: bold; -fx-text-align: center;");   
        Button logoutButton = new Button("Déconnexion");
        logoutButton.setOnAction(e -> {
        	MainView.setCurrentCustomer(null);
        	mainView.showProductView(Produit.class);
        });
        //logoutButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000; -fx-padding: 5 10;");
        userBox.getChildren().addAll(profileIcon, userNameLabel, logoutButton);

        // Liens du menu
        Button dashboardButton = new Button("Mon Tableau de bord");
        Button accountInfoButton = new Button("Mes informations");
        Button ordersButton = new Button("Mes commandes");
        Button deleteAccountButton = new Button("Supprimer le compte");
        
        dashboardButton.setOnAction(e -> showDashboard());
        accountInfoButton.setOnAction(e -> editCustomerInfo());
        ordersButton.setOnAction(e -> showCustomerOrders());
        deleteAccountButton.setOnAction(e -> {deleteAccount(); mainView.showProductView(Produit.class);});
        
        deleteAccountButton.setStyle("-fx-background-color: white; -fx-text-fill: red; -fx-border-color: red; -fx-border-width: 2");
        
        // Appliquer un style aux boutons
        for (Button button : new Button[]{dashboardButton, accountInfoButton, ordersButton, deleteAccountButton}) {
         //   button.setStyle("-fx-background-color: transparent; -fx-font-size: 14px; -fx-alignment: CENTER_LEFT; -fx-padding: 10;");
        	  button.setPrefWidth(220.0);
        }

        menuBox.getChildren().addAll(userBox, dashboardButton, accountInfoButton, ordersButton, deleteAccountButton);
        rootPane.getChildren().add(menuBox);
    }
    
    public void createMainSection() {
        mainContent = new VBox(15);
        mainContent.setMaxSize(900, 800);
        mainContent.setSpacing(10);
        mainContent.setPadding(new Insets(20));
        mainContent.setStyle("-fx-background-color: #FFFFFF;");
        
        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Pas de scroll horizontal
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Scroll vertical au besoin
        scrollPane.setPadding(new Insets(20));

        showDashboard();

        AnchorPane.setLeftAnchor(scrollPane, 280.0);
        AnchorPane.setTopAnchor(scrollPane, 116.0);
        AnchorPane.setRightAnchor(scrollPane, 10.0); // S'assurer qu'il occupe tout l'espace horizontal
        AnchorPane.setBottomAnchor(scrollPane, 10.0); // S'assurer qu'il occupe tout l'espace vertical
        rootPane.getChildren().add(scrollPane);
    }
    
    private void showDashboard() {
    	Label dashboardTitle = new Label("Mon tableau de bord");
        dashboardTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Label dashboardDesc = new Label("Depuis le tableau de bord 'Mon compte', vous pouvez avoir un aperçu de vos récentes activités et mettre à jour les informations de votre compte. Sélectionnez un lien ci-dessous pour voir ou modifier les informations.");
        dashboardDesc.setWrapText(true);
        dashboardDesc.setStyle("-fx-font-size: 12px;");
        
        Label recentOrdersTitle = new Label ("Commandes récentes");
        recentOrdersTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        Hyperlink hyperlink1 = new Hyperlink("Voir tout");
        hyperlink1.setOnAction(event -> showCustomerOrders());
    	HBox tableHeader = new HBox(10, recentOrdersTitle, hyperlink1);
        tableHeader.setAlignment(Pos.CENTER_LEFT);
        
        TableView<String> tableSection = createOrdersTable();
        
        Label infoTitle = new Label ("Informations du compte");
        infoTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Hyperlink hyperlink = new Hyperlink("Modifier");
        hyperlink.setOnAction(event -> editCustomerInfo());
        HBox infoHeader = new HBox(10, infoTitle, hyperlink);
        infoHeader.setAlignment(Pos.CENTER_LEFT);
        
        Label nameLabel = new Label("Nom : " + MainView.getCurrentCustomer().getFirstName() + " "+ MainView.getCurrentCustomer().getLastName());        
        Label emailLabel = new Label("Email : " + MainView.getCurrentCustomer().getEmail());        
        Label addressLabel = new Label("Adresse : " + MainView.getCurrentCustomer().getAddress());        
        Label phoneLabel = new Label("Numéro de téléphone : " + MainView.getCurrentCustomer().getPhoneNumber());
        
        nameLabel.setStyle("-fx-font-size: 12px;");
        emailLabel.setStyle("-fx-font-size: 12px;");
        addressLabel.setStyle("-fx-font-size: 12px;");
        phoneLabel.setStyle("-fx-font-size: 12px;");
        
        VBox clientInfoBox = new VBox(1, nameLabel, emailLabel, addressLabel, phoneLabel);
        
        mainContent.getChildren().setAll(dashboardTitle, dashboardDesc, tableHeader, tableSection, infoHeader, clientInfoBox);    	
    }
    
    private void editCustomerInfo() {
    	Label editLabel = new Label("Editer les informations du compte");
    	TextField lastNameField = new TextField(MainView.getCurrentCustomer().getLastName());
    	TextField firstNameField = new TextField(MainView.getCurrentCustomer().getFirstName());
    	TextField emailField = new TextField(MainView.getCurrentCustomer().getEmail());
    	TextField phoneField = new TextField(MainView.getCurrentCustomer().getPhoneNumber());
    	TextField addressField = new TextField(MainView.getCurrentCustomer().getAddress());
    	
    	lastNameField.setPromptText("Nom");
    	firstNameField.setPromptText("Prénom");
    	emailField.setPromptText("Email");
    	phoneField.setPromptText("Téléphone");
    	addressField.setPromptText("Adresse");

    	Button saveButton = new Button("Enregistrer");
    	
    	saveButton.setOnAction(event -> {
    	    String newLastName = lastNameField.getText();
    	    String newFirstName = firstNameField.getText();
    	    String newEmail = emailField.getText();
    	    String newPhone = phoneField.getText();
    	    String newAddress = addressField.getText();

    	    try (Connection conn = DatabaseConnection.getConnection()){
    	        String updateQuery = "UPDATE Customer SET FirstName = ?, LastName = ?, Email = ?, PhoneNumber = ?, Address = ? WHERE CustomerID = ?";
    	        PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
    	        updateStmt.setString(1, newFirstName);
    	        updateStmt.setString(2, newLastName);
    	        updateStmt.setString(3, newEmail);
    	        updateStmt.setString(4, newPhone);
    	        updateStmt.setString(5, newAddress);
    	        updateStmt.setInt(6, MainView.getCurrentCustomer().getId());
    	        int rowsAffected = updateStmt.executeUpdate();

    	        if (rowsAffected > 0) {
    	        	MainView.getCurrentCustomer().setLastName(newLastName);
    	        	MainView.getCurrentCustomer().setFirstName(newFirstName);
    	        	MainView.getCurrentCustomer().setEmail(newEmail);
    	        	MainView.getCurrentCustomer().setPhoneNumber(newPhone);
    	        	MainView.getCurrentCustomer().setAddress(newAddress);
    	        	
    	        	MainView.showAlert("Succès", null, "Vos informations ont été mis à jour avec succès.", AlertType.INFORMATION);
    	        }
    	        
    	    } catch (SQLException e) {
    	    	MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
    	        e.printStackTrace();
    	    }
    	});
    	
    	mainContent.getChildren().setAll(editLabel, lastNameField, firstNameField, emailField, phoneField, addressField, saveButton); 
    }
    
    private void showCustomerOrders() {
    	Label ordersTitle = new Label ("Toutes mes commandes");
    	
    	Label subtitle = new Label("L'ensemble de vos commandes sont affichées dans le tableau ci-dessous. Vous pouvez suivre leurs états d'avancement, les visualiser, télécharger vos factures.");
        subtitle.setWrapText(true);
        subtitle.setStyle("-fx-font-size: 12px;");
        
        TableView<String> tableSection = createOrdersTable();
        
        mainContent.getChildren().setAll(ordersTitle, subtitle, tableSection);
    }
    
    private TableView<String> createOrdersTable() {
    	
    	// Table des commandes récentes
        TableView<String> ordersTable = new TableView<>();
        ordersTable.setMinHeight(200);
        ordersTable.setMaxHeight(300);
        ordersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ordersTable.setId("ordersTable");

        TableColumn<String, String> orderNumberCol = new TableColumn<>("Commande n°");
        TableColumn<String, String> dateCol = new TableColumn<>("Date");
        TableColumn<String, String> shippingToCol = new TableColumn<>("Expédition à");
        TableColumn<String, String> totalCol = new TableColumn<>("Total de la commande");
        TableColumn<String, String> statusCol = new TableColumn<>("Statut");
        TableColumn<String, String> actionCol = new TableColumn<>("Actions");

        ordersTable.getColumns().add(orderNumberCol);
        ordersTable.getColumns().add(dateCol);
        ordersTable.getColumns().add(shippingToCol);
        ordersTable.getColumns().add(totalCol);
        ordersTable.getColumns().add(statusCol);
        ordersTable.getColumns().add(actionCol);

        // Placeholder pour la table
        ordersTable.setPlaceholder(new Label("Aucune commande."));
        
        

        return ordersTable;
    }
    
    public void deleteAccount() {
    	String query = "DELETE FROM customer WHERE CustomerID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
        	PreparedStatement statement = conn.prepareStatement(query)) {

            // On définit l'ID du client à supprimer dans la requête
            statement.setInt(1, MainView.getCurrentCustomer().getId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                MainView.showAlert("Succès", null, "Votre compte a été supprimé avec succès.", AlertType.INFORMATION);
                MainView.setCurrentCustomer(null); // On réinitialise l'instance après suppression
            }
            }
        catch (SQLException e) {
            MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }
}
