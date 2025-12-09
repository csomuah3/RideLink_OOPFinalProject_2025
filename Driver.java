public class Driver extends User {
    // storing driver car details and experience
    private final String carModel;
    private final String carPlateNumber;
    private final int carCapacity;
    private final int yearsExperience;
    
    // creating a new driver with all their info and car details
    public Driver(String id, String name, String contactInfo, int age, String gender,
                  String carModel, String carPlateNumber, int carCapacity, int yearsExperience) {
        super(id, name, contactInfo, age, gender);
        this.carModel = carModel;
        this.carPlateNumber = carPlateNumber;
        this.carCapacity = carCapacity;
        this.yearsExperience = yearsExperience;
    }
    
    // getting all driver details
    public String getCarModel() {
        return carModel;
    }
    
    public String getCarPlateNumber() {
        return carPlateNumber;
    }
    
    public int getCarCapacity() {
        return carCapacity;
    }
    
    public int getYearsExperience() {
        return yearsExperience;
    }
    
    // identifying this user as a driver
    @Override
    public String getUserType() {
        return "Driver";
    }
    
    // showing driver profile with all stats
    @Override
    public String toString() {
        return String.format(
            "DRIVER PROFILE\n" +
            "ID: %s | Name: %s | Age: %d | Gender: %s\n" +
            "Contact: %s\n" +
            "Car: %s (%s) | Capacity: %d passengers\n" +
            "Experience: %d years",
            getId(), getName(), getAge(), getGender(), getContactInfo(),
            carModel, carPlateNumber, carCapacity,
            yearsExperience
        );
    }
}
