/*
 * This class is the main class of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.
 * 
 * This class holds an enumeration of all command words known to the game.
 * It is used to recognise commands as they are typed in.
 *
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0 (February 2002)
 */

class CommandWords
{
    // a constant array that holds all valid command words
	/*
	 * go: used to go between rooms(go north)
	 * quit: quits game(quit)
	 * help: lists all commands(help)
	 * pickup: picks up an item from the current room or container, it can also pickup containers if they're liftable(pickup fork)
	 * drop: drops and item or container in your inv(drop spork)
	 * list: lists all of the items and containers in your inv(list)
	 * open: opens a container(open drawer)
	 * close: closes the current container(close drawer)
	 * info: prints the longdesc of the current room(info)
	 * activate: works only with the lever(activate lever)
	 * unlock: the command word for unlocking the locked door in the kitchen(unlock door)
	 * read: the command for reading the paper found in the chest on the second floor(read)
	 */
    private static final String validCommands[] = {
        "go", "quit", "help", "pickup", "drop", "list", "open", "close", "info", "activate", "unlock", "read"
    };

    /**
     * Constructor - initialise the command words.
     */
    public CommandWords()
    {
        // nothing to do at the moment...
    }

    /**
     * Check whether a given String is a valid command word. 
     * Return true if it is, false if it isn't.
     */
    public boolean isCommand(String aString)
    {
        for(int i = 0; i < validCommands.length; i++) {
            if(validCommands[i].equals(aString))
                return true;
        }
        // if we get here, the string was not found in the commands
        return false;
    }

    /*
     * Print all valid commands to System.out.
     */
    public void showAll() 
    {
        for(int i = 0; i < validCommands.length; i++) {
            System.out.print(validCommands[i] + "  ");
        }
        System.out.println();
    }
}
