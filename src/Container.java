import java.util.ArrayList;
//This class is used for containers, each container has a name and holds items
public class Container {
	private String name;
	private Room parent_room;
	private ArrayList<String> items;
	private boolean liftable;
	public Container(String name, Room parent_room, boolean liftable){
		this.name = name;
		this.parent_room = parent_room;
		items = new ArrayList<String>();
		this.liftable = liftable;
	}
	//Lists contents of the container, used when you open a container. 
	public String listContents(){
		String itemList = "";
		if(items.isEmpty()){
			itemList = "It's empty...";
			return itemList;
		}
		itemList += name + " has a ";  
		for(int i = 0; i < items.size(); i++){
			if(i != items.size() - 1){
				itemList += items.get(i) + ", ";
			}else{
				itemList += "and a " + items.get(i);
			}
		}
			
		return itemList;
	}
	public ArrayList<String> getItems(){
		return items;
	}
	
	public void addItem(String item){
		items.add(item);
	}
	public void removeItem(String item){
		items.remove(items.indexOf(item));
	}
	public Room getParent(){
		return parent_room;
	}
	public void setname(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public boolean getLiftable(){
		return liftable;
	}
	public void setLiftable(boolean liftable){
		this.liftable = liftable;
	}
}
