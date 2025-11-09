/**
 * The item class represents an object that can be found in a room
 * in in the game. Each item has a name, weight, and description.
 *
 * @author Angelo
 * @version 1.0
 */
public class Item
{
    // fields 
    private String name;
    private String description;
    private double weight;

    /**
     * 
     * Constructs a new Item with the specified name and description
     * 
     * @param name the name of the item
     * @param description a brief description of the item
     * @param weight the weight of the item
     */
    public Item(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    /**
     * 
     * Returns the name of the item
     * 
     * @return the item name
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * 
     * Return the description of the item
     * 
     * @return the item description
     */
    public String getDescription() 
    {
        return description;
    }
    
    /**
     * 
     * Return the weight of the item
     * 
     * @return the item weight
     */
    public double getWeight()
    {
        return weight;
    }
}