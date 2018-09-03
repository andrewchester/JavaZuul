import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/*
 * Class Room - a room in an adventure game.
 *
 * This class is the main class of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0 (February 2002)
 */
/*
 * This class has been modified to print out more information, like containers.
 */
class Room 
{
    private String description;
    private String itemDesc;
    private HashMap exits;        // stores exits of this room.
    private ArrayList<String> items; // stores items in the room
    private String name;
    private ArrayList<Container> containers;//Stores containers of the room
    private Game game;
    
    /**
     * Create a room described "description". Initially, it has no exits.
     * "description" is something like "in a kitchen" or "in an open court 
     * yard".
     */
    public Room(String description, String name, Game game) 
    {
        this.description = description;
        itemDesc = "The room contains ";
        exits = new HashMap();
        items = new ArrayList<String>();
        containers = new ArrayList<Container>();
        this.name = name;
        this.game = game;
    }

    /**
     * Define an exit from this room.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }
    public void giveItem(String item){
    	items.add(item);
    }
    /**
     * Return the description of the room (the one that was defined in the
     * constructor).
     */
    public String getShortDescription()
    {
        return description;
    }
    public String getName(){
    	return name;
    }
    /**
     * Return a long description of this room, in the form:
     *     You are in the kitchen.
     *     It contains: item1, item2, and item3
     *     It has 2 container(s): container1 and container2
     *     Exits: north west
     */
    public String getLongDescription()
    {
    	//Creates a desc variable that gets added onto throughout the method, uses proper punctuation in the for loops too
    	String desc = "You are ";
        desc += description + ". " + "\n"; 
        if(items.size() != 0){
        	desc += "The room contains:";
        	if(items.size() == 1){
        		desc += " a " + items.get(0);
        	}else{
        		boolean proceed;
            	for(String item : items){
            		if(items.indexOf(item) != items.size() - 1)
            			desc += items.get(items.indexOf(item)) + ", ";
            		else
            			desc += "and a " + items.get(items.indexOf(item));
            	
            	}
        	}
        }else{
        	desc += "The room doesn't have any items!";
        }
        
        desc += "\n";
        
        if(containers.size() != 0){
        	desc += "The room has " + containers.size() + " container(s):";
        	for(int i = 0; i < containers.size(); i++)
        		desc += " " + containers.get(i).getName();
        }
        
        desc += "\n" + getExitString();
        
        //Some special cases for some of the rooms to drop clues, or well, a clue
        if(this.name.equals("basement")){
        	System.out.println("An empty basement, with a blank wall that looks unstable.");
        }
        if(this.name.equals("second floor")){
        	System.out.println("A room with blank walls and seemingly no purpose");
        }
        if(this.name.equals("secret room")){
        	System.out.println("You won the game! You found the secret room!");
        	game.setFinished(true);
        	System.exit(0);
        }
        return desc;
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set keys = exits.keySet();
        for(Iterator iter = keys.iterator(); iter.hasNext(); )
            returnString += " " + iter.next();
        return returnString;
    }
    public ArrayList<String> getItems(){
    	return items;
    }
    public ArrayList<Container> getContainers(){
    	return containers;
    }
    public void addContainer(Container c){
    	containers.add(c);
    }
    public void addItem(String item){
    	items.add(item);
    	itemDesc += item + " ";
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     */
    public Room getExit(String direction) 
    {
        return (Room)exits.get(direction);
    }
}

