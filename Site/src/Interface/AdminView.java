package Interface;

import database.AdminStatsDAO;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class AdminView {
	public AdminView(MainView mainView) {        
        AnchorPane rootPane = new AnchorPane();     
        HeaderView v = new HeaderView(mainView);
        
        rootPane.getChildren().addAll(v.getHeader(), createMainSection());
        
        Scene adminScene = new Scene(rootPane, 1350, 670);
        adminScene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
        mainView.getPrimaryStage().setScene(adminScene);
    }
	
	public VBox createMainSection() {
		VBox mainContent = new VBox();
		AnchorPane.setLeftAnchor(mainContent, 100.0);
		AnchorPane.setTopAnchor(mainContent, 130.0);
		
		AdminStatsDAO adminStatsDAO = new AdminStatsDAO();
		Label totalProductsLabel = new Label("Nombre total de produits " + adminStatsDAO.getTotalProducts());
		Label topSellingProductsLabel = new Label("Produits les plus vendus " + adminStatsDAO.getTopSellingProducts(5));		
		Label OutOfStockProductsLabel = new Label("Produits en rupture de stock " + adminStatsDAO.getOutOfStockProducts());	
		Label totalOrdersLabel = new Label("Nombre total de commandes " + adminStatsDAO.getTotalOrders());
		Label pendingOrders = new Label("Nombre de commandes en cours " + adminStatsDAO.getPendingOrders());
		Label deliveredOrders = new Label("Nombre de commandes livrées " + adminStatsDAO.getDeliveredOrders());
		Label totalRevenue = new Label("Chiffre d'affaires " + adminStatsDAO.getTotalRevenue() );
		Label totalUsers = new Label("Nombre total d'utilisateurs " + adminStatsDAO.getTotalUsers());
		Label activeUsers = new Label("Utilisateurs actifs ayant commandé dans les 30 derniers jours " + adminStatsDAO.getTotalActiveUsers());
		Label bestTypeProducts = new Label("Meilleures catégories de produits " + adminStatsDAO.getBestTypeProducts(5)); 
		Label averageOrderValue = new Label("Panier moyen " + adminStatsDAO.getAverageOrderValue());
		Label sellingByPeriod = new Label("Évolution des ventes par mois " + adminStatsDAO.getSellingByPeriod());
		
		mainContent.getChildren().addAll(totalProductsLabel, topSellingProductsLabel, OutOfStockProductsLabel, totalOrdersLabel, pendingOrders,
				deliveredOrders, totalRevenue, totalUsers, activeUsers, bestTypeProducts, averageOrderValue, sellingByPeriod);
		return mainContent;
		
	}

}
