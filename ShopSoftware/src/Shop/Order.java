package Shop;

public class Order {
	
	int customerId;
	int numProducts;
	int[] orderList;
	
	public Order(int customerId, int numProducts, int[] orderList) {
		this.customerId = customerId;
		this.numProducts = numProducts;
		this.orderList = orderList;
	}
}
