package Interface;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class OrderView {
	private AnchorPane rootPane;
	
	public OrderView(MainView mainView) {
		rootPane = new AnchorPane();
        HBox mainBox = new HBox();
        mainBox.setPadding(new Insets(50)); // Espace  Haut, Droite>, Bas, Gauche<  de l'AnchorPane
        mainBox.setSpacing(50); // Espacement entre mainBox et Panier
        mainBox.setPrefSize(1350, 554);
        mainBox.setStyle("-fx-background-color: #EEEEEE");
        mainBox.getChildren().addAll(MainView.createScrollPane(createOrderZone()), createInfoBar());	
        
        AnchorPane.setTopAnchor(mainBox, 116.0);
        
        Scene orderScene = new Scene(rootPane, 1350, 670);
        HeaderView v = new HeaderView(mainView);      
        rootPane.getChildren().addAll(v.getHeader(), mainBox);
        String css = this.getClass().getResource("/style.css").toExternalForm();        
        orderScene.getStylesheets().add(css);
        mainView.getPrimaryStage().setScene(orderScene);
	}
	
	public VBox createOrderZone() {
		VBox facturationInfoBox = createFacturationInfoBox();
		VBox deliveryInfoBox = createDeliveryInfoBox();
		VBox deliveryOptionBox = createDeliveryOptionsBox();
		VBox paymentOptionBox = createPaymentOptionsBox();
		
		Button backButton = new Button("Retour");
        Button continueButton = new Button("Continuer");

        HBox buttonBox = new HBox(100);
        buttonBox.setStyle("-fx-padding: 20;");
        buttonBox.getChildren().addAll(backButton, continueButton);
        
		VBox orderBox = new VBox();
		orderBox.getChildren().addAll(facturationInfoBox, deliveryInfoBox, deliveryOptionBox, paymentOptionBox, buttonBox);
		orderBox.setPrefWidth(900);
		orderBox.setSpacing(20);
        
        return orderBox;
	}
	
	private VBox createFacturationInfoBox() {
		Label facturation = new Label("1 - Informations de facturation");
		Label facturationInfo = new Label("Sélectionnez l'adresse de facturation");
		facturationInfo.setStyle("-fx-font-size: 12px; -fx-font-weight: normal");
		TextField addressField = new TextField(MainView.getCurrentCustomer().getAddress());
		
		VBox vbox = new VBox();
		vbox.setStyle("-fx-padding: 10; -fx-background-color : white");
        vbox.getChildren().addAll(facturation, facturationInfo, addressField);
        return vbox;
	}
	
	private VBox createInfoBar() {
		VBox box = new VBox();
		box.getChildren().addAll(new VBox());
        return box;    
	}
	
	private VBox createDeliveryInfoBox() {
		Label delivery = new Label("2 - Informations de livraison");
		Label deliveryInfo = new Label("Sélectionnez l'adresse de livraison");
		deliveryInfo.setStyle("-fx-font-size: 12px; -fx-font-weight: normal");
		TextField addressField = new TextField(MainView.getCurrentCustomer().getAddress());
		
		VBox vbox = new VBox();
		vbox.setStyle("-fx-padding: 10; -fx-background-color : white");
        vbox.getChildren().addAll(delivery, deliveryInfo, addressField);
		return vbox;
	}
	
	private VBox createDeliveryOptionsBox() {
		Label deliveryModeTitle = new Label("3 - Mode de livraison");
		Label deliveryModeInfo = new Label("Choisissez votre mode de livraison");
		deliveryModeInfo.setStyle("-fx-font-size: 12px; -fx-font-weight: normal");
		
        ToggleGroup deliveryGroup = new ToggleGroup();

        RadioButton upsOption = new RadioButton("UPS Domicile    48h* 		9,00 €");
        upsOption.setToggleGroup(deliveryGroup);
        upsOption.setStyle("-fx-font-size: 12px;");

        RadioButton colissimoOption = new RadioButton("Colissimo mon domicile    48h*		0,00 €");
        colissimoOption.setToggleGroup(deliveryGroup);
        colissimoOption.setStyle("-fx-font-size: 12px;");

        RadioButton chronopostOption = new RadioButton("Chronopost    24h*    15,00 €");
        chronopostOption.setToggleGroup(deliveryGroup);
        chronopostOption.setStyle("-fx-font-size: 12px;");

        RadioButton storePickupOption = new RadioButton("Retrait en magasin TennisShop    72h*    0,00 €");
        storePickupOption.setToggleGroup(deliveryGroup);
        storePickupOption.setStyle("-fx-font-size: 12px;");

        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-padding: 10; -fx-background-color : white");
        vbox.getChildren().addAll(deliveryModeTitle, deliveryModeInfo, upsOption, colissimoOption, chronopostOption, storePickupOption);
        
        return vbox;
	}
	private VBox createPaymentOptionsBox() {	
        Label titleLabel = new Label("4 - Informations de paiement");

        Label instructionLabel = new Label("Sélectionner votre méthode de paiement");
        instructionLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: normal");
        
        ToggleGroup paymentGroup = new ToggleGroup();

        RadioButton cardPaymentOption = new RadioButton("Paiement sécurisé par carte bancaire");
        cardPaymentOption.setToggleGroup(paymentGroup);
        cardPaymentOption.setStyle("-fx-font-size: 12px;");

        RadioButton paypalOption = new RadioButton("Paiement par Paypal");
        paypalOption.setToggleGroup(paymentGroup);
        paypalOption.setStyle("-fx-font-size: 12px;");

        RadioButton bankTransferOption = new RadioButton("Paiement par virement bancaire");
        bankTransferOption.setToggleGroup(paymentGroup);
        bankTransferOption.setStyle("-fx-font-size: 12px;");

        // Layout
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-padding: 10; -fx-background-color : white");
        vbox.getChildren().addAll(titleLabel, instructionLabel, cardPaymentOption, paypalOption, bankTransferOption);

        return vbox;
	}	
}
