package Interface;

import java.util.Optional;

import customer.Cart;
import customer.CartManager;
import customer.Customer;
import customer.Customer.Civility;
import customer.Customer.Role;
import database.CustomerDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SignUpView {
	public SignUpView(MainView mainView) {
		Label mainLabel = new Label("Créer un compte");

		// Champ civilité
        Label civilityLabel = new Label("Civilité");
        civilityLabel.setStyle("-fx-font-weight: normal");
        RadioButton mrRadio = new RadioButton("M");
        RadioButton mmeRadio = new RadioButton("Mme");
        ToggleGroup civilityGroup = new ToggleGroup();
        mrRadio.setToggleGroup(civilityGroup);
        mmeRadio.setToggleGroup(civilityGroup);

        HBox civilityBox = new HBox(20, civilityLabel, mrRadio, mmeRadio);
        civilityBox.setAlignment(Pos.CENTER_LEFT);
        
        
        // Champ prénom
        Label firstNameLabel = new Label("Prénom");
        firstNameLabel.setStyle("-fx-font-weight: normal");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Prénom *");

        // Champ nom
        Label lastNameLabel = new Label("Nom");
        lastNameLabel.setStyle("-fx-font-weight: normal");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Nom *");
        
        // Champ adresse
        Label addressLabel = new Label("Adresse");
        addressLabel.setStyle("-fx-font-weight: normal");
        TextField addressField = new TextField();
        addressField.setPromptText("Adresse *");

        // Champ email
        Label emailLabel = new Label("E-mail");
        emailLabel.setStyle("-fx-font-weight: normal");
        TextField emailField = new TextField();
        emailField.setPromptText("Adresse mail *");

        // Champ mot de passe
        Label passwordLabel = new Label("Mot de passe");
        passwordLabel.setStyle("-fx-font-weight: normal");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe *");
        Button showPasswordButton = new Button("SHOW");
        showPasswordButton.setStyle("-fx-background-color: #E0E0E0; -fx-font-weight: bold;");
        
        TextField visiblePasswordField = new TextField();
        visiblePasswordField.setVisible(false);  // Le champ texte est caché par défaut
        
        HBox passwordBox = new HBox(passwordField, visiblePasswordField, showPasswordButton);
        passwordBox.setSpacing(5);
        passwordBox.setAlignment(Pos.CENTER_LEFT);       
        passwordField.textProperty().bindBidirectional(visiblePasswordField.textProperty()); // Synchroniser les valeurs des deux champs pour le mot de passe
        
        // Fonctionnalité pour afficher/masquer le mot de passe
        showPasswordButton.setOnAction(e -> {
            if (visiblePasswordField.isVisible()) {
            	visiblePasswordField.setVisible(false);
                passwordField.setVisible(true);
                showPasswordButton.setText("SHOW");
            } else {
            	visiblePasswordField.setVisible(true);
                passwordField.setVisible(false);
                showPasswordButton.setText("HIDE");
            }
        });
        
        // Champ checkBox
        CheckBox newsletterCheckBox = new CheckBox("Recevoir notre newsletter");
        CheckBox termsCheckBox = new CheckBox("J'accepte les conditions générales et la politique de confidentialité");
        
        //Bouton Submit
        Button submitButton = new Button("VALIDER");       
        HBox buttonBox = new HBox(submitButton);
        buttonBox.setAlignment(Pos.CENTER);        
        submitButton.setOnAction(e -> handleSubmitButton(mainView, mrRadio, firstNameField, lastNameField, addressField, emailField, passwordBox));

        // Disposition du formulaire
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(15));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);        
        gridPane.add(civilityBox, 0, 0);
        gridPane.addRow(1, firstNameLabel, firstNameField);
        gridPane.addRow(2, lastNameLabel, lastNameField);
        gridPane.addRow(3, addressLabel, addressField);
        gridPane.addRow(4, emailLabel, emailField);
        gridPane.addRow(5, passwordLabel, passwordBox);
        gridPane.add(newsletterCheckBox, 0, 7, 2, 1);
        gridPane.add(termsCheckBox, 0, 8, 2, 1);

        // Mise en page principale
        VBox main = new VBox(mainLabel, gridPane, buttonBox);
        main.setPadding(new Insets(10));
        main.setMaxSize(700, 400);
        main.setAlignment(Pos.CENTER);
        main.setStyle("-fx-background-color: white");
        
        VBox root = new VBox(20, main);
        AnchorPane.setTopAnchor(root, 116.0);
        root.setPrefSize(1350, 550);
        root.setStyle("-fx-background-color: #EEEEEE");
        root.setAlignment(Pos.CENTER);
        mainView.getPrimaryStage().widthProperty().addListener((observable, oldValue, newValue) -> {
            root.setPrefWidth(newValue.doubleValue()); // Ajuste la largeur
        });       
        mainView.getPrimaryStage().heightProperty().addListener((observable, oldValue, newValue) -> {
            root.setPrefHeight(newValue.doubleValue()-116); // Ajuste la hauteur
        });
        
        HeaderView v = new HeaderView(mainView);

        AnchorPane rootPane = new AnchorPane();
        rootPane.getChildren().addAll(root, v.getHeader());
        
        Scene createAccountScene = new Scene(rootPane, 1350, 670);

         
        createAccountScene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
        
        mainView.getPrimaryStage().setScene(createAccountScene);
	}
	
	private void handleSubmitButton(MainView mainView, RadioButton mrRadio, TextField firstNameField, TextField lastNameField, TextField addressField, TextField emailField, HBox passwordBox) {
		// Récupère les valeurs saisies par l'utilisateur
    	Civility civility;
        if (mrRadio.isSelected()) {
            civility = Civility.M;
        } else{
            civility = Civility.Mme;
        }
    	String firstName = firstNameField.getText();
    	String lastName = lastNameField.getText();
    	String address = addressField.getText();
    	String email = emailField.getText();
    	String password = ((PasswordField) passwordBox.getChildren().get(0)).getText();
    	Role role = Role.CUSTOMER; // A modifier si possibilite creation compte admin
    	
    	// Crée une instance de Customer avec les informations saisies
        Customer newCustomer = new Customer(firstName, lastName, civility, email, null, password, role, address);
        
        // Ajoute dans la base de données
        CustomerDAO customerDAO = new CustomerDAO();
		customerDAO.signUpCustomer(newCustomer);
 	
		// Initialise la session
		MainView.setCurrentCustomer(newCustomer);
		Cart cart = CartManager.getTempCart();
		if (cart!=null) {
			AuthentificationView.syncUserCart();
		}
     
		// Affiche une alerte de création de compte
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Création de compte réussie");
		alert.setHeaderText("Votre compte a bien été créée.");
		alert.setContentText("Vous pouvez désormais modifier vos informations dans les paramètres du compte.");

		// Redirige vers la page du compte
		ButtonType loginButton = new ButtonType("Continuer");
		alert.getButtonTypes().setAll(loginButton);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == loginButton) {
			new AccountView(mainView);
		}
		
	}
}
