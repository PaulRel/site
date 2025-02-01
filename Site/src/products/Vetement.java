package products;

import java.util.HashMap;

public class Vetement extends Product implements ProductWithSize{
	

	public enum TypeVetement{
		SHORT, SWEAT, DEBARDEUR, TSHIRT, ROBE, VESTE
	}
	
	private String gender, couleur;
	private HashMap<String, Integer> tailleStock;
	private TypeVetement typeVetement;

	public Vetement(int id, String Nom, String Description, String Type, String Marque, double Price,  String imagePath, TypeVetement typeVetement, String gender, String couleur) {
		super(id, Nom, Description, Type, Marque, Price, imagePath);
		this.typeVetement = typeVetement;
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
	public HashMap<String, Integer> getSizeStock() {
		return tailleStock;
	}

	public void setSizeStock(HashMap<String, Integer> tailleStock) {
		this.tailleStock = tailleStock;
	}

	public TypeVetement getTypeVetement() {
		return typeVetement;
	}

	public void setTypeVetement(TypeVetement typeVetement) {
		this.typeVetement = typeVetement;
	}

}
