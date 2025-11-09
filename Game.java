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
    private Room currentRoom;
    private Room previousRoom;
    private Stack<Room> roomHistory = new Stack<>();
    private List<Item> inventory = new ArrayList<>();     //player's inventory 

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
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
        outside.addItem("map");
        theater.addItem("notebook");
        lab.addItem("laptop");
        pub.addItem("beer");
        library.addItem("book");
        cafeteria.addItem("sandwich");
        garden.addItem("flower");

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

        currentRoom = outside;  // start game outside
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
        System.out.println(currentRoom.getLongDescription());
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
                dropItem(command);
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
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            previousRoom = currentRoom;     //for back
            roomHistory.push(currentRoom);  //for backtrack
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /**
     * Prints a description of the current room
     */
    private void look(){
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Takes you back to the previous room (single-step back)
     */
    private void back(){
        if(previousRoom != null) {
            Room temp = currentRoom;
            currentRoom = previousRoom;
            previousRoom = temp;
            System.out.println(currentRoom.getLongDescription());
        } else {
            System.out.println("You can't go back any further!");
        }
    }

    /**
     * Takes you back through whole room history (multi-step back)
     */
    private void backtrack(){
        if(roomHistory.isEmpty()) {
            System.out.println("No more rooms to backtrack to!");
            return;
        }
        
        while (!roomHistory.isEmpty()) {
            currentRoom = roomHistory.pop();
        }
        
        System.out.println("You have backtracked to the starting room");
        System.out.println(currentRoom.getLongDescription());
    }

    private void takeItem(Command command) {
        if(!command.hasSecondWord()) {
            System.out.println("Take what?");
            return;
        }

        String itemName = command.getSecondWord();
        Item item = currentRoom.getItem(itemName);

        if(item != null) {
            inventory.add(item);
            currentRoom.removeItem(item);
            System.out.println("You have take the " + item.getName() + ".");
        } else { 
            System.out.print("There is no " + itemName + " here!");

        }
    }

    private void dropItem(Command command) {
        if(!command.hasSecondWord()) {
            System.out.println("Drop what?");
            return;
        }

        String itemName = command.getSecondWord();
        Item itemToDrop = null;
        
        for(Item item : inventory) {
            if(item.getName().equalsIgnoreCase(itemName)) {
                itemToDrop = item;
                break;
            }
        }

        if(itemToDrop != null) {
            inventory.remove(itemToDrop);
            currentRoom.addItem(itemToDrop);
            System.out.println("You have dropped the " + itemToDrop.getName() + ".");
        } else {
            System.out.println("You don't have a " + itemName + "!");
        }
    }
    
    private void showInventory() {
        if(inventory.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("You are carrying:");
            for(Item item : inventory) {
                System.out.println(" - " + item.getName() + ": " + item.getDescription());
            }
        }
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
