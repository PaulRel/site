package products;

import java.util.HashMap;

public class Chaussures extends Product implements ProductWithSize{

	
	private String genre, couleur, surface;
	private HashMap<String, Integer> tailleStock;

	public Chaussures(int id, String Nom, String Description, String Type, String Marque, double Price, int QtDispo,  String imagePath, String surface, String genre, String couleur) {
		super(id, Nom, Description, Type, Marque, Price, QtDispo, imagePath);
		this.surface = surface;
		this.tailleStock = new HashMap<>();
		this.genre = genre;
		this.couleur = couleur;
	}	
	
	@Override
	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
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
