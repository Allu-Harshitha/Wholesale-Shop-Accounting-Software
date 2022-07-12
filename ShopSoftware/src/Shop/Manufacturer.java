package Shop;

import java.util.ArrayList;

public class Manufacturer {
	
	int id; 
	String name;
	ArrayList<Product> products = new ArrayList<>();
	
	public Manufacturer(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
}
