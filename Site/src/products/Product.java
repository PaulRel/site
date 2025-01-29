package products;


public class Product {
	private int id;
	private String description, name, type, brand, imagePath;
	private double price;

	/**
	 * Represente un produit pour l'instant
	 * @param Description
	 * @param Price
	 * @param QtDispo
	 * @param Type
	 */
	public Product(int id, String name, String description, String type, String brand, double price, String imagePath) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.type = type;
		this.brand = brand;
		this.price = price; 
		this.imagePath = imagePath;
	}
	
	@Override
	public String toString() {
		return "Nom : " + name + "\nType : " + type + "\nMarque : " + brand +"\nPrix : " + price;
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

	public String getName() {
		return name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public void setId(int id) {
		this.id = id;
	}
}
