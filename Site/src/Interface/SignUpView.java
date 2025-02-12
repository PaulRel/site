package Interface;

import java.util.Optional;
import java.util.regex.Pattern;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SignUpView {
	public SignUpView(MainView mainView) {
		Label mainLabel = new Label("Créer un compte");

		// Champ civilité
        Label civilityLabel = new Label("Civilité");
        RadioButton mrRadio = new RadioButton("M");
        RadioButton mmeRadio = new RadioButton("Mme");
        ToggleGroup civilityGroup = new ToggleGroup();
        mrRadio.setToggleGroup(civilityGroup);
        mmeRadio.setToggleGroup(civilityGroup);

        HBox civilityBox = new HBox(20, mrRadio, mmeRadio);
        civilityBox.setAlignment(Pos.CENTER_LEFT);
        
        
        // Champ prénom
        Label firstNameLabel = new Label("Prénom");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Prénom *");

        // Champ nom
        Label lastNameLabel = new Label("Nom");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Nom *");
        
        // Champ adresse
        Label addressLabel = new Label("Rue");
        TextField addressField = new TextField();
        addressField.setPromptText("Rue *");
        
        // Champ ville
        Label cityLabel = new Label("Ville");
        TextField cityField = new TextField();
        cityField.setPromptText("Ville *");

        // Champ email
        Label emailLabel = new Label("E-mail");
        TextField emailField = new TextField();
        emailField.setPromptText("Adresse mail *");
        
        
        // Champ mot de passe
        Label passwordLabel = new Label("Mot de passe");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe *");
        
        TextField visiblePasswordField = new TextField();
        visiblePasswordField.setVisible(false);  // Le champ texte est caché par défaut
        passwordField.textProperty().bindBidirectional(visiblePasswordField.textProperty()); // Synchroniser les valeurs des deux champs pour le mot de passe
        
        Button showPasswordButton = new Button();       
        showPasswordButton.setStyle("-fx-background-color: white; -fx-padding: 5;");
        ImageView showIcon = new ImageView(new Image(getClass().getResource("/Image/Icons/eyeIcon.png").toExternalForm()));
        ImageView hideIcon = new ImageView(new Image(getClass().getResource("/Image/Icons/eyeOffIcon.png").toExternalForm()));
        showIcon.setFitHeight(20);
        showIcon.setFitWidth(20);
        hideIcon.setFitHeight(20);
        hideIcon.setFitWidth(20);
        showPasswordButton.setGraphic(showIcon);
        
        // Fonctionnalité pour afficher/masquer le mot de passe
        showPasswordButton.setOnAction(e -> {
        	boolean isMasked = passwordField.isVisible();
    	    passwordField.setVisible(!isMasked);
    	    visiblePasswordField.setVisible(isMasked);
            showPasswordButton.setGraphic(isMasked? hideIcon:showIcon);
        });
        
        Label passwordConditions = new Label("min 8 caractères, 1 majuscule, 1 minuscule, 1 chiffre, et 1 caractère spécial");
        passwordConditions.setStyle("-fx-font-weight: normal; -fx-font-size:10; -fx-padding: 0;");
        
        for (Label label : new Label[]{civilityLabel, firstNameLabel, lastNameLabel, addressLabel, cityLabel, emailLabel, passwordLabel}) {
        	label.setStyle("-fx-font-weight: normal;");
        }
        
        // Champ checkBox
        CheckBox newsletterCheckBox = new CheckBox("Recevoir notre newsletter");
        CheckBox termsCheckBox = new CheckBox("J'accepte les conditions générales et la politique de confidentialité *");
        
        //Bouton Submit
        Button submitButton = new Button("VALIDER");       
        HBox buttonBox = new HBox(submitButton);
        buttonBox.setAlignment(Pos.CENTER);        
        submitButton.setOnAction(e -> handleSubmitButton(mainView, mrRadio, firstNameField, lastNameField, addressField, cityField, emailField, passwordField, termsCheckBox));

        // Disposition du formulaire
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(15));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER); 
        gridPane.addRow(0, civilityLabel);
        gridPane.add(civilityBox, 1, 0);
        gridPane.addRow(1, firstNameLabel, firstNameField);
        gridPane.addRow(2, lastNameLabel, lastNameField);
        gridPane.addRow(3, addressLabel, addressField);
        gridPane.addRow(4, cityLabel, cityField);
        gridPane.addRow(5, emailLabel, emailField);
        gridPane.addRow(6, passwordLabel);
        gridPane.add(passwordField, 1, 6);
        gridPane.add(visiblePasswordField, 1, 6);
        gridPane.add(showPasswordButton, 2, 6);
        gridPane.add(passwordConditions, 1, 7);
        gridPane.add(newsletterCheckBox, 0, 9, 2, 1);
        gridPane.add(termsCheckBox, 0, 10, 2, 1);

        // Mise en page principale
        VBox main = new VBox(mainLabel, gridPane, buttonBox);
        main.setPadding(new Insets(10));
        main.setMaxSize(700, 400);
        main.setAlignment(Pos.CENTER);
        main.setStyle("-fx-background-color: white");
        
        VBox root = new VBox(20, main);
        AnchorPane.setTopAnchor(root, 116.0);
        root.setPrefSize(1368, 574);
        root.setStyle("-fx-background-color: #EEEEEE");
        root.setAlignment(Pos.CENTER);
        
        HeaderView v = new HeaderView(mainView);

        AnchorPane rootPane = new AnchorPane();
        rootPane.getChildren().addAll(root, v.getHeader());
        
        Scene createAccountScene = new Scene(rootPane, 1368, 690);        
        createAccountScene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
        
        mainView.getPrimaryStage().setScene(createAccountScene);
	}
	
	
	private void handleSubmitButton(MainView mainView, RadioButton mrRadio, TextField firstNameField, TextField lastNameField, TextField addressField, TextField cityField, TextField emailField, TextField passwordField, CheckBox termsCheckBox) {
		// Récupère les valeurs saisies par l'utilisateur
    	Civility civility;
        if (mrRadio.isSelected()) {
            civility = Civility.M;
        } else{
            civility = Civility.Mme;
        }
        
        String firstName = firstNameField.getText();
    	String lastName = lastNameField.getText();
    	String address = addressField.getText() + ", " + cityField.getText();
    	String email = emailField.getText();
    	String password = passwordField.getText();
    	Role role = Role.CUSTOMER; // A modifier si possibilite creation compte admin
        
        // Vérifier que les champs sont bien remplis
        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || addressField.getText().isEmpty() || cityField.getText().isEmpty()){
        	MainView.showAlert("Erreur", null, "Merci de remplir tous les champs ", AlertType.ERROR);
        }
        else if (!termsCheckBox.isSelected()) {
        	MainView.showAlert("Erreur", null, "Merci d'accepter les règles de confidentialité ", AlertType.ERROR);
        }
        
        // Vérifier que les noms et prénoms ne contiennent pas de chiffres
        else if (!firstName.matches("[a-zA-Z]+") || !lastName.matches("[a-zA-Z]+")) {
            MainView.showAlert("Erreur", null, "Les noms et prénoms ne doivent pas contenir de caractères spéciaux ou des chiffres", AlertType.ERROR);
        }
        
        // Vérifier que l'adresse contient uniquement des lettres, chiffres, espaces, apostrophes, tirets et virgules
        // Vérifier que la ville contient uniquement des lettres, espaces et accents (pas de chiffres ni de symboles)
        else if (!addressField.getText().matches("^[a-zA-Z0-9à-ÿÀ-Ÿ\\s,'-]+$") || 
        	    !cityField.getText().matches("^[a-zA-Zà-ÿÀ-Ÿ\\s-]+$")) {
        	    MainView.showAlert("Erreur", null, "Veuillez entrer une adresse et une ville valides", Alert.AlertType.ERROR);
        }
        
        // Vérifier que l'email est valide
        else if (!isValidEmail(email)) {
            MainView.showAlert("Erreur", null, "L'adresse email saisie est invalide. Exemple : nom@domaine.fr", AlertType.ERROR);
        }
        
        else if (!isValidPassword(password)) {
	        MainView.showAlert("Erreur", null, "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre, et un caractère spécial", Alert.AlertType.ERROR);
	    }
        
        else {    	
    	// Crée une instance de Customer avec les informations saisies
        Customer newCustomer = new Customer(firstName, lastName, civility, email, null, password, role, address);
        
        // Ajoute dans la base de données
        CustomerDAO customerDAO = new CustomerDAO();
		customerDAO.signUpCustomer(newCustomer);
		
     
		// Affiche une alerte de création de compte
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Création de compte réussie");
		alert.setHeaderText("Le compte a bien été créée.");
		alert.setContentText("Vous pouvez désormais modifier les informations dans les paramètres du compte.");

		// Redirige vers la page du compte
		ButtonType loginButton = new ButtonType("Continuer");
		alert.getButtonTypes().setAll(loginButton);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == loginButton) {
			
			if (MainView.getCurrentCustomer() == null || MainView.getCurrentCustomer().getRole() == Role.CUSTOMER) {  
			    // Si CurrentCustomer est null ou si le rôle est CUSTOMER
			    MainView.setCurrentCustomer(newCustomer); // Initialise la session
			    
			    Cart cart = CartManager.getTempCart();
			    if (cart != null) {
			        AuthentificationView.syncUserCart();
			    }
			    
			    new AccountView(mainView); // Affiche la vue du compte
			    
			} else if (MainView.getCurrentCustomer().getRole() == Role.ADMIN) {  
			    // Si c'est l'admin qui ajoute un client
			    new AdminView(mainView);  
			} else {  
			    // Cas par défaut
			    new AccountView(mainView);  
			}
        }
	}
	}
	
	
	// Méthode pour vérifier la validité de l'email (avec @ et nom de domaine obligatoire
    public static boolean isValidEmail(String email) {
        // Expression régulière pour une adresse email basique
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

	// Méthode pour valider le mot de passe
	public static boolean isValidPassword(String password) {
	    // Longueur minimale
	    if (password.length() < 8) {
	        return false;
	    }

	    // Vérifier au moins un chiffre
	    if (!password.matches(".*\\d.*")) { // \\d représente un chiffre
	        return false;
	    }

	    // Vérifier au moins une lettre majuscule
	    if (!password.matches(".*[A-Z].*")) {
	        return false;
	    }

	    // Vérifier au moins une lettre minuscule
	    if (!password.matches(".*[a-z].*")) {
	        return false;
	    }

	    // Vérifier au moins un caractère spécial
	    if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
	        return false;
	    }

	    return true; // Tous les critères sont respectés
	}    	    
}
