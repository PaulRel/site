package products;

import java.util.HashMap;

public class Clothing extends Product implements ProductWithSize{
	
	public enum TypeVetement{
		SHORT, SWEAT, DEBARDEUR, TSHIRT, ROBE, VESTE
	}
	
	private String gender, color;
	private HashMap<String, Integer> sizeStock;
	private TypeVetement typeVetement;

	public Clothing(int id, String name, String description, String Type, String brand, double price,  String imagePath, TypeVetement typeVetement, String gender, String color) {
		super(id, name, description, Type, brand, price, imagePath);
		this.typeVetement = typeVetement;
		this.sizeStock = new HashMap<>();
		this.gender = gender;
		this.color = color;
	}

	@Override
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public HashMap<String, Integer> getSizeStock() {
		return sizeStock;
	}

	public void setSizeStock(HashMap<String, Integer>  sizeStock) {
		this.sizeStock = sizeStock;
	}

	public TypeVetement getTypeVetement() {
		return typeVetement;
	}

	public void setTypeVetement(TypeVetement typeVetement) {
		this.typeVetement = typeVetement;
	}

}
