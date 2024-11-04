package Products;

public class Chaussures extends Produit {
	public enum Surface {	    
		TOUTES_SURFACES,
		TERRE_BATTUE,
	    GAZON,
	    DUR
	}
	
	private Surface surface;

	public Chaussures(int id, String Nom, String Description, double Price, int QtDispo, String Type, String imagePath, Surface surface) {
		super(id, Nom, Description, Price, QtDispo, Type, imagePath);
		this.surface=surface;		
	}
	

}
