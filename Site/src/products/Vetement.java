package products;

import java.util.HashMap;

public class Vetement extends Product implements ProductWithSize{
	

	public enum TypeVetement{
		SHORT, SWEAT, DEBARDEUR, TSHIRT, ROBE, VESTE
	}
	
	private String genre, couleur;
	private HashMap<String, Integer> tailleStock;
	private TypeVetement typeVetement;

	public Vetement(int id, String Nom, String Description, String Type, String Marque, double Price, int QtDispo,  String imagePath, TypeVetement typeVetement, String genre, String couleur) {
		super(id, Nom, Description, Type, Marque, Price, QtDispo, imagePath);
		this.typeVetement = typeVetement;
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

	public TypeVetement getTypeVetement() {
		return typeVetement;
	}

	public void setTypeVetement(TypeVetement typeVetement) {
		this.typeVetement = typeVetement;
	}

}
