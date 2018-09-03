import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
//This class is used as an array list, but I needed two array lists, each a separate data type and for them to function as one
//Most of the methods are similar to an arraylist, such as add, but have 2 versions for each data type add(String) for items and add(Container) for containers
public class Inventory {
	
	private ArrayList<String> items;
	private ArrayList<Container> containers;
	private Game game;
	private int inv_space = 3;
	private int inv_space_taken = 0;
	
	Inventory(Game game){
		this.game = game;
		items = new ArrayList<String>();
		containers = new ArrayList<Container>();
	}
	
	//Adding items
	public void add(String item){
		System.out.println("Adding " + item);
		items.add(item);
		inv_space_taken++;
	}
	public void add(Container c){
		if((inv_space - inv_space_taken) < (1 + c.getItems().size())){
			System.out.println("You can't pick up that, you don't have enough room!");
			return;
		}
		containers.add(c);
		
		inv_space_taken += (1 + c.getItems().size());
		System.out.println(c.getName() + " added. You've got " + (inv_space - inv_space_taken) + " slot(s) left");
	}
	
	//Removing items
	public void remove(String item){
		//checks if the string could also be a container, if it is, it removes it
		for(Container c : containers){
			if(c.getName().equals(item)){
				remove(c);
				return;
			}
		}
		
		items.remove(items.indexOf(item));
		inv_space_taken--;
	}
	public void remove(Container c){
		containers.remove(containers.indexOf(c));
		game.getCurrentRoom().addContainer(c);
		inv_space_taken -= (1 + c.getItems().size());
	}
	
	//Inventory space management
	public void increaseSpace(int i){
		inv_space += i;
		System.out.println("You've gained " + i + " slot(s)");
	}
	public void decreaseSpace(int i ){
		inv_space -= i;
		System.out.println("You've gained " + i + " slot(s)");
	}
	public int size(){
		return items.size() + containers.size();
	}
	
	//Space taken 
	public int getSpace(){
		return inv_space;
	}
	public int getSpaceTaken(){
		return inv_space_taken;
	}
	
	//Getting items
	public String get(String item){
		return items.get(items.indexOf(item));
	}
	public Container get(Container c){
		return containers.get(containers.indexOf(c));
	}

	//Testing array for items
	public boolean contains(String item){
		//If String item is a container, then recall this method except pass the appropriate container 
		for(Container c : containers)
			if(c.getName().equals(item))
				return contains(c);
		
		if(items.contains(item))
			return true;
		
		return false;
	}
	public boolean contains(Container c){
		if(containers.contains(c))
			return true;
		
		return false;
	}
	
	//Randomly dropping 1 item from the arrays
	public void randomDrop(){
		int rand = ThreadLocalRandom.current().nextInt(0, 2);
		if(rand == 1){
			int random = ThreadLocalRandom.current().nextInt(0, items.size());
			remove(items.get(random));
		}else{
			int random = ThreadLocalRandom.current().nextInt(0, containers.size());
			remove(containers.get(random));
		}
	}
	
	//List out items inside the inventory, called with the list command
	public void list(){
		String item_list = " ";
		item_list += "Items: ";
		if(items.size() < 2)
			item_list += "a " + items.get(0);
		else
			for(int i = 0; i < items.size(); i++)
				if(i != items.size() - 1)
					item_list += items.get(i) + ", ";
				else
					item_list += "and a " + items.get(i);
		
		item_list += "\n";
		item_list += "Containers: ";
		for(Container c : containers){
			String c_items = "";
			for(int i = 0; i < c.getItems().size(); i++)
				if(i != c.getItems().size() - 1)
					c_items +=  c.getItems().get(i) + " ";
				else
					c_items += c.getItems().get(i);
			
			item_list += c.getName() + "(" + c_items + ")" + " ";
		}
		
		System.out.println(item_list);
	}
	//Takes a string and checks if it's the name of a container
	public boolean isContainer(String item){
		for(Container c : containers)
			if(c.getName().equals(item))
				return true;
		
		return false;
	}
}
