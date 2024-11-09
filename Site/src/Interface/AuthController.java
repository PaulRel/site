package Interface;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AuthController {
    private Scene authScene;
    private boolean isAuthenticated = false;

    public AuthController(Stage primaryStage, Scene mainScene, VBox header) {
    	AnchorPane rootPane = new AnchorPane();
    	
        AnchorPane leftPane = createLeftPane();
        AnchorPane rightPane = createRightPane();

        rootPane.getChildren().addAll(header, leftPane, rightPane);
        
        Scene authScene = new Scene(rootPane, 800, 600);
        primaryStage.setScene(authScene);
    }

    private AnchorPane createLeftPane() {
        AnchorPane leftPane = new AnchorPane();
        leftPane.setPrefSize(250, 600);
        AnchorPane.setTopAnchor(leftPane, 150.0);
        leftPane.setStyle("-fx-background-color: #263F73;");
        
        Label connectionLabel = new Label("Connexion");
        connectionLabel.setLayoutX(4);
        connectionLabel.setLayoutY(127);
        connectionLabel.setPrefSize(220, 17.8);
        connectionLabel.setAlignment(Pos.CENTER);
        connectionLabel.setTextFill(Color.web("#f7f4f4"));
        connectionLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
        connectionLabel.setEffect(new DropShadow());
        connectionLabel.setStyle("-fx-text-fill: #f7f4f4; -fx-alignment: center;");

        Label shopLabel = new Label("TennisShop");
        shopLabel.setLayoutX(4);
        shopLabel.setLayoutY(170);
        shopLabel.setPrefSize(220, 17.8);
        shopLabel.setAlignment(Pos.CENTER);
        shopLabel.setTextFill(Color.web("#f7f4f4"));
        shopLabel.setFont(Font.font("Apple Braille Outline 6 Dot", 22));
        shopLabel.setEffect(new DropShadow());
        shopLabel.setStyle("-fx-text-fill: #f7f4f4; -fx-alignment: center;");

        Line separatorLine = new Line(56.6, 164, 100, 164);
        separatorLine.setStroke(Color.WHITE);
        separatorLine.setStrokeWidth(2.0);
        separatorLine.setEffect(new DropShadow());

        leftPane.getChildren().addAll(connectionLabel, shopLabel, separatorLine);
        return leftPane;
    }
    

    private AnchorPane createRightPane() {
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

