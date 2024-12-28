package Interface;

import database.AdminStatsDAO;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
		Label averageOrderValue = new Label("Panier moyen " + adminStatsDAO.getAverageOrderValue());
		Label topSellingProductsLabel = new Label("Produits les plus vendus " + adminStatsDAO.getTopSellingProducts());
		
		mainContent.getChildren().addAll(topSellingProductsLabel, pendingOrders, deliveredOrders, averageOrderValue);
		return scrollPane;
	}
	
	private void showStats() {
		VBox topVBox = new VBox();
		topVBox.setStyle("-fx-padding: 10; -fx-background-color: #FFFFFF; -fx-background-radius:10;");
		Label topVBoxTitle = new Label("CHIFFRES CLES");
		topVBoxTitle.setStyle("-fx-font-size: 16px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5)");
		
		HBox topHBox = new HBox();
		topHBox.setStyle("-fx-alignment: center; -fx-spacing:20; -fx-background-color: #FFFFFF");
		
		Label totalProducts = new Label(""+adminStatsDAO.getTotalProducts());
		Label totalProductsLabel = new Label("Nombre total de produits ");
		VBox a = new VBox();
		a.setStyle("-fx-alignment: center;");
		a.setPrefSize(250, 100);
		a.getChildren().addAll(totalProducts, totalProductsLabel);
		
		Pane pane1 = new Pane();
		pane1.setPrefSize(1, 50);
		pane1.setStyle("-fx-background-color: #ececec");
		
		
		Label totalRevenueLabel = new Label("Chiffre d'affaires");
		Label totalRevenue = new Label(""+adminStatsDAO.getTotalRevenue());
		VBox b = new VBox();
		b.setStyle("-fx-alignment: center;");
		b.setPrefSize(250, 100);
		b.getChildren().addAll(totalRevenue, totalRevenueLabel);
		
		Pane pane2 = new Pane();
		pane2.setPrefSize(1, 50);
		pane2.setStyle("-fx-background-color: #ececec");
		
		Label totalOrdersLabel = new Label("Nombre total de commandes ");
		Label totalOrders = new Label(""+adminStatsDAO.getTotalOrders());
		VBox c = new VBox();
		c.setStyle("-fx-alignment: center;");
		c.setPrefSize(250, 100);
		c.getChildren().addAll(totalOrders, totalOrdersLabel);
		
		topHBox.getChildren().addAll(a, pane1, b, pane2, c);
		topVBox.getChildren().addAll(topVBoxTitle, topHBox);
		
		
		Label salesByPeriod = new Label("Évolution des ventes par mois " );
		LineChart<String, Number> salesLineChart= adminStatsDAO.getSalesByPeriodLineChart();
		VBox d = createVBox(salesByPeriod, salesLineChart);
		
		PieChart pieChart = adminStatsDAO.getTypeProductsPieChart();
		VBox e = createVBox(new Label("Répartition des produits vendus par catégories"), pieChart);
		
		HBox midHBox = new HBox(10);
		midHBox.setSpacing(30);
		midHBox.getChildren().addAll(d, e);
		
		BarChart<String, Number> barChart = adminStatsDAO.getSalesByBrandBarChart();
		VBox v = createVBox(new Label("Ventes par marques"), barChart);
		
		for (Label data : new Label[]{totalProducts, totalRevenue, totalOrders}) {
            data.setStyle("-fx-font-size: 32px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");
            //-fx-alignment: CENTER_LEFT;
      	  //label.setPrefWidth(220.0);
		}
		
		for (Label dataLabel : new Label[]{totalProductsLabel, totalRevenueLabel, totalOrdersLabel}) {
            dataLabel.setStyle("-fx-font-size: 16px;-fx-font-weight: normal;-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");
		}
		
		mainContent.getChildren().setAll(topVBox, midHBox, v);
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

}
