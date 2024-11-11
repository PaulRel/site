package products;


public class Produit {
	private int id, QtDispo;
	private String Description, Nom, Type, Marque, imagePath;
	private double Price;

	/**
	 * Represente un produit pour l'instant
	 * @param Description
	 * @param Price
	 * @param QtDispo
	 * @param Type
	 */
	public Produit(int id, String Nom, String Description, String Type, String Marque, double Price, int QtDispo, String imagePath) {
		this.Nom=Nom;
		this.Description = Description;
		this.Type = Type;
		this.Marque= Marque;
		this.Price = Price; 
		this.QtDispo=QtDispo;
		this.imagePath = imagePath;
	}

	public Produit RechercherPdt(String c) {
		return new Produit(QtDispo, c, c, c, c, Price, QtDispo, c);
	}
	
	@Override
	public String toString() {
		return (String) Nom;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public double getPrice() {
		return Price;
	}

	public void setPrice(int price) {
		Price = price;
	}

	public int getQtDispo() {
		return QtDispo;
	}

	public void setQtDispo(int qtDispo) {
		QtDispo = qtDispo;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public int getId() {
		return id;
	}

	public String getImagePath() {
		return imagePath;
	}

	public String getNom() {
		return Nom;
	}
}
