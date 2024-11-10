package Interface;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AuthController {
    private AnchorPane rootPane;
    private boolean isAuthenticated = false;
    private Label mainLabel;

    public AuthController(Stage primaryStage, Scene mainScene) {
    	// Création des labels principaux
        mainLabel = new Label("Identifiez-vous ou créez un compte");
        //mainLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        mainLabel.setOnMouseClicked(e -> primaryStage.setScene(mainScene));
                
    	rootPane = new AnchorPane();
        createLeftBox(primaryStage);
        
        //AnchorPane rightPane = createRightPane();
        //AnchorPane.setTopAnchor(mainLabel, 150.0);
        //AnchorPane.setLeftAnchor(mainLabel, 20.0);
        
        Scene authScene = new Scene(rootPane, 1350, 670);
        
        HeaderView v=new HeaderView(primaryStage, authScene);      
        rootPane.getChildren().addAll(v.getHeader());
        String css = this.getClass().getResource("/style.css").toExternalForm();        
        authScene.getStylesheets().add(css);
        primaryStage.setScene(authScene);
    }

    private void createLeftBox(Stage primaryStage) {        
        // Section pour les clients existants
        VBox existingCustomerBox = new VBox(10);
        existingCustomerBox.setPrefSize(600, 400);
        //AnchorPane.setTopAnchor(existingCustomerBox, 200.0);
        //AnchorPane.setLeftAnchor(existingCustomerBox, 20.0);
        
        Label existingLabel = new Label("Déjà clients chez TennisShop");
        
        Label instructionLabel = new Label("Si vous avez déjà un compte, veuillez vous identifier.");
        instructionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: normal");
        
        // Champs de connexion
        TextField emailField = new TextField();
        emailField.setPromptText("Adresse mail *");
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe *");
        
        Hyperlink forgotPasswordLink = new Hyperlink("Mot de passe oublié ?");
        
        Button loginButton = new Button("CONNEXION");
        
        // Organisation des champs dans le VBox
        existingCustomerBox.getChildren().addAll(existingLabel, instructionLabel, emailField, passwordField, forgotPasswordLink, loginButton);
        
        
        
        // Section pour les nouveaux clients
        VBox newCustomerBox = new VBox(10);
        newCustomerBox.setPrefSize(600, 400);
        //AnchorPane.setTopAnchor(newCustomerBox, 200.0);
        //AnchorPane.setRightAnchor(newCustomerBox, 20.0);
        
        Label newCustomerLabel = new Label("Vous êtes nouveau client chez TennisShop");
        
        Label newCustomerInfo = new Label("En créant un compte sur notre boutique, vous pourrez passer vos commandes plus rapidement, enregistrer plusieurs adresses de livraison, consulter et suivre vos commandes, et plein d'autres choses encore.");
        newCustomerInfo.setStyle("-fx-font-size: 16px; -fx-font-weight: normal");
        newCustomerInfo.setWrapText(true);
        
        Button createAccountButton = new Button("CRÉER UN COMPTE");
        
        newCustomerBox.getChildren().addAll(newCustomerLabel, newCustomerInfo, createAccountButton);
        
        
     // Mise en page principale
        HBox mainBox = new HBox(20);
        mainBox.setPadding(new Insets(20));
        mainBox.setPrefSize(1200, 400);
        mainBox.getChildren().addAll(existingCustomerBox, newCustomerBox);
        mainBox.setStyle("-fx-background-color: white");
        
        VBox root = new VBox(40, mainLabel, mainBox);
        root.setPadding(new Insets(50));
        AnchorPane.setTopAnchor(root, 116.0);
        root.setPrefSize(1350, 670);
        root.setStyle("-fx-background-color: #CFCFCF");
        
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            root.setPrefWidth(newValue.doubleValue()); // Ajuste la largeur
        });

        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            root.setPrefHeight(newValue.doubleValue()); // Ajuste la hauteur
        });
        
        //rootPane.getChildren().addAll(existingCustomerBox, newCustomerBox);
        rootPane.getChildren().addAll(root);
    }
    

    private AnchorPane createRightBox() {
        AnchorPane rightPane = new AnchorPane();
        rightPane.setPrefSize(550, 600);
        AnchorPane.setTopAnchor(rightPane, 150.0);

        Label loginLabel = new Label("Secure Login");
        loginLabel.setLayoutX(300.8);
        loginLabel.setLayoutY(53);
        loginLabel.setPrefSize(288.8, 28.8);
        loginLabel.setAlignment(Pos.CENTER);
        loginLabel.setFont(Font.font("Apple Braille Outline 6 Dot", 14.8));
        loginLabel.setEffect(new DropShadow());
        loginLabel.setStyle("-fx-alignment: center; -fx-text-fill: #f7f4f4;");

        Label usernameLabel = new Label("Username");
        usernameLabel.setLayoutX(300.8);
        usernameLabel.setLayoutY(117.8);
        usernameLabel.setFont(Font.font(14.8));

        TextField usernameTextField = new TextField();
        usernameTextField.setId("usernameTextField");
        usernameTextField.setLayoutX(300.0);
        usernameTextField.setLayoutY(113.0);
        usernameTextField.setPromptText("Username");
        usernameTextField.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");


        Label passwordLabel = new Label("Password");
        passwordLabel.setLayoutX(300.8);
        passwordLabel.setLayoutY(157.8);
        passwordLabel.setFont(Font.font(14.8));

        PasswordField passwordField = new PasswordField();
        passwordField.setId("passwordPasswordField");
        passwordField.setLayoutX(300.0);
        passwordField.setLayoutY(154.0);
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");

        Button loginButton = new Button("Login");
        loginButton.setId("loginButton");
        loginButton.setLayoutX(300);
        loginButton.setLayoutY(228);
        loginButton.setPrefSize(236, 31);
        loginButton.setStyle("-fx-background-color: #263F73; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        loginButton.setTextFill(Color.WHITE);        
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #1c3158; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color: #263F73; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;"));

        rightPane.getChildren().addAll(loginLabel, usernameLabel, usernameTextField, passwordLabel, passwordField, loginButton);
        return rightPane;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    private void authenticate(String username, String password, Stage primaryStage, Scene mainScene) {
        if ("user".equals(username) && "password".equals(password)) {
            isAuthenticated = true;
            primaryStage.setScene(mainScene);
        } else {
            System.out.println("Invalid credentials");
        }
    }
}

