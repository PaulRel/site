package Products;

public class Commande {
	private static int nextId = 0;
	private int id, Date, IDCustomer, IDProduit, NumberOfProducts, Montant;
	
	public Commande(int id, int Date, int IDCustomer, int IDProduit, int NumberOfProducts, int Montant) {
		this.id=nextId;
		nextId++;
		this.Date=Date;
		this.IDCustomer=IDCustomer;
		this.IDProduit=IDProduit;
		this.NumberOfProducts=NumberOfProducts;
		this.Montant=Montant;
	}

	public void AddtoC(Produit C) {
		this.NumberOfProducts+=1;
	}
	
	public void DeleteFromC(Produit C) {
		this.NumberOfProducts-=1;
		
	}
}
