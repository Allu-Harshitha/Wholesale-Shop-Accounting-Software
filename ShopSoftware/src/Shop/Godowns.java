package Shop;

import java.util.ArrayList;

public class Godowns {

	int id;
	String name;
	int zipCode;
	ArrayList<Product> prodsInventory = new ArrayList<>();
	ArrayList<Integer> numsInventory = new ArrayList<>();
	
	public Godowns(int id, String name, int zipCode){
		this.id = id;
		this.name = name;
		this.zipCode = zipCode;
	}
}
