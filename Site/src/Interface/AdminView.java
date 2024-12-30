package Interface;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import customer.CartItem;
import customer.Order;
import database.AdminStatsDAO;
import database.DatabaseConnection;
import database.ProductDAO;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import products.Chaussures;
import products.Product;
import products.ProductWithSize;
import products.Vetement;

public class AdminView {
	
	AdminStatsDAO adminStatsDAO;
	VBox mainContent;
	
	public AdminView(MainView mainView) {        
        AnchorPane rootPane = new AnchorPane();     
        HeaderView v = new HeaderView(mainView);
        
        rootPane.getChildren().addAll(v.getHeader(), createLeftMenu(mainView), createMainSection());
        
        Scene adminScene = new Scene(rootPane, 1350, 670);
        adminScene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
        mainView.getPrimaryStage().setScene(adminScene);
    }
	
	public VBox createLeftMenu(MainView mainView) {
		VBox menuBox = new VBox(20);
		AnchorPane.setTopAnchor(menuBox, 119.0);
		AnchorPane.setBottomAnchor(menuBox, 0.0);
		menuBox.setPadding(new Insets(20));
	    menuBox.setStyle("-fx-background-color: linear-gradient(to bottom right, #2d658c, #2ca772)");
		//#F8F8F8
		Button statsButton = new Button("Statistiques globales");
		Button customersButton = new Button("Clients");
		Button stockManagementButton = new Button("Gestion des stocks");
		Button editInvoiceButton = new Button("Modifier les factures");
		Button logoutButton = new Button("Déconnexion");
		
		for (Button button : new Button[]{statsButton, customersButton, stockManagementButton, editInvoiceButton}) {
	              button.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -text-fill:#fff; -fx-padding: 10; -fx-border-color: transparent");
	              //-fx-alignment: CENTER_LEFT;
	        	  button.setPrefWidth(220.0);
	        	  button.setOnMouseEntered(event -> {
	                  button.setStyle("-fx-background-color: #3A7F9C; -fx-border-color: WHITE");
	              });
	        	  button.setOnMouseExited(event -> {
	                  button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent");
	              });
	        }
        
		statsButton.setOnAction(e -> showStats());
		customersButton.setOnAction(e -> showClients());
		stockManagementButton.setOnAction(e -> manageStocks(mainView));
		editInvoiceButton.setOnAction(e -> editInvoice());
		logoutButton.setOnAction(e -> {
        	MainView.setCurrentCustomer(null);
        	mainView.showProductView(Product.class, null);
        });
		
		menuBox.getChildren().addAll(statsButton, customersButton, stockManagementButton, editInvoiceButton, logoutButton);
		return menuBox;
	}
	
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
		
		Label pendingOrders = new Label("Nombre de commandes en cours " + adminStatsDAO.getPendingOrders());
		Label deliveredOrders = new Label("Nombre de commandes livrées " + adminStatsDAO.getDeliveredOrders());
		
		mainContent.getChildren().addAll(pendingOrders, deliveredOrders);
		return scrollPane;
	}
	
	
	private void showStats() {
		VBox topVBox = new VBox();
		topVBox.setStyle("-fx-padding: 10; -fx-background-color: #FFFFFF; -fx-background-radius:10;");
		Label topVBoxTitle = new Label("CHIFFRES CLES");
		topVBoxTitle.setStyle("-fx-font-size: 16px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5)");
		
		
		HBox topHBox = new HBox();
		topHBox.setStyle("-fx-alignment: center; -fx-spacing:20; -fx-background-color: #FFFFFF");
		
		// Nombre total de produits vendus
		Label totalProducts = new Label(""+adminStatsDAO.getTotalProducts());
		Label totalProductsLabel = new Label("Nombre total de produits ");
		VBox a = new VBox();
		a.setStyle("-fx-alignment: center;");
		a.setPrefSize(250, 100);
		a.getChildren().addAll(totalProducts, totalProductsLabel);
				
		Pane pane1 = new Pane();
		pane1.setPrefSize(1, 50);
		pane1.setStyle("-fx-background-color: #ececec");
			
		// Chiffres d'affaires
		Label totalRevenueLabel = new Label("Chiffre d'affaires");
		Label totalRevenue = new Label(""+adminStatsDAO.getTotalRevenue());
		VBox b = new VBox();
		b.setStyle("-fx-alignment: center;");
		b.setPrefSize(250, 100);
		b.getChildren().addAll(totalRevenue, totalRevenueLabel);
				
		Pane pane2 = new Pane();
		pane2.setPrefSize(1, 50);
		pane2.setStyle("-fx-background-color: #ececec");
		
		// Nombre total de commandes
		Label totalOrdersLabel = new Label("Nombre total de commandes ");
		Label totalOrders = new Label(""+adminStatsDAO.getTotalOrders());
		VBox c = new VBox();
		c.setStyle("-fx-alignment: center;");
		c.setPrefSize(250, 100);
		c.getChildren().addAll(totalOrders, totalOrdersLabel);
		
		topHBox.getChildren().addAll(a, pane1, b, pane2, c);
		
		
		topVBox.getChildren().addAll(topVBoxTitle, topHBox);
		
		
		// Graphique des ventes par mois (en euros)
		Label salesByPeriod = new Label("Évolution des ventes par mois " );
		LineChart<String, Number> salesLineChart= adminStatsDAO.getSalesByPeriodLineChart();
		VBox d = createVBox(salesByPeriod, salesLineChart);
		
		// Camembert des ventes par catégories de produits
		PieChart pieChart = adminStatsDAO.getTypeProductsPieChart();
		VBox e = createVBox(new Label("Répartition des produits vendus par catégories"), pieChart);
		
		HBox midHBox = new HBox(10);
		midHBox.setSpacing(30);
		midHBox.setPrefSize(350, 300);
		midHBox.getChildren().addAll(d, e);
		
		
		//Histogramme des ventes par marques (montant et quantité)
		BarChart<String, Number> barChart = adminStatsDAO.getSalesByBrandBarChart();
		VBox f = createVBox(new Label("Ventes par marques"), barChart);
		
		//Table des produits les plus vendus (nom et quantité)
		Label bestProductsLabel = new Label("Produits les plus vendus");
		TableView<Map.Entry<String, Integer>> tableView = createBestProductsTable();
		VBox g = new VBox(bestProductsLabel, tableView);
		g.setPrefSize(500, 300);
		g.setStyle("-fx-padding: 10;-fx-spacing:10;-fx-background-color: #FFFFFF; -fx-background-radius:10;");
		
		HBox midHBox2 = new HBox(10);
		midHBox2.setSpacing(30);
		midHBox2.setPrefSize(350, 300);
		midHBox2.getChildren().addAll(f, g);
		
		
		Label averageOrder = new Label(""+adminStatsDAO.getAverageOrderValue());
		Label averageOrderLabel = new Label("Montant moyen d'une commande");
		VBox h = new VBox();
		h.setStyle("-fx-alignment: center;-fx-padding: 10;-fx-spacing:10;-fx-background-color: #FFFFFF; -fx-background-radius:10;");
		//h.setPrefSize(250, 100);
		h.getChildren().addAll(averageOrder, averageOrderLabel);
		
		Label salesqtByPeriod = new Label("Évolution des ventes par mois " );
		LineChart<String, Number> salesqtLineChart= adminStatsDAO.getSalesQtyLineChart();
		VBox i = createVBox(salesqtByPeriod, salesqtLineChart);
		
		HBox bottomHBox = new HBox(10);
		bottomHBox.setSpacing(30);
		bottomHBox.getChildren().addAll(i, h);
		bottomHBox.setPrefSize(250, 100);
		
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
	
	
	private void showClients() {
		Label totalUsers = new Label("Nombre total d'utilisateurs " + adminStatsDAO.getTotalUsers());
		Label activeUsers = new Label("Utilisateurs actifs ayant commandé dans les 30 derniers jours " + adminStatsDAO.getTotalActiveUsers());
		mainContent.getChildren().setAll(totalUsers, activeUsers);
	}
	
	
	private void manageStocks(MainView mainView) {
		Label OutOfStockProductsLabel = new Label("Produits en rupture de stock " + adminStatsDAO.getOutOfStockProducts());
		VBox editProductsBox = editProducts(mainView);
		mainContent.getChildren().setAll(editProductsBox, OutOfStockProductsLabel);
	}
	
	private void editInvoice() {
		
	}
	
	private VBox createVBox(Label label, Chart chart) {
		VBox vBox = new VBox();
		vBox.getChildren().addAll(label, chart);
		vBox.setStyle("-fx-padding: 10;-fx-spacing:10;-fx-background-color: #FFFFFF; -fx-background-radius:10;");
		return vBox;
	}
	
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
	
	
	
	private TextField idField, nameField, descriptionField, brandField, priceField, qtDispoField, genderField, colorField;
	private String name, description, type, brand, imagePath;
	private Button addButton, deleteButton, clearButton,updateButton, importButton;
	private double price;
	private Label idLabel, typeLabel, sizeLabel, qtDispoLabel, surfaceLabel, genderLabel, colorLabel, typeVLabel;
	private int qtDispo;
    private ImageView imageView;
    private VBox a;
	private ComboBox<String> typeComboBox, sizeComboBox, surfaceComboBox, typeVComboBox;
	private TableView<Product> tableView;
	private ProductDAO productDAO = new ProductDAO();
	private GridPane gridPane;
	HashMap<String, Integer> sizesStock;
	
	private VBox editProducts(MainView mainView) {
		VBox productDetails = new VBox();
		productDetails.setStyle("-fx-padding: 10;-fx-spacing:10;-fx-background-color: #FFFFFF; -fx-background-radius:10;");
		
		// TextField
		idField = new TextField();
    	nameField = new TextField();
    	descriptionField = new TextField();
    	typeComboBox =  getComboBox();
    	brandField = new TextField();
    	priceField = new TextField();
    	qtDispoField = new TextField();
    	
    	// Specifique à un type de produit
    	surfaceComboBox = new ComboBox<String>();
    	surfaceComboBox.getItems().addAll("TOUTES SURFACES","terre battue","dur","gazon");
    	genderField = new TextField();
    	colorField = new TextField();
    	typeVComboBox = new ComboBox<String>();
    	typeVComboBox.getItems().addAll("Short","Sweat","Debardeur","Tshirt","Robe","Veste");
    	
    	for (TextField txt : new TextField[]{idField, nameField, descriptionField, brandField, priceField, qtDispoField, genderField, colorField}) {
            txt.setStyle("-fx-pref-height: 30px;");
		}
    	
    	// ImageView
    	imageView = new ImageView();
    	
    	//VBox qui permet de réserver un espace pour l'imageView
        a = new VBox();
        a.setPrefSize(50, 50);
        a.setStyle("-fx-background-color:transparent;-fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 5;");
        
        // Ajout des boutons
        addButton = new Button("+ Ajouter un produit");
        addButton.setStyle("-fx-background-color : #007bff; -fx-text-fill: white;");
        deleteButton = new Button("Supprimer");
        updateButton = new Button("Mettre à jour");
        clearButton = new Button("Effacer");
        importButton = getImportImageButton(mainView);
        tableView = getProductTableView();

        addButton.setOnAction(event -> addProduct());
        deleteButton.setOnAction(event -> deleteProduct());
        updateButton.setOnAction(event -> updateProduct());
        clearButton.setOnAction(event -> clearField());
        
        // Ajout de gridPane et table des produits
		productDetails.getChildren().addAll(createGridPane(), tableView);
		return productDetails;
	}
	
	private GridPane createGridPane() {
		gridPane = new GridPane();
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
        qtDispoLabel = new Label("Quantité");
        
        sizeLabel.setVisible(false);
        sizeComboBox.setVisible(false);        
        
        gridPane.addRow(0, idLabel, idField, priceLabel, priceField);
        gridPane.addRow(1, nameLabel, nameField, qtDispoLabel, qtDispoField);
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
            gridPane.getChildren().removeAll(qtDispoLabel, qtDispoField);
            gridPane.add(qtDispoLabel, 2, 4); // Nouvelle position pour quantité
            gridPane.add(qtDispoField, 3, 4);

            if ("chaussures".equals(selectedCategory)) {
                sizeComboBox.getItems().addAll("36", "37", "38", "39");
                showShoesComponents();
                
            } else if ("vetement".equals(selectedCategory)) {
                sizeComboBox.getItems().addAll("S", "M", "L", "XL");
                showClothesComponents();
            }
        });
        
        sizeComboBox.setOnAction(event -> {
        	String selectedSize = sizeComboBox.getValue();
        	if (sizesStock != null) {
        	for (Map.Entry<String, Integer> entry : sizesStock.entrySet()) {
        		if (entry.getKey().equals(selectedSize)) {
                    qtDispoField.setText(String.valueOf(entry.getValue()));
        		}
        	}       
        	}	
        });
        return typeComboBox;
    }    
	
    private void addProduct() {
    	clearField();
    	gridPane.getChildren().removeAll(idLabel, idField, typeLabel, typeComboBox, addButton);
        	
        gridPane.add(typeLabel, 0, 0); // Nouvelle position pour typeLabel
        gridPane.add(typeComboBox, 1, 0);
        
            
        Button addProductButton = new Button("Ajouter");
        gridPane.add(addProductButton, 5, 4, 2, 1);
            
        deleteButton.setVisible(false);
        updateButton.setVisible(false);
        tableView.setVisible(false);
            

        addProductButton.setOnAction(event ->{
        	if(!checkIfEmpty()) {
        		getTextFieldValues();
        		
        		Product product = new Product(0, name, description, type, brand, price, qtDispo, imagePath);
        		int id = productDAO.insertProduct(product);
        		
        		if (type == "chaussures") {
        			productDAO.insertChaussures(id, surfaceComboBox.getValue(), genderField.getText(), colorField.getText(), sizeComboBox.getValue(), qtDispo);
        		}
        		if (type == "vetement"){
        			productDAO.insertVetement(id, typeVComboBox.getValue(), genderField.getText(), colorField.getText(), sizeComboBox.getValue(), qtDispo);
        		}
                        
        		MainView.showAlert("Information Message", null, "Ajouter avec succès", AlertType.INFORMATION);
                
        		gridPane.getChildren().removeAll(addProductButton);
        		deleteButton.setVisible(true);
        		updateButton.setVisible(true);
        		tableView.setVisible(true);
        		clearField();
        		tableView.setItems(FXCollections.observableArrayList(productDAO.getAllProduits()));
        	}
        });
    }
    
    public void updateProduct(){
    	if(!idField.getText().isEmpty()) {
    		int id = Integer.parseInt(idField.getText());
    		getTextFieldValues();
        
    		String sql = "UPDATE Produit SET Nom = ?, Description = ?, Type = ?, Marque = ?, Prix = ?, Qt_Dispo = ?, image_path = ? WHERE id = ?";
        
        
            try(Connection conn = DatabaseConnection.getConnection();
            	PreparedStatement updateStmt = conn.prepareStatement(sql)){
            		updateStmt.setString(1, name);
                    updateStmt.setString(2, description);
                    updateStmt.setString(3, type);
                    updateStmt.setString(4, brand);
                    updateStmt.setDouble(5, price);
                    updateStmt.setInt(6, qtDispo);
                    updateStmt.setString(7, imagePath);
                    updateStmt.setInt(8, id);
                    int rowsAffected = updateStmt.executeUpdate();
                    if (rowsAffected > 0) {
                    	MainView.showAlert("Information Message", null, "Modifier avec succès", AlertType.INFORMATION);                    
                    	clearField();
                    	tableView.setItems(FXCollections.observableArrayList(productDAO.getAllProduits()));
                    }
              	}catch(Exception e){e.printStackTrace();}
        }
    	else {MainView.showAlert("Erreur", null, "Merci d'ajouter l'identifiant du produit à supprimer", AlertType.ERROR);}
    }
    
    
    public void deleteProduct(){ 
    	if(!checkIfEmpty()) {
        	productDAO.deleteProduct(Integer.parseInt(idField.getText()));
        	tableView.setItems(FXCollections.observableArrayList(productDAO.getAllProduits()));
        	clearField();      
        }
    }
    
    
    public Button getImportImageButton(MainView mainView){       
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

                // 4. Mettre à jour le chemin de l'image si nécessaire
                System.out.println("Image importée et enregistrée à : " + destFile.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
                MainView.showAlert("Erreur", "Impossible d'importer l'image", e.getMessage(), AlertType.ERROR);
            }
    });       
        return button;
    }
    
    
    public TableView<Product> getProductTableView(){
    	ObservableList<Product> productsList = FXCollections.observableArrayList(productDAO.getAllProduits());
        tableView = new TableView<>();
        
        TableColumn<Product, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Product, String> colName = new TableColumn<>("Nom");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, String> colDescription = new TableColumn<>("Description");
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Product, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Product, String> colBrand = new TableColumn<>("Marque");
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn<Product, Double> colPrice = new TableColumn<>("Prix");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Integer> colQtDispo = new TableColumn<>("Quantité");
        colQtDispo.setCellValueFactory(new PropertyValueFactory<>("qtDispo"));
        
        // Créer une colonne pour le bouton d'action
        TableColumn<Product, Void> actionColumn = new TableColumn<>("Action");
        
        tableView.getColumns().add(colId);
        tableView.getColumns().add(colName);
        tableView.getColumns().add(colDescription);
        tableView.getColumns().add(colType);
        tableView.getColumns().add(colBrand);
        tableView.getColumns().add(colPrice);
        tableView.getColumns().add(colQtDispo);
        tableView.getColumns().add(actionColumn);
        tableView.setItems(productsList);
        
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) { // Vérifie qu'une ligne est sélectionnée
                productSelect();
            }
        });
        
        return tableView;
    }
    
    private void productSelect(){
        Product product = tableView.getSelectionModel().getSelectedItem();
        
        idField.setText(String.valueOf(product.getId()));
        nameField.setText(product.getName());
        descriptionField.setText(product.getDescription());
        typeComboBox.setValue(product.getType());
        brandField.setText(product.getBrand());
        priceField.setText(String.valueOf(product.getPrice()));
        qtDispoField.setText("");
        sizeComboBox.getSelectionModel().clearSelection();
        
        if (product instanceof ProductWithSize) {
        	ProductWithSize productWithSize = (ProductWithSize) product;
        	sizesStock = productWithSize.getTailleStock();
        }
        
        if (product instanceof Chaussures) {
        	hideClothesComponents();
        	showShoesComponents();;
        	surfaceComboBox.setValue(((Chaussures)product).getSurface());
            genderField.setText(((Chaussures)product).getGender());
            colorField.setText(((Chaussures)product).getColor());
        }
        
        if (product instanceof Vetement) {
        	hideShoesComponents();
        	showClothesComponents();
        	typeVComboBox.setValue(((Vetement)product).getTypeVetement().toString());
            genderField.setText(((Vetement)product).getGender());
            colorField.setText(((Vetement)product).getCouleur());
        }
        
        
        imagePath = product.getImagePath();
        System.out.println(imagePath);
        imageView.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
    	imageView.setFitWidth(90);  // Largeur fixe
        imageView.setFitHeight(90); // Hauteur fixe
        imageView.setPreserveRatio(true);       
    }
    
    
    public void clearField(){
        idField.setText("");
        nameField.setText("");
        descriptionField.setText("");
        typeComboBox.getSelectionModel().clearSelection();
        brandField.setText("");
        priceField.setText("");
        qtDispoField.setText("");
        imageView.setImage(null);
        imagePath = "";
        sizesStock = null;
        
        hideShoesComponents(); hideClothesComponents();
        
        sizeLabel.setVisible(false);
        sizeComboBox.getItems().clear();
        sizeComboBox.setVisible(false);
        gridPane.getChildren().removeAll(qtDispoLabel, qtDispoField);
        gridPane.add(qtDispoLabel, 2, 1); // Nouvelle position pour quantité
        gridPane.add(qtDispoField, 3, 1);
    }
    
    
    private void getTextFieldValues() {
    	name = nameField.getText();
    	description = descriptionField.getText();
    	type = typeComboBox.getValue();
    	brand = brandField.getText();
    	price = Double.parseDouble(priceField.getText());
    	qtDispo = Integer.parseInt(qtDispoField.getText()); 	
    }
    
    private boolean checkIfEmpty() {
    	 if(nameField.getText().isEmpty()
                 || descriptionField.getText().isEmpty()
                 || typeComboBox.getSelectionModel().getSelectedItem() == null
                 || brandField.getText().isEmpty()
                 || priceField.getText().isEmpty()
                 || qtDispoField.getText().isEmpty()
                 || imagePath == null || imagePath == ""){
    		System.out.println("ImagePath : "+ imagePath);
         	MainView.showAlert("Erreur", null, "Merci de remplir tous les champs ", AlertType.ERROR);
    	 	return true;
    	 }
    	 return false;	
    }
    
    private void showShoesComponents() {
    	surfaceLabel.setVisible(true);
        genderLabel.setVisible(true);
        colorLabel.setVisible(true);
        
        surfaceComboBox.setVisible(true);
        genderField.setVisible(true);
        colorField.setVisible(true);
    }
    
    private void showClothesComponents() {
    	typeVLabel.setVisible(true);
        genderLabel.setVisible(true);
        colorLabel.setVisible(true);
        
        typeVComboBox.setVisible(true);
        genderField.setVisible(true);
        colorField.setVisible(true);
    }
    
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

}
