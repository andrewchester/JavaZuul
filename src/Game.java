import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initializes all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0 (February 2002)
 */
/*
 * This is a modified version of Zuul to include more rooms, an inventory system, dynamic room changes, usable items, containers,  and win/loss scenarios
 * Andrew Chester
 */
class Game 
{
    private Parser parser;
    private Room currentRoom;
    private boolean finished;
    private Inventory inv;
    private boolean opened = false;
    private Container currentContainer = null;
    private int moves;
    private final int max_moves = 25;    
    //Initializing rooms and containers as global variables so I can change the layout of the house during the game
    Room outside, entrance, hallway, hallway2, stairs, living, family, bathroom, kitchen, basement_stairs, dining, stairs_landing, basement_landing, secret_room, locked_room, bedroom;
    Container drawer, fridge, closet, cabinet, dish_cabinet, chest;
    
    /**
     * Create the game and initialise its internal map.
     */
    //Initializes the number of moves at the start and the inventory
    public Game() 
    {
    	inv = new Inventory(this);
    	moves = 0;
    	finished = false;
        createRooms();
    }

    //Creates all the rooms and containers. Adds exits to the rooms and items to both the containers and rooms
    private void createRooms()
    { 
        // create the rooms
        outside = new Room("outside the main entrance of the house", "outside", this);
        entrance = new Room("in the main entry area of the house", "entrance", this);
        hallway = new Room("in a hallway, between two, maybe more rooms", "hallway", this);
        hallway2 = new Room("in a hallway, between two, maybe more rooms", "hallway", this);
        stairs = new Room("on the dark stairs, heading to floor 2", "stairs", this);
        living = new Room("in the living room", "living room", this);
        family = new Room("in the family room, basically the same as the living room", "family room", this);
        bathroom = new Room("in the bathroom", "bathroom", this);
        kitchen = new Room("in the kitchen", "kitchen", this);
        basement_stairs = new Room("on the stairs to the basement", "basement stairs", this);
        dining = new Room("in the dining room", "dining room", this);
        stairs_landing = new Room("on the second floor", "second floor", this);
        basement_landing = new Room("in the basement", "basment", this);
        secret_room = new Room("in the secret room", "secret room", this);
        locked_room = new Room("in the locked room", "locked room", this);
        bedroom = new Room("in the bedroom", "bedroom", this);
        
        //creates containers
        drawer = new Container("drawer", kitchen, false);
        fridge = new Container("fridge", kitchen, false);
        closet = new Container("closet", entrance, true);
        cabinet = new Container("cabinet", bathroom, false);
        dish_cabinet = new Container("dish cabinet", dining, false);
        chest = new Container("chest", bedroom, true);
        
        // initialize room exits
        outside.setExit("north", entrance);
        outside.addItem("shovel");
        
        entrance.setExit("south", outside);
        entrance.setExit("north", hallway);
        entrance.setExit("east", family);
        entrance.setExit("west", basement_stairs);
        
        hallway.setExit("north", hallway2);
        hallway.setExit("west", kitchen);
        hallway.setExit("east", bathroom);
        hallway.setExit("south", entrance);
        
        hallway2.setExit("north", stairs);
        hallway2.setExit("east", living);
        hallway2.setExit("south", hallway);
        hallway2.setExit("west", dining);
        
        stairs.setExit("south", hallway2);
        stairs.setExit("north", stairs_landing);
        
        living.setExit("west", hallway2);
        
        family.setExit("west", entrance);
        
        bathroom.setExit("west", hallway);
        
        kitchen.setExit("east", hallway);
        kitchen.setExit("north", dining);
        
        locked_room.setExit("east", kitchen);
        
        basement_stairs.setExit("east", entrance);
        basement_stairs.setExit("west", basement_landing);
        
        dining.setExit("south", kitchen);
        dining.setExit("east", hallway2);
        
        stairs_landing.setExit("south", stairs);
        stairs_landing.setExit("north", bedroom);
        
        basement_landing.setExit("east", basement_stairs);
        
        secret_room.setExit("east", basement_landing);

        bedroom.setExit("south", stairs_landing);
        
        //Adding items
        
        entrance.addItem("backpack");
        
        hallway.addItem("torch");
        hallway.addItem("rug");
        
        hallway2.addItem("torch");
        hallway2.addItem("rug");
        
        stairs.addItem("torch");
        stairs.addItem("key");
        stairs.addItem("painting");
        
        living.addItem("TV");
        living.addItem("couch");
        
        family.addItem("couch");
        family.addItem("table");
        
        bathroom.addItem("sink");
        
        kitchen.addItem("door");
        
        locked_room.addItem("lever");
        
        dining.addItem("table");
        
        bedroom.addItem("bed");
        bedroom.addItem("dresser");
        
        //Adding containers and items to those containers
        
        entrance.addContainer(closet);
	        closet.addItem("sweatshirt");
	        closet.addItem("jacket");
	        closet.addItem("shoes");
	        
	    bathroom.addContainer(cabinet);
	        cabinet.addItem("teethbrush");
	        cabinet.addItem("teethpaste");
	        
	    kitchen.addContainer(drawer);
	    	drawer.addItem("fork");
	    	drawer.addItem("fork");
	    	drawer.addItem("fork");
	    	drawer.addItem("spoon");
	    	drawer.addItem("spoon");
	    	drawer.addItem("spoon");
	    	drawer.addItem("spork");
	    	drawer.addItem("spork");
	    	drawer.addItem("spork");
	    	
	    kitchen.addContainer(fridge);
        	fridge.addItem("spoiled milk");
        	
        dining.addContainer(dish_cabinet);
	        dish_cabinet.addItem("plate");
	        dish_cabinet.addItem("plate");
	        dish_cabinet.addItem("bowl");
	        dish_cabinet.addItem("bowl");
	        
	    bedroom.addContainer(chest);
	    	chest.addItem("paper");
	    	chest.addItem("coins");

        currentRoom = outside;  // start game outside
        
        play();//Starting the game
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        parser = new Parser();//Needed to initialize the parser here because, it was null if you initialized it in the game constructor
        while (! finished) {
        	if(parser != null){
                Command command = parser.getCommand();
                finished = processCommand(command);	
        	}
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println("Use clues in the mansion to help discover the secret room, you have 25 moves to win");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * If this command ends the game, true is returned, otherwise false is
     * returned.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help"))
            printHelp();
        else if (commandWord.equals("go"))
        	if(!opened)
        		goRoom(command);
        	else
        		System.out.println("You can't leave the room while searching a container!");
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }else  if(commandWord.equals("pickup")){
        	pickup(command);
        }else if(commandWord.equals("drop")){
        	drop(command);
        }else if(commandWord.equals("list")){
        	inv.list();
        }else if(commandWord.equals("open")){
        	open(command);
        }else if(commandWord.equals("close")){
        	close();
        }else if(commandWord.equals("info")){
        	info();
        }else if(commandWord.equals("activate")){
        	activate(command);
        }else if(commandWord.equals("read")){
        	read();
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("Your command words are:");
        parser.showCommands();
    }
    private void info(){
    	System.out.println(currentRoom.getLongDescription());
    	if(opened){
    		System.out.println("You've opened the " + currentContainer.getName());
    		System.out.println("It has a " + currentContainer.listContents());
    	}
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();
        
        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);
        
        //Increase your move number as long as its less than max_moves, if it is than run the losing condition
        if (nextRoom == null){
            System.out.println("There is no door!");
        } else {
        	if(moves < max_moves){
                currentRoom = nextRoom;
                System.out.println(currentRoom.getLongDescription());
                moves++;
        	}else{
        		System.out.println("You've used to many moves! The game is over.");
        		finished = true;
        		System.exit(0);
        	}
        }
    }
    // The player is going to pickup an item in a room, if they have enough space, they pick it up, if they don't, then they drop a random item
    //They they can also pickup a container and drop them
    private void pickup(Command command){
    	if(!command.hasSecondWord()){
    		System.out.println("What do you want to pick up?");
    		return;
    	}
    	
    	String pickedItem  = command.getSecondWord();
    	
    	//Special items that give you more inventory space or do other stuff
        if(pickedItem.equals("backpack") && inv.getSpaceTaken() < inv.getSpace())
        	inv.increaseSpace(10);
        
        if(pickedItem.equals("sweatshirt") && inv.getSpaceTaken() < inv.getSpace())
        	inv.increaseSpace(4);
        
        if(pickedItem.equals("jacket") && inv.getSpaceTaken() < inv.getSpace())
        	inv.increaseSpace(2);
        
        if(pickedItem.equals("lever")){
        	System.out.println("It's fixed into the ground, you can't pick it up");
        	return;
        }
        if(pickedItem.equals("door")){
        	System.out.println("You can't pick that up!");
        	return;
        }
        
        //Looks for containers in the room, the inv.add method takes a string for items and a type container for containers, so we had to find the container by name
        Container cToRemove = null;
        for(int i = 0; i < currentRoom.getContainers().size(); i++){
        	Container current = currentRoom.getContainers().get(i);
        	if(current.getName().equals(pickedItem)){
        		if(current.getLiftable()){
        			inv.add(current);
            		cToRemove = current;
        		}else{
        			System.out.println("You can't lift that!");
        		}
        	}
        }
        //Removes it from the room
        if(cToRemove != null)
        	currentRoom.getContainers().remove(currentRoom.getContainers().indexOf(cToRemove));
    	
        //Just picks up the item if its not a container
    	if(currentRoom.getItems().contains(pickedItem)){
			inv.add(pickedItem);
			currentRoom.getItems().remove(pickedItem);
    		if(!(inv.getSpaceTaken() >= inv.getSpace())){
    			System.out.println("You picked up a " + pickedItem + ". You've got " + (inv.getSpace() - inv.getSpaceTaken()) + " slot(s) left");
    		}else{
    			inv.randomDrop();//Randomly drops an item if your inventory is too full
    		}
    	}
    }
    //This method drops items from your inventory, if you drop a container, then it will drop all of the items in the container
    private void drop(Command command){
    	if(!command.hasSecondWord()){
    		System.out.println("You didn't specify what you wanted to drop");
    		return;
    	}
    	String itemToDrop = command.getSecondWord();
    	if(!inv.contains(itemToDrop)){
    		System.out.println("Your inventory doesn't have that item");
    		return;
    	}
    	
    	if(opened){ //If you're currently searching a container inside a room, then it stops you from putting containers inside of containers
    		if(inv.isContainer(itemToDrop)){
    			System.out.println("You can't drop a container inside another container!");
    			return;
    		}
    		
    		currentContainer.addItem(itemToDrop);
    		System.out.println("You've dropped a " + itemToDrop + " and the " + currentContainer.getName() + " gained a " + itemToDrop);
    	}
    	inv.remove(itemToDrop);
    }
    //This method is used to open containers, uses a currentContainer method like currentRoom to keep track of which container you're in
    private void open(Command c){
    	if(!c.hasSecondWord()){
    		System.out.println("Open what?");
    		return;
    	}
    	//Special conditions for if you have a key
    	if(c.getSecondWord().equals("door")){
    		if(inv.contains("key")){
    			kitchen.setExit("west", locked_room);
    			System.out.println("You've unlocked the door with the key! However, the key got stuck in the lock");
    			inv.remove("key");
    			System.out.println("The kitchen gained a new exit(west)");
    			return;
    		}else{
    			System.out.println("The door's locked! You can see the faint silhouette of some sort of lever through the keyhole");
    			return;
    		}
    	}
    	
    	opened = true;
    	
    	//Finds the container that corresponds with the user's string
    	String container = c.getSecondWord();
    	Container tempContainer = null;
    	ArrayList<Container> containers = currentRoom.getContainers();
    	for(int i = 0; i < containers.size(); i++){
    		if(containers.get(i).getName().equals(container)){
    			tempContainer = containers.get(i);
    		}
    	}
    	currentContainer = tempContainer;
    	/*Checks to see if that container actually exists
    	 * if it does, then adds all the items to the room so you can just pick them up from currentRoom
    	 * however it also changes the desc of the room so if you printed out the info it wouldn't include all of the containers items in the rooms items
    	 */
    	if(tempContainer == null){
    		System.out.println("That's not in this room!");
    	}else{
    		System.out.println(tempContainer.listContents());
    		currentRoom.getItems().addAll(tempContainer.getItems());
    	}
    }
    //Closes the container, removes all the items from the room so the player can't pick them up 
    private void close(){
    	if(currentContainer == null){
    		System.out.println("You haven't opened anything yet.");
    		return;
    	}
    	currentRoom.getItems().removeAll(currentContainer.getItems());
    	
    	System.out.println("You closed the " + currentContainer.getName());
    	
    	opened = false;
    	currentContainer = null;
    }
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game. Return true, if this command
     * quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else
            return true;  // signal that we want to quit
    }
    //This method is used just with the lever
    //Opens a new rooom in the basement
    private void activate(Command c){
    	if(!c.hasSecondWord()){
    		System.out.println("What are you activating?");
    		return;
    	}
    	
    	if(!c.getSecondWord().equals("lever")){
    		System.out.println("You can't activate that!");
    	}else{
    		basement_landing.setExit("west", secret_room);
    		System.out.println("There's a rumbling nearby, as if the walls of the house were moving.");
    	}
    }
    //A command for reading the clue in the upstairs chest, basically gives the player the answer
    private void read(){
    	if(inv.contains(chest) || inv.contains("paper")){
    		System.out.println("The paper reads: ");
    		System.out.println("stairs --> kitchen --> basement");
    	}else{
    		System.out.println("You have nothing you can read.");
    		return;
    	}
    }
    public void setFinished(boolean finished){
    	this.finished = finished;
    }
    public Room getCurrentRoom(){
    	return currentRoom;
    }
    //Adding a main method because it wasn't here before?
    public static void main(String[] args){
    	new Game();
    }
}
