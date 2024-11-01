package Products;

public class Customer {
	private static int nextId = 0;
	private int id, tel;
	private String Nom, Prenom, Login, Password, Adresse, Mail;
	
	public Customer (String Nom,String Prenom,String Login, String Password, String Adresse,String Mail, int tel) {
		this.id=nextId;
		nextId++;
		this.Nom=Nom;
		this.Prenom=Prenom;
		this.Login=Login;
		this.Password=Password;
		this.Adresse=Adresse;
		this.Mail=Mail;
		this.tel=tel;
	}
	
}
