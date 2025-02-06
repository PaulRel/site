package Interface;

import java.time.LocalDate;
import java.util.List;

import customer.Customer;
import customer.Order;
import database.CustomerDAO;
import database.OrderDAO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import products.Product;

public class AccountView {
	private AnchorPane rootPane;
	private VBox mainContent;
	private MainView mainView;

    public AccountView(MainView mainView) {      
        rootPane = new AnchorPane();
        
        this.mainView = mainView;
        
        createLeftMenu(mainView);
        createMainSection();
     
        Scene accountScene = new Scene(rootPane);
        
        HeaderView v = new HeaderView(mainView);
        rootPane.getChildren().addAll(v.getHeader());    
        accountScene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
        
        // Sauvegarde la taille actuelle avant de changer de scène
        double currentWidth = mainView.getPrimaryStage().getWidth();
        double currentHeight = mainView.getPrimaryStage().getHeight();

        // Appliquer la nouvelle scène
        mainView.getPrimaryStage().setScene(accountScene);

        // Réappliquer la taille après le changement de scène
        mainView.getPrimaryStage().setWidth(currentWidth);
        mainView.getPrimaryStage().setHeight(currentHeight);
    }
    

    // Menu de gauche avec les boutons Deconnexion, Mon tableau de Bord, Mes commandes, Supprimer le compte
    public void createLeftMenu(MainView mainView) {
        VBox menuBox = new VBox(15);
        AnchorPane.setTopAnchor(menuBox, 116.0);
        menuBox.setPadding(new Insets(20));
        menuBox.setStyle("-fx-background-color: #F8F8F8;");

        // Section utilisateur
        VBox userBox = new VBox(10);
        userBox.setAlignment(Pos.CENTER);
        userBox.setPadding(new Insets(10));
        ImageView profileIcon = new ImageView(new Image(getClass().getResource("/Image/Icons/accountIcon.png").toExternalForm()));
        profileIcon.setFitWidth(60);
        profileIcon.setFitHeight(60);
        Label userNameLabel = new Label("Bonjour,\n" + MainView.getCurrentCustomer().getFirstName() + " " + MainView.getCurrentCustomer().getLastName());
        userNameLabel.setStyle("-fx-font-weight: bold; -fx-text-align: center;");   
        Button logoutButton = new Button("Déconnexion");
        logoutButton.setOnAction(e -> {
        	MainView.setCurrentCustomer(null);
        	mainView.showProductView(Product.class, null);
        });
        
        userBox.getChildren().addAll(profileIcon, userNameLabel, logoutButton);

        Button dashboardButton = new Button("Mon Tableau de bord");
        Button accountInfoButton = new Button("Mes informations");
        Button ordersButton = new Button("Mes commandes");
        Button deleteAccountButton = new Button("Supprimer le compte");
        
        dashboardButton.setOnAction(e -> showDashboard());
        accountInfoButton.setOnAction(e -> mainContent.getChildren().setAll(editCustomerInfo(MainView.getCurrentCustomer())));
        ordersButton.setOnAction(e -> showCustomerOrders());
        deleteAccountButton.setOnAction(e -> {
        	new CustomerDAO().deleteCustomer(MainView.getCurrentCustomer());
        	MainView.setCurrentCustomer(null); 
        	mainView.showProductView(Product.class, null);
        });
        
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

        showDashboard();
        
        ScrollPane scrollPane = new ScrollPane(mainContent);
        
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Pas de scroll horizontal
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Scroll vertical au besoin
        scrollPane.setPadding(new Insets(20));    

        AnchorPane.setLeftAnchor(scrollPane, 280.0);
        AnchorPane.setTopAnchor(scrollPane, 119.0);
        AnchorPane.setBottomAnchor(scrollPane, 10.0);
        AnchorPane.setRightAnchor(scrollPane, 10.0);

        rootPane.getChildren().add(scrollPane);
    }
    
    private void showDashboard() {
    	Label dashboardTitle = new Label("Mon tableau de bord");
        Label dashboardDesc = new Label("Depuis le tableau de bord 'Mon compte', vous pouvez avoir un aperçu de vos récentes activités et mettre à jour les informations de votre compte. Sélectionnez un lien ci-dessous pour voir ou modifier les informations.");
        Label recentOrdersTitle = new Label ("Commandes récentes");
        Label infoTitle = new Label ("Informations du compte");

        //dashboardTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        recentOrdersTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        dashboardDesc.setWrapText(true);
        dashboardDesc.setStyle("-fx-font-size: 12px; -fx-font-weight: normal");
        infoTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        Hyperlink hyperlink1 = new Hyperlink("Voir tout");
        hyperlink1.setOnAction(event -> showCustomerOrders());
    	HBox tableHeader = new HBox(10, recentOrdersTitle, hyperlink1);
        tableHeader.setAlignment(Pos.CENTER_LEFT);
        
        TableView<Order> tableSection = createOrdersTable();   
        
        Hyperlink hyperlink = new Hyperlink("Modifier");
        hyperlink.setOnAction(event -> mainContent.getChildren().setAll(editCustomerInfo(MainView.getCurrentCustomer())));
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
    
    public static VBox editCustomerInfo(Customer customer) {
    	VBox editCustomerInfoBox = new VBox(15);
    	editCustomerInfoBox.setSpacing(10);
    	editCustomerInfoBox.setPadding(new Insets(10));
    	editCustomerInfoBox.setStyle("-fx-background-color: #FFFFFF;");
    	editCustomerInfoBox.setAlignment(Pos.CENTER);
    	
    	Label editLabel = new Label("Modifier les informations du compte");
        Label lastNameLabel = new Label("Nom");
        Label firstNameLabel = new Label("Prénom");
        Label emailLabel = new Label("Email");
        Label phoneLabel = new Label("Téléphone");
        Label addressLabel= new Label("Adresse");
        Label passwordLabel = new Label("Mot de passe");
        
        for (Label label : new Label[]{firstNameLabel, lastNameLabel, addressLabel, phoneLabel, emailLabel, passwordLabel}) {
        	label.setStyle("-fx-font-weight: normal;");
        }
	
    	TextField lastNameField = new TextField(customer.getLastName());
    	TextField firstNameField = new TextField(customer.getFirstName());
    	TextField emailField = new TextField(customer.getEmail());
    	TextField phoneField = new TextField(customer.getPhoneNumber());
    	TextField addressField = new TextField(customer.getAddress());
    	PasswordField passwordField = new PasswordField();
    	TextField textField = new TextField(customer.getPassword());
    	
    	textField.setVisible(false); // Masquer le TextField initialement
    	passwordField.textProperty().bindBidirectional(textField.textProperty()); // Synchronisation du texte entre les champs
    	
    	lastNameField.setPromptText("Nom");
    	firstNameField.setPromptText("Prénom");
    	emailField.setPromptText("Email");
    	phoneField.setPromptText("Téléphone");
    	addressField.setPromptText("Adresse");
    	addressField.setPrefWidth(300);
    	

    	Button showPasswordButton = new Button();       
        showPasswordButton.setStyle("-fx-background-color: white; -fx-padding: 5;");
        ImageView showIcon = new ImageView(new Image(AccountView.class.getResource("/Image/Icons/eyeIcon.png").toExternalForm()));
        ImageView hideIcon = new ImageView(new Image(AccountView.class.getResource("/Image/Icons/eyeOffIcon.png").toExternalForm()));
        showIcon.setFitHeight(20);
        showIcon.setFitWidth(20);
        hideIcon.setFitHeight(20);
        hideIcon.setFitWidth(20);
        showPasswordButton.setGraphic(showIcon);
        
        showPasswordButton.setOnAction(e -> {
        	boolean isMasked = passwordField.isVisible();
    	    passwordField.setVisible(!isMasked);
    	    textField.setVisible(isMasked);
            showPasswordButton.setGraphic(isMasked? hideIcon : showIcon);
        });

    	Button saveButton = new Button("Enregistrer");
    	
    	saveButton.setOnAction(event -> {
    	    String newLastName = lastNameField.getText();
    	    String newFirstName = firstNameField.getText();
    	    String newEmail = emailField.getText();
    	    String newPhone = phoneField.getText();
    	    String newAddress = addressField.getText();
    	    String newPassword = passwordField.getText();
    	    
    		if(newFirstName.isEmpty() || newLastName.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty() || newAddress.isEmpty()){
            	MainView.showAlert("Erreur", null, "Merci de remplir tous les champs ", AlertType.ERROR);
            }
    		else if (!newFirstName.matches("[a-zA-Z]+") || !newLastName.matches("[a-zA-Z]+")) {
    	          MainView.showAlert("Erreur", null, "Les noms et prénoms ne doivent pas contenir de caractères spéciaux ou des chiffres", AlertType.ERROR);
    	    }
    		
    		else if (!newAddress.matches("^[a-zA-Z0-9à-ÿÀ-Ÿ\\s,'-]+$")) {
    			MainView.showAlert("Erreur", null, "Veuillez entrer une adresse valide", Alert.AlertType.ERROR);
    		}
    		
    		else if (!newPhone.matches("^0[0-9]{9}$")) {
    			MainView.showAlert("Erreur", null, "Le numéro de téléphone doit comporter 10 chiffres et commencé par 0", Alert.AlertType.ERROR);
    		}
    		
            else if (!SignUpView.isValidEmail(newEmail)) {
                MainView.showAlert("Erreur", null, "L'adresse email saisie est invalide. Exemple : nom@domaine.fr", AlertType.ERROR);
            }
    		
            else if (!SignUpView.isValidPassword(newPassword)) {
    	        MainView.showAlert("Erreur", null, "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre, et un caractère spécial", Alert.AlertType.ERROR);
    	    }
    		
            else {
    	    // Mise à jour de la BDD
    	    Customer newCustomer = new Customer(customer.getId(), newLastName, newFirstName, customer.getCivility(), newEmail, newPhone, customer.getPassword(), customer.getRole(), newAddress);
    	    new CustomerDAO().updateCustomer(newCustomer);
    	    
    	    // Mise à jour du client actuel surtout si c currentCustomer
    	    customer.setLastName(newLastName);
        	customer.setFirstName(newFirstName);
        	customer.setEmail(newEmail);
        	customer.setPhoneNumber(newPhone);
        	customer.setAddress(newAddress);
            }	
    	});

    	// Disposition du formulaire
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER); 
        gridPane.addRow(0, lastNameLabel, lastNameField );
        gridPane.addRow(1, firstNameLabel, firstNameField);
        gridPane.addRow(2, emailLabel, emailField);
        gridPane.addRow(3, phoneLabel, phoneField);
        gridPane.addRow(4, addressLabel, addressField);
        gridPane.addRow(5, passwordLabel);
    	gridPane.add(passwordField, 1, 5);
    	gridPane.add(textField, 1, 5);
    	gridPane.add(showPasswordButton, 2, 5);
        
    	editCustomerInfoBox.getChildren().addAll(editLabel, gridPane, saveButton);
    	return editCustomerInfoBox;
    }

    
    private void showCustomerOrders() {
    	Label ordersTitle = new Label ("Toutes mes commandes");
    	
    	Label subtitle = new Label("L'ensemble de vos commandes sont affichées dans le tableau ci-dessous. Vous pouvez suivre leurs états d'avancement, les visualiser, télécharger vos factures.");
        subtitle.setWrapText(true);
        subtitle.setStyle("-fx-font-size: 12px; -fx-font-weight: normal");
        
        TableView<Order> ordersTable = createOrdersTable();
        ordersTable.setMaxHeight(300);
        
        mainContent.getChildren().setAll(ordersTitle, subtitle, ordersTable);
    }
    
    private TableView<Order> createOrdersTable() {    	
    	TableView<Order> ordersTable = new TableView<>();
        ordersTable.setMaxHeight(200);
        ordersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ordersTable.setId("ordersTable");

        // Colonnes
        TableColumn<Order, String> orderNumberCol = new TableColumn<>("Commande n°");
        TableColumn<Order, String> dateCol = new TableColumn<>("Date");
        TableColumn<Order, String> shippingToCol = new TableColumn<>("Expédition à");
        TableColumn<Order, String> totalCol = new TableColumn<>("Total de la commande");
        TableColumn<Order, String> statusCol = new TableColumn<>("Statut");
        TableColumn<Order, Void> actionCol = new TableColumn<>("Visualiser");

        // Associer les colonnes aux propriétés de la classe Order
        orderNumberCol.setCellValueFactory(order -> new SimpleStringProperty(String.valueOf(order.getValue().getOrderId())));
        dateCol.setCellValueFactory(order -> new SimpleStringProperty(order.getValue().getOrderDate().toString()));
        shippingToCol.setCellValueFactory(order -> new SimpleStringProperty(order.getValue().getCustomer().getAddress()));
        statusCol.setCellValueFactory(order -> new SimpleStringProperty(order.getValue().getStatus()));
        totalCol.setCellValueFactory(order -> new SimpleStringProperty(String.format("%.2f €", order.getValue().getTotalPrice())));
        actionCol.setCellFactory(order -> new TableCell<Order, Void>() {
            private final Button viewButton = new Button("FACTURE");
            private final Button finalizeButton = new Button("FINALISER");
            private final Label canceledLabel = new Label("Annulée");

            {
            	viewButton.setStyle("-fx-font-size: 12px; -fx-text-fill: white ; -fx-background-color: BLUEVIOLET; -fx-background-radius: 20px; -fx-padding: 10 20 10 20; ");
            	finalizeButton.setStyle("-fx-font-size: 12px; -fx-text-fill: white; -fx-background-color: royalblue; -fx-background-radius: 20px; -fx-padding: 10 20 10 20; ");
            	canceledLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: red; -fx-font-weight: bold;");
                // Action pour le bouton "Visualiser"
                viewButton.setOnAction(event -> {
                    InvoiceView invoiceView = new InvoiceView();
                    invoiceView.generateInvoice(getTableRow().getItem());
                });

                // Action pour le bouton "Finaliser"
                finalizeButton.setOnAction(event -> {
                    new OrderView(mainView, getTableRow().getItem());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                	Order currentOrder = getTableRow().getItem();

                	switch (currentOrder.getStatus()) {
                    case "En cours":
                        setGraphic(finalizeButton);
                        break;
                    case "Validée":
                        setGraphic(viewButton);
                        break;
                    case "Annulée":
                        setGraphic(canceledLabel);
                        break;
                    default:
                        setGraphic(null); // Aucun graphique pour les autres statuts
                        break;
                }
            }
        }
    });

        // Ajouter les colonnes à la table
        ordersTable.getColumns().add(orderNumberCol);
        ordersTable.getColumns().add(dateCol);
        ordersTable.getColumns().add(shippingToCol);
        ordersTable.getColumns().add(totalCol);
        ordersTable.getColumns().add(statusCol);
        ordersTable.getColumns().add(actionCol);

        // Placeholder
        ordersTable.setPlaceholder(new Label("Aucune commande."));
        
        OrderDAO orderDAO = new OrderDAO();
        List<Order> orders = orderDAO.getOrdersByCustomer(MainView.getCurrentCustomer());
        
        // Si la commande n'a pas été finalisé et qu'elle date d'il y a + de 2 jours
        // alors elle est annulée et les produits sont remis en vente
        for (Order order: orders) {
        	if (order.getStatus().equals("En cours") && order.getOrderDate().isBefore(LocalDate.now().minusDays(2))) {
                order.cancelOrder();
                order.getProducts().forEach(order::incrementStock);
            }
        }
        
        // Trier les commandes dans l'ordre inverse des IDs
        orders.sort((o1, o2) -> Integer.compare(o2.getOrderId(), o1.getOrderId()));
        
        ObservableList<Order> observableOrders = FXCollections.observableArrayList(orders);
        ordersTable.setItems(observableOrders);

        return ordersTable;
    }    	
}
