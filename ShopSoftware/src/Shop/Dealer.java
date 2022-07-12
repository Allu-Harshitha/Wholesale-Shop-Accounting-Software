package Shop;

import java.util.ArrayList;

public class Dealer {

	int id;
	String name;
	int zipCode;
	ArrayList<Product> purchasedProds = new ArrayList<>();
	
	public Dealer(int id, String name, int zipCode){
		this.id = id;
		this.name = name;
		this.zipCode = zipCode;
	}
	
	public Dealer(ArrayList<Product> purchasedProds) {
		this.purchasedProds = purchasedProds;
	}
	
}
