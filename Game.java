import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game 
{
    private Parser parser;
    private Player player;//player's inventory 

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        Room startingRoom = createRooms();
        player = new Player(startingRoom);
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private Room createRooms()
    {
        Room outside, theater, pub, lab, office, library, cafeteria, garden;

        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theater = new Room("in a lecture theater");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        library = new Room("in the university library");
        cafeteria = new Room ("in the campus cafeteria");
        garden = new Room ("in a peaceful university garden.");

        // adding items to rooms
        
        outside.addItem(new Item("map", "A detailed campus map.", 0.1));
        theater.addItem(new Item("notebook", "your lecture notes", 1.2));
        lab.addItem(new Item("laptop", "your trusty laptop", 2.5));
        pub.addItem(new Item("beer", "a cold beverage", 0.2));
        library.addItem(new Item("book", "a thick textbook", 7));
        cafeteria.addItem(new Item("sandwich", "a tasty snack", 0.3));
        garden.addItem(new Item("flower", " a beautiful flower", 0.1));

        // initialise room exits
        outside.setExit("east", theater);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        outside.setExit("north", garden);

        theater.setExit("west", outside);
        theater.setExit("north", library);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);
        lab.setExit("south", cafeteria);

        office.setExit("west", lab);

        library.setExit("south", theater);

        cafeteria.setExit("north", lab);

        garden.setExit("south", outside);

        return outside;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(player.getCurrentRoom().getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;

            case LOOK:
                look();
                break;

            case BACK:
                back();
                break;

            case BACKTRACK:
                backtrack();
                break;

            case TAKE:
                takeItem(command);
                break;

            case DROP:
                removeItem(command);
                break;

            case USE:
                useItem(command.getSecondWord());
                break;

            case INVENTORY:
                showInventory();
                break;
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
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * @param command The command containing te direction
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
        Room nextRoom = player.getCurrentRoom().getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            player.setCurrentRoom(nextRoom);
            System.out.println(player.getCurrentRoom().getLongDescription());
        }
    }

    /**
     * Prints a description of the current room
     */
    private void look(){
        Room currentRoom = player.getCurrentRoom();
        System.out.println(currentRoom.getLongDescription());
    }
    
    private void back() {
        player.back();
    }
    
    private void backtrack() {
        player.backtrack();
    }

    private void takeItem(Command command) {
        if(!command.hasSecondWord()) {
            System.out.println("Take what?");
            return;
        }

        Room currentRoom = player.getCurrentRoom();
        String itemName = command.getSecondWord();
        Item item = currentRoom.getItem(itemName);

        if(item != null) {
            player.addItem(item);
            currentRoom.removeItem(item);
            System.out.println("You have taken the " + item.getName() + ".");
        } else { 
            System.out.print("There is no " + itemName + " here!");

        }
    }

    private void removeItem(Command command) {
        if(!command.hasSecondWord()) {
            System.out.println("Drop what?");
            return;
        }

        String itemName = command.getSecondWord();
        Item item = player.findInInventory(itemName);

        if(item != null) {
            player.removeItem(item);
            player.getCurrentRoom().addItem(item);
            System.out.println("You have dropped the " + item.getName() + ".");
            } else {
                System.out.println("You don't have a " + itemName + "!");
            }
        }

    private void useItem(String itemName) {
        if(itemName == null || itemName.isEmpty()) {
            System.out.println("Use what?");
            return;
        }

        Item item = player.findInInventory(itemName);
        if(item != null) {
            item.use();
            return;
        } 

        Room currentRoom = player.getCurrentRoom();
        item = currentRoom.getItem(itemName);
        if(item != null) {
            item.use();
            return;
        }

        System.out.println("You don't have a " + itemName + ", and it's not in this room.");
    }

    private void showInventory() {
        player.showInventory();
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
