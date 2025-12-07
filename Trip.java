import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Trip {
    // storing all trip details and tracking status
    private String id;
    private Driver driver;
    private ArrayList<User> passengers;
    private int passengerCount;
    private Location origin;
    private Location destination;
    private LocalDateTime departureTime;
    private double tripDistanceKm;
    private String tripStatus;
    
    // constants for fare calculations
    private static final double FUEL_COST_PER_KM = 2.5;
    private static final double BASE_FARE = 15.0;
    
    // creating a new trip with all the route and time details
    public Trip(String id, Driver driver, Location origin, Location destination,
                LocalDateTime departureTime) {
        this.id = id;
        this.driver = driver;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.passengers = new ArrayList<>();
        this.passengerCount = 0;
        this.tripDistanceKm = 10.0; // fixed distance for all trips
        this.tripStatus = "Pending";
    }
    
    // getting all trip information
    public String getId() { return id; }
    public Driver getDriver() { return driver; }
    public ArrayList<User> getPassengers() { return passengers; }
    public int getPassengerCount() { return passengerCount; }
    public Location getOrigin() { return origin; }
    public Location getDestination() { return destination; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public double getTripDistanceKm() { return tripDistanceKm; }
    public String getTripStatus() { return tripStatus; }
    
    // adding a passenger if there's space and they're not already in the trip
    public boolean addPassenger(User rider) {
        if (passengerCount >= (driver.getCarCapacity() - 1)) {
            System.out.println("Sorry, this trip is full!");
            return false;
        }
        
        for (User p : passengers) {
            if (p.getId().equals(rider.getId())) {
                System.out.println("This rider is already in this trip!");
                return false;
            }
        }
        
        passengers.add(rider);
        passengerCount++;
        System.out.println(rider.getName() + " has been added to the trip!");
        return true;
    }
    
    // starting the trip if it's still pending
    public void startTrip() {
        if (tripStatus.equals("Pending")) {
            tripStatus = "Active";
            System.out.println("Trip " + id + " has started! Drive safe!");
        } else {
            System.out.println("Cannot start trip - current status: " + tripStatus);
        }
    }
    
    // getting trip status
    public String getStatus() {
        return tripStatus;
    }
    
    // setting trip status
    public void setStatus(String status) {
        this.tripStatus = status;
    }
    
    // completing the trip and updating all stats for driver and riders
    public void completeTrip() {
        if (!tripStatus.equals("Active")) {
            System.out.println("Cannot complete trip - must be active first!");
            return;
        }
        
        double farePerPerson = calculateFarePerPerson();
        
        // updating each rider's stats with distance and savings
        for (User passenger : passengers) {
            if (passenger instanceof Rider) {
                Rider rider = (Rider) passenger;
                rider.addToDistanceCommuted(tripDistanceKm);
                
                double soloCost = (tripDistanceKm * FUEL_COST_PER_KM) + BASE_FARE;
                double savings = soloCost - farePerPerson;
                rider.updateSavings(savings);
            }
        }
        
        tripStatus = "Completed";
        System.out.println("Trip " + id + " completed successfully!");
    }
    
    // calculating how much each passenger pays by splitting the total cost
    public double calculateFarePerPerson() {
        double totalCost = BASE_FARE + (tripDistanceKm * FUEL_COST_PER_KM);
        
        // if no passengers yet show estimated fare assuming full car for best price
        if (passengerCount == 0) {
            return totalCost / driver.getCarCapacity();
        }
        
        // otherwise calculate based on actual passengers
        return totalCost / passengerCount;
    }
    
    
    // displaying trip info in a readable format with all details
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        String formattedTime = departureTime.format(formatter);
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Trip %s [%s]\n", id, tripStatus));
        sb.append(String.format("  Route: %s -> %s (%.2f km)\n",
                               origin.getName(), destination.getName(), tripDistanceKm));
        sb.append(String.format("  Departure: %s\n", formattedTime));
        sb.append(String.format("  Driver: %s (%s) - Rating: %.1f/5.0\n",
                               driver.getName(), driver.getCarModel(), driver.getRating()));
        sb.append(String.format("  Seats: %d/%d available\n",
                               (driver.getCarCapacity() - 1 - passengerCount),
                               (driver.getCarCapacity() - 1)));
        sb.append(String.format("  Fare per person: GHS %.2f\n", calculateFarePerPerson()));
        
        if (passengerCount > 0) {
            sb.append("  Passengers:\n");
            for (User p : passengers) {
                sb.append(String.format("    - %s\n", p.getName()));
            }
        }
        
        return sb.toString();
    }
}
