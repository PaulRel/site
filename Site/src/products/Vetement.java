package products;

import java.util.HashMap;

public class Vetement extends Produit{
	

	public enum TypeVetement{
		SHORT, SWEAT, DEBARDEUR, TSHIRT, ROBE, VESTE
	}
	
	private String type, genre, couleur;
	private HashMap<String, Integer> tailleStock;
	private TypeVetement typeVetement;

	public Vetement(int id, String Nom, String Description, String type, String Marque, double Price, int QtDispo,  String imagePath, TypeVetement typeVetement, String genre, String couleur) {
		super(id, Nom, Description, type, Marque, Price, QtDispo, imagePath);
		this.typeVetement = typeVetement;
		this.tailleStock = new HashMap<>();
		this.genre = genre;
		this.couleur = couleur;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

	public HashMap<String, Integer> getTailleStock() {
		return tailleStock;
	}

	public void setTailleStock(HashMap<String, Integer> tailleStock) {
		this.tailleStock = tailleStock;
	}

	public TypeVetement getTypeVetement() {
		return typeVetement;
	}

	public void setTypeVetement(TypeVetement typeVetement) {
		this.typeVetement = typeVetement;
	}

}
