import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class RideLinkDriver {
    // managing the system state and user session
    private static RideLinkMatcher system;
    private static Scanner scanner;
    private static User currentUser = null;
    private static int tripCounter = 1;
    private static int userCounter = 1;
    
    // starting the application and main loop
    public static void main(String[] args) {
        system = new RideLinkMatcher();
        scanner = new Scanner(System.in);
        printWelcomeBanner();
        
        // load data from CSV files
        system.loadUsersFromCSV();
        system.loadTripsFromCSV();
        
        // if no data loaded, setup sample data
        if (system.getAllUsers().isEmpty()) {
            setupSampleData();
        }
        
        boolean running = true;
        while (running) {
            running = (currentUser == null) ? showGuestMenu() : showUserMenu();
        }
        
        // save data to CSV files before exit
        System.out.println("\nSaving data...");
        system.saveUsersToCSV();
        system.saveTripsToCSV();
        System.out.println("Data saved successfully!");
        
        scanner.close();
        System.out.println("Thanks for using RideLink! Drive safe!");
    }
    
    // displaying the welcome banner
    private static void printWelcomeBanner() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                      WELCOME TO");
        System.out.println("                   RIDELINK by Group 8 ");
        System.out.println("              Smart Carpooling for Everyone!");
        System.out.println("=".repeat(60));
        System.out.println("    Save money. Save the environment. Share the journey.");
        System.out.println("=".repeat(60) + "\n");
    }
    
    // loading sample data with users and trips for testing
    private static void setupSampleData() {
        System.out.println("Loading sample data...\n");
        
        Location ashesi = new Location("Ashesi University", "Berekuso");
        Location accraMan = new Location("Accra Mall", "Tetteh Quarshie");
        Location kotoka = new Location("Kotoka Airport", "Airport");
        Location osuOxford = new Location("Osu Oxford Street", "Osu");
        
        Driver driver1 = new Driver("DRV001", "Akua Dede Brown", "0244123456", 28, "Female",
                                    "Toyota Corolla", "GR-4567-20", 5, 6);
        Driver driver2 = new Driver("DRV002", "Princess Cheryl Sam", "0201234567", 32, "Female",
                                    "Honda Civic", "AS-8901-19", 4, 8);
        
        Rider rider1 = new Rider("RDR001", "Akua Manisha", "0559876543", 22, "Female", "Mobile Money");
        Rider rider2 = new Rider("RDR002", "Ann Joseph", "0267890123", 25, "Female", "Cash");
        
        system.registerUser(driver1);
        system.registerUser(driver2);
        system.registerUser(rider1);
        system.registerUser(rider2);
        
        LocalDateTime tomorrow9am = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0);
        LocalDateTime tomorrow2pm = LocalDateTime.now().plusDays(1).withHour(14).withMinute(0);
        
        Trip trip1 = new Trip("TRIP001", driver1, ashesi, accraMan, tomorrow9am);
        Trip trip2 = new Trip("TRIP002", driver2, kotoka, osuOxford, tomorrow2pm);
        
        system.postTrip(trip1);
        system.postTrip(trip2);
        
        System.out.println("\nSample data loaded successfully!\n");
        
        userCounter = 3;
        tripCounter = 3;
    }
    
    // showing menu options for guests who aren't logged in
    private static boolean showGuestMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Register as Driver");
        System.out.println("2. Register as Rider");
        System.out.println("3. Login");
        System.out.println("4. View Available Trips");
        System.out.println("5. View System Impact Report");
        System.out.println("6. Exit");
        System.out.print("\nChoose an option: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                registerDriver();
                break;
            case "2":
                registerRider();
                break;
            case "3":
                login();
                break;
            case "4":
                viewAvailableTrips();
                break;
            case "5":
                System.out.println(system.getSystemImpactReport());
                break;
            case "6":
                return false;
            default:
                System.out.println("Invalid option! Please try again.");
        }
        
        return true;
    }
    
    // showing menu for logged in users based on their type
    private static boolean showUserMenu() {
        System.out.println("\n--- WELCOME, " + currentUser.getName().toUpperCase() + "! ---");
        System.out.println("Logged in as: " + currentUser.getUserType());
        System.out.println("Rating: " + String.format("%.1f", currentUser.getRating()) + "/5.0");
        
        if (currentUser instanceof Driver) {
            showDriverMenu();
        } else {
            showRiderMenu();
        }
        
        return true;
    }
    
    // displaying driver specific menu options
    private static void showDriverMenu() {
        System.out.println("\n--- DRIVER MENU ---");
        System.out.println("1. Post a New Trip");
        System.out.println("2. View My Trips");
        System.out.println("3. View My Stats");
        System.out.println("4. View All Available Trips");
        System.out.println("5. Start a Trip");
        System.out.println("6. Complete a Trip");
        System.out.println("7. Logout");
        System.out.print("\nChoose an option: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                postNewTrip();
                break;
            case "2":
                viewMyTrips();
                break;
            case "3":
                viewMyStats();
                break;
            case "4":
                viewAvailableTrips();
                break;
            case "5":
                startTrip();
                break;
            case "6":
                completeTrip();
                break;
            case "7":
                logout();
                break;
            default:
                System.out.println("Invalid option! Please try again.");
        }
    }
    
    // displaying rider specific menu options
    private static void showRiderMenu() {
        System.out.println("\n--- RIDER MENU ---");
        System.out.println("1. Search for a Ride");
        System.out.println("2. View Available Trips");
        System.out.println("3. Join a Trip");
        System.out.println("4. View My Stats");
        System.out.println("5. Logout");
        System.out.print("\nChoose an option: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                searchForRide();
                break;
            case "2":
                viewAvailableTrips();
                break;
            case "3":
                joinTrip();
                break;
            case "4":
                viewMyStats();
                break;
            case "5":
                logout();
                break;
            default:
                System.out.println("Invalid option! Please try again.");
        }
    }
    
    // collecting driver info and registering them in the system
    private static void registerDriver() {
        System.out.println("\n=== DRIVER REGISTRATION ===");
        
        try {
            System.out.print("Full Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Contact Info (phone/email): ");
            String contact = scanner.nextLine().trim();
            System.out.print("Age: ");
            int age = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Gender: ");
            String gender = scanner.nextLine().trim();
            System.out.print("Car Model: ");
            String carModel = scanner.nextLine().trim();
            System.out.print("License Plate Number: ");
            String plate = scanner.nextLine().trim();
            System.out.print("Car Capacity (including driver): ");
            int capacity = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Years of Driving Experience: ");
            int experience = Integer.parseInt(scanner.nextLine().trim());
            
            String id = "DRV" + String.format("%03d", userCounter++);
            Driver driver = new Driver(id, name, contact, age, gender, 
                                     carModel, plate, capacity, experience);
            
            if (system.registerUser(driver)) {
                System.out.println("\nRegistration successful!");
                System.out.println("Your Driver ID: " + id);
                System.out.println("You can now login with this ID.");
            }
            
        } catch (Exception e) {
            System.out.println("Error during registration: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }
    
    // collecting rider info and registering them in the system
    private static void registerRider() {
        System.out.println("\n=== RIDER REGISTRATION ===");
        
        try {
            System.out.print("Full Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Contact Info (phone/email): ");
            String contact = scanner.nextLine().trim();
            System.out.print("Age: ");
            int age = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Gender: ");
            String gender = scanner.nextLine().trim();
            System.out.print("Preferred Payment Method (Cash/Mobile Money/Card): ");
            String payment = scanner.nextLine().trim();
            
            String id = "RDR" + String.format("%03d", userCounter++);
            Rider rider = new Rider(id, name, contact, age, gender, payment);
            
            if (system.registerUser(rider)) {
                System.out.println("\nRegistration successful!");
                System.out.println("Your Rider ID: " + id);
                System.out.println("You can now login with this ID.");
            }
            
        } catch (Exception e) {
            System.out.println("Error during registration: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }
    
    // logging in a user by their ID
    private static void login() {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Enter your User ID: ");
        String userId = scanner.nextLine().trim();
        
        User user = system.getUserById(userId);
        
        if (user != null) {
            currentUser = user;
            System.out.println("\nLogin successful! Welcome back, " + user.getName() + "!");
        } else {
            System.out.println("\nUser ID not found. Please check and try again.");
        }
    }
    
    // logging out the current user
    private static void logout() {
        System.out.println("\nLogging out... Goodbye, " + currentUser.getName() + "!");
        currentUser = null;
    }
    
    // letting driver create a new trip with all the route details
    private static void postNewTrip() {
        if (!(currentUser instanceof Driver)) {
            System.out.println("Only drivers can post trips!");
            return;
        }
        
        Driver driver = (Driver) currentUser;
        System.out.println("\n=== POST A NEW TRIP ===");
        
        try {
            System.out.println("\nORIGIN LOCATION:");
            System.out.print("  Name: ");
            String originName = scanner.nextLine().trim();
            System.out.print("  Zone/Area: ");
            String originZone = scanner.nextLine().trim();
            
            Location origin = new Location(originName, originZone);
            
            System.out.println("\nDESTINATION LOCATION:");
            System.out.print("  Name: ");
            String destName = scanner.nextLine().trim();
            System.out.print("  Zone/Area: ");
            String destZone = scanner.nextLine().trim();
            
            Location destination = new Location(destName, destZone);
            
            System.out.println("\nDEPARTURE TIME:");
            System.out.print("  Date and Time (format: yyyy-MM-dd HH:mm): ");
            String timeStr = scanner.nextLine().trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime departureTime = LocalDateTime.parse(timeStr, formatter);
            
            String tripId = "TRIP" + String.format("%03d", tripCounter++);
            Trip trip = new Trip(tripId, driver, origin, destination, departureTime);
            system.postTrip(trip);
            
            System.out.println("\nTrip posted successfully!");
            
        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date/time format. Use: yyyy-MM-dd HH:mm");
        } catch (Exception e) {
            System.out.println("Error creating trip: " + e.getMessage());
        }
    }
    
    // showing all trips that are available to join
    private static void viewAvailableTrips() {
        System.out.println("\n=== AVAILABLE TRIPS ===");
        
        ArrayList<Trip> trips = system.getAvailableTrips();
        
        if (trips.isEmpty()) {
            System.out.println("No available trips at the moment. Check back later!");
        } else {
            for (Trip trip : trips) {
                System.out.println(trip);
                System.out.println("-".repeat(60));
            }
        }
    }
    
    // showing all trips posted by current driver
    private static void viewMyTrips() {
        if (!(currentUser instanceof Driver)) return;
        
        System.out.println("\n=== MY TRIPS ===");
        
        Driver driver = (Driver) currentUser;
        boolean found = false;
        
        for (Trip trip : system.getAllTrips()) {
            if (trip.getDriver().getId().equals(driver.getId())) {
                System.out.println(trip);
                System.out.println("-".repeat(60));
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("You haven't posted any trips yet.");
        }
    }
    
    // displaying current user's stats and achievements
    private static void viewMyStats() {
        System.out.println("\n=== MY STATISTICS ===");
        System.out.println(currentUser);
        
        if (currentUser instanceof Driver) {
            Driver driver = (Driver) currentUser;
        } else if (currentUser instanceof Rider) {
            Rider rider = (Rider) currentUser;
            System.out.println("\nYour Savings:");
            System.out.println("  Average per trip: GHS " + 
                             String.format("%.2f", 
                             rider.getTotalDistanceCommuted() > 0 ? 
                             rider.getTotalMoneySaved() / rider.getTotalDistanceCommuted() : 0));
        }
    }
    
    // letting rider search for rides matching their needs
    private static void searchForRide() {
        if (!(currentUser instanceof Rider)) {
            System.out.println("Only riders can search for rides!");
            return;
        }
        
        System.out.println("\n=== SEARCH FOR A RIDE ===");
        
        try {
            System.out.println("\nWHERE ARE YOU STARTING FROM?");
            System.out.print("  Name: ");
            String originName = scanner.nextLine().trim();
            System.out.print("  Zone: ");
            String originZone = scanner.nextLine().trim();
            
            Location origin = new Location(originName, originZone);
            
            System.out.println("\nWHERE DO YOU WANT TO GO?");
            System.out.print("  Name: ");
            String destName = scanner.nextLine().trim();
            System.out.print("  Zone: ");
            String destZone = scanner.nextLine().trim();
            
            Location destination = new Location(destName, destZone);
            
            System.out.print("\nWhen do you want to travel? (yyyy-MM-dd HH:mm): ");
            String timeStr = scanner.nextLine().trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime desiredTime = LocalDateTime.parse(timeStr, formatter);
            
            ArrayList<Trip> matches = system.findMatches(origin, destination, desiredTime);
            
            if (matches.isEmpty()) {
                System.out.println("\nSorry, no matching trips found.");
                System.out.println("Try adjusting your search or check back later!");
            } else {
                System.out.println("\nFound " + matches.size() + " matching trip(s)!");
                System.out.println("\n=== MATCHING TRIPS ===");
                for (Trip trip : matches) {
                    System.out.println(trip);
                    System.out.println("-".repeat(60));
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error during search: " + e.getMessage());
        }
    }
    
    // letting rider join a specific trip by ID
    private static void joinTrip() {
        if (!(currentUser instanceof Rider)) {
            System.out.println("Only riders can join trips!");
            return;
        }
        
        System.out.println("\n=== JOIN A TRIP ===");
        System.out.print("Enter Trip ID: ");
        String tripId = scanner.nextLine().trim();
        
        Trip trip = system.getTripById(tripId);
        
        if (trip == null) {
            System.out.println("Trip not found!");
        } else if (!trip.getTripStatus().equals("Pending")) {
            System.out.println("This trip is not available (Status: " + trip.getTripStatus() + ")");
        } else {
            if (trip.addPassenger(currentUser)) {
                System.out.println("\nSuccessfully joined the trip!");
                System.out.println("Driver: " + trip.getDriver().getName());
                System.out.println("Contact: " + trip.getDriver().getContactInfo());
                System.out.println("Fare: GHS " + String.format("%.2f", trip.calculateFarePerPerson()));
            }
        }
    }
    
    // letting driver start one of their trips
    private static void startTrip() {
        if (!(currentUser instanceof Driver)) return;
        
        System.out.println("\n=== START A TRIP ===");
        System.out.print("Enter Trip ID: ");
        String tripId = scanner.nextLine().trim();
        
        Trip trip = system.getTripById(tripId);
        
        if (trip == null) {
            System.out.println("Trip not found!");
        } else if (!trip.getDriver().getId().equals(currentUser.getId())) {
            System.out.println("You can only start your own trips!");
        } else {
            trip.startTrip();
        }
    }
    
    // letting driver mark their trip as completed
    private static void completeTrip() {
        if (!(currentUser instanceof Driver)) return;
        
        System.out.println("\n=== COMPLETE A TRIP ===");
        System.out.print("Enter Trip ID: ");
        String tripId = scanner.nextLine().trim();
        
        Trip trip = system.getTripById(tripId);
        
        if (trip == null) {
            System.out.println("Trip not found!");
        } else if (!trip.getDriver().getId().equals(currentUser.getId())) {
            System.out.println("You can only complete your own trips!");
        } else {
            trip.completeTrip();
        }
    }
}
