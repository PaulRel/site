package Products;

public class Test {

	public static void main(String[] args) {
		Produit pdt = new Produit(0, "pdt1", "desc", 10, 3, "sac", null);
		Produit pdt2 = new Produit(0, "pdt2", "desc", 150, 4, "raquette", null);
		System.out.println(pdt.getId()+ " type : "+ pdt.getType());
		System.out.println(pdt2.toString());
		
		

	}

}
