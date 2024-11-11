package Interface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import customer.Customer;
import customer.Customer.Civility;
import customer.Customer.Role;
import database.DatabaseConnection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.stage.Stage;

public class SignUpController {
	public SignUpController(Stage primaryStage, Scene mainScene) {
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
               
        CheckBox newsletterCheckBox = new CheckBox("Recevoir notre newsletter");

        CheckBox termsCheckBox = new CheckBox("J'accepte les conditions générales et la politique de confidentialité");
        
        Button submitButton = new Button("VALIDER");       
        HBox buttonBox = new HBox(submitButton);
        buttonBox.setAlignment(Pos.CENTER);
        
        
        submitButton.setOnAction(e -> { 
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
        	String password = passwordField.getText();
        	Role role = Role.CUSTOMER; // A modifier si possibilite creation compte admin
        	
        	// Crée une instance de Customer avec les informations saisies
            Customer newCustomer = new Customer(firstName, lastName, civility, email, null, password, role, address);
            
        	// Appelle la méthode d'authentification
        	handleSignUp(newCustomer);});

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
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            root.setPrefWidth(newValue.doubleValue()); // Ajuste la largeur
        });       
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            root.setPrefHeight(newValue.doubleValue()-116); // Ajuste la hauteur
        });

        AnchorPane rootPane = new AnchorPane();
        rootPane.getChildren().addAll(root);
               
        Scene createAccountScene = new Scene(rootPane, 1350, 670);
        HeaderView v = new HeaderView(primaryStage, mainScene);
        rootPane.getChildren().addAll(v.getHeader());
               
        String css = this.getClass().getResource("/style.css").toExternalForm();        
        createAccountScene.getStylesheets().add(css);
        
        primaryStage.setScene(createAccountScene);
	}
	
	public void handleSignUp(Customer customer) {
		String query = "INSERT INTO Customer (FirstName, LastName, Civility, Email, Passwords, Address) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            // Remplace les paramètres de la requête par les valeurs de l'objet Customer
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getCivility().name());
            statement.setString(4, customer.getEmail());            
            statement.setString(5, customer.getPassword());
            statement.setString(6, customer.getAddress());

            // Exécute l'insertion dans la base de données
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Le client a été ajouté avec succès !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }	
	}

}
