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
        this.weight = weight;
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
     * Returns the weight of the item
     * 
     * @return the item weight
     */
    public double getWeight()
    {
        return weight;
    }
    
    /**
     * 
     * Prints detials about items when used.
     * 
     */
    public void use() 
    {
        System.out.println("You use the " + name + ".");
        System.out.println(description);
        if(weight > 0) {
            System.out.println("It weighs " + weight + " lbs.");
        }
    }
}