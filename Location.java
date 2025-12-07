public class Location {
    // storing location details and safety information
    private final String name;
    private final String area;
    private double safetyRating;
    
    // creating the new location with name and area
    public Location(String name, String area) {
        this.name = name;
        this.area = area;
        this.safetyRating = 3.0;
    }
    
    // getting all the location info
    public String getName() {
        return name;
    }
    
    public String getArea() {
        return area;
    }
    
    public double getSafetyRating() {
        return safetyRating;
    }
    
    
    // updating safety rating if it's valid
    public void setSafetyRating(double rating) {
        if (rating >= 0 && rating <= 5) {
            this.safetyRating = rating;
        }
    }
    
    // checking if two locations are in the same area or are the same place
    public static boolean locationsMatch(Location loc1, Location loc2) {
        return loc1.getArea().equalsIgnoreCase(loc2.getArea()) || 
               loc1.getName().equalsIgnoreCase(loc2.getName());
    }
    
    // displaying location info in a readable format
    @Override
    public String toString() {
        return String.format("%s (%s) - Safety: %.1f/5.0", name, area, safetyRating);
    }
}
