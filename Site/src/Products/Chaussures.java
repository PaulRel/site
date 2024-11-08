package Products;

public class Chaussures extends Produit {
	public enum Surface {	    
		TOUTES_SURFACES,
		TERRE_BATTUE,
	    GAZON,
	    DUR
	}
	
	private Surface surface;
	private String taille, genre, couleur;

	public Chaussures(int id, String Nom, String Description, String Type, String Marque, double Price, int QtDispo,  String imagePath, Surface surface, String genre, String taille, String couleur) {
		super(id, Nom, Description, Type, Marque, Price, QtDispo, imagePath);
		this.surface=surface;
		this.taille=taille;
		this.genre=genre;
		this.couleur=couleur;
	}
	

}
