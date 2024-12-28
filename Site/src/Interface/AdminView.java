package Interface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import products.Product;

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
		stockManagementButton.setOnAction(e -> manageStocks());
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
		Label averageOrderLabel = new Label("Nombre total de produits ");
		VBox h = new VBox();
		h.setStyle("-fx-alignment: center;-fx-padding: 10;-fx-spacing:10;-fx-background-color: #FFFFFF; -fx-background-radius:10;");
		//h.setPrefSize(250, 100);
		h.getChildren().addAll(averageOrder, averageOrderLabel);
		
		Label salesQtyByPeriod = new Label("Évolution des ventes par mois " );
		LineChart<String, Number> salesQtyLineChart= adminStatsDAO.getSalesQtyLineChart();
		VBox i = createVBox(salesQtyByPeriod, salesQtyLineChart);
		
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
	
	private void manageStocks() {
		Label OutOfStockProductsLabel = new Label("Produits en rupture de stock " + adminStatsDAO.getOutOfStockProducts());
		mainContent.getChildren().setAll(OutOfStockProductsLabel);
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
	
	
	private void editProducts() {
		
	}
	
	private TextField idField, nameField, descriptionField, brandField, priceField, qtyDispoField;
	
    public void addMedicinesAdd(){
    	idField = new TextField();
    	nameField = new TextField();
    	brandField = new TextField();
    	priceField = new TextField();
    	qtyDispoField = new TextField();
        if(nameField.getText().isEmpty()
                    || descriptionField.getText().isEmpty()
                    || brandField.getSelectionModel().getSelectedItem() == null
                    || priceField.getText().isEmpty()
                    || qtyDispoField.getText().isEmpty()
                    || getData.path == null || getData.path == ""){
        	MainView.showAlert("Erreur", null, "Merci de remplir tous les champs " + e.getMessage(), AlertType.ERROR);
        }else{
        	String name = nameField.getText();
        	String description = descriptionField.getText();
        	String brand = brandField.getText();
        	//convertir en double
        	String price = priceField.getText();
        	//convertir en int
        	String qtyDispo = qtyDispoField.getText();

                    String uri = getData.path;
                    uri = uri.replace("\\", "\\\\");

                    prepare.setString(7, uri);
                    
                    MainView.showAlert("Information Message", null, "Ajouter avec succès", AlertType.INFORMATION);
                    
                    addMedicineShowListData();
                    addMedicineReset();
                    
                }
    }
    
    public void addMedicineUpdate(){
    	String id = idField.getText();
    	String name = nameField.getText();
    	String description = descriptionField.getText();
    	String brand = brandField.getText();
    	//convertir en double
    	String price = priceField.getText();
    	//convertir en int
    	String qtyDispo = qtyDispoField.getText();
        
        String uri = getData.path;
        uri = uri.replace("\\", "\\\\");
        
        String sql = "UPDATE Product SET Nom = '"
                +name+"', Description = '"
                +description+"', Marque = '"
                +brand+"', Prix = '"
                +price+"', Qt_dispo = '"
                +qtyDispo+"', image = '"+uri+"' WHERE id = '"
                +id+"'";
        
        Connection conn = DatabaseConnection.getConnection();
        
        try{
        	if(nameField.getText().isEmpty()
                    || descriptionField.getText().isEmpty()
                    || brandField.getSelectionModel().getSelectedItem() == null
                    || priceField.getText().isEmpty()
                    || qtyDispoField.getText().isEmpty()
                    || getData.path == null || getData.path == ""){
        	MainView.showAlert("Erreur", null, "Merci de remplir tous les champs ", AlertType.ERROR);
            }else{
            	PreparedStatement updateStmt = conn.prepareStatement(sql);
            	updateStmt.executeUpdate(sql);
            	MainView.showAlert("Information Message", null, "Modifier avec succès", AlertType.INFORMATION);
                    
                    addMedicineShowListData();
                    addMedicineReset();
                }  
        }catch(Exception e){e.printStackTrace();}
    }
    
    public void addMedicineDelete(){
        
        String sql = "DELETE FROM Produit WHERE id = '"+ idField.getText()+"'";
            
        if(nameField.getText().isEmpty()
                || descriptionField.getText().isEmpty()
                || brandField.getSelectionModel().getSelectedItem() == null
                || priceField.getText().isEmpty()
                || qtyDispoField.getText().isEmpty()
                || getData.path == null || getData.path == ""){
        	MainView.showAlert("Erreur", null, "Merci de remplir tous les champs ", AlertType.ERROR);
            }else{
            	try (Connection conn = DatabaseConnection.getConnection();
                    	PreparedStatement statement = conn.prepareStatement(sql)) {

                        int rowsAffected = statement.executeUpdate();

                        if (rowsAffected > 0) {
                            MainView.showAlert("Succès", null, "Votre produit a été supprimé avec succès.", AlertType.INFORMATION);

                        }
                    
                    addMedicineShowListData();
                    addMedicineReset();
                }
            	 catch (SQLException e) {
                     MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
            }
        }
    }
    
    public void addMedicineReset(){
        idField.setText("");
        nameField.setText("");
        descriptionField.setText("");
        brandField.setText("");
        priceField.getSelectionModel().clearSelection();
        qtyDispoField.getSelectionModel().clearSelection();
        
        addMedicines_imageView.setImage(null);
        
        getData.path = "";
        
    }
    
    private String[] addMedicineListT = {"Hydrocodone", "Antibiotics", "Metformin", "Losartan", "Albuterol"};
    public void addMedicineListType(){
        List<String> listT = new ArrayList<>();
        
        for(String data: addMedicineListT){
            listT.add(data);
        }
        
        ObservableList listData = FXCollections.observableArrayList(listT);
        addMedicines_type.setItems(listData);
        
    }
    
    
    
    private String[] addMedicineStatus = {"Available", "Not Available"};
    public void addMedicineListStatus(){
        List<String> listS = new ArrayList<>();
        
        for(String data: addMedicineStatus){
            listS.add(data);
        }
        
        ObservableList listData = FXCollections.observableArrayList(listS);
        addMedicines_status.setItems(listData);
    }
    
    public void addMedicineImportImage(){
        
        FileChooser open = new FileChooser();
        open.setTitle("Import Image File");
        open.getExtensionFilters().add(new ExtensionFilter("Image File", "*jpg", "*png"));
        
        File file = open.showOpenDialog(main_form.getScene().getWindow());
        
        if(file != null){
            image = new Image(file.toURI().toString(), 118, 147, false, true);
            
            addMedicines_imageView.setImage(image);
            
            getData.path = file.getAbsolutePath();
        }
        
    }
    
    
    //A modifier
    public ObservableList<Product> addMedicinesListData(){
        
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getAllProduits();
        
        ObservableList<Product> listData = FXCollections.observableArrayList();
        
        return listData;
    }
    
    private ObservableList<Product> addProductList;
    public void addMedicineShowListData(){
        addProductList = addMedicinesListData();
        
        TableColumn<CartItem, String> productColumn = new TableColumn<>("Produit");
        productColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));

        TableColumn<CartItem, String> sizeColumn = new TableColumn<>("Taille");
        sizeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSize()));

        TableColumn<CartItem, Integer> quantityColumn = new TableColumn<>("Quantité");
        quantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        
        TableColumn<CartItem, Double> priceColumn = new TableColumn<>("Prix à l'unite");
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getProduct().getPrice()).asObject());
        
        // Créer une colonne pour le bouton d'action
        TableColumn<CartItem, Void> actionColumn = new TableColumn<>("Action");
        
        addMedicines_tableView.setItems(addMedicineList);
        
    }

}
