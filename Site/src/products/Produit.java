package products;


public class Produit {
	private int id, qtDispo;
	private String description, nom, type, marque, imagePath;
	private double price;

	/**
	 * Represente un produit pour l'instant
	 * @param Description
	 * @param Price
	 * @param QtDispo
	 * @param Type
	 */
	public Produit(int id, String nom, String description, String type, String marque, double price, int qtDispo, String imagePath) {
		this.id = id;
		this.nom = nom;
		this.description = description;
		this.type = type;
		this.marque = marque;
		this.price = price; 
		this.qtDispo = qtDispo;
		this.imagePath = imagePath;
	}

	public Produit RechercherPdt(String c) {
		return new Produit(qtDispo, c, c, c, c, price, qtDispo, c);
	}
	
	@Override
	public String toString() {
		return (String) nom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getQtDispo() {
		return qtDispo;
	}

	public void setQtDispo(int qtDispo) {
		this.qtDispo = qtDispo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public String getImagePath() {
		return imagePath;
	}

	public String getNom() {
		return nom;
	}

	public String getMarque() {
		return marque;
	}

	public void setMarque(String marque) {
		this.marque = marque;
	}
}
