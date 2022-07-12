package Shop;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Scanner;

/*All id's (other than class order) are 5-digit integers
 * Customer id's hash function: id%1000
 * Manufacturer id's hash function: id%10^2
 * Shops and Warehouses id's hash function: id%10^2
 * Order id is the order number itself
 */
/*All id's except for products' are assumed to be in sequential order each indicating their indices in the arraylist
 * For example, if the entity number is 123, it's id is taken as 10123 (adapted from KGP Roll no. system)
 */
//Products' id's are considered to be market id's and a hash mapping is implemented

public class Main {
	
	public static void printPurchasedProducts(Dealer C) {
		for(int i=0; i<C.purchasedProds.size(); i++) System.out.println(C.purchasedProds.get(i).id + " " + C.purchasedProds.get(i).name);
	}
	
	public static void printInventory(Godowns G) {
		for(int i=0; i<G.prodsInventory.size(); i++) {
			Product P = G.prodsInventory.get(i); 
			int num = G.numsInventory.get(i);
			
			System.out.println(i+1 + ". " + P.id + " " + P.name + " - " + num);
		}
	}

	public static void printProdsList(Manufacturer M) {
		for(int i=0; i<M.products.size(); i++) System.out.println(M.products.get(i).id + " " + M.products.get(i).name);
	}
	
	final static int maxProducts = 10;
	public static int pHash(int key) { 
		return key % maxProducts;
		}
	//to insert a product into products list
	public static void addToProdsList(Product[] list, Product P, int hash) {
		//if the hash position is empty, product is inserted
		//Here, the default initialized integers would be 0 positions where elements deleted were marked -1, hence the condition
		if(list[hash] == null || list[hash].id == -1) {
			list[hash] = P;
			return;
		}
		int i = hash+1;
		//probing until an empty position is obtained
		while(!(list[i] == null || list[i].id == -1) && i <= maxProducts-1) i++;
		if(i < maxProducts) {
			list[i] = P;
			return;
		}
		i=0;
		while(!(list[i] == null || list[i].id == -1) && i <= hash-1) i++;
		if(i < hash) {
			list[i] = P;
			return;
		}
		//if no empty position is found,
		Toolkit.getDefaultToolkit().beep(); //an alert sound is given 
		System.out.println("Error! Products list overload..."); // prints error warning
		return;
	}
	public static int getIndex(Product[] list, int reqId) {
		
		int hash = pHash(reqId);
		int k = hash;
		
		//search algorithm for linear probing
		if(list[k] != null && list[k].id == reqId) {
			return k;
		}
		while(++k < maxProducts) if(list[k] != null && list[k].id == reqId) return k;
		k=0;
		while(++k < hash) if(list[k] != null && list[k].id == reqId) return k;
		
		//if product is not found,
		Toolkit.getDefaultToolkit().beep();
		System.out.println("Error! Product not found");
		return -1; //returns -1 if product is not found
	}
	public static void deleteProd(Product[] list, int delId) {
		
		int index = getIndex(list, delId); 
		
		if(index == -1) {
			Toolkit.getDefaultToolkit().beep();
			System.out.println("Error! Product not found");
			return;
		}
		//product is found 
		list[index].id = -1; //product id at that position is made -1 as a mark of position's emptiness
	}
	
	public static void printDealersList(ArrayList<Dealer> dealersList) {
		System.out.println("Dealer id\tDealer name\t\t\tZipcode");
		for(int i=0; i<dealersList.size(); i++) {
			Dealer C = dealersList.get(i);
			if(C.id == -1) continue;
			System.out.printf("%d\t\t%-32s%d\n", C.id, C.name, C.zipCode);
		}
	}
	public static void printManufacturersList(ArrayList<Manufacturer> manufacturersList) {
		System.out.println("Manufacturer id\tManufacturer name\t\t\t");
		for(int i=0; i<manufacturersList.size(); i++) {
			Manufacturer M = manufacturersList.get(i);
			if(M.id == -1) continue;
			System.out.printf("%d\t\t\t%s\n", M.id, M.name);
		}
	}
	public static void printProductsList(Product[] productsList) {
		System.out.println("Product id\tProduct name\t\t\tManufacturer id");
		for(int i=0; i<maxProducts; i++) {
			Product P = productsList[i];
			if(P != null && P.id != -1) System.out.printf("%d\t\t%-32s%d\n", P.id, P.name, P.manufacturerId); 
		}
	}
	public static void printGodownsList(ArrayList<Godowns> godownsList) {
		System.out.println("Godown id\t\tGodown name\t\t\t\tZipcode");
		for(int i=0; i<godownsList.size(); i++) {
			Godowns G = godownsList.get(i);
			if(G.id == -1) continue;
			System.out.printf("%d\t\t%-32s%d\n", G.id, G.name, G.zipCode);
		}
	}
	public static void printAgentsList(ArrayList<DeliveryAgent> deliAgentsList) {
		System.out.println("Agent id\t\tAgent name\t\t\tZipcode");
		for(int i=0; i<deliAgentsList.size(); i++) {
			DeliveryAgent D = deliAgentsList.get(i);
			if(D.id == -1) continue;
			System.out.printf("%d\t\t%-32s%d\n", D.id, D.name, D.zipCode);
		}
	}
	
	public static boolean checkProdAvailability(Godowns G, int pId, int numC) {
		for(int i=0; i<G.prodsInventory.size(); i++) {
			int prodId = G.prodsInventory.get(i).id;
			if(prodId == pId) //if product is available
			{
				int numAvailable = G.numsInventory.get(i);
				if(numAvailable >= numC) //if sufficient stock is available
				{
					G.numsInventory.set(i, numAvailable-numC ); //available stock at the shop is decreased
					return true;
				}
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		
		System.out.println("VVR Fertilizers and Pesticides welcomes you!"); //prints welcome message
		//Scanner object
		Scanner userIn = new Scanner(System.in);
		 
		//required lists
		Product[] productsList = new Product[maxProducts]; 
		ArrayList<Dealer> dealersList = new ArrayList<>();
		ArrayList<Godowns> godownsList = new ArrayList<>();
		ArrayList<Manufacturer> manufacturersList = new ArrayList<>();
		ArrayList<DeliveryAgent> deliAgentsList = new ArrayList<>();
		ArrayList<Order> ordersList = new ArrayList<>();

		while(true) {
			//loop iterates until user chooses to terminate
			
			//prints available options
			System.out.println("\nPlease select an option-\n");
			System.out.println("1: Add/Delete/Print any of the entities");
			System.out.println("2: Add a product to a manufacturer");
			System.out.println("3: Add a certain number of copies of a product to a shop");
			System.out.println("4: Add an order of a product from a dealer");
			System.out.println("5: Process an order");
			System.out.println("6: List all the purchases made by a customer");
			System.out.println("7: List inventory of a shop");
			System.out.println("8: List products made by a manufacturer");
			System.out.println("9: Logout");
			System.out.print("\nEnter your option here: ");
			
			//takes input from the user
			int selectedOption = userIn.nextInt();
			
			if(selectedOption == 9) {
				System.out.println("Logging out...\nSee you again!");
				break; //loop terminates
			}
			
			switch(selectedOption) {
		/******* Add/Print/Delete an entity *******/
			case 1: 
				
				//asks user to select the function to be performed
				System.out.println("\nPlease select an option-\n");
				System.out.println("1: To add an entity");
				System.out.println("2: To print the list of an entity");
				System.out.println("3: To delete an entity");
				System.out.print("\nEnter your option here(Enter -1 to cancel): ");
				
				int entered = userIn.nextInt();
				if(entered == -1) {
					System.out.println("Request cancelled.");
					break;
				}
				switch(entered) {
				/* To add an entity */
				case 1:
					System.out.println("Select the entity you want to add-");
					System.out.println("1:Dealer 2:Manufacturer 3:Product 4:Godown 5:Delivery Agent");
					System.out.print("Enter your option here(Enter -1 to cancel): ");
					
					int entity = userIn.nextInt();
					if(entity == -1) {
						System.out.println("Request cancelled.");
						break;
					}
					
					switch(entity) {
					case 1: //add a dealer
						int cId = 10000 + dealersList.size() + 1; //new id is generated
						//takes input
						System.out.println("Enter the dealer details-");
						System.out.print("Dealer name: ");
						String cName = userIn.nextLine(); cName = userIn.nextLine(); //to clear the newline buffer
						System.out.print("Dealer zipcode: ");
						int cZCode = userIn.nextInt();
						
						Dealer C = new Dealer(cId, cName, cZCode);
						dealersList.add(C); //new dealer added to dealer's list
						System.out.println("Dealer added successfully!"
								+ "\nPlease note the dealer id- " + cId);
						break;
						
					case 2: //add a manufacturer
						int mId = 10000 + manufacturersList.size() + 1; //new id is generated
						//takes input
						System.out.println("Enter the manufacturer details-");
						System.out.print("Manufacturer name: ");
						String mName = userIn.nextLine(); mName = userIn.nextLine();
						
						Manufacturer M = new Manufacturer(mId, mName);
						manufacturersList.add(M); //added to the list
						System.out.println("Manufacturer added successfully! You can add its products later using option 2."
								+ "\nPlease note the Manufacturer id- " + mId);
						break;
						
					case 3: //add a product
						//takes input
						System.out.println("Enter the product details-");
						System.out.print("Product id: ");
						int pId = userIn.nextInt();
						System.out.print("Product name: ");
						String pName = userIn.nextLine(); pName = userIn.nextLine(); 
						System.out.print("Product's Manufacturer id: ");
						int pMId = userIn.nextInt();

						if(pMId%100 > manufacturersList.size()){
							System.out.println("Invalid Manufacturer id");
							break;
						}
						
						Product P = new Product(pId, pName, pMId);
						addToProdsList(productsList, P, pHash(pId)); //function call to add new product to products list 
						manufacturersList.get(pMId%100 - 1).products.add(P); //added to the products list of the manufacturer
						
						System.out.println("Product added successfully!");
						
						break;
						
					case 4: //add a godown
						int gId = 10000 + godownsList.size() + 1; // new id generated
						//takes input
						System.out.println("Enter the godown details-");
						System.out.print("Godown name: ");
						String gName = userIn.nextLine(); cName = userIn.nextLine(); 
						System.out.print("Godown zipcode: ");
						int gZCode = userIn.nextInt();
						
						Godowns G = new Godowns(gId, gName, gZCode);
						godownsList.add(G); //added to the list
						System.out.println("Shop added successfully! You can add to its inventory later using option 3."
								+ "\nPlease note the shop id- " + gId);
						break;
					
					case 5: //add a delivery agent
						int dId = 10000 + deliAgentsList.size() + 1;
						System.out.println("Enter the delivery agent details-");
						System.out.print("Agent name: ");
						String dName = userIn.nextLine(); dName = userIn.nextLine(); 
						System.out.print("Agent zipcode: ");
						int dZCode = userIn.nextInt();
						
						DeliveryAgent D = new DeliveryAgent(dId, dName, dZCode);
						deliAgentsList.add(D);
						System.out.println("Delivery Agent added successfully!"
								+ "\nPlease note the agent id- " + dId);
						break;
					//if a number other than given options is entered	
					default: System.out.println("Invalid entry...select among given entities."); break;
					}
					break;
				/* To print the list of selected entity */	
				case 2:
					System.out.println("Select the entity you want to list-");
					System.out.println("1:Dealer 2:Manufacturer 3:Product 4:Godown 5:Delivery Agent");
					System.out.print("Enter your option here(Enter -1 to cancel): ");
					
					entity = userIn.nextInt();
					if(entity == -1) {
						System.out.println("Request cancelled.");
						break;
					}
					
					switch(entity) {
					case 1: //to print the dealers list
						printDealersList(dealersList); //function call
						break;
					case 2: //print the manufacturer's list
						printManufacturersList(manufacturersList);
						break;
					case 3: //print the products list
						printProductsList(productsList);
						break;
					case 4: //print godowns list
						printGodownsList(godownsList);
						break;
					case 5: //print delivery agent's list
						printAgentsList(deliAgentsList);
						break;
					default: System.out.println("Invalid entry...select among given entities."); break;
					}
				break;	
				/* To delete a selected entity */
				case 3: 
					System.out.println("Select the entity you want to list-");
					System.out.println("1:Dealer 2:Manufacturer 3:Product 4:Godown 5:Delivery Agent");
					System.out.print("Enter your option here(Enter -1 to cancel): ");
					
					entity = userIn.nextInt();
					if(entity == -1) {
						System.out.println("Request cancelled.");
						break;
					}
					
					switch(entity) {
					case 1: //delete a dealer
						printDealersList(dealersList); //prints the dealers list
						System.out.print("Enter the id of the dealer to be deleted: ");
						int delId = userIn.nextInt(); //takes input of the id to be deleted
						dealersList.get(delId%1000 - 1).id = -1; //marked as delete
						break;
					case 2: //delete a manufacturer
						printManufacturersList(manufacturersList);
						System.out.print("Enter the id of the manufacturer to be deleted: ");
						delId = userIn.nextInt();
						manufacturersList.get(delId%100 - 1).id = -1;
						break;
					case 3: //delete a product
						printProductsList(productsList);
						System.out.print("Enter the id of the product to be deleted: ");
						delId = userIn.nextInt();
						deleteProd(productsList, delId);
						break;
					case 4: //delete a godown
						printGodownsList(godownsList);
						System.out.print("Enter the id of the godown to be deleted: ");
						delId = userIn.nextInt();
						godownsList.get(delId%100 - 1).id = -1;
						break;
					case 5: //delete a delivery agent
						printAgentsList(deliAgentsList);
						System.out.print("Enter the id of the delivery agent to be deleted: ");
						delId = userIn.nextInt();
						deliAgentsList.get(delId%100 - 1).id = -1;
						break;
					default: System.out.println("Invalid entry...select among given entities."); break;
					}
					break;
				default: System.out.println("Invalid entry...select among given options."); break;
				}
				break;
		
		/******* Add a product to manufacturer *******/
			case 2: 
				System.out.print("You chose to add a product to a manufacturer!"
						+ "\nEnter 1 to continue/-1 to cancel the request : ");
				
				entered = userIn.nextInt();
				if(entered == -1) {
					System.out.println("Request cancelled.");
					break;
				}
				
				//takes info from the user if the product is new to market so that it will have to be added to productsList
				System.out.print("Is it a new product? \nEnter here (Yes/No) : ");
				String yOrN = userIn.nextLine(); yOrN = userIn.nextLine();
				if(!(yOrN.equals("Yes") || yOrN.equals("No"))) {
					System.out.println("Error! You should enter \"Yes\" or \"No\".");
					break;
				}
				
				//takes input
				System.out.println("Enter the details-");
				int mId, pId; String pName;
				
				System.out.print("Product id: ");
				pId = userIn.nextInt();
				System.out.print("Product name: ");
				pName = userIn.nextLine(); pName = userIn.nextLine(); //due to newline buffer
				System.out.print("Manufacturer id: ");
				mId = userIn.nextInt();
				
				if((mId % 100) > manufacturersList.size()) {
					//condition true implies manufacturer id is invalid since sequential id's are given
					System.out.println("Invalid manufacturer id!");
					break;
				}
				
				Product P = new Product(pId, pName, mId);
				
				if(yOrN.equals("Yes")) addToProdsList(productsList, P, pHash(pId)); //if the product is new, it is added to the productsList
				
				manufacturersList.get((mId%100)-1).products.add(P); //adds the product to the products list of the manufacturer
				
				System.out.println("Product added!");
				
				break;
				
		/******* Add a certain number of copies of a product to a godown *******/
			case 3: 
				System.out.println("You have chosen to add copies of a product to a godown!");
				
				System.out.print("Enter the godown id (Enter -1 to cancel): ");
				int gId = userIn.nextInt();
				if(gId == -1) {
					System.out.println("Request cancelled.");
					break;
				}
				//similarly as in case 2-
				if((gId % 100) > godownsList.size()) {
					System.out.println("Invalid shop id!");
					break;
				}
				
				//takes input
				int prodId, numC;
				System.out.print("Enter the product id: ");
				prodId = userIn.nextInt();
				System.out.print("Enter the number of copies of the product added: "
						+ "(Enter -1 to undo the entered product):");
				numC = userIn.nextInt();
				
				Godowns gToAdd = godownsList.get(gId%100 - 1);
				Product pAdded = productsList[getIndex(productsList, prodId)];
				int index = gToAdd.prodsInventory.indexOf(pAdded); 
				if(index == -1) {
					//if the products list is initially empty
					gToAdd.prodsInventory.add(pAdded);
					gToAdd.numsInventory.add(0);
				}
				index = gToAdd.prodsInventory.indexOf(pAdded);
				int newCount = gToAdd.numsInventory.get(index) + numC;
				
				gToAdd.numsInventory.set(index, newCount); //copies added
				System.out.println("Stock updated!");
				
				break;
			
		/******* Add a product order from a dealer *******/
			case 4: 
				
				System.out.println("You have chosen to add an order from a dealer!");
				
				System.out.print("Enter the dealer id (Enter -1 to cancel): ");
				int cId = userIn.nextInt();
				
				if(cId == -1) {
					System.out.println("Request cancelled.");
					break;
				}	
				
				if((cId % 1000) > dealersList.size()) {
					System.out.println("Invalid customer id!");
					break;
				}
				
				System.out.print("Enter the no.of products in the order: ");
				int t=userIn.nextInt(); //takes input of no.of products in the order
				int[] orders = new int[t];
				
				for(int i=1; i<=t; i++) {
					//loop iterates for t times
					System.out.print("Enter the product" + i +" id: ");
					prodId = userIn.nextInt();
					System.out.print("Enter the number of copies of the product ordered "
							+ "(Enter -1 to undo the entered product):");
					numC = userIn.nextInt();
					
					if(numC == -1) {
						i--;
						continue; 
					}
					
					orders[i-1] = numC*100000 + prodId;
				}
				
				Order o = new Order(cId, t, orders);
				ordersList.add(o); //new ordered placed is added to the pending orders list
				//user is asked to note the order id so that they can process the order later
				//here the order id is considered to be o123 if the order is at the index 122
				System.out.println("Order placed successfully!"
						+ "\nPlease note the order id (It is required while processisng the order)- o" + ordersList.size());
				
				break;
			
		/******* Process an Order *******/	
			case 5: 
				System.out.println("You have chosen to process an order!");
				System.out.print("Enter the id of the order to be processed( Enter -1 to cancel)"
						+ "(If id is o123, enter 123): ");
				int oId = userIn.nextInt(); //takes input of the order id to be processed
				
				if(oId == -1) {
					System.out.println("Request cancelled.");
					break;
				}
				
				Order O = ordersList.get(oId - 1);
				
				int zCode = dealersList.get(O.customerId%1000 - 1).zipCode;
				int[] orderList = O.orderList;
				
				//array lists to store the list of products in stock and out of stock
				ArrayList<Integer> prodsAvailable = new ArrayList<>();
				ArrayList<Integer> prodsUnavailable = new ArrayList<>();
				
				boolean inStock = false; //boolean to store the availability of the total order
				for(int i=0; i<orderList.length; i++) {
					//decoding the order integer
					pId =  orderList[i]%100000;
					numC = orderList[i]/100000;
					
					boolean orderAvailable = false; //boolean to store the availability of single product in all shops
					for(int j=0; j<godownsList.size(); j++) {
						Godowns G = godownsList.get(j);
			
						if(G.zipCode == zCode) {
							boolean prodAvailable = checkProdAvailability(G, pId, numC);
							//prodAvailable stores the availability of the product at this godown
							orderAvailable = orderAvailable || prodAvailable;
							
							if(prodAvailable) {
								prodsAvailable.add(pId); //added to the available products list
								break;
							}
						}
					}
					if(!orderAvailable) prodsUnavailable.add(pId); //added to the unavailable products list
					inStock = inStock || orderAvailable;	
				}
				if(inStock) {
					
					//assigning a delivery agent
					DeliveryAgent D;
					int min = 10000, reqAgent=0;
					for(int i=0; i<deliAgentsList.size(); i++) {
						D = deliAgentsList.get(i);
						if(D.numProdsDeli < min && D.zipCode == zCode) {
							min = D.numProdsDeli;
							reqAgent = D.id;
						}
					}
					
					deliAgentsList.get(reqAgent%100 - 1).numProdsDeli++; //delivers made by the agent incremented
					
					if(prodsUnavailable.size() == 0) System.out.println("Order processed successfully!");
					else {
						System.out.print("Products instock: ");
						for(int i=0; i<prodsAvailable.size(); i++) System.out.print(prodsAvailable.get(i));
						System.out.print("\nProducts out of stock: ");
						for(int i=0; i<prodsUnavailable.size(); i++) System.out.print(prodsAvailable.get(i));
						System.out.println("Order processed with available products!");
					}
				}
				else System.out.println("Products are not available");
				break;
		
		/******* Case 6: List all the purchases made by a dealer *******/
			
			case 6: 
				System.out.println("You have requested to list a dealer's purchases!");
				
				System.out.print("Enter the dealer id (Enter -1 to cancel): ");
				cId = userIn.nextInt();
				
				if(cId == -1) {
					System.out.println("Request cancelled.");
					break;
				}	
				
				if((cId % 1000) > dealersList.size()) {
					System.out.println("Invalid dealer id!");
					break;
				}
				
				Dealer C = dealersList.get((cId%1000)-1);
				printPurchasedProducts(C); //method prints the list of all products purchased by the customer 
				
				break;
			
		/******* Print the inventory of a godown *******/
			case 7: 
				System.out.println("You have requested to list the inventory of a godown!");
				
				System.out.print("Enter the godown id (Enter -1 to cancel): ");
				gId = userIn.nextInt();
				
				if(gId == -1) {
					System.out.println("Request cancelled.");
					break;
				}
				if((gId % 1000) > godownsList.size()) {
					System.out.println("Invalid shop id!");
					break;
				}
				
				Godowns G = godownsList.get((gId%100)-1);
				printInventory(G); //method prints the inventory of the godown
				break;
		
		/******* Print list of the products made by a manufacturer *******/
			case 8: 
				System.out.println("You have requested for the list of products made by a manufacturer!");
				
				System.out.print("Enter the manufacturer id (Enter -1 to cancel): ");
				mId = userIn.nextInt();
				
				if(mId == -1) {
					System.out.println("Request cancelled.");
					break;
				}
				if((mId % 100) > manufacturersList.size()) {
					System.out.println("Invalid manufacturer id!");
					break;
				}
				
				Manufacturer M = manufacturersList.get((mId%100)-1);
				printProdsList(M); //prints the list of the products made by the manufacturer
				
				break;
		
			//Default case is when the user enters an option not given 	
			default: System.out.println("Invalid number! Enter a number from given otions.");
			
			}
			
		}
		userIn.close(); //scanner object is closed
	}
}