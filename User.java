public abstract class User {
    // storing basic user information
    private final String id;
    private final String name;
    private final String contactInfo;
    private final int age;
    private final String gender;
    
    // creating a new user with their basic details
    public User(String id, String name, String contactInfo, int age, String gender) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.age = age;
        this.gender = gender;
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
    
    // each subclass defines whether they're a driver or rider
    public abstract String getUserType();
    
    // displaying user info in a readable format
    @Override
    public String toString() {
        return String.format("%s (%s) - %s, Age: %d",
                           id, getUserType(), name, age);
    }
}
