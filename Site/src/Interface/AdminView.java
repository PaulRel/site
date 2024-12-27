package Interface;

import database.AdminStatsDAO;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import products.Product;

public class AdminView {
	
	AdminStatsDAO adminStatsDAO;
	VBox mainContent;
	
	public AdminView(MainView mainView) {        
        AnchorPane rootPane = new AnchorPane();     
        HeaderView v = new HeaderView(mainView);
        
        rootPane.getChildren().addAll(v.getHeader(), createLeftMenu(), createMainSection());
        
        Scene adminScene = new Scene(rootPane, 1350, 670);
        adminScene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
        mainView.getPrimaryStage().setScene(adminScene);
    }
	
	public VBox createLeftMenu() {
		VBox menuBox = new VBox();
		AnchorPane.setTopAnchor(menuBox, 116.0);
		menuBox.setPadding(new Insets(20));
	    //menuBox.setStyle("-fx-background-color: #F8F8F8;");
		
		Button statsButton = new Button("Statistiques globales");
		Button customersButton = new Button("Clients");
		Button stockManagementButton = new Button("Gestion des stocks");
		Button editInvoiceButton = new Button("Modifier les factures");
		 
		statsButton.setOnAction(e -> showStats());
		customersButton.setOnAction(e -> showClients());
		stockManagementButton.setOnAction(e -> manageStocks());
		editInvoiceButton.setOnAction(e -> editInvoice());
		menuBox.getChildren().addAll(statsButton, customersButton, stockManagementButton, editInvoiceButton);
		return menuBox;
		
	}
	
	public VBox createMainSection() {
		mainContent = new VBox();
		AnchorPane.setLeftAnchor(mainContent, 280.0);
		AnchorPane.setTopAnchor(mainContent, 180.0);
		
		adminStatsDAO = new AdminStatsDAO();
		
		Label totalOrdersLabel = new Label("Nombre total de commandes " + adminStatsDAO.getTotalOrders());
		Label pendingOrders = new Label("Nombre de commandes en cours " + adminStatsDAO.getPendingOrders());
		Label deliveredOrders = new Label("Nombre de commandes livrées " + adminStatsDAO.getDeliveredOrders());
		Label totalRevenue = new Label("Chiffre d'affaires " + adminStatsDAO.getTotalRevenue() );
		Label averageOrderValue = new Label("Panier moyen " + adminStatsDAO.getAverageOrderValue());
		Label sellingByPeriod = new Label("Évolution des ventes par mois " + adminStatsDAO.getSellingByPeriod());
		
		mainContent.getChildren().addAll(totalOrdersLabel, pendingOrders,
				deliveredOrders, totalRevenue, averageOrderValue, sellingByPeriod);
		return mainContent;
	}
	
	private void showStats() {
		Label totalProductsLabel = new Label("Nombre total de produits " + adminStatsDAO.getTotalProducts());
		Label topSellingProductsLabel = new Label("Produits les plus vendus " + adminStatsDAO.getTopSellingProducts(5));			
		Label bestTypeProducts = new Label("Meilleures catégories de produits " + adminStatsDAO.getBestTypeProducts(5));
		mainContent.getChildren().setAll(totalProductsLabel, topSellingProductsLabel, bestTypeProducts);
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

}
