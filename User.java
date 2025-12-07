public abstract class User {
    // storing basic user information and rating
    private final String id;
    private final String name;
    private final String contactInfo;
    private final int age;
    private final String gender;
    private double rating;
    private int ratingCount;
    
    // creating a new user with their basic details
    public User(String id, String name, String contactInfo, int age, String gender) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.age = age;
        this.gender = gender;
        this.rating = 5.0;
        this.ratingCount = 0;
    }
    
    // getting all user information
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getContactInfo() {
        return contactInfo;
    }
    
    public int getAge() {
        return age;
    }
    
    public String getGender() {
        return gender;
    }
    
    public double getRating() {
        return rating;
    }
    
    public int getRatingCount() {
        return ratingCount;
    }
    
    // updating user rating with a new score using running average
    public void updateRating(double newRating) {
        if (newRating < 0.0 || newRating > 5.0) {
            System.out.println("Invalid rating! Must be between 0.0 and 5.0");
            return;
        }
        
        double totalPoints = (this.rating * this.ratingCount) + newRating;
        this.ratingCount++;
        this.rating = totalPoints / this.ratingCount;
    }
    
    // each subclass defines whether they're a driver or rider
    public abstract String getUserType();
    
    // displaying user info in a readable format
    @Override
    public String toString() {
        return String.format("%s (%s) - %s, Age: %d, Rating: %.1f/5.0 (%d reviews)",
                           id, getUserType(), name, age, rating, ratingCount);
    }
}
