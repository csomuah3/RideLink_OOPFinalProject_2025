public class Location {
    // storing location details
    private final String name;
    private final String area;
    
    // creating the new location with name and area
    public Location(String name, String area) {
        this.name = name;
        this.area = area;
    }
    
    // getting all the location info
    public String getName() {
        return name;
    }
    
    public String getArea() {
        return area;
    }
    
    // checking if two locations are in the same area or are the same place
    public static boolean locationsMatch(Location loc1, Location loc2) {
        return loc1.getArea().equalsIgnoreCase(loc2.getArea()) || 
               loc1.getName().equalsIgnoreCase(loc2.getName());
    }
    
    // displaying location info in a readable format
    @Override
    public String toString() {
        return String.format("%s (%s)", name, area);
    }
}
