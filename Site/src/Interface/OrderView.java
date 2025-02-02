package Interface;

import java.util.List;

import customer.CartItem;
import customer.Invoice;
import customer.Order;
import database.InvoiceDAO;
import database.OrderDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import products.Product;

public class OrderView {
	private AnchorPane rootPane;
	private VBox orderBox;
	
	public OrderView(MainView mainView, Order order) {
		rootPane = new AnchorPane();
        HBox mainBox = new HBox();
        mainBox.setPadding(new Insets(20)); // Espace  Haut, Droite>, Bas, Gauche<  de l'AnchorPane
        mainBox.setSpacing(20); // Espacement entre mainBox et Panier
        //mainBox.setPrefSize(1350, 570);
        mainBox.setStyle("-fx-background-color: #EEEEEE");
        mainBox.getChildren().addAll(MainView.createScrollPane(createOrderZone(mainView, order)), createInfoBar(order));
        
        AnchorPane.setTopAnchor(mainBox, 116.0);
        
        Scene orderScene = new Scene(rootPane);
        HeaderView v = new HeaderView(mainView);
        rootPane.getChildren().addAll(v.getHeader(), mainBox);       
        orderScene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
        mainView.getPrimaryStage().setScene(orderScene);
	}
	
	
	public VBox createOrderZone(MainView mainView, Order order) {
		VBox billingInfoBox = createBillingInfoBox();
		VBox deliveryInfoBox = createDeliveryInfoBox();
		VBox deliveryOptionBox = createDeliveryOptionsBox();
		VBox paymentOptionBox = createPaymentOptionsBox();
		
		Button backButton = new Button("Retour");
        Button continueButton = new Button("Continuer");
        backButton.setOnAction(event -> goBack(mainView));
        continueButton.setOnAction(event -> continueProcess(mainView, order));

        HBox buttonBox = new HBox(100);
        buttonBox.setStyle("-fx-padding: 20;");
        buttonBox.getChildren().addAll(backButton, continueButton);
        
		orderBox = new VBox();
		orderBox.getChildren().addAll(billingInfoBox, deliveryInfoBox, deliveryOptionBox, paymentOptionBox, buttonBox);
		orderBox.setPrefWidth(900);
		orderBox.setSpacing(20);
        
        return orderBox;
	}
	
	private void goBack(MainView mainView) {
		MainView.showAlert("Commande en cours", "Vous avez 2 jours pour finaliser votre commande.", "Votre commande sera ANNULEE dans 2 jours et les produits choisis seront remis en vente", AlertType.INFORMATION);
		new AccountView(mainView);
	}
	
	private void continueProcess(MainView mainView, Order order) {
		if (!isOrderValid()) {
			MainView.showAlert("Erreur", null, "Veuillez remplir tous les champs", AlertType.ERROR);
		} else {
		order.validateOrder();
		MainView.showAlert("Commande", null, "Commande validée", AlertType.INFORMATION);
		createInvoice(order);
		new AccountView(mainView);
		}
	}
	
	private ScrollPane createInfoBar(Order order) {
		Label recapTitle = new Label("Récapitulatif");
		VBox orderProductsBox = createOrderProductsBox(order);
		VBox totalBox = createTotalBox(order);
		
		VBox recapBox = new VBox();
		recapBox.setStyle("-fx-padding: 10; -fx-background-color : white; -fx-pref-height: 510px;");
		
		Region spacer = new Region();
		VBox.setVgrow(spacer, Priority.ALWAYS);
		
		recapBox.getChildren().addAll(recapTitle, orderProductsBox, spacer, totalBox);
		ScrollPane infoBar = MainView.createScrollPane(recapBox);
		infoBar.setStyle("-fx-background-color: white");
		
		return infoBar;
	}
	
	
	// ORDER ZONE
	private VBox createBillingInfoBox() {
		Label billing  = new Label("1 - Informations de facturation");
		Label billingInfo  = new Label("Sélectionnez l'adresse de facturation");
		billingInfo .setStyle("-fx-font-size: 12px; -fx-font-weight: normal");
		TextField addressField = new TextField(MainView.getCurrentCustomer().getAddress());
		
		VBox vbox = new VBox();
		vbox.setStyle("-fx-padding: 10; -fx-background-color : white");
        vbox.getChildren().addAll(billing, billingInfo , addressField);
        return vbox;
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

        RadioButton upsOption = new RadioButton("UPS Domicile    	9,00 €");
        upsOption.setToggleGroup(deliveryGroup);
        upsOption.setStyle("-fx-font-size: 12px;");

        RadioButton colissimoOption = new RadioButton("Colissimo mon domicile	4,00 €");
        colissimoOption.setToggleGroup(deliveryGroup);
        colissimoOption.setStyle("-fx-font-size: 12px;");

        RadioButton chronopostOption = new RadioButton("Chronopost		15,00 €");
        chronopostOption.setToggleGroup(deliveryGroup);
        chronopostOption.setStyle("-fx-font-size: 12px;");

        RadioButton storePickupOption = new RadioButton("Retrait en magasin TennisShop 0,00 €");
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
	
	// CREATION FACTURE
	private void createInvoice(Order order) {
		// Récupère les valeurs saisies par l'utilisateur
		TextField billingField = (TextField) ((VBox) orderBox.getChildren().get(0)).getChildren().get(2);
	    TextField deliveryField = (TextField) ((VBox) orderBox.getChildren().get(1)).getChildren().get(2);
	    ToggleGroup shippingGroup = ((RadioButton) ((VBox) orderBox.getChildren().get(2)).getChildren().get(2)).getToggleGroup();
	    ToggleGroup paymentGroup = ((RadioButton) ((VBox) orderBox.getChildren().get(3)).getChildren().get(2)).getToggleGroup();
	    
        String billingAddress = billingField.getText();
        String shippingAddress = deliveryField.getText();
        String shippingMethod = ((RadioButton) shippingGroup.getSelectedToggle()).getText();
        String paymentMethod = ((RadioButton) paymentGroup.getSelectedToggle()).getText();
        
        //RadioButton selectedShippingOption = (RadioButton) shippingGroup.getSelectedToggle();
        //RadioButton selectedPaymentOption = (RadioButton) paymentGroup.getSelectedToggle();
        
        // Crée la facture
        Invoice invoice = new Invoice(order);
        invoice.setBillingAddress(billingAddress);
        invoice.setShippingAddress(shippingAddress);
        invoice.setShippingMethod(shippingMethod);
        invoice.setShippingPrice(getShippingPrice(shippingMethod));
        invoice.setPaymentMethod(paymentMethod);
        
        // Insère la facture dans la base de données
        InvoiceDAO invoiceDAO = new InvoiceDAO();
        invoiceDAO.insertInvoice(invoice);
    }
	
	private boolean isOrderValid() {
		TextField billingField = (TextField) ((VBox) orderBox.getChildren().get(0)).getChildren().get(2);
	    TextField deliveryField = (TextField) ((VBox) orderBox.getChildren().get(1)).getChildren().get(2);
	    ToggleGroup shippingGroup = ((RadioButton) ((VBox) orderBox.getChildren().get(2)).getChildren().get(2)).getToggleGroup();
	    ToggleGroup paymentGroup = ((RadioButton) ((VBox) orderBox.getChildren().get(3)).getChildren().get(2)).getToggleGroup();
	    
	    if(billingField.getText().isEmpty() || deliveryField.getText().isEmpty() || shippingGroup.getSelectedToggle() == null || paymentGroup.getSelectedToggle() == null) {
	    		return false;
	    }
	return true;
	}
	
	
	// RECAP ZONE
	private VBox createOrderProductsBox(Order order) {
		VBox orderProductsBox = new VBox();
		orderProductsBox.setSpacing(10);
		
		OrderDAO orderDAO = new OrderDAO();
		Order currentOrder = orderDAO.getOrderById(order.getOrderId());
		
		List<CartItem> products = currentOrder.getProducts();
		
		for (CartItem item : products) {
            Product product = item.getProduct();
            Label nameLabel = new Label(product.getName());
            nameLabel.setWrapText(true);
            Label sizeLabel = new Label ("Taille : " + item.getSize());
            Label quantityLabel = new Label("Quantité : " + item.getQuantity());
            Label priceLabel = new Label(product.getPrice() + "€");
            priceLabel.setStyle("-fx-padding: 10 0 0 0; -fx-margin: 0;");
            ImageView imageView = new ImageView(new Image(getClass().getResource(product.getImagePath()).toExternalForm()));
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            
            for (Label label : new Label[]{nameLabel, sizeLabel, quantityLabel}) {
    			label.setStyle("-fx-font-weight: normal; -fx-font-size: 14px; -fx-padding: 0; -fx-margin: 0; ");
    		}
            
            HBox hBox = new HBox();
            hBox.getChildren().addAll(imageView,  new VBox(nameLabel, sizeLabel, quantityLabel, priceLabel));
            
            Region separator = new Region();
            separator.setStyle("-fx-background-color: silver; -fx-pref-height: 1px; -fx-max-height: 1px;");
            orderProductsBox.widthProperty().addListener((observable, oldValue, newValue) -> {
                separator.setMaxWidth(newValue.doubleValue() * 0.8);
            });
            orderProductsBox.setAlignment(Pos.CENTER); // Centrer le séparateur
            orderProductsBox.getChildren().addAll(hBox, separator);
		}
		
		return orderProductsBox;
	}
	
	private VBox createTotalBox(Order order) {
		VBox totalBox = new VBox();
		
		ToggleGroup shippingGroup = ((RadioButton) ((VBox) orderBox.getChildren().get(2)).getChildren().get(2)).getToggleGroup();
		RadioButton selectedShippingOption = (RadioButton) shippingGroup.getSelectedToggle();
		String shippingMethod = selectedShippingOption != null ? selectedShippingOption.getText() : "";
		double shippingPrice = getShippingPrice(shippingMethod);
		
		HBox subTotalLine = createLine("\nTotal produits TTC", String.format("%.2f €", order.getTotalPrice()));
		HBox htLine = createLine("Total HT", String.format("%.2f €", order.getTotalPrice() / 1.2));
		HBox tvaLine = createLine("TVA (20%)", String.format("%.2f €", order.getTotalPrice() * 0.2));
		HBox shippingLine = createLine("Frais de Port", String.format("%.2f €", shippingPrice));
		HBox totalLine = createLine("Total TTC", String.format("%.2f €", order.getTotalPrice() + shippingPrice));

		//for (Label label : new Label[]{subTotal, HTLabel, TVALabel, shippingPriceLabel}) {
			//label.setStyle("-fx-font-weight: normal;-fx-font-size: 14px; -fx-padding: 0; -fx-margin: 0; ");
		//}
		shippingGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue != null) {
		        String newShippingMethod = ((RadioButton) newValue).getText(); // Méthode d'expédition sélectionnée
		        double newShippingPrice = getShippingPrice(newShippingMethod); // Calcul du nouveau prix des frais de port
		        shippingLine.getChildren().set(1, new Label(String.format("%.2f €", newShippingPrice))); // Mise à jour
		        totalLine.getChildren().set(1, new Label(String.format("%.2f €", order.getTotalPrice() + newShippingPrice)));
		    }
		});

		totalBox.getChildren().addAll(subTotalLine, htLine, tvaLine, shippingLine, totalLine);
		//totalBox.setStyle("");
		
		return totalBox;
	}
	
	private HBox createLine(String labelText, String valueText) {
	    Label textLabel = new Label(labelText);
	    Label valueLabel = new Label(valueText);
	    textLabel.setMaxWidth(Double.MAX_VALUE); // Permet de prendre toute la largeur disponible 
	    HBox.setHgrow(textLabel, Priority.ALWAYS); // Force l'alignement à gauche
	    
	    if (labelText != "Total TTC") {
	    	textLabel.setStyle("-fx-font-weight: normal; -fx-font-size: 14px; -fx-padding: 0; -fx-margin: 0;");
	    	valueLabel.setStyle("-fx-font-weight: normal; -fx-font-size: 14px; -fx-padding: 0; -fx-margin: 0;");
	    }
	    
	    HBox line = new HBox(textLabel, valueLabel);
	    line.setAlignment(Pos.CENTER_LEFT); // Aligne le contenu
	    return line;
	}
	
	private double getShippingPrice(String shippingMethod) {	
        double shippingPrice = 0.0;
		if (shippingMethod != null) {
		    if (shippingMethod.contains("UPS Domicile")) {
		        shippingPrice = 9.00;
		    } else if (shippingMethod.contains("Colissimo mon domicile")) {
		        shippingPrice = 4.00;
		    } else if (shippingMethod.contains("Chronopost")) {
		        shippingPrice = 15.00;
		    } else if (shippingMethod.contains("Retrait en magasin TennisShop")) {
		        shippingPrice = 0.00;
		    }
		}		
		return shippingPrice;
	}
}
