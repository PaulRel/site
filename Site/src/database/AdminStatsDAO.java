package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.chart.PieChart;

public class AdminStatsDAO {
	
	private Connection connection;
	
	public AdminStatsDAO() {
        try {
			this.connection = DatabaseConnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    // 1. Nombre total de produits
    public int getTotalProducts() {
        String query = "SELECT COUNT(*) AS total_produits FROM Produit";
        try (PreparedStatement stmt = connection.prepareStatement(query);){
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total_produits");
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}
        return 0;
    }

    // 2. Produits les plus vendus
    public String getTopSellingProducts(int limit) {
        String query = """
            SELECT p.Nom, SUM(od.quantity) AS total_vendus
            FROM Orderdetails od
            JOIN Produit p ON od.product_id = p.ID
            GROUP BY p.ID, p.Nom
            ORDER BY total_vendus DESC
            LIMIT ?""";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    return ("Produit: " + rs.getString("Nom") + 
                                       ", Ventes: " + rs.getInt("total_vendus"));
                }
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}
        return null;
    }

    // 3. Produits en rupture de stock
    public int getOutOfStockProducts() {
        String query = "SELECT COUNT(*) AS produits_en_rupture FROM Produit WHERE Qt_Dispo = 0";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("produits_en_rupture");
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}
        return 0;
    }

    // 4. Nombre total de commandes
    public int getTotalOrders() {
        String query = "SELECT COUNT(*) AS total_commandes FROM Orders";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total_commandes");
            }
        } catch (SQLException e) {

			e.printStackTrace();
		}
        return 0;
    }

    // 5. Nombre de commandes en cours
    public int getPendingOrders() {
        String query = "SELECT COUNT(*) AS commandes_en_cours FROM Orders WHERE status = 'En cours'";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("commandes_en_cours");
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}
        return 0;
    }
    
    // 6. Nombre de commandes livrées
    public int getDeliveredOrders() {
        String query = "SELECT COUNT(*) AS commandes_livrées FROM Orders WHERE status = 'Délivrée	'";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("commandes_livrées");
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}
        return 0;
    }

    // 7. Chiffre d'affaires généré
    public double getTotalRevenue() {
        String query = """
            SELECT SUM(od.quantity * p.Prix) AS chiffre_affaires
            FROM Orderdetails od
            JOIN Produit p ON od.product_id = p.ID""";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("chiffre_affaires");
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}
        return 0.0;
    }

    // 8. Nombre total d'utilisateurs
    public int getTotalUsers() {
        String query = "SELECT COUNT(*) AS total_utilisateurs FROM Customer";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total_utilisateurs");
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}
        return 0;
    }
    
    // 9. Utilisateurs actifs ayant commandé dans les 30 derniers jours
    public int getTotalActiveUsers() {
        String query = "SELECT COUNT(DISTINCT customer_id) AS utilisateurs_actifs FROM Orders "
        		+ "WHERE order_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY);";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("utilisateurs_actifs");
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}
        return 0;
    }
    
    // 10. Meilleures catégories de produits
    public PieChart getTypeProductsPieChart() {
    	PieChart pieChart = new PieChart();
        pieChart.setTitle("Répartition des produits vendus par catégories");
    	String query = """
                SELECT p.Type, SUM(od.quantity) AS total_vendus 
    			FROM Orderdetails od
    			JOIN Produit p ON od.product_id = p.ID
    			GROUP BY p.Type
    			""";
    	try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
             while (rs.next()) {
            	 String type = rs.getString("Type");
                 int totalVendus = rs.getInt("total_vendus");
                 pieChart.getData().add(new PieChart.Data(type, totalVendus));
                 pieChart.setLegendVisible(false);
             }
        } catch (SQLException e) {
    			e.printStackTrace();
    	}
    	return pieChart;
    }

    // 11. Panier moyen
    public double getAverageOrderValue() {
        String query = """
            SELECT AVG(total_panier) AS panier_moyen
            FROM (
                SELECT o.order_id, SUM(od.quantity * p.Prix) AS total_panier
                FROM Orders o
                JOIN Orderdetails od ON o.order_id = od.order_id
                JOIN Produit p ON od.product_id = p.ID
                GROUP BY o.order_id
            ) AS panier_totaux""";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("panier_moyen");
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}
        return 0.0;
    }
    
    // 12. Évolution des ventes par mois
    public String getSellingByPeriod() {
    	String query = """
                SELECT DATE_FORMAT(o.order_date, '%Y-%m') AS mois, 
    			SUM(od.quantity * p.Prix) AS ventes_mensuelles
    			FROM Orderdetails od
    			JOIN Orders o ON od.order_id = o.order_id
    			JOIN Produit p ON od.product_id = p.ID
    			GROUP BY DATE_FORMAT(o.order_date, '%Y-%m')
    			ORDER BY mois;""";
            try (PreparedStatement stmt = connection.prepareStatement(query);
            		ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        return("Date: " + rs.getString("mois") + 
                                           ", Ventes: " + rs.getDouble("ventes_mensuelles"));
                    }
            } catch (SQLException e) {
    			e.printStackTrace();
    		}
            return null;
        }
}

