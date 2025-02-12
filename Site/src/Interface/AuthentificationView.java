package Interface;

import customer.Cart;
import customer.CartItem;
import customer.CartManager;
import customer.Customer;
import customer.Customer.Role;
import database.CustomerDAO;
import database.SendEmail;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
/**
 * Classe représentant l'interface d'authentification des clients.
 * Permet aux utilisateurs de se connecter ou de créer un compte.
 */
public class AuthentificationView {
    private AnchorPane rootPane;
    private Label mainLabel;

    /**
     * Constructeur de la vue d'authentification.
     *
     * @param mainView La vue principale de l'application.
     */
    public AuthentificationView(MainView mainView) {
    	// Création des labels principaux
        mainLabel = new Label("Identifiez-vous ou créez un compte");
        
    	rootPane = new AnchorPane();
        createBox(mainView);
        
        Scene authScene = new Scene(rootPane, 1368, 690);
        
        HeaderView v=new HeaderView(mainView);      
        rootPane.getChildren().addAll(v.getHeader()); 
        authScene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
        mainView.getPrimaryStage().setScene(authScene);
    }

    
    /**
     * Crée l'interface utilisateur de la page d'authentification.
     *
     * @param mainView La vue principale de l'application.
     */
    private void createBox(MainView mainView) {        
        // Section pour les clients existants (VBox gauche)
        VBox existingCustomerBox = new VBox(10);
        existingCustomerBox.setPrefSize(600, 400);
        
        Label existingLabel = new Label("Déjà clients chez TennisShop");
        
        Label instructionLabel = new Label("Si vous avez déjà un compte, veuillez vous identifier.");
        instructionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: normal");
        
        // Champs de connexion
        TextField emailField = new TextField();
        emailField.setPromptText("Adresse mail *");
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe *");
        TextField textField = new TextField();
    	textField.setVisible(false); // Masquer le TextField initialement
    	
    	passwordField.textProperty().bindBidirectional(textField.textProperty()); // Synchronisation du texte entre les champs
    	passwordField.setStyle("-fx-font-size: 14px; -fx-max-width: 300px; -fx-min-width: 300px; -fx-min-height: 50px; -fx-pref-height: 50px; -fx-max-height: 50px;");
    	
    	// Bouton pour afficher/masquer le mot de passe
        Button showPasswordButton = new Button();       
        showPasswordButton.setStyle("-fx-background-color: white; -fx-padding: 5;");
        ImageView showIcon = new ImageView(new Image(AccountView.class.getResource("/Image/Icons/eyeIcon.png").toExternalForm()));
        ImageView hideIcon = new ImageView(new Image(AccountView.class.getResource("/Image/Icons/eyeOffIcon.png").toExternalForm()));
        showIcon.setFitHeight(20);
        showIcon.setFitWidth(20);
        hideIcon.setFitHeight(20);
        hideIcon.setFitWidth(20);
        showPasswordButton.setGraphic(showIcon);
        
        emailField.setStyle("-fx-font-size: 14px; -fx-max-width: 300px; -fx-min-height: 50px; -fx-pref-height: 50px; -fx-max-height: 50px;");
        
        // StackPane pour superposer les champs
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(textField, passwordField);

        // Synchronisation des dimensions
        textField.prefWidthProperty().bind(passwordField.widthProperty());
        textField.prefHeightProperty().bind(passwordField.heightProperty());
        
        showPasswordButton.setOnAction(e -> {
        	boolean isMasked = passwordField.isVisible();
    	    passwordField.setVisible(!isMasked);
    	    textField.setVisible(isMasked);
            showPasswordButton.setGraphic(isMasked? hideIcon : showIcon);
        });
        
        HBox passwordBox = new HBox();
        passwordBox.getChildren().addAll(stackPane, showPasswordButton);
        
        Hyperlink forgotPasswordLink = new Hyperlink("Mot de passe oublié ?");
        forgotPasswordLink.setOnAction(event -> {
        	SendEmail.sendPasswordToCustomer(emailField.getText());
        	MainView.showAlert("Mot de passe oublié", null, "Un mail vient de vous être envoyé sur "+ emailField.getText() + "/n", AlertType.INFORMATION);
        });
        
        // Bouton de connexion
        Button loginButton = new Button("CONNEXION");
        loginButton.setOnAction(e -> { 
        	// Récupère les valeurs saisies par l'utilisateur
        	String email = emailField.getText();
        	String password = passwordField.getText();
        	
        	/*
        	if (!SignUpView.isValidEmail(email)) {
        		MainView.showAlert("Erreur", null, "L'adresse email saisie est invalide. Exemple : nom@domaine.fr", AlertType.ERROR);
        	}
            else if (!SignUpView.isValidPassword(password)) {
            	MainView.showAlert("Erreur", null, "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre, et un caractère spécial", Alert.AlertType.ERROR);
     	    }
        	*/
        	
        	// Appelle la méthode d'authentification
        	handleLogin(email, password);
        	if (MainView.getCurrentCustomer() != null) {
        		if (MainView.getCurrentCustomer().getRole()==Role.CUSTOMER)
        			new AccountView(mainView);
        		else new AdminView(mainView);       		
        	}
        });
        
        existingCustomerBox.getChildren().addAll(existingLabel, instructionLabel, emailField, passwordBox, loginButton);
        
        
        
        // Section pour les nouveaux clients (VBox droite)
        VBox newCustomerBox = new VBox(10);
        newCustomerBox.setPrefSize(600, 400);
        
        Label newCustomerLabel = new Label("Vous êtes nouveau client chez TennisShop");
        
        Label newCustomerInfo = new Label("En créant un compte sur notre boutique, vous pourrez passer vos commandes plus rapidement, enregistrer plusieurs adresses de livraison, consulter et suivre vos commandes, et plein d'autres choses encore.");
        newCustomerInfo.setStyle("-fx-font-size: 16px; -fx-font-weight: normal");
        newCustomerInfo.setWrapText(true);
        
        Button createAccountButton = new Button("CRÉER UN COMPTE");
        
    	createAccountButton.setOnMouseClicked(event -> {
    	    new SignUpView(mainView);
    	   });	
        
        newCustomerBox.getChildren().addAll(newCustomerLabel, newCustomerInfo, createAccountButton);
        
        
        
          
        // Mise en page principale (Hbox contenant 2 Vbox)
        HBox mainBox = new HBox(20);
        mainBox.setPadding(new Insets(20));
        mainBox.setPrefSize(1200, 400);
        mainBox.getChildren().addAll(existingCustomerBox, newCustomerBox);
        mainBox.setStyle("-fx-background-color: white");
        
        VBox root = new VBox(40, mainLabel, mainBox);
        root.setPadding(new Insets(50));
        AnchorPane.setTopAnchor(root, 116.0);
        root.setStyle("-fx-background-color: #EEEEEE");
        
        root.prefWidthProperty().bind(mainView.getPrimaryStage().widthProperty());
        root.prefHeightProperty().bind(mainView.getPrimaryStage().heightProperty().subtract(118));
        
        rootPane.getChildren().addAll(root);
    }
    
    
    /**
     * Gère le processus de connexion d'un utilisateur.
     *
     * @param email L'adresse email saisie par l'utilisateur.
     * @param password Le mot de passe saisi.
     */
    private void handleLogin(String email, String password) {
    	Customer customer = new CustomerDAO().authenticate(email, password);
    	
    	if (customer != null) {
    		MainView.setCurrentCustomer(customer);
    		Cart cart = CartManager.getTempCart();
    		if (cart!=null) {
    			AuthentificationView.syncUserCart();
    		}
    		//MainView.showAlert("Connexion réussie", null, "Bienvenue " + customer.getFirstName()+" !", AlertType.INFORMATION);
        } else {
        	MainView.showAlert("Échec de la connexion", null, "Adresse e-mail ou mot de passe incorrect.", AlertType.ERROR);
        }
    }
    
    
    /**
     * Synchronise le panier temporaire avec le panier de l'utilisateur authentifié.
     */
    public static void syncUserCart() {
    	Customer customer = MainView.getCurrentCustomer();
        if (customer != null) {
        	Cart userCart = customer.getCart();
        	Cart tempCart = CartManager.getTempCart();

        	//Fusionner le panier temporaire avec le panier de l'utilisateur
        	for (CartItem tempItem : tempCart.getItems()) {
        		userCart.addProduct(tempItem.getProduct(), tempItem.getSize(), tempItem.getQuantity());
        	}

        	// Vider le panier temporaire
        	CartManager.clearTempCart();
        } else {
        	MainView.showAlert("Echec", null, "Utilisateur non reconnu", AlertType.ERROR);;
        }
    }
}

