package Interface;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import customer.Customer;
import customer.Invoice;
import database.AdminStatsDAO;
import database.CustomerDAO;
import database.InvoiceDAO;
import database.ProductDAO;
import database.SendEmail;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import products.Shoes;
import products.Product;
import products.ProductWithSize;
import products.Clothing;


/**
 * Classe représentant la vue administrateur de l'application.
 * Elle permet la gestion des statistiques, des clients, des stocks et des factures.
 */
public class AdminView {
	
	AdminStatsDAO adminStatsDAO;
	VBox mainContent;
	MainView mainView;
	
	/**
     * Constructeur de la classe AdminView.
     * Initialise l'interface administrateur et configure la scène.
     *
     * @param mainView La vue principale de l'application.
     */
	public AdminView(MainView mainView) {        
        AnchorPane rootPane = new AnchorPane();     
        HeaderView v = new HeaderView(mainView);
        this.mainView = mainView;
        
        rootPane.getChildren().addAll(v.getHeader(), createLeftMenu(), createMainSection());
        
        Scene adminScene = new Scene(rootPane, 1368, 690);
        adminScene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
        mainView.getPrimaryStage().setScene(adminScene);
    }
	
	/**
     * Crée le menu latéral gauche contenant les boutons de navigation.
     *
     * @return Un VBox contenant le menu.
     */
	public VBox createLeftMenu() {
		VBox menuBox = new VBox(20);
		
		AnchorPane.setTopAnchor(menuBox, 119.0);
		AnchorPane.setBottomAnchor(menuBox, 0.0);
		
		menuBox.setPadding(new Insets(20));
	    menuBox.setStyle("-fx-background-color: linear-gradient(to bottom right, #2d658c, #2ca772)");
	    
		//#F8F8F8
		Button statsButton = new Button("Statistiques");
		Button customersButton = new Button("Clients");
		Button stockManagementButton = new Button("Gestion des stocks");
		Button editInvoiceButton = new Button("Modifier les factures");
		Button logoutButton = new Button("Déconnexion");
		
		addIcon(statsButton, "monitoringIcon");
		addIcon(customersButton, "manageAccountIcon2");
		addIcon(stockManagementButton, "stockIcon");
		addIcon(editInvoiceButton, "invoiceIcon");
		addIcon(logoutButton, "logoutIcon");
		
		logoutButton.setPrefWidth(220.0);
		logoutButton.setStyle("-fx-background-color: #2d658c; -fx-border-color: WHITE");
		
		for (Button button : new Button[]{statsButton, customersButton, stockManagementButton, editInvoiceButton}) {
			button.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -text-fill:#fff; -fx-padding: 7; -fx-border-color: transparent");
			button.setPrefWidth(220.0);
			button.setOnMouseEntered(event -> {
				button.setStyle("-fx-background-color: #3A7F9C; -fx-border-color: WHITE; -fx-padding: 7;");
			});
	        button.setOnMouseExited(event -> {
	            button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 7;-fx-padding: 7;");
	        });
	    }
		
		statsButton.setOnAction(e -> showStats());
		customersButton.setOnAction(e -> showClients());
		stockManagementButton.setOnAction(e -> manageStocks());
		editInvoiceButton.setOnAction(e -> manageInvoice());
		logoutButton.setOnAction(e -> {
        	MainView.setCurrentCustomer(null);
        	mainView.showProductView(Product.class, null);
        });
		
		menuBox.getChildren().addAll(statsButton, customersButton, stockManagementButton, editInvoiceButton, logoutButton);
		return menuBox;
	}
	
	/**
     * Ajoute une icône à un bouton spécifique.
     *
     * @param button   Le bouton auquel ajouter l'icône.
     * @param iconName Le nom du fichier icône.
     */
	private void addIcon(Button button, String iconName) {
		ImageView icon = new ImageView(new Image(getClass().getResource("/Image/Icons/"+iconName+".png").toExternalForm()));
		icon.setFitHeight(20);
		icon.setFitWidth(20);
		button.setGraphic(icon);
	}
	
	
	/**
     * Crée la section principale avec un ScrollPane.
     *
     * @return Un ScrollPane contenant la section principale.
     */
	public ScrollPane createMainSection() {
		mainContent = new VBox(20);
	    mainContent.setStyle("-fx-background-color: derive(#ececec,26.4%); -fx-padding: 20;");
		//AnchorPane.setLeftAnchor(mainContent, 280.0);
		//AnchorPane.setTopAnchor(mainContent, 180.0);
		
		ScrollPane scrollPane = MainView.createScrollPane(mainContent);
		AnchorPane.setLeftAnchor(scrollPane, 250.0);
	    AnchorPane.setRightAnchor(scrollPane, 0.0); // S'assurer qu'il occupe tout l'espace horizontal
	    //AnchorPane.setTopAnchor(scrollPane, 116.0);
	    //scrollPane.setStyle("-fx-background-color: rgba(0,0,0, 0.6)");
		
		adminStatsDAO = new AdminStatsDAO();
		showStats();
		return scrollPane;
	}
	
	
	/**
     * Affiche les statistiques clés de l'administrateur.
     */
	private void showStats() {
		VBox topVBox = new VBox();
		Label topVBoxTitle = new Label("CHIFFRES CLES");
		HBox topHBox = new HBox();
		
		topVBox.setStyle("-fx-padding: 10; -fx-background-color: #FFFFFF; -fx-background-radius:10;");		
		topVBoxTitle.setStyle("-fx-font-size: 16px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5)");
		topHBox.setStyle("-fx-alignment: center; -fx-spacing:20; -fx-background-color: #FFFFFF");
		
		
		Label totalProductsLabel = new Label("Nombre total de produits ");
		Label totalProducts = new Label(""+adminStatsDAO.getTotalProducts());
		Label totalRevenueLabel = new Label("Chiffre d'affaires");
		Label totalRevenue = new Label(""+adminStatsDAO.getTotalRevenue()+" €");
		Label totalOrdersLabel = new Label("Nombre total de commandes ");
		Label totalOrders = new Label(""+adminStatsDAO.getTotalOrders());
		
		VBox a = new VBox();
		a.setStyle("-fx-alignment: center;");
		a.setPrefSize(250, 100);
		a.getChildren().addAll(totalProducts, totalProductsLabel);
		
		VBox b = new VBox();
		b.setStyle("-fx-alignment: center;");
		b.setPrefSize(250, 100);
		b.getChildren().addAll(totalRevenue, totalRevenueLabel);
		
		VBox c = new VBox();
		c.setStyle("-fx-alignment: center;");
		c.setPrefSize(250, 100);
		c.getChildren().addAll(totalOrders, totalOrdersLabel);
					
		Pane pane1 = new Pane();
		pane1.setPrefSize(1, 50);
		pane1.setStyle("-fx-background-color: #ececec");		
						
		Pane pane2 = new Pane();
		pane2.setPrefSize(1, 50);
		pane2.setStyle("-fx-background-color: #ececec");
		
		topHBox.getChildren().addAll(a, pane1, b, pane2, c);		
		topVBox.getChildren().addAll(topVBoxTitle, topHBox);
		
		
	
		Label salesByPeriod = new Label("Évolution des ventes par mois " );
		Label bestProductsLabel = new Label("Produits les plus vendus");
		Label averageOrder = new Label(""+adminStatsDAO.getAverageOrderValue() + " €");
		Label averageOrderLabel = new Label("Montant moyen d'une commande");
		Label salesqtByPeriod = new Label("Évolution des ventes par mois " );
		
		LineChart<String, Number> salesLineChart= adminStatsDAO.getSalesByPeriodLineChart(); // Graphique des ventes par mois (en euros)
		BarChart<String, Number> barChart = adminStatsDAO.getSalesByBrandBarChart(); //Histogramme des ventes par marques (montant et quantité)
		PieChart pieChart = adminStatsDAO.getTypeProductsPieChart(); // Camembert des ventes par catégories de produits
		TableView<Map.Entry<String, Integer>> tableView = createBestProductsTable(); //Table des produits les plus vendus (nom et quantité)
		LineChart<String, Number> salesqtLineChart= adminStatsDAO.getSalesQtyLineChart();
		
		VBox d = createVBox(salesByPeriod, salesLineChart);
		VBox e = createVBox(new Label("Répartition des produits vendus par catégories"), pieChart);
		VBox f = createVBox(new Label("Ventes par marques"), barChart);
		VBox g = new VBox(bestProductsLabel, tableView);
		VBox h = new VBox(); h.getChildren().addAll(averageOrder, averageOrderLabel);
		VBox i = createVBox(salesqtByPeriod, salesqtLineChart);
		
		HBox midHBox = new HBox(10);
		midHBox.setSpacing(30);
		midHBox.setPrefSize(350, 300);
		midHBox.getChildren().addAll(d, e);
		
		HBox midHBox2 = new HBox(10);
		midHBox2.setSpacing(30);
		midHBox2.setPrefSize(350, 300);
		midHBox2.getChildren().addAll(f, g);
		
		HBox bottomHBox = new HBox(10);
		bottomHBox.setSpacing(30);
		bottomHBox.getChildren().addAll(i, h);
		bottomHBox.setPrefSize(250, 100);
			
		g.setPrefSize(500, 300);
		//h.setPrefSize(250, 100);
		g.setStyle("-fx-padding: 10;-fx-spacing:10;-fx-background-color: #FFFFFF; -fx-background-radius:10;");
		h.setStyle("-fx-alignment: center;-fx-padding: 10;-fx-spacing:10;-fx-background-color: #FFFFFF; -fx-background-radius:10;");
		
		
		for (Label data : new Label[]{totalProducts, totalRevenue, totalOrders, averageOrder}) {
            data.setStyle("-fx-font-size: 32px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");
            //-fx-alignment: CENTER_LEFT;
      	  	//label.setPrefWidth(220.0);
		}
		
		for (Label dataLabel : new Label[]{totalProductsLabel, totalRevenueLabel, totalOrdersLabel, averageOrderLabel}) {
            dataLabel.setStyle("-fx-font-size: 16px;-fx-font-weight: normal;-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");
		}
		
		
		mainContent.getChildren().setAll(topVBox, midHBox, midHBox2, bottomHBox);
	}
	
	 /**
     * Affiche la liste des clients.
     */
	private void showClients() {
		HBox statsUsersBox = getStatsUsersBox();
		VBox customerTableView = getCustomerTableBox();
		mainContent.getChildren().setAll(statsUsersBox, customerTableView);
	}
	
	/**
     * Gère l'affichage et la modification des stocks.
     */
	private void manageStocks() {
		VBox editProductsBox = editProducts();
		VBox outOfStockProductBox = getOutOfStockProductTable();
		mainContent.getChildren().setAll(editProductsBox, outOfStockProductBox);
	}
	
	 /**
     * Gère l'affichage et la modification des factures.
     */
	private void manageInvoice() {
		mainContent.getChildren().setAll(editInvoiceBox());
	}

	/**
     * Crée un VBox contenant un graphique et un titre.
     *
     * @param label Le titre du graphique.
     * @param chart Le graphique à afficher.
     * @return Un VBox contenant le titre et le graphique.
     */
	private VBox createVBox(Label label, Chart chart) {
		VBox vBox = new VBox();
		vBox.getChildren().addAll(label, chart);
		vBox.setStyle("-fx-padding: 10;-fx-spacing:10;-fx-background-color: #FFFFFF; -fx-background-radius:10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5)");
		return vBox;
	}
	
	
	
	// STATISTIQUES
		
	
	/**
    * Crée une table affichant les produits les plus vendus.
    *
    * @return Une TableView contenant les produits les plus vendus.
    */
	private TableView<Map.Entry<String, Integer>> createBestProductsTable() {
		TableView<Map.Entry<String, Integer>> tableView = new TableView<>();
        // Configurer les colonnes
        TableColumn<Map.Entry<String, Integer>, String> productNameColumn = new TableColumn<>("Produit");
        productNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKey()));
        productNameColumn.setMinWidth(365);

        TableColumn<Map.Entry<String, Integer>, Integer> totalSoldColumn = new TableColumn<>("Ventes");
        totalSoldColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getValue()).asObject());
        totalSoldColumn.setMinWidth(100);

        // Ajouter les colonnes à la TableView
        tableView.getColumns().add(productNameColumn);
        tableView.getColumns().add(totalSoldColumn);

        // Charger les données dans la TableView
        tableView.setItems(adminStatsDAO.getTopSellingProducts());
        
        return tableView;
	}
	
	
	
	// GESTION DES CLIENTS
	
	
	/**
     * Obtient les statistiques des utilisateurs sous forme de boîte horizontale.
     *
     * @return Un HBox contenant les statistiques des utilisateurs.
     */
	private HBox getStatsUsersBox() {
		Label totalUsers = new Label("Nombre total de clients " );	
		Label total = new Label(""+ adminStatsDAO.getTotalUsers());
		VBox totalUsersBox = new VBox();
		total.setStyle("-fx-font-size: 32px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");
		totalUsersBox.setStyle("-fx-alignment: center;-fx-padding: 10;-fx-spacing: 10;-fx-background-color: #FFFFFF; -fx-background-radius:10;");
		//h.setPrefSize(250, 100);
		totalUsersBox.getChildren().addAll(total, totalUsers);
		
		Label activeUsers = new Label("                          Clients actifs\n(ayant commandé dans les 30 derniers jours)");	
		Label totalActive = new Label("" + adminStatsDAO.getTotalActiveUsers());
		VBox activeUsersBox = new VBox();
		totalActive.setStyle("-fx-font-size: 32px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5); -fx-padding: 10 0 5 0;");
		activeUsersBox.setStyle("-fx-alignment: center;-fx-padding: 10 10 0 10;-fx-spacing: 1; -fx-background-color: #FFFFFF; -fx-background-radius:10;");
		//h.setPrefSize(250, 100);
		activeUsersBox.getChildren().addAll(totalActive, activeUsers);
		
		HBox box = new HBox();
		box.getChildren().addAll(totalUsersBox, activeUsersBox);
		box.setStyle("-fx-alignment: center; -fx-spacing:20;");
		
		return box;	
	}
	
	
	/**
     * Crée un VBox contenant la table des clients et un bouton d'ajout.
     *
     * @return Un VBox contenant la table des clients et un bouton d'ajout.
     */
	private VBox getCustomerTableBox() {
        VBox box = new VBox();
		box.setStyle("-fx-padding: 10;-fx-spacing:10;-fx-background-color: #FFFFFF; -fx-background-radius:10;");
		
		TableView<Customer> customerTableView = getCustomerTableView();
		Button addButton = new Button("+ Ajouter un client");
        addButton.setStyle("-fx-background-color : #007bff; -fx-text-fill: white; -fx-pref-height: 20px");
        addButton.setOnAction(event -> new SignUpView(mainView));
        VBox.setMargin(addButton, new Insets(0, 0, 0, 700));
        
		box.getChildren().addAll(customerTableView, addButton);
		return box;	
	}
	
	/**
     * Crée et configure une table affichant la liste des clients.
     *
     * @return Une TableView contenant les clients.
     */
	private TableView<Customer> getCustomerTableView() {
		CustomerDAO customerDAO = new CustomerDAO();
	    ObservableList<Customer> customersList = FXCollections.observableArrayList(customerDAO.getAllCustomers());
	    TableView<Customer> tableView = new TableView<>();
	    tableView.setId("customerTable");
	    tableView.setMaxHeight(300);

	    TableColumn<Customer, Integer> colCustomerId = new TableColumn<>("ID");
	    colCustomerId.setCellValueFactory(new PropertyValueFactory<>("id"));

	    TableColumn<Customer, String> colFirstName = new TableColumn<>("Prénom");
	    colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

	    TableColumn<Customer, String> colLastName = new TableColumn<>("Nom");
	    colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

	    TableColumn<Customer, String> colCivility = new TableColumn<>("Civilité");
	    colCivility.setCellValueFactory(new PropertyValueFactory<>("civility"));

	    TableColumn<Customer, String> colEmail = new TableColumn<>("Email");
	    colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

	    TableColumn<Customer, String> colPhoneNumber = new TableColumn<>("Téléphone");
	    colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

	    TableColumn<Customer, String> colPassword = new TableColumn<>("Mot de passe");
	    colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));

	    TableColumn<Customer, String> colRole = new TableColumn<>("Rôle");
	    colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

	    TableColumn<Customer, String> colAddress = new TableColumn<>("Adresse");
	    colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

	    TableColumn<Customer, Void> actionColumn = new TableColumn<>("Action");
	    actionColumn.setCellFactory(param -> new TableCell<>() {
	        private final Button deleteCustomerButton = new Button();
	        private final Button editCustomerButton = new Button();
	        private final HBox buttonContainer = new HBox(10); // Conteneur pour regrouper les boutons
	        {
	            // Configuration du bouton Supprimer
	            ImageView binIcon = new ImageView(new Image(getClass().getResource("/Image/Icons/binIcon.png").toExternalForm()));
	            binIcon.setFitHeight(20);
	            binIcon.setFitWidth(20);
	            deleteCustomerButton.setGraphic(binIcon);
	            deleteCustomerButton.setId("roundButton");
	            deleteCustomerButton.setStyle("-fx-background-color: red;");
	            deleteCustomerButton.setOnAction(event -> {
	                // Supprimer le client correspondant à cette ligne
	                customerDAO.deleteCustomer(getTableView().getItems().get(getIndex()));
	                // Mettre à jour la TableView
	                tableView.setItems(FXCollections.observableArrayList(customerDAO.getAllCustomers()));
	            });

	            // Configuration du bouton Modifier
	            ImageView editIcon = new ImageView(new Image(getClass().getResource("/Image/Icons/editIcon.png").toExternalForm()));
	            editIcon.setFitHeight(20);
	            editIcon.setFitWidth(20);
	            editCustomerButton.setGraphic(editIcon);
	            editCustomerButton.setId("roundButton");
	            editCustomerButton.setOnAction(event -> {
	                // Affichage pour modifier le client correspondant à la ligne sélectionnée
	                Customer customer = getTableView().getItems().get(getIndex());
	                VBox editCustomerInfoBox = AccountView.editCustomerInfo(customer);
	                mainContent.getChildren().setAll(editCustomerInfoBox);
	                tableView.setItems(FXCollections.observableArrayList(customerDAO.getAllCustomers()));
	            });

	            // Ajouter les boutons au conteneur
	            buttonContainer.getChildren().addAll(deleteCustomerButton, editCustomerButton);
	            buttonContainer.setAlignment(Pos.CENTER); // Centrer les boutons
	        }

	        @Override
	        protected void updateItem(Void item, boolean empty) {
	            super.updateItem(item, empty);
	            if (empty) {
	                setGraphic(null); // Pas de boutons pour les lignes vides
	            } else {
	                setGraphic(buttonContainer); // Afficher le conteneur de boutons pour les lignes valides
	            }
	        }
	    });

	    // Ajout des colonnes à la table	    
	    tableView.getColumns().add(colCustomerId);
        tableView.getColumns().add(colFirstName);
        tableView.getColumns().add(colLastName);
        tableView.getColumns().add(colCivility);
        tableView.getColumns().add(colEmail);
        tableView.getColumns().add(colPhoneNumber);
        tableView.getColumns().add(colPassword);
        tableView.getColumns().add(colRole);
        tableView.getColumns().add(colAddress);
        tableView.getColumns().add(actionColumn);

	    // Associer les données à la table
	    tableView.setItems(customersList);

	    return tableView;
	}
	
	
	// MODIFIER PRODUITS et STOCKS
	
	private TextField idField, nameField, descriptionField, brandField, priceField, qtyField, genderField, colorField;
	private String name, description, type, brand, imagePath;
	private Button addButton, deleteButton, clearButton,updateButton, importButton;
	private double price;
	private Label idLabel, typeLabel, sizeLabel, qtyLabel, surfaceLabel, genderLabel, colorLabel, typeVLabel;
	private int qty;
    private ImageView imageView;
    private VBox a;
	private ComboBox<String> typeComboBox, sizeComboBox, surfaceComboBox, typeVComboBox;
	private TableView<Product> productTableView;
	private ProductDAO productDAO = new ProductDAO();
	private GridPane productGridPane;
	HashMap<String, Integer> sizesStock;
	
	
	/**
     * Crée et retourne l'interface d'édition des produits.
     * @return VBox contenant les composants d'édition de produits.
     */
	private VBox editProducts() {
		VBox editProductsBox = new VBox();
		editProductsBox.setStyle("-fx-padding: 10;-fx-spacing:10;-fx-background-color: #FFFFFF; -fx-background-radius:10;");
		
		// TextField
		idField = new TextField();
    	nameField = new TextField();
    	descriptionField = new TextField();
    	typeComboBox =  getComboBox();
    	brandField = new TextField();
    	priceField = new TextField();
    	qtyField = new TextField();
    	
    	// Specifique à un type de produit
    	surfaceComboBox = new ComboBox<String>();
    	surfaceComboBox.getItems().addAll("TOUTES SURFACES","terre battue","dur","gazon");
    	genderField = new TextField();
    	colorField = new TextField();
    	typeVComboBox = new ComboBox<String>();
    	typeVComboBox.getItems().addAll("Short","Sweat","Debardeur","Tshirt","Robe","Veste");
    	
    	for (TextField txt : new TextField[]{idField, nameField, descriptionField, brandField, priceField, qtyField, genderField, colorField}) {
            txt.setStyle("-fx-pref-height: 30px;");
		}
    	
    	// ImageView
    	imageView = new ImageView();
    	imageView.setFitWidth(90);  // Largeur fixe
        imageView.setFitHeight(90); // Hauteur fixe
        imageView.setPreserveRatio(true);    
    	
    	//VBox qui permet de réserver un espace pour l'imageView
        a = new VBox();
        a.setPrefSize(50, 50);
        a.setStyle("-fx-background-color:transparent;-fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 5;");
        
        // Ajout des boutons
        addButton = new Button("+ Ajouter un produit");
        addButton.setStyle("-fx-background-color : #007bff; -fx-text-fill: white;");
        
        deleteButton = new Button();
        deleteButton.setStyle("-fx-background-color : red;");
        ImageView binIcon = new ImageView(new Image(getClass().getResource("/Image/Icons/binIcon.png").toExternalForm()));
        binIcon.setFitHeight(20);
        binIcon.setFitWidth(20);
        deleteButton.setGraphic(binIcon);
        
        updateButton = new Button("Mettre à jour");
        clearButton = new Button("Effacer");
        importButton = getImportImageButton();

        addButton.setOnAction(event -> addProduct());
        deleteButton.setOnAction(event -> deleteProduct());
        updateButton.setOnAction(event -> updateProduct());
        clearButton.setOnAction(event -> clearField());
        
        // Ajout de gridPane et table des produits
        productGridPane = createProductGridPane();
        productTableView = getProductTableView();
        editProductsBox.getChildren().addAll(productGridPane, productTableView);
		return editProductsBox;
	}
	
	
	/**
     * Crée et configure la grille pour l'édition des produits.
     * @return GridPane contenant les champs et boutons d'édition de produits.
     */
	private GridPane createProductGridPane() {
		GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(15));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 5;");
        
        idLabel = new Label("ID");
        Label nameLabel = new Label("Nom");
        Label descriptionLabel = new Label("Description");
        typeLabel = new Label("Type");
        Label brandLabel = new Label("Marque");
        Label priceLabel = new Label("Prix");
        sizeLabel = new Label("Taille");
        qtyLabel = new Label("Quantité");
        
        sizeLabel.setVisible(false);
        sizeComboBox.setVisible(false);        
        
        gridPane.addRow(0, idLabel, idField, priceLabel, priceField);
        gridPane.addRow(1, nameLabel, nameField, qtyLabel, qtyField);
        gridPane.addRow(2, descriptionLabel);
        gridPane.add(descriptionField, 1, 2, 3, 1); //colonne, ligne, nb col occupe
        gridPane.addRow(3, typeLabel, typeComboBox, sizeLabel, sizeComboBox);
        gridPane.addRow(4, brandLabel, brandField);
        gridPane.add(a, 4, 0, 1, 2);
        gridPane.add(imageView, 4, 0, 1, 2);
        GridPane.setHalignment(imageView, HPos.CENTER); // Alignement horizontal
        GridPane.setValignment(imageView, VPos.CENTER);
        gridPane.add(importButton, 4, 2, 1, 1);
        
        gridPane.add(clearButton, 0, 5, 1, 1);
        gridPane.add(updateButton, 1, 5, 1, 1);
        gridPane.add(deleteButton, 2, 5, 1, 1);
        gridPane.add(addButton, 3, 5, 2, 1);
        
        
        // Specifique à un type de produit
        surfaceLabel = new Label("Surface");
        genderLabel = new Label("Genre");
        colorLabel = new Label("Couleur");
        typeVLabel = new Label("Cat.");
        
        hideShoesComponents(); hideClothesComponents();
              
        gridPane.add(surfaceLabel, 5, 0);
        gridPane.add(typeVLabel, 5, 0);
        gridPane.add(genderLabel, 5, 1);
        gridPane.add(colorLabel, 5, 2);
        gridPane.add(surfaceComboBox, 6, 0);
        gridPane.add(typeVComboBox, 6, 0);
        gridPane.add(genderField, 6, 1);
        gridPane.add(colorField, 6, 2);
                      
        return gridPane;
	}
	

	/**
     * Crée et retourne une ComboBox permettant de sélectionner un type de produit (chaussures, vêtement ou sac). 
     * Selon le type sélectionné, une ComboBox de taille adaptée (chaussures ou vêtements) sera affichée, 
     * permettant de sélectionner une taille. Des composants associés à chaque catégorie de produit sont également 
     * affichés ou masqués dynamiquement.
     *
     * @return ComboBox La ComboBox permettant de sélectionner le type de produit.
     */
    public ComboBox<String> getComboBox(){
        ComboBox<String> typeComboBox = new ComboBox<String>();
        sizeComboBox = new ComboBox<>();
        
        typeComboBox.getItems().addAll("chaussures", "vetement", "sac");
        
        // Ajouter une nouvelle ComboBox pour afficher taille vetements ou chaussures
        typeComboBox.setOnAction(event -> {
            String selectedCategory = typeComboBox.getValue();
            
            sizeLabel.setVisible(true);
            sizeComboBox.getItems().clear();
            sizeComboBox.setVisible(true);
            productGridPane.getChildren().removeAll(qtyLabel, qtyField);
            productGridPane.add(qtyLabel, 2, 4); // Nouvelle position pour quantité
            productGridPane.add(qtyField, 3, 4);

            if ("chaussures".equals(selectedCategory)) {
                sizeComboBox.getItems().addAll("36", "37", "38", "39");
                hideClothesComponents();
                showShoesComponents();
                
            } else if ("vetement".equals(selectedCategory)) {
                sizeComboBox.getItems().addAll("S", "M", "L", "XL");
                hideShoesComponents();
                showClothesComponents();
            }
        });
        
        sizeComboBox.setOnAction(event -> {
        	String selectedSize = sizeComboBox.getValue();
        	if (sizesStock != null) {
        	for (Map.Entry<String, Integer> entry : sizesStock.entrySet()) {
        		if (entry.getKey().equals(selectedSize)) {
                    qtyField.setText(String.valueOf(entry.getValue()));
        		}
        	}       
        	}	
        });
        return typeComboBox;
    }    
	
    
    /**
     * Ajoute un produit à la base de données après vérification.
     */
    private void addProduct() {
    	clearField();
    	
    	// Cacher des éléments et tableView
    	productGridPane.getChildren().removeAll(idLabel, idField, typeLabel, typeComboBox, addButton);
    	deleteButton.setVisible(false);
    	updateButton.setVisible(false);
    	productTableView.setVisible(false);
    	
    	// Nouvelle position pour le champ type (à la position du champ id)
    	productGridPane.add(typeLabel, 0, 0);
    	productGridPane.add(typeComboBox, 1, 0);
        
        // Ajouter un bouton permettant d'ajouter le produit dans la bdd    
        Button addProductButton = new Button("Ajouter");
        productGridPane.add(addProductButton, 5, 4, 2, 1);

        addProductButton.setOnAction(event ->{
        	// Ajouter le produit à la bdd
        	if(!checkIfEmpty() && checkIfValid()) {
        		getTextFieldValues();
        		boolean hasError = false;
        		
        		if (type == "chaussures") {
        			if (surfaceComboBox.getSelectionModel().getSelectedItem() == null || genderField.getText().isEmpty() || !genderField.getText().matches("^(Homme|Femme)$") || colorField.getText().isEmpty() || !colorField.getText().matches("[a-zA-Z\\s'-]+") ||  sizeComboBox.getSelectionModel().getSelectedItem() == null) {
        				MainView.showAlert("Erreur", null, "Merci de remplir tous les champs avec des valeurs valides pour le genre (Homme, Femme) et la couleur.", AlertType.ERROR);
        				hasError = true;
        			}
        			else {
        				Product product = new Product(0, name, description, type, brand, price, imagePath);
                		int id = productDAO.insertProduct(product);
        				productDAO.insertShoes(id, surfaceComboBox.getValue(), genderField.getText(), colorField.getText(), sizeComboBox.getValue(), qty);
        			}
        		}
        		
        		else if (type == "vetement"){
        			if (genderField.getText().isEmpty() || !genderField.getText().matches("^(Homme|Femme)$")  || colorField.getText().isEmpty() || !colorField.getText().matches("[a-zA-Z\\s'-]+") || typeVComboBox.getSelectionModel().getSelectedItem() == null || sizeComboBox.getSelectionModel().getSelectedItem() == null) {
        				MainView.showAlert("Erreur", null, "Merci de remplir tous les champs avec des valeurs valides pour le genre (Homme, Femme) et la couleur. ", AlertType.ERROR);
        				hasError = true;
        			}
        			else {
        				Product product = new Product(0, name, description, type, brand, price, imagePath);
                		int id = productDAO.insertProduct(product);
        				productDAO.insertVetement(id, typeVComboBox.getValue(), genderField.getText(), colorField.getText(), sizeComboBox.getValue(), qty);
        			}
        			
        		}
        		if (!hasError) {
        		// Afficher l'affichage par défaut pour manageProduct
        		productGridPane.getChildren().removeAll(addProductButton);
        		deleteButton.setVisible(true);
        		updateButton.setVisible(true);
        		productTableView.setVisible(true);
        		clearField();
        		
        		// Mettre à jour la table
        		productTableView.setItems(FXCollections.observableArrayList(productDAO.getAllProducts()));
        		}
        	}
        });
    }
    
    /**
     * Met à jour les informations d'un produit sélectionné.
     */
    public void updateProduct(){
    	if(!idField.getText().isEmpty() && idField.getText().matches("\\d+") && !checkIfEmpty() && checkIfValid()) {
    		int id = Integer.parseInt(idField.getText());
    		getTextFieldValues();
    		
    		if (type == "chaussures") {
        		if (surfaceComboBox.getSelectionModel().getSelectedItem() == null || genderField.getText().isEmpty() || !genderField.getText().matches("^(Homme|Femme)$") || colorField.getText().isEmpty() || !colorField.getText().matches("[a-zA-Z\\s'-,]+") || sizeComboBox.getSelectionModel().getSelectedItem() == null) {
        			MainView.showAlert("Erreur", null, "Merci de remplir tous les champs avec des valeurs valides.", AlertType.ERROR);
        		}
        		else{
            		Product product = new Product(id, name, description, type, brand, price, imagePath);
            		productDAO.updateProduct(product);
        			productDAO.updateShoes(id, surfaceComboBox.getValue(), genderField.getText(), colorField.getText(), sizeComboBox.getValue(), qty);
        		}
    		}
    		
    		else if (type == "vetement"){
    			if (genderField.getText().isEmpty() || !genderField.getText().matches("^(Homme|Femme)$") || colorField.getText().isEmpty() || !colorField.getText().matches("[a-zA-Z\\s'-,]+")|| typeVComboBox.getSelectionModel().getSelectedItem() == null || sizeComboBox.getSelectionModel().getSelectedItem() == null) {
    				MainView.showAlert("Erreur", null, "Merci de remplir tous les champs avec des valeurs valides.", AlertType.ERROR);
    			}
    			else {
    	    		Product product = new Product(id, name, description, type, brand, price, imagePath);
    	    		productDAO.updateProduct(product);
    				productDAO.updateVetement(id, typeVComboBox.getValue(), genderField.getText(), colorField.getText(), sizeComboBox.getValue(), qty);
    			}
    		}
    		
            clearField();
            productTableView.setItems(FXCollections.observableArrayList(productDAO.getAllProducts()));
        }
    }
    
    /**
     * Supprime un produit sélectionné de la base de données.
     */
    public void deleteProduct(){ 
    	if(!checkIfEmpty()) {
        	productDAO.deleteProduct(Integer.parseInt(idField.getText()));
        	productTableView.setItems(FXCollections.observableArrayList(productDAO.getAllProducts()));
        	clearField();      
        }
    }
    
    /**
     * Retourne le bouton permettant d'importer une image pour un produit.
     * @return Bouton configuré pour importer une image.
     */
    public Button getImportImageButton(){       
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Image File");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Image File", "*jpg", "*png"));
        
        Button button = new Button("Importer");
        //imageView = new ImageView();
        
        button.setOnAction(event -> {
            // Ouvrir le FileChooser et récupérer le fichier sélectionné
            File selectedFile = fileChooser.showOpenDialog(mainView.getPrimaryStage());

            try {
                // 1. Renommer le fichier (ajout d'un timestamp)
                String newFileName = "image_" + System.currentTimeMillis() + ".png";
                String resourcePath = "./resources/Image/";
                imagePath = "/Image/" + newFileName;
                File destFile = new File(resourcePath + newFileName);

                // 2. Copier le fichier dans le dossier de ressources
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // 3. Charger l'image et l'afficher dans l'ImageView
                Image image = new Image(destFile.toURI().toString());
                imageView.setImage(image);

                // 4. Afficher une confirmation et le chemin de l'image
                MainView.showAlert("Information Message", null, "Image importée et enregistrée à : " + destFile.getAbsolutePath(), AlertType.INFORMATION);
            } catch (IOException e) {
                e.printStackTrace();
                MainView.showAlert("Erreur", "Impossible d'importer l'image", e.getMessage(), AlertType.ERROR);
            }
    });       
        return button;
    }
    
    /**
     * Retourne une table affichant tous les produits enregistrés.
     * @return TableView contenant la liste des produits.
     */
    public TableView<Product> getProductTableView(){
    	ObservableList<Product> productsList = FXCollections.observableArrayList(productDAO.getAllProducts());
    	TableView<Product> tableView = new TableView<>();
        
        TableColumn<Product, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Product, String> colName = new TableColumn<>("Nom");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<Product, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Product, String> colBrand = new TableColumn<>("Marque");
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn<Product, Double> colPrice = new TableColumn<>("Prix");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        TableColumn<Product, String> colDescription = new TableColumn<>("Description");
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDescription.setStyle("-fx-alignment: CENTER-LEFT;");
        
        tableView.getColumns().add(colId);
        tableView.getColumns().add(colName);
        tableView.getColumns().add(colType);
        tableView.getColumns().add(colBrand);
        tableView.getColumns().add(colPrice);
        tableView.getColumns().add(colDescription);
        tableView.setItems(productsList);
    
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        	if (newValue != null) { // Vérifie qu'une ligne est sélectionnée
        		productSelect();
        	}
        });
    
        return tableView;
    }
       
    
    /**
     * Met à jour les champs du formulaire avec les informations du produit sélectionné dans la table.
     * Les composants sont ensuite ajustés en fonction du type de produit (chaussures, vêtements, etc.).
     */
    private void productSelect(){
        Product product = productTableView.getSelectionModel().getSelectedItem();
        
        idField.setText(String.valueOf(product.getId()));
        nameField.setText(product.getName());
        descriptionField.setText(product.getDescription());
        typeComboBox.setValue(product.getType());
        brandField.setText(product.getBrand());
        priceField.setText(String.valueOf(product.getPrice()));
        qtyField.setText("");
        sizeComboBox.getSelectionModel().clearSelection();
        
        if (product instanceof ProductWithSize) {
        	ProductWithSize productWithSize = (ProductWithSize) product;
        	sizesStock = productWithSize.getSizeStock();
        }
        
        if (product instanceof Shoes) {
        	hideClothesComponents();
        	showShoesComponents();;
        	surfaceComboBox.setValue(((Shoes)product).getSurface());
            genderField.setText(((Shoes)product).getGender());
            colorField.setText(((Shoes)product).getColor());
        }
        
        if (product instanceof Clothing) {
        	hideShoesComponents();
        	showClothesComponents();
        	typeVComboBox.setValue(((Clothing)product).getTypeVetement().toString());
            genderField.setText(((Clothing)product).getGender());
            colorField.setText(((Clothing)product).getColor());
        }
        
        imagePath = product.getImagePath();
        imageView.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));   
    }
    
    
    /**
     * Retourne un VBox contenant la liste des produits en rupture de stock, avec les informations sur les tailles.
     * 
     * @return VBox contenant la liste des produits en rupture de stock.
     */
    private VBox getOutOfStockProductTable() {
    	VBox outOfStockBox = new VBox();
    	outOfStockBox.setStyle("-fx-padding: 10;-fx-spacing:10;-fx-background-color: #FFFFFF; -fx-background-radius:10; -fx-max-width: 700px; -fx-max-height: 300px;");
    	mainContent.setAlignment(Pos.CENTER);
    	
    	Label OutOfStockProductsLabel = new Label("Nombre de produits en rupture de stock : " + adminStatsDAO.getOutOfStockProducts());
    	OutOfStockProductsLabel.setStyle("-fx-text-fill: red; fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");
    	
    	// Récupérer les produits en rupture avec leur taille
        Map<Product, List<String>> productIds = adminStatsDAO.getOutOfStockProductInfo();
        
        ObservableList<Map.Entry<Product, List<String>>> observableList = FXCollections.observableArrayList(productIds.entrySet());

        TableView<Map.Entry<Product, List<String>>> tableView = new TableView<>();

        TableColumn<Map.Entry<Product, List<String>>, Integer> colId = new TableColumn<>("ID Produit");
        TableColumn<Map.Entry<Product, List<String>>, String> colSize = new TableColumn<>("Taille en Rupture");
        TableColumn<Map.Entry<Product, List<String>>, String> colName = new TableColumn<>("Nom");
        
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getKey().getId()).asObject());
        colSize.setCellValueFactory(cellData -> {
            List<String> sizes = cellData.getValue().getValue();
            return new SimpleStringProperty(String.join(", ", sizes)); // Convertir la liste de tailles en une chaîne de caractères séparée par des virgules
        });
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey().getName()));
 
        colId.setMaxWidth(90);
        colSize.setMaxWidth(120);
        colName.setMaxWidth(490);

        tableView.getColumns().add(colId);
        tableView.getColumns().add(colSize);
        tableView.getColumns().add(colName);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setItems(observableList);
        tableView.setId("outofstockTable");
        
        outOfStockBox.getChildren().addAll(OutOfStockProductsLabel, tableView);
        
        return outOfStockBox;
    }
    
    
    /**
     * Efface tous les champs du formulaire, réinitialise les éléments d'interface associés et masque les composants spécifiques.
     */
    public void clearField(){
        idField.setText("");
        nameField.setText("");
        descriptionField.setText("");
        typeComboBox.getSelectionModel().clearSelection();
        brandField.setText("");
        priceField.setText("");
        qtyField.setText("");
        imageView.setImage(null);
        imagePath = "";
        sizesStock = null;
        
        hideShoesComponents(); hideClothesComponents();
        
        sizeLabel.setVisible(false);
        sizeComboBox.getItems().clear();
        sizeComboBox.setVisible(false);
        productGridPane.getChildren().removeAll(qtyLabel, qtyField);
        productGridPane.add(qtyLabel, 2, 1); // Nouvelle position pour quantité
        productGridPane.add(qtyField, 3, 1);
    }
    
    /**
     * Récupère les valeurs des champs de texte et les convertit dans les variables correspondantes.
     */
    private void getTextFieldValues() {
    	name = nameField.getText();
    	description = descriptionField.getText();
    	type = typeComboBox.getValue();
    	brand = brandField.getText();
    	price = Double.parseDouble(priceField.getText());
    	qty = Integer.parseInt(qtyField.getText()); 	
    }
    
    /**
     * Vérifie si tous les champs nécessaires sont remplis.
     * 
     * @return true si l'un des champs est vide, sinon false.
     */
    private boolean checkIfEmpty() {
    	 if(nameField.getText().isEmpty()
                 || descriptionField.getText().isEmpty()
                 || typeComboBox.getSelectionModel().getSelectedItem() == null
                 || brandField.getText().isEmpty()
                 || priceField.getText().isEmpty()
                 || qtyField.getText().isEmpty()
                 || imagePath == null || imagePath == ""){
         	MainView.showAlert("Erreur", null, "Merci de remplir tous les champs", AlertType.ERROR);
    	 	return true;
    	 }
    	 return false;	
    }
    
    /**
     * Vérifie si les valeurs saisies dans les champs de texte sont valides selon les critères définis.
     * 
     * @return true si tous les champs sont valides, sinon false.
     */
    private boolean checkIfValid() {
    	if (nameField.getText().matches("[a-zA-Z0-9\\s'-]+") // Vérifie que le nom contient des lettres, des espaces, des apostrophes ou des tirets
    			&& brandField.getText().matches("[a-zA-Z\\s'-]+") // Vérifie que la marque contient seulement des lettres, des espaces, des apostrophes ou des tirets
    			&& priceField.getText().matches("\\d+(\\.\\d{1,2})?") // Vérifie que le prix est un nombre avec une ou deux décimales
    			&& qtyField.getText().matches("\\d+")) { // Vérifie que la quantité est un nombre entier
    		    return true;
    		}
		MainView.showAlert("Erreur", null, "Merci de remplir tous les champs avec des valeurs valides.", AlertType.ERROR);
    	return false;
   }

    /**
     * Affiche les composants spécifiques pour les chaussures dans l'interface (surface, genre, couleur).
     */
    private void showShoesComponents() {
    	surfaceLabel.setVisible(true);
        genderLabel.setVisible(true);
        colorLabel.setVisible(true);
        
        surfaceComboBox.setVisible(true);
        genderField.setVisible(true);
        colorField.setVisible(true);
    }
    
    /**
     * Affiche les composants spécifiques pour les vêtements dans l'interface (type de vêtement, genre, couleur).
     */
    private void showClothesComponents() {
    	typeVLabel.setVisible(true);
        genderLabel.setVisible(true);
        colorLabel.setVisible(true);
        
        typeVComboBox.setVisible(true);
        genderField.setVisible(true);
        colorField.setVisible(true);
    }
    
    /**
     * Masque les composants spécifiques aux chaussures dans l'interface.
     */
    private void hideShoesComponents() {
    	surfaceLabel.setVisible(false);
        genderLabel.setVisible(false);
        colorLabel.setVisible(false);
        
        surfaceComboBox.setVisible(false);
        genderField.setVisible(false);
        colorField.setVisible(false);
          
        genderField.setText("");
        colorField.setText("");
    }
    
    /**
    * Masque les composants spécifiques aux vêtements dans l'interface.
    */
    private void hideClothesComponents() {
    	typeVLabel.setVisible(false);
        genderLabel.setVisible(false);
        colorLabel.setVisible(false);
        
        typeVComboBox.setVisible(false);
        genderField.setVisible(false);
        colorField.setVisible(false);
        
        typeVComboBox.getSelectionModel().clearSelection();
        genderField.setText("");
        colorField.setText("");
    }
    
    
    
    
    // EDIT INVOICE
    
    Label invoiceIdLabel;
    TextField billingAddressField, shippingAddressField;
    ComboBox<String> shippingMethodComboBox, paymentMethodComboBox;
    Button updateInvoiceButton;
    TableView<Invoice> invoicesTable;
    InvoiceDAO invoiceDAO;
    int invoiceId, orderId;
    String billingAddress, shippingAddress, shippingMethod, paymentMethod;
    GridPane invoiceGridPane;
    
    /**
     * Crée et retourne un conteneur VBox pour l'édition des factures.
     * Ce conteneur inclut une table des factures et un formulaire de modification.
     * 
     * @return VBox contenant les composants d'édition de facture.
     */
    private VBox editInvoiceBox() {
    	invoiceGridPane = createInvoiceGridPane();
    	invoicesTable = getInvoiceTableView();
    	VBox editInvoiceBox = new VBox(invoicesTable, invoiceGridPane);
    	editInvoiceBox.setStyle("-fx-padding: 20;-fx-spacing:10;-fx-background-color: #FFFFFF; -fx-background-radius:10;");
    	return editInvoiceBox;
    }
    
    
    /**
     * Crée et retourne une grille contenant les champs de modification d'une facture.
     * 
     * @return GridPane contenant les champs d'édition de facture.
     */
    private GridPane createInvoiceGridPane() {
    	GridPane gridPane = new GridPane();
    	gridPane.setPadding(new Insets(15));
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 5;");
        
    	invoiceIdLabel = new Label("Identifiant");
    	Label billingAddressLabel = new Label("Adresse de facturation");
    	Label shippingAddressLabel = new Label("Adresse de livraison");
    	Label shippingMethodLabel = new Label("Méthode de livraison");
    	Label paymentMethodLabel = new Label("Méthode de paiement");
        
    	updateInvoiceButton = new Button("Mettre à jour");
    	updateInvoiceButton.setOnAction(event -> {
    		updateInvoice();
    		
    	});
    	
    	billingAddressField = new TextField();
    	shippingAddressField = new TextField();
    	
    	shippingMethodComboBox = new ComboBox<String>();
    	shippingMethodComboBox.getItems().addAll("UPS Domicile 9,00 €", "Colissimo mon domicile	4,00 €", "Chronopost 15,00 €", "Retrait en magasin TennisShop 0,00 €");
    	
    	paymentMethodComboBox = new ComboBox<String>();
    	paymentMethodComboBox.getItems().addAll("Paiement sécurisé par carte bancaire", "Paiement par Paypal", "Paiement par virement bancaire");
    	
    	for (TextField txt : new TextField[]{billingAddressField, shippingAddressField}) {
            txt.setStyle("-fx-pref-height: 30px;");
		}
    	
    	gridPane.addRow(0, invoiceIdLabel);
    	gridPane.addRow(1, billingAddressLabel, billingAddressField);
    	gridPane.addRow(2, shippingAddressLabel, shippingAddressField);
    	gridPane.addRow(3, shippingMethodLabel, shippingMethodComboBox);
    	gridPane.addRow(4, paymentMethodLabel, paymentMethodComboBox);
    	gridPane.add(updateInvoiceButton, 1, 5, 1, 1);
    	
    	gridPane.setVisible(false);
    	return gridPane;
    }

    /**
     * Crée et retourne une table affichant la liste des factures.
     * La table inclut des colonnes pour les détails des factures ainsi que des boutons d'actions : supprimer, visualiser, livrer la commande
     * 
     * @return TableView contenant la liste des factures.
     */
    private TableView<Invoice> getInvoiceTableView(){
    	invoiceDAO = new InvoiceDAO();
    	ObservableList<Invoice> invoicesList = FXCollections.observableArrayList(invoiceDAO.getAllInvoices());
    	TableView<Invoice> invoicesTable = new TableView<>();
    	invoicesTable.setPrefHeight(200);
    	invoicesTable.setId("ordersTable"); // réduire taille des entetes
        
        TableColumn<Invoice, Integer> colId = new TableColumn<>("Réf.");
        colId.setCellValueFactory(new PropertyValueFactory<>("invoiceId"));

        TableColumn<Invoice, Integer> colOrderId = new TableColumn<>("N° Commande");
        colOrderId.setCellValueFactory(invoice -> new SimpleIntegerProperty(invoice.getValue().getOrder().getOrderId()).asObject());
        
        TableColumn<Invoice, String> colCustomerName = new TableColumn<>("Nom Client");
        colCustomerName.setCellValueFactory(invoice -> new SimpleStringProperty(invoice.getValue().getOrder().getCustomer().getLastName()));

        TableColumn<Invoice, String> colBillingAddress = new TableColumn<>("Adresse de facturation");
        colBillingAddress.setCellValueFactory(invoice -> new SimpleStringProperty(invoice.getValue().getBillingAddress()));

        TableColumn<Invoice, String> colShippingAddress = new TableColumn<>("Adresse de livraison");
        colShippingAddress.setCellValueFactory(invoice -> new SimpleStringProperty(invoice.getValue().getShippingAddress()));

        TableColumn<Invoice, String> colShippingMethod = new TableColumn<>("Méthode de livraison");
        colShippingMethod.setCellValueFactory(invoice -> new SimpleStringProperty(invoice.getValue().getShippingMethod()));

        TableColumn<Invoice, String> colPaymentMethod = new TableColumn<>("Méthode de paiement");
        colPaymentMethod.setCellValueFactory(invoice -> new SimpleStringProperty(invoice.getValue().getPaymentMethod()));
        
        TableColumn<Invoice, String> colOrderStatus = new TableColumn<>("Statut");
        colOrderStatus.setCellValueFactory(invoice -> new SimpleStringProperty(invoice.getValue().getOrder().getStatus()));

        // Créer une colonne pour le bouton d'action
        TableColumn<Invoice, Void> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteInvoiceButton = new Button();
            private final Button showInvoiceButton = new Button();
	        private final Button shipInvoiceButton = new Button();
	        private final HBox buttonContainer = new HBox(10); // Conteneur pour regrouper les boutons
	        {
                ImageView binIcon = new ImageView(new Image(getClass().getResource("/Image/Icons/binIcon.png").toExternalForm()));
                binIcon.setFitHeight(20);
                binIcon.setFitWidth(20);
                deleteInvoiceButton.setGraphic(binIcon);
                deleteInvoiceButton.setId("roundButton");
                deleteInvoiceButton.setStyle("-fx-background-color: red;");
                deleteInvoiceButton.setOnAction(event -> {
                	// Récupérer la facture correspondant à cette ligne                    
                	Invoice invoice = getTableView().getItems().get(getIndex());
                    // Supprimer la facture
                    invoiceDAO.deleteInvoice(invoice);
                    // Mettre à jour la TableView
                    invoicesTable.setItems(FXCollections.observableArrayList(invoiceDAO.getAllInvoices()));
                });
                
                ImageView showIcon = new ImageView(new Image(getClass().getResource("/Image/Icons/eyeIconW.png").toExternalForm()));
                showIcon.setFitHeight(20);
                showIcon.setFitWidth(20);
                showInvoiceButton.setGraphic(showIcon);
                showInvoiceButton.setId("roundButton");
                showInvoiceButton.setStyle("-fx-background-color: blue;");
                showInvoiceButton.setOnAction(event -> {
                	// Récupérer la facture correspondant à cette ligne                    
                	Invoice invoice = getTableView().getItems().get(getIndex());
                    // Afficher la facture
                	new InvoiceView().showInvoice(invoice);
                });
                
                ImageView shipIcon = new ImageView(new Image(getClass().getResource("/Image/Icons/manageStockIcon.png").toExternalForm()));
                shipIcon.setFitHeight(20);
                shipIcon.setFitWidth(20);
                shipInvoiceButton.setGraphic(shipIcon);
                shipInvoiceButton.setId("roundButton");
                shipInvoiceButton.setStyle("-fx-background-color: green;");
                shipInvoiceButton.setOnAction(event -> {
                	// Récupérer la facture correspondant à cette ligne                    
                	Invoice invoice = getTableView().getItems().get(getIndex());
                    // Afficher la facture
                	invoice.getOrder().deliverOrder();
        	        MainView.showAlert("Succès", null, "Commande livrée avec succès. ", AlertType.INFORMATION);
        	        SendEmail.sendOrderDeliveryEmail("fourel.pauline@gmail.com"); // Modifier ici pour insérer la vrai adresse mail d'un client
        	        invoicesTable.setItems(FXCollections.observableArrayList(invoiceDAO.getAllInvoices()));
                });
                
                buttonContainer.getChildren().addAll(deleteInvoiceButton, showInvoiceButton, shipInvoiceButton);
	            buttonContainer.setAlignment(Pos.CENTER);
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null); // Pas de bouton pour les lignes vides
                } else {
                	Invoice invoice = getTableView().getItems().get(getIndex());
                    String orderStatus = invoice.getOrder().getStatus();
                    
                    // Afficher shipInvoiceButton seulement si la commande est "Validée"
                    shipInvoiceButton.setVisible("Validée".equals(orderStatus));

                    setGraphic(buttonContainer); // Afficher le bouton pour les lignes valides
                }
            }
        });
        
        invoicesTable.getColumns().add(actionColumn);
        invoicesTable.getColumns().add(colId);
        invoicesTable.getColumns().add(colOrderId);
        invoicesTable.getColumns().add(colOrderStatus);
        invoicesTable.getColumns().add(colCustomerName);
        invoicesTable.getColumns().add(colBillingAddress);
        invoicesTable.getColumns().add(colShippingAddress);
        invoicesTable.getColumns().add(colShippingMethod);
        invoicesTable.getColumns().add(colPaymentMethod);
        invoicesTable.setItems(invoicesList);
        
        invoicesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) { // Vérifie qu'une ligne est sélectionnée
                invoiceSelect();
            }
        });
        
        return invoicesTable;
    }
    
    
    /**
     * Récupère les informations de la facture sélectionnée dans la table et 
     * remplit les champs du formulaire d'édition.
     */
    private void invoiceSelect(){
        Invoice invoice = invoicesTable.getSelectionModel().getSelectedItem();
        invoiceId = invoice.getInvoiceId();
        orderId = invoice.getOrder().getOrderId();
        invoiceIdLabel.setText("Identifiant : "+ invoiceId);
    	
        invoiceGridPane.setVisible(true);
    	billingAddressField.setText(invoice.getBillingAddress());
    	shippingAddressField.setText(invoice.getShippingAddress());
    	shippingMethodComboBox.setValue(invoice.getShippingMethod());
    	paymentMethodComboBox.setValue(invoice.getPaymentMethod());
    }
    
    /**
     * Met à jour la facture sélectionnée avec les nouvelles valeurs saisies.
     * Vérifie d'abord que tous les champs sont remplis avant d'appliquer les modifications.
     */
    private void updateInvoice() {
    	if(!checkIfEmptyInvoice()) {
    		getInvoiceTextFieldValues();
    		invoiceDAO.updateInvoice(billingAddress, shippingAddress, shippingMethod, paymentMethod, invoiceId);
    		clearInvoiceField();
    		invoicesTable.setItems(FXCollections.observableArrayList(invoiceDAO.getAllInvoices()));
    		InvoiceView invoiceView = new InvoiceView();
            invoiceView.generateInvoice(invoiceDAO.getOrderByInvoiceId(invoiceId));
    	}
    }
    
    
    /**
     * Récupère les valeurs saisies dans les champs du formulaire de modification de facture.
     */
    private void getInvoiceTextFieldValues() {
    	billingAddress = billingAddressField.getText();
    	shippingAddress = shippingAddressField.getText();
    	shippingMethod = shippingMethodComboBox.getValue();
    	paymentMethod = paymentMethodComboBox.getValue();
    }
    
    /**
     * Réinitialise tous les champs du formulaire de modification de facture.
     */
    private void clearInvoiceField(){
    	billingAddressField.setText("");
    	shippingAddressField.setText("");
    	shippingMethodComboBox.getSelectionModel().clearSelection();
    	paymentMethodComboBox.getSelectionModel().clearSelection();;
    }
    
    /**
     * Vérifie si un ou plusieurs champs du formulaire de modification de facture sont vides.
     * Affiche une alerte en cas de champ vide.
     * 
     * @return true si au moins un champ est vide, false sinon.
     */
    private boolean checkIfEmptyInvoice() {
    	if(billingAddressField.getText().isEmpty()
                || shippingAddressField.getText().isEmpty()
                || shippingMethodComboBox.getSelectionModel().getSelectedItem() == null
                || paymentMethodComboBox.getSelectionModel().getSelectedItem() == null){
        	MainView.showAlert("Erreur", null, "Merci de remplir tous les champs", AlertType.ERROR);
   	 	return true;
   	 }return false;}
}
