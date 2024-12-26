package products;

import java.util.HashMap;

public class Chaussures extends Product implements ProductWithSize{

	
	private String gender, couleur, surface;
	private HashMap<String, Integer> tailleStock;

	public Chaussures(int id, String Nom, String Description, String Type, String Brand, double Price, int QtDispo,  String imagePath, String surface, String gender, String couleur) {
		super(id, Nom, Description, Type, Brand, Price, QtDispo, imagePath);
		this.surface = surface;
		this.tailleStock = new HashMap<>();
		this.gender = gender;
		this.couleur = couleur;
	}	
	
	@Override
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCouleur() {
		return couleur;
	}

	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}
	
	@Override
	public HashMap<String, Integer> getTailleStock() {
		return tailleStock;
	}

	public void setTailleStock(HashMap<String, Integer> tailleStock) {
		this.tailleStock = tailleStock;
	}

	public String getSurface() {
		return surface;
	}

	public void setSurface(String surface) {
		this.surface = surface;
	}
	

}
