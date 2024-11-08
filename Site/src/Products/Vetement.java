package Products;

public class Vetement extends Produit{
	
	private enum Taille{
		XS, S, M, L, XL
	}
	
	private Taille taille;
	private String type, genre, couleur;

	public Vetement(int id, String Nom, String Description, String Type, String Marque, double Price, int QtDispo,  String imagePath, String type, String genre, Taille taille, String couleur) {
		super(id, Nom, Description, Type, Marque, Price, QtDispo, imagePath);
		this.type=type;
		this.taille=taille;
		this.genre=genre;
		this.couleur=couleur;
	}

}
