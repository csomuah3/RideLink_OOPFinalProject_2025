import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class RideLinkMatcher {
    // managing all users and trips in the system
    private ArrayList<Trip> availableTrips;
    private ArrayList<User> allUsers;
    
    // matching algorithm threshold for time compatibility
    private static final long MAX_TIME_DIFF_MINUTES = 30;
    
    // initializing the matcher system with empty lists
    public RideLinkMatcher() {
        this.availableTrips = new ArrayList<>();
        this.allUsers = new ArrayList<>();
        System.out.println("RideLink Matcher System initialized! Ready to connect commuters!");
    }
    
    // registering a new user and checking for duplicate IDs
    public boolean registerUser(User user) {
        for (User existingUser : allUsers) {
            if (existingUser.getId().equals(user.getId())) {
                System.out.println("Error: User ID " + user.getId() + " already exists!");
                return false;
            }
        }
        
        allUsers.add(user);
        System.out.println("Welcome to RideLink, " + user.getName() + "!");
        System.out.println("You're registered as a " + user.getUserType());
        return true;
    }
    
    // adding a new trip to the available trips list
    public void postTrip(Trip trip) {
        availableTrips.add(trip);
        System.out.println("Trip posted successfully!");
        System.out.println("Trip ID: " + trip.getId());
        System.out.println("Route: " + trip.getOrigin().getName() + 
                         " -> " + trip.getDestination().getName());
    }
    
    // finding trips that match rider's location and time requirements
    public ArrayList<Trip> findMatches(Location riderOrigin, Location riderDestination,
                                       LocalDateTime desiredTime) {
        ArrayList<Trip> matches = new ArrayList<>();
        
        System.out.println("\nSearching for matching trips...");
        System.out.println("Origin: " + riderOrigin.getName());
        System.out.println("Destination: " + riderDestination.getName());
        
        // filtering trips based on status, seats, location and time
        for (Trip trip : availableTrips) {
            if (!trip.getTripStatus().equals("Pending")) continue;
            if (trip.getPassengerCount() >= (trip.getDriver().getCarCapacity() - 1)) continue;
            
            // checking if origin and destination zones match
            boolean originMatches = Location.locationsMatch(riderOrigin, trip.getOrigin());
            boolean destMatches = Location.locationsMatch(riderDestination, trip.getDestination());
            
            if (originMatches && destMatches) {
                long timeDiff = Math.abs(ChronoUnit.MINUTES.between(desiredTime, trip.getDepartureTime()));
                
                if (timeDiff <= MAX_TIME_DIFF_MINUTES) {
                    matches.add(trip);
                    System.out.println("Found match: " + trip.getId());
                }
            }
        }
        
        System.out.println("Found " + matches.size() + " matching trip(s)!");
        return matches;
    }
    
    // generating a report showing system usage and environmental impact
    public String getSystemImpactReport() {
        StringBuilder report = new StringBuilder();
        
        report.append("\n" + "=".repeat(50) + "\n");
        report.append("       RIDELINK SYSTEM IMPACT REPORT\n");
        report.append("=".repeat(50) + "\n\n");
        
        // counting users
        int driverCount = 0;
        int riderCount = 0;
        double totalMoneySaved = 0.0;
        
        for (User user : allUsers) {
            if (user instanceof Driver) {
                driverCount++;
            } else if (user instanceof Rider) {
                riderCount++;
                totalMoneySaved += ((Rider) user).getTotalMoneySaved();
            }
        }
        
        // counting trips by status
        int pendingTrips = 0;
        int activeTrips = 0;
        int completedTrips = 0;
        
        for (Trip trip : availableTrips) {
            switch (trip.getTripStatus()) {
                case "Pending": pendingTrips++; break;
                case "Active": activeTrips++; break;
                case "Completed": completedTrips++; break;
            }
        }
        
        // building the full report with all stats
        report.append(String.format("Total Users: %d\n", allUsers.size()));
        report.append(String.format("  - Drivers: %d\n", driverCount));
        report.append(String.format("  - Riders: %d\n\n", riderCount));
        
        report.append(String.format("Total Trips: %d\n", availableTrips.size()));
        report.append(String.format("  - Pending: %d\n", pendingTrips));
        report.append(String.format("  - Active: %d\n", activeTrips));
        report.append(String.format("  - Completed: %d\n\n", completedTrips));
        
        report.append("FINANCIAL IMPACT:\n");
        report.append(String.format("  Total Money Saved by Riders: GHS %.2f\n", totalMoneySaved));
        
        report.append("\n" + "=".repeat(50) + "\n");
        
        return report.toString();
    }
    
    // finding a user by their ID
    public User getUserById(String userId) {
        for (User user : allUsers) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
    
    // finding a trip by its ID
    public Trip getTripById(String tripId) {
        for (Trip trip : availableTrips) {
            if (trip.getId().equals(tripId)) {
                return trip;
            }
        }
        return null;
    }
    
    // getting all trips that are pending and have space
    public ArrayList<Trip> getAvailableTrips() {
        ArrayList<Trip> available = new ArrayList<>();
        for (Trip trip : availableTrips) {
            if (trip.getTripStatus().equals("Pending") && 
                trip.getPassengerCount() < (trip.getDriver().getCarCapacity() - 1)) {
                available.add(trip);
            }
        }
        return available;
    }
    
    // getting all registered users and trips
    public ArrayList<User> getAllUsers() { return allUsers; }
    public ArrayList<Trip> getAllTrips() { return availableTrips; }
    
    // saving all users to CSV file
    public void saveUsersToCSV() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("users.csv"));
            writer.println("ID,Type,Name,ContactInfo,Age,Gender,Rating,RatingCount,CarModel,CarPlate,CarCapacity,YearsExp,PaymentMethod,MoneySaved,DistanceCommuted");
            
            for (User user : allUsers) {
                if (user instanceof Driver) {
                    Driver d = (Driver) user;
                    writer.printf("%s,Driver,%s,%s,%d,%s,%.1f,%d,%s,%s,%d,%d,,,\n",
                        d.getId(), d.getName(), d.getContactInfo(), d.getAge(), d.getGender(),
                        d.getRating(), d.getRatingCount(), d.getCarModel(), d.getCarPlateNumber(),
                        d.getCarCapacity(), d.getYearsExperience());
                } else if (user instanceof Rider) {
                    Rider r = (Rider) user;
                    writer.printf("%s,Rider,%s,%s,%d,%s,%.1f,%d,,,,,,%s,%.2f,%.2f\n",
                        r.getId(), r.getName(), r.getContactInfo(), r.getAge(), r.getGender(),
                        r.getRating(), r.getRatingCount(), r.getPreferredPaymentMethod(),
                        r.getTotalMoneySaved(), r.getTotalDistanceCommuted());
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
    
    // loading users from CSV file
    public void loadUsersFromCSV() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("users.csv"));
            reader.readLine(); // skip header
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // skip empty lines
                String[] data = line.split(",", -1); // -1 preserves trailing empty fields
                String id = data[0];
                String type = data[1];
                String name = data[2];
                String contact = data[3];
                int age = Integer.parseInt(data[4]);
                String gender = data[5];
                
                if (type.equals("Driver")) {
                    String carModel = data[8];
                    String carPlate = data[9];
                    int carCapacity = Integer.parseInt(data[10]);
                    int yearsExp = Integer.parseInt(data[11]);
                    
                    Driver driver = new Driver(id, name, contact, age, gender, 
                                             carModel, carPlate, carCapacity, yearsExp);
                    allUsers.add(driver);
                } else if (type.equals("Rider")) {
                    String paymentMethod = data[13];
                    Rider rider = new Rider(id, name, contact, age, gender, paymentMethod);
                    rider.updateSavings(Double.parseDouble(data[14]));
                    rider.addToDistanceCommuted(Double.parseDouble(data[15]));
                    allUsers.add(rider);
                }
            }
            reader.close();
            System.out.println("Loaded " + allUsers.size() + " users from CSV");
        } catch (FileNotFoundException e) {
            System.out.println("No previous user data found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading users: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // saving all trips to CSV file
    public void saveTripsToCSV() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("trips.csv"));
            writer.println("TripID,DriverID,OriginName,OriginArea,DestName,DestArea,DepartureTime,Status,PassengerCount");
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (Trip trip : availableTrips) {
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%d\n",
                    trip.getId(),
                    trip.getDriver().getId(),
                    trip.getOrigin().getName(),
                    trip.getOrigin().getArea(),
                    trip.getDestination().getName(),
                    trip.getDestination().getArea(),
                    trip.getDepartureTime().format(formatter),
                    trip.getTripStatus(),
                    trip.getPassengerCount());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving trips: " + e.getMessage());
        }
    }
    
    // loading trips from CSV file
    public void loadTripsFromCSV() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("trips.csv"));
            reader.readLine(); // skip header
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // skip empty lines
                String[] data = line.split(",", -1); // -1 preserves trailing empty fields
                String tripId = data[0];
                String driverId = data[1];
                
                // find the driver
                Driver driver = null;
                for (User user : allUsers) {
                    if (user.getId().equals(driverId) && user instanceof Driver) {
                        driver = (Driver) user;
                        break;
                    }
                }
                
                if (driver != null) {
                    Location origin = new Location(data[2], data[3]);
                    Location destination = new Location(data[4], data[5]);
                    LocalDateTime departureTime = LocalDateTime.parse(data[6], formatter);
                    
                    Trip trip = new Trip(tripId, driver, origin, destination, departureTime);
                    availableTrips.add(trip);
                }
            }
            reader.close();
            System.out.println("Loaded " + availableTrips.size() + " trips from CSV");
        } catch (FileNotFoundException e) {
            System.out.println("No previous trip data found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading trips: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
