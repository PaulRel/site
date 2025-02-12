package products;

import java.util.HashMap;

public class Shoes extends Product implements ProductWithSize{
	
	private String gender, color, surface;
	private HashMap<String, Integer> sizeStock;

	public Shoes(int id, String Nom, String Description, String Type, String Brand, double Price, String imagePath, String surface, String gender, String color) {
		super(id, Nom, Description, Type, Brand, Price, imagePath);
		this.surface = surface;
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

	public void setSizeStock(HashMap<String, Integer> sizeStock) {
		this.sizeStock = sizeStock;
	}

	public String getSurface() {
		return surface;
	}

	public void setSurface(String surface) {
		this.surface = surface;
	}
	

}
