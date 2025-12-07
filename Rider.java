public class Rider extends User {
    // storing rider payment info and travel stats
    private String preferredPaymentMethod;
    private double totalMoneySaved;
    private double totalDistanceCommuted;
    
    // creating a new rider with their payment preference
    public Rider(String id, String name, String contactInfo, int age, String gender,
                 String preferredPaymentMethod) {
        super(id, name, contactInfo, age, gender);
        this.preferredPaymentMethod = preferredPaymentMethod;
        this.totalMoneySaved = 0.0;
        this.totalDistanceCommuted = 0.0;
    }
    
    // getting all rider details
    public String getPreferredPaymentMethod() {
        return preferredPaymentMethod;
    }
    
    public double getTotalMoneySaved() {
        return totalMoneySaved;
    }
    
    public double getTotalDistanceCommuted() {
        return totalDistanceCommuted;
    }
    
    // updating payment method if rider wants to change it
    public void setPreferredPaymentMethod(String method) {
        this.preferredPaymentMethod = method;
    }
    
    // tracking how much money rider saved by carpooling
    public void updateSavings(double amount) {
        if (amount > 0) {
            this.totalMoneySaved += amount;
        }
    }
    
    // adding to total distance traveled
    public void addToDistanceCommuted(double distance) {
        if (distance > 0) {
            this.totalDistanceCommuted += distance;
        }
    }
    
    // identifying this user as a rider
    @Override
    public String getUserType() {
        return "Rider";
    }
    
    // displaying rider info in a readable format
    @Override
    public String toString() {
        String basicInfo = super.toString();
        String riderInfo = String.format("\n  Payment: %s - Money Saved: GHS %.2f" +
                                       "\n  Total Distance: %.2f km",
                                       preferredPaymentMethod, totalMoneySaved,
                                       totalDistanceCommuted);
        return basicInfo + riderInfo;
    }
}
