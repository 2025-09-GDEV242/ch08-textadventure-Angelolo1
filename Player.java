import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
/**
 * The Player class represents the player in the game.
 * It stored the current room the player is in and their inventory.
 *
 * @author Angelo Martino
 * @version 1.0
 */
public class Player
{
    // fields
    private Room currentRoom;
    private Stack<Room> roomHistory;
    private List<Item> inventory;

    /**
     * Constructs a Player starting in a specific room
     * 
     * @param startingRoom the room where the player begins
     */
    public Player(Room startingRoom)
    {
        this.currentRoom = startingRoom;
        this.roomHistory = new Stack<>();
        this.inventory = new ArrayList<>();
    }

    /**
     *Returns the room the player is currently in
     *
     * @return the current room
     */
    public Room getCurrentRoom()
    {
        return currentRoom;
    }

    /**
     * Sets the player's current room
     * 
     * @param nextRoom the room the player moves to
     */
    public void setCurrentRoom(Room nextRoom)
    {
        if(currentRoom != null) {
            roomHistory.push(currentRoom);
        }
        this.currentRoom = nextRoom;
    }
    
    /**
     * Takes you back to the previous room (single-step back)
     */
    private void back(){
        if(roomHistory.isEmpty()) {
            System.out.println("You can't go back any further!");
        } else {
            currentRoom = roomHistory.pop();
            System.out.println(currentRoom.getLongDescription());
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

        while (roomHistory.size() > 1) {
            roomHistory.pop();
        }

        currentRoom = roomHistory.pop();
        System.out.println("You have backtracked to the starting room");
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Adds an item to the player's inventory
     * 
     * @param item the item to add
     */
    public void addItem(Item item)
    {
        inventory.add(item);
    }

    /**
     * Removes an item from the player's inventory
     * 
     * @param item the item to remove
     */
    public void removeItem(Item item)
    {
        inventory.remove(item);
    }

    /**
     * Finds an item in the player's inventory by name
     * 
     * @param itemName the name of the item to find
     * @return the item if found, otherwse null
     */
    public Item findInInventory(String itemName)
    {
        for (Item item : inventory) {
            if(item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Prints all of the items currently in the player's inventory
     * 
     */
    public void showInventory()
    {
        if(inventory.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("You are carrying:");
            for(Item item : inventory) {
                System.out.println(" - " + item.getName() + " (Weight: "
                    + item.getWeight() + " lbs) : " + item.getDescription());
            }
        }
    }
}