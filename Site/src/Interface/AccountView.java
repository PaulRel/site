package Interface;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AccountView {
	private AnchorPane rootPane;

    public AccountView(MainView mainView, Stage primaryStage) {        
        rootPane = new AnchorPane();
        createLeftMenu();
        createMainSection();
        
        Scene accountScene = new Scene(rootPane, 1350, 670);
        
        HeaderView v=new HeaderView(mainView, primaryStage);      
        rootPane.getChildren().addAll(v.getHeader());
        String css = this.getClass().getResource("/style.css").toExternalForm();        
        accountScene.getStylesheets().add(css);
        primaryStage.setScene(accountScene);
    }
    
    public void createLeftMenu() {

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
        //logoutButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000; -fx-padding: 5 10;");
        userBox.getChildren().addAll(profileIcon, userNameLabel, logoutButton);

        // Liens du menu
        Button dashboardButton = new Button("Mon Tableau de bord");
        Button accountInfoButton = new Button("Informations du compte");
        Button addressBookButton = new Button("Carnet d'adresses");
        Button ordersButton = new Button("Mes commandes");
        Button paymentMethodsButton = new Button("Mes moyens de paiement");

        // Appliquer un style aux boutons
        for (Button button : new Button[]{dashboardButton, accountInfoButton, addressBookButton, ordersButton, paymentMethodsButton}) {
         //   button.setStyle("-fx-background-color: transparent; -fx-font-size: 14px; -fx-alignment: CENTER_LEFT; -fx-padding: 10;");
        	  button.setPrefWidth(250.0);
        }

        menuBox.getChildren().addAll(userBox, dashboardButton, accountInfoButton, addressBookButton, ordersButton, paymentMethodsButton);
        rootPane.getChildren().add(menuBox);
    }
    
    
    public void createMainSection() {
        VBox mainContent = new VBox(15);
        AnchorPane.setTopAnchor(mainContent, 116.0);
        mainContent.setMaxSize(1000, 500);
        mainContent.setPadding(new Insets(20));
        mainContent.setStyle("-fx-background-color: #FFFFFF;");

        Label dashboardTitle = new Label("Mon tableau de bord");
        dashboardTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label dashboardDesc = new Label("Depuis le tableau de bord 'Mon compte', vous pouvez avoir un aperçu de vos récentes activités et mettre à jour les informations de votre compte. Sélectionnez un lien ci-dessous pour voir ou modifier les informations.");
        dashboardDesc.setWrapText(true);

        // Table des commandes récentes
        TableView<String> ordersTable = new TableView<>();
        ordersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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
        ordersTable.setPlaceholder(new Label("Aucune commande récente."));

        HBox tableHeader = new HBox(10, new Label("Commandes récentes"), new Hyperlink("Voir tout"));
        tableHeader.setAlignment(Pos.CENTER_LEFT);

        mainContent.getChildren().addAll(dashboardTitle, dashboardDesc, tableHeader, ordersTable);
        AnchorPane.setLeftAnchor(mainContent, 250.0);
        AnchorPane.setTopAnchor(mainContent, 116.0);
        rootPane.getChildren().add(mainContent);    	
    }
}
