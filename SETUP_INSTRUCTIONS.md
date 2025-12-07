# RideLink - How to Run

## Data Storage

RideLink uses CSV files to store data between sessions:

- `users.csv` - Stores all drivers and riders
- `trips.csv` - Stores all trips

Data is automatically loaded on startup and saved on exit.

## Running in VSCode

### GUI Version (Recommended)

1. Open `RideLinkGUI.java`
2. Click the Run button (triangle icon) at top right
3. A window opens with the graphical interface

### Console Version (Text-based)

1. Open `RideLinkDriver.java`
2. Click the Run button (triangle icon) at top right
3. Program starts in the integrated terminal

## Expected Output

```
RideLink Matcher System initialized! Ready to connect commuters!
Loading sample data...

Welcome to RideLink, Kwame Mensah!
You're registered as a Driver
Welcome to RideLink, Ama Serwaa!
You're registered as a Driver
Welcome to RideLink, Kofi Appiah!
You're registered as a Rider
Welcome to RideLink, Abena Osei!
You're registered as a Rider
Trip posted successfully!
Trip ID: TRIP001
Route: Ashesi University -> Accra Mall
Trip posted successfully!
Trip ID: TRIP002
Route: Kotoka Airport -> Osu Oxford Street

Sample data loaded successfully!

============================================================
                      WELCOME TO
                   RIDELINK by Group 8
              Smart Carpooling for Everyone!
============================================================
    Save money. Save the environment. Share the journey.
============================================================

--- MAIN MENU ---
1. Register as Driver
2. Register as Rider
3. Login
4. View Available Trips
5. View System Impact Report
6. Exit

Choose an option:
```

## Sample Login Credentials

Driver IDs: `DRV001`, `DRV002`
Rider IDs: `RDR001`, `RDR002`

## Sample Locations (for testing)

When creating trips, use:

- Name: Ashesi University, Zone: Berekuso
- Name: Accra Mall, Zone: Tetteh Quarshie
- Name: Kotoka Airport, Zone: Airport
- Name: Osu Oxford Street, Zone: Osu

## Date Format

`yyyy-MM-dd HH:mm` (Example: `2025-12-08 09:00`)

## Project Files

**Java Classes:**

- Location.java
- User.java
- Driver.java
- Rider.java
- Trip.java
- RideLinkMatcher.java
- RideLinkDriver.java (main file to run)

**Data Files (CSV):**

- users.csv - User data storage
- trips.csv - Trip data storage
