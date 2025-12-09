import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;


public class RideLinkGUI extends JFrame {
    
    // the backend system that handles all the matching and data
    private RideLinkMatcher system;
    
    // keeps track of who is currently logged in
    private User currentUser;
    
    
    // color theme for the app - gradient blue colors
    private final Color LIGHT_BLUE = new Color(173, 216, 230);
    private final Color MID_BLUE = new Color(100, 149, 237);
    private final Color NAVY_BLUE = new Color(25, 25, 112);
    private final Color DARK_NAVY = new Color(15, 15, 70);
    private final Color WHITE = Color.WHITE;
    private final Color LIGHT_GRAY = new Color(240, 240, 240);
    
    
    
    // constructor - this runs when we create the GUI
    public RideLinkGUI() {
        
        // force cross-platform look and feel so button colors work on Mac
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
            // if it fails just use default
        }
        
        // create the backend system
        system = new RideLinkMatcher();
        
        // try to load existing users from file
        system.loadUsersFromCSV();
        
        // try to load existing trips from file
        system.loadTripsFromCSV();
        
        
        // if there are no users in the system, create some sample ones
        if (system.getAllUsers().isEmpty()) {
            setupSampleData();
        }
        
        // set the window title
        setTitle("RideLink - Smart Carpooling System");
        
        // set window size
        setSize(650, 850);
        
        
        // make sure app closes when window is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // center the window on screen
        setLocationRelativeTo(null);
        
        
        // show the login screen first
        showLoginScreen();
        
        // make the window visible
        setVisible(true);
    }
    
    
    
    // creates a panel with a simple blue background
    private JPanel createSimplePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(LIGHT_BLUE);
        return panel;
    }
    
    
    
    // adds placeholder functionality to a text field
    private void addPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);
        
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().trim().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
        
        // store the placeholder text as a client property for later retrieval
        textField.putClientProperty("placeholder", placeholder);
    }
    
    
    
    // gets the actual text from a field with placeholder (returns empty string if still showing placeholder)
    private String getFieldText(JTextField textField) {
        String text = textField.getText().trim();
        String placeholder = (String) textField.getClientProperty("placeholder");
        
        // if text is gray or equals placeholder, return empty string
        if (textField.getForeground().equals(Color.GRAY) || text.equals(placeholder)) {
            return "";
        }
        
        return text;
    }
    
    
    
    // shows the login screen where users enter their ID
    private void showLoginScreen() {
        
        // create main panel with blue background
        JPanel mainPanel = createSimplePanel();
        mainPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        
        // create white card panel for the login form
        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(WHITE);
        cardPanel.setLayout(new GridBagLayout());
        
        // set card size
        cardPanel.setPreferredSize(new Dimension(450, 700));
        
        // add padding around the edges
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        
        
        // create the big title label
        JLabel titleLabel = new JLabel("RIDELINK");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 42));
        titleLabel.setForeground(NAVY_BLUE);
        
        
        // create subtitle
        JLabel subtitleLabel = new JLabel("Smart Carpooling for Everyone");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(MID_BLUE);
        
        
        // create group label
        JLabel groupLabel = new JLabel("by Group 8");
        groupLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        groupLabel.setForeground(MID_BLUE);
        
        
        
        // create label for user ID field
        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setFont(new Font("Arial", Font.BOLD, 13));
        userIdLabel.setForeground(DARK_NAVY);
        
        
        // create text field for user to enter their ID
        JTextField userIdField = new JTextField();
        
        // set size of text field
        userIdField.setPreferredSize(new Dimension(300, 40));
        
        // set font
        userIdField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // create border for text field
        userIdField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MID_BLUE, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        
        
        // create LOGIN button
        JButton loginButton = new JButton("LOGIN");
        
        // set button size
        loginButton.setPreferredSize(new Dimension(320, 45));
        loginButton.setMinimumSize(new Dimension(320, 45));
        loginButton.setMaximumSize(new Dimension(320, 45));
        
        // set button colors
        loginButton.setBackground(MID_BLUE);
        loginButton.setForeground(Color.WHITE);
        
        // set button font
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        
        // remove focus border
        loginButton.setFocusPainted(false);
        
        // show border
        loginButton.setBorderPainted(true);
        loginButton.setBorder(BorderFactory.createLineBorder(MID_BLUE.darker(), 2));
        
        // change cursor to hand
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // make sure color shows
        loginButton.setOpaque(true);
        loginButton.setContentAreaFilled(true);
        
        
        
        // create REGISTER AS DRIVER button
        JButton registerDriverButton = new JButton("REGISTER AS DRIVER");
        
        // set size
        registerDriverButton.setPreferredSize(new Dimension(320, 45));
        registerDriverButton.setMinimumSize(new Dimension(320, 45));
        registerDriverButton.setMaximumSize(new Dimension(320, 45));
        
        // set colors
        registerDriverButton.setBackground(NAVY_BLUE);
        registerDriverButton.setForeground(Color.WHITE);
        
        // set font
        registerDriverButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        
        registerDriverButton.setFocusPainted(false);
        registerDriverButton.setBorderPainted(true);
        registerDriverButton.setBorder(BorderFactory.createLineBorder(NAVY_BLUE.darker(), 2));
        registerDriverButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerDriverButton.setOpaque(true);
        registerDriverButton.setContentAreaFilled(true);
        
        
        
        // create REGISTER AS RIDER button
        JButton registerRiderButton = new JButton("REGISTER AS RIDER");
        
        registerRiderButton.setPreferredSize(new Dimension(320, 45));
        registerRiderButton.setMinimumSize(new Dimension(320, 45));
        registerRiderButton.setMaximumSize(new Dimension(320, 45));
        
        registerRiderButton.setBackground(DARK_NAVY);
        registerRiderButton.setForeground(Color.WHITE);
        
        registerRiderButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        registerRiderButton.setFocusPainted(false);
        registerRiderButton.setBorderPainted(true);
        registerRiderButton.setBorder(BorderFactory.createLineBorder(DARK_NAVY.darker(), 2));
        
        registerRiderButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerRiderButton.setOpaque(true);
        registerRiderButton.setContentAreaFilled(true);
        
        
        
        // create quick login section label
        JLabel quickLabel = new JLabel("Quick Login (Sample Users):");
        quickLabel.setFont(new Font("Arial", Font.BOLD, 11));
        quickLabel.setForeground(MID_BLUE);
        
        
        
        // create quick login button for driver
        JButton driver1Button = new JButton();
        driver1Button.setText("Driver: DRV001");
        
        driver1Button.setPreferredSize(new Dimension(320, 45));
        driver1Button.setMinimumSize(new Dimension(320, 45));
        driver1Button.setMaximumSize(new Dimension(320, 45));
        
        driver1Button.setBackground(new Color(70, 130, 180));
        driver1Button.setForeground(Color.WHITE);
        driver1Button.setFont(new Font("SansSerif", Font.BOLD, 13));
        
        driver1Button.setFocusPainted(false);
        driver1Button.setBorderPainted(true);
        driver1Button.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180).darker(), 2));
        driver1Button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        driver1Button.setOpaque(true);
        driver1Button.setContentAreaFilled(true);
        
        
        
        // create quick login button for rider
        JButton rider1Button = new JButton();
        rider1Button.setText("Rider: RDR001");
        
        rider1Button.setPreferredSize(new Dimension(320, 45));
        rider1Button.setMinimumSize(new Dimension(320, 45));
        rider1Button.setMaximumSize(new Dimension(320, 45));
        
        rider1Button.setBackground(new Color(100, 149, 237));
        rider1Button.setForeground(Color.WHITE);
        rider1Button.setFont(new Font("SansSerif", Font.BOLD, 13));
        rider1Button.setFocusPainted(false);
        rider1Button.setBorderPainted(true);
        rider1Button.setBorder(BorderFactory.createLineBorder(new Color(100, 149, 237).darker(), 2));
        rider1Button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rider1Button.setOpaque(true);
        rider1Button.setContentAreaFilled(true);
        
        
        
        // add action to login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                // get the user ID from text field
                String userId = userIdField.getText().trim();
                
                // search for user in system
                User user = system.getUserById(userId);
                
                // check if we found the user
                if (user != null) {
                    
                    // save who logged in
                    currentUser = user;
                    
                    // check what type of user it is
                    if (user instanceof Driver) {
                        // show driver dashboard
                        showDriverDashboard();
                        
                    } else {
                        // show rider dashboard
                        showRiderDashboard();
                    }
                    
                } else {
                    // user not found - show error message
                    JOptionPane.showMessageDialog(
                        RideLinkGUI.this, 
                        "User ID not found!", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        
        
        
        // add action to driver quick login button
        driver1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                // get the sample driver user
                currentUser = system.getUserById("DRV001");
                
                if (currentUser != null) {
                    // show driver dashboard
                    showDriverDashboard();
                }
            }
        });
        
        
        
        // add action to rider quick login button
        rider1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                // get the sample rider user
                currentUser = system.getUserById("RDR001");
                
                if (currentUser != null) {
                    // show rider dashboard
                    showRiderDashboard();
                }
            }
        });
        
        
        
        // add action to register driver button
        registerDriverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // show registration dialog for drivers
                showRegisterDialog(true);
            }
        });
        
        
        // add action to register rider button
        registerRiderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // show registration dialog for riders
                showRegisterDialog(false);
            }
        });
        
        
        
        // now add all components to the card panel using GridBagLayout
        
        // reset constraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        
        // add title
        cardPanel.add(titleLabel, gbc);
        
        
        // add subtitle
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 3, 0);
        cardPanel.add(subtitleLabel, gbc);
        
        
        // add group label
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        cardPanel.add(groupLabel, gbc);
        
        
        // add user ID label
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        cardPanel.add(userIdLabel, gbc);
        
        
        // add user ID text field
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 15, 0);
        cardPanel.add(userIdField, gbc);
        
        
        // add login button
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 10, 0);
        cardPanel.add(loginButton, gbc);
        
        
        // add register driver button
        gbc.gridy = 6;
        gbc.insets = new Insets(5, 0, 10, 0);
        cardPanel.add(registerDriverButton, gbc);
        
        
        // add register rider button
        gbc.gridy = 7;
        gbc.insets = new Insets(5, 0, 20, 0);
        cardPanel.add(registerRiderButton, gbc);
        
        
        // add quick login label
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        cardPanel.add(quickLabel, gbc);
        
        
        // add driver quick login button
        gbc.gridy = 9;
        gbc.insets = new Insets(0, 0, 10, 0);
        cardPanel.add(driver1Button, gbc);
        
        
        // add rider quick login button
        gbc.gridy = 10;
        gbc.insets = new Insets(0, 0, 20, 0);
        cardPanel.add(rider1Button, gbc);
        
        
        
        // add card panel to main panel
        mainPanel.add(cardPanel);
        
        // set this as the content of the window
        setContentPane(mainPanel);
        
        // refresh the window
        revalidate();
        repaint();
    }
    
    
    
    // shows the dashboard for drivers
    private void showDriverDashboard() {
        
        // cast current user to Driver type
        Driver driver = (Driver) currentUser;
        
        // create main panel
        JPanel mainPanel = createSimplePanel();
        mainPanel.setLayout(new BorderLayout());
        
        
        // create header panel at the top
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_NAVY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        headerPanel.setLayout(new GridLayout(3, 1, 0, 5));
        
        
        // create welcome label
        JLabel welcomeLabel = new JLabel("Welcome, " + driver.getName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(WHITE);
        
        
        // create type label
        JLabel typeLabel = new JLabel("Driver Dashboard | " + driver.getCarModel());
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        typeLabel.setForeground(LIGHT_BLUE);
        
        
        // create stats label
        String statsText = String.format("Experience: %d years", 
            driver.getYearsExperience());
        JLabel statsLabel = new JLabel(statsText);
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statsLabel.setForeground(LIGHT_BLUE);
        
        
        // add labels to header
        headerPanel.add(welcomeLabel);
        headerPanel.add(typeLabel);
        headerPanel.add(statsLabel);
        
        
        
        // create menu panel in center
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(WHITE);
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        
        
        
        // create POST NEW TRIP button
        JButton postTripButton = new JButton("POST NEW TRIP");
        
        postTripButton.setPreferredSize(new Dimension(320, 45));
        postTripButton.setMinimumSize(new Dimension(320, 45));
        postTripButton.setMaximumSize(new Dimension(320, 45));
        
        postTripButton.setBackground(MID_BLUE);
        postTripButton.setForeground(Color.WHITE);
        postTripButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        postTripButton.setFocusPainted(false);
        postTripButton.setBorderPainted(true);
        postTripButton.setBorder(BorderFactory.createLineBorder(MID_BLUE.darker(), 2));
        postTripButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        postTripButton.setOpaque(true);
        
        
        
        // create VIEW MY TRIPS button
        JButton viewTripsButton = new JButton("VIEW MY TRIPS");
        
        viewTripsButton.setPreferredSize(new Dimension(320, 45));
        viewTripsButton.setMinimumSize(new Dimension(320, 45));
        viewTripsButton.setMaximumSize(new Dimension(320, 45));
        
        viewTripsButton.setBackground(MID_BLUE);
        viewTripsButton.setForeground(Color.WHITE);
        viewTripsButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        viewTripsButton.setFocusPainted(false);
        viewTripsButton.setBorderPainted(true);
        viewTripsButton.setBorder(BorderFactory.createLineBorder(MID_BLUE.darker(), 2));
        viewTripsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewTripsButton.setOpaque(true);
        
        
        
        // create MY STATISTICS button
        JButton statsButton = new JButton("MY STATISTICS");
        
        statsButton.setPreferredSize(new Dimension(320, 45));
        statsButton.setMinimumSize(new Dimension(320, 45));
        statsButton.setMaximumSize(new Dimension(320, 45));
        
        statsButton.setBackground(NAVY_BLUE);
        statsButton.setForeground(Color.WHITE);
        statsButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        statsButton.setFocusPainted(false);
        statsButton.setBorderPainted(true);
        statsButton.setBorder(BorderFactory.createLineBorder(NAVY_BLUE.darker(), 2));
        statsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        statsButton.setOpaque(true);
        
        
        
        // create LOGOUT button
        JButton logoutButton = new JButton("LOGOUT");
        
        logoutButton.setPreferredSize(new Dimension(320, 45));
        logoutButton.setMinimumSize(new Dimension(320, 45));
        logoutButton.setMaximumSize(new Dimension(320, 45));
        
        logoutButton.setBackground(DARK_NAVY);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(true);
        logoutButton.setBorder(BorderFactory.createLineBorder(DARK_NAVY.darker(), 2));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setOpaque(true);
        
        
        
        // add action to post trip button
        postTripButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPostTripDialog();
            }
        });
        
        
        // add action to view trips button
        viewTripsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMyTrips();
            }
        });
        
        
        // add action to stats button
        statsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStats();
            }
        });
        
        
        // add action to logout button
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // save data before logging out
                saveData();
                // go back to login screen
                showLoginScreen();
            }
        });
        
        
        
        // add buttons to menu panel
        gbc.gridy = 0;
        menuPanel.add(postTripButton, gbc);
        
        gbc.gridy = 1;
        menuPanel.add(viewTripsButton, gbc);
        
        gbc.gridy = 2;
        menuPanel.add(statsButton, gbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(20, 0, 0, 0);
        menuPanel.add(logoutButton, gbc);
        
        
        
        // add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        
        // set as window content
        setContentPane(mainPanel);
        revalidate();
        repaint();
    }
    
    
    
    // shows the dashboard for riders  
    private void showRiderDashboard() {
        
        // cast current user to Rider type
        Rider rider = (Rider) currentUser;
        
        // create main panel
        JPanel mainPanel = createSimplePanel();
        mainPanel.setLayout(new BorderLayout());
        
        
        // create header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_NAVY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        headerPanel.setLayout(new GridLayout(3, 1, 0, 5));
        
        
        // welcome label
        JLabel welcomeLabel = new JLabel("Welcome, " + rider.getName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(WHITE);
        
        
        // type label
        JLabel typeLabel = new JLabel("Rider Dashboard | " + rider.getPreferredPaymentMethod());
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        typeLabel.setForeground(LIGHT_BLUE);
        
        
        // stats label
        String statsText = String.format("Money Saved: GHS %.2f", 
            rider.getTotalMoneySaved());
        JLabel statsLabel = new JLabel(statsText);
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statsLabel.setForeground(LIGHT_BLUE);
        
        
        // add to header
        headerPanel.add(welcomeLabel);
        headerPanel.add(typeLabel);
        headerPanel.add(statsLabel);
        
        
        
        // create menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(WHITE);
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        
        
        
        // create SEARCH FOR RIDES button
        JButton searchButton = new JButton("SEARCH FOR RIDES");
        
        searchButton.setPreferredSize(new Dimension(320, 45));
        searchButton.setMinimumSize(new Dimension(320, 45));
        searchButton.setMaximumSize(new Dimension(320, 45));
        searchButton.setBackground(MID_BLUE);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        searchButton.setFocusPainted(false);
        searchButton.setBorderPainted(true);
        searchButton.setBorder(BorderFactory.createLineBorder(MID_BLUE.darker(), 2));
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.setOpaque(true);
        
        
        
        // create VIEW AVAILABLE TRIPS button
        JButton viewTripsButton = new JButton("VIEW AVAILABLE TRIPS");
        
        viewTripsButton.setPreferredSize(new Dimension(320, 45));
        viewTripsButton.setMinimumSize(new Dimension(320, 45));
        viewTripsButton.setMaximumSize(new Dimension(320, 45));
        viewTripsButton.setBackground(MID_BLUE);
        viewTripsButton.setForeground(Color.WHITE);
        viewTripsButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        viewTripsButton.setFocusPainted(false);
        viewTripsButton.setBorderPainted(true);
        viewTripsButton.setBorder(BorderFactory.createLineBorder(MID_BLUE.darker(), 2));
        viewTripsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewTripsButton.setOpaque(true);
        
        
        
        // create MY STATISTICS button
        JButton statsButton = new JButton("MY STATISTICS");
        
        statsButton.setPreferredSize(new Dimension(320, 45));
        statsButton.setMinimumSize(new Dimension(320, 45));
        statsButton.setMaximumSize(new Dimension(320, 45));
        statsButton.setBackground(NAVY_BLUE);
        statsButton.setForeground(Color.WHITE);
        statsButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        statsButton.setFocusPainted(false);
        statsButton.setBorderPainted(true);
        statsButton.setBorder(BorderFactory.createLineBorder(NAVY_BLUE.darker(), 2));
        statsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        statsButton.setOpaque(true);
        
        
        
        // create LOGOUT button
        JButton logoutButton = new JButton("LOGOUT");
        
        logoutButton.setPreferredSize(new Dimension(320, 45));
        logoutButton.setMinimumSize(new Dimension(320, 45));
        logoutButton.setMaximumSize(new Dimension(320, 45));
        logoutButton.setBackground(DARK_NAVY);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(true);
        logoutButton.setBorder(BorderFactory.createLineBorder(DARK_NAVY.darker(), 2));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setOpaque(true);
        
        
        
        // add actions to buttons
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSearchDialog();
            }
        });
        
        
        viewTripsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAvailableTrips();
            }
        });
        
        
        statsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStats();
            }
        });
        
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveData();
                showLoginScreen();
            }
        });
        
        
        
        // add buttons to menu
        gbc.gridy = 0;
        menuPanel.add(searchButton, gbc);
        
        gbc.gridy = 1;
        menuPanel.add(viewTripsButton, gbc);
        
        gbc.gridy = 2;
        menuPanel.add(statsButton, gbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(20, 0, 0, 0);
        menuPanel.add(logoutButton, gbc);
        
        
        
        // add to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        revalidate();
        repaint();
    }
    
    
    
    // show the post trip dialog for drivers
    private void showPostTripDialog() {
        
        // create dialog window
        JDialog dialog = new JDialog(this, "Post New Trip", true);
        dialog.setSize(450, 600);
        dialog.setLocationRelativeTo(this);
        
        // create panel for dialog
        JPanel panel = new JPanel();
        panel.setBackground(WHITE);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 0);
        
        
        // title label
        JLabel titleLabel = new JLabel("Post a New Trip");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(NAVY_BLUE);
        
        
        
        // create text fields for trip info
        
        // origin name field
        JTextField originNameField = new JTextField();
        originNameField.setPreferredSize(new Dimension(300, 40));
        originNameField.setFont(new Font("Arial", Font.PLAIN, 13));
        originNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MID_BLUE, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        addPlaceholder(originNameField, "Origin Name (e.g., Ashesi University)");
        
        
        // origin area field
        JTextField originAreaField = new JTextField();
        originAreaField.setPreferredSize(new Dimension(300, 40));
        originAreaField.setFont(new Font("Arial", Font.PLAIN, 13));
        originAreaField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MID_BLUE, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        addPlaceholder(originAreaField, "Origin Area (e.g., Berekuso)");
        
        
        // destination name field
        JTextField destNameField = new JTextField();
        destNameField.setPreferredSize(new Dimension(300, 40));
        destNameField.setFont(new Font("Arial", Font.PLAIN, 13));
        destNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MID_BLUE, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        addPlaceholder(destNameField, "Destination Name (e.g., Accra Mall)");
        
        
        // destination area field
        JTextField destAreaField = new JTextField();
        destAreaField.setPreferredSize(new Dimension(300, 40));
        destAreaField.setFont(new Font("Arial", Font.PLAIN, 13));
        destAreaField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MID_BLUE, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        addPlaceholder(destAreaField, "Destination Area (e.g., Tetteh Quarshie)");
        
        
        // time field
        JTextField timeField = new JTextField();
        timeField.setPreferredSize(new Dimension(300, 40));
        timeField.setFont(new Font("Arial", Font.PLAIN, 13));
        timeField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MID_BLUE, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        addPlaceholder(timeField, "Departure Time (HH:mm, e.g., 09:00)");
        
        
        
        // create POST TRIP button
        JButton submitButton = new JButton("POST TRIP");
        
        submitButton.setPreferredSize(new Dimension(320, 45));
        submitButton.setMinimumSize(new Dimension(320, 45));
        submitButton.setMaximumSize(new Dimension(320, 45));
        submitButton.setBackground(MID_BLUE);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        submitButton.setFocusPainted(false);
        submitButton.setBorderPainted(true);
        submitButton.setBorder(BorderFactory.createLineBorder(MID_BLUE.darker(), 2));
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.setOpaque(true);
        
        
        
        // create CANCEL button
        JButton cancelButton = new JButton("CANCEL");
        
        cancelButton.setPreferredSize(new Dimension(320, 45));
        cancelButton.setMinimumSize(new Dimension(320, 45));
        cancelButton.setMaximumSize(new Dimension(320, 45));
        cancelButton.setBackground(new Color(70, 130, 180));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(true);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setOpaque(true);
        
        
        
        // add action to submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                try {
                    // get field values
                    String originName = getFieldText(originNameField);
                    String originArea = getFieldText(originAreaField);
                    String destName = getFieldText(destNameField);
                    String destArea = getFieldText(destAreaField);
                    String timeStr = getFieldText(timeField);
                    
                    // validate all fields are filled
                    if (originName.isEmpty() || originArea.isEmpty() || 
                        destName.isEmpty() || destArea.isEmpty() || timeStr.isEmpty()) {
                        
                        JOptionPane.showMessageDialog(
                            dialog, 
                            "Please fill in all fields!", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                    
                    // create origin location
                    Location origin = new Location(originName, originArea);
                    
                    // create destination location
                    Location destination = new Location(destName, destArea);
                    
                    
                    // parse the time
                    String[] timeParts = timeStr.split(":");
                    
                    // create departure time for tomorrow
                    LocalDateTime departureTime = LocalDateTime.now()
                        .plusDays(1)
                        .withHour(Integer.parseInt(timeParts[0]))
                        .withMinute(Integer.parseInt(timeParts[1]))
                        .withSecond(0);
                    
                    
                    // generate trip ID
                    String tripId = "TRIP" + String.format("%03d", system.getAllTrips().size() + 1);
                    
                    // create new trip
                    Trip trip = new Trip(
                        tripId, 
                        (Driver) currentUser, 
                        origin, 
                        destination, 
                        departureTime
                    );
                    
                    // add trip to system
                    system.postTrip(trip);
                    
                    
                    // show success message
                    String successMessage = "Trip posted successfully!\nTrip ID: " + tripId + 
                        "\nDeparture: " + departureTime.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm"));
                    
                    JOptionPane.showMessageDialog(
                        dialog, 
                        successMessage, 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    
                    // close dialog
                    dialog.dispose();
                    
                    
                } catch (Exception ex) {
                    // show error message
                    JOptionPane.showMessageDialog(
                        dialog, 
                        "Error: Please enter time in HH:mm format (e.g., 09:00)", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        
        
        
        // add action to cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        
        
        // create section labels
        JLabel originSectionLabel = new JLabel("Origin Information");
        originSectionLabel.setFont(new Font("Arial", Font.BOLD, 12));
        originSectionLabel.setForeground(NAVY_BLUE);
        
        JLabel destSectionLabel = new JLabel("Destination Information");
        destSectionLabel.setFont(new Font("Arial", Font.BOLD, 12));
        destSectionLabel.setForeground(NAVY_BLUE);
        
        JLabel timeSectionLabel = new JLabel("Departure Time");
        timeSectionLabel.setFont(new Font("Arial", Font.BOLD, 12));
        timeSectionLabel.setForeground(NAVY_BLUE);
        
        
        
        // add components to panel
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(titleLabel, gbc);
        
        
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.gridy = 1;
        panel.add(originSectionLabel, gbc);
        
        gbc.gridy = 2;
        panel.add(originNameField, gbc);
        
        gbc.gridy = 3;
        panel.add(originAreaField, gbc);
        
        
        gbc.gridy = 4;
        gbc.insets = new Insets(15, 0, 10, 0);
        panel.add(destSectionLabel, gbc);
        
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.gridy = 5;
        panel.add(destNameField, gbc);
        
        gbc.gridy = 6;
        panel.add(destAreaField, gbc);
        
        
        gbc.gridy = 7;
        gbc.insets = new Insets(15, 0, 10, 0);
        panel.add(timeSectionLabel, gbc);
        
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.gridy = 8;
        panel.add(timeField, gbc);
        
        
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.gridy = 9;
        panel.add(submitButton, gbc);
        
        gbc.gridy = 10;
        panel.add(cancelButton, gbc);
        
        
        // add panel to dialog and show
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    
    
    // show search dialog for riders
    private void showSearchDialog() {
        
        // create dialog
        JDialog dialog = new JDialog(this, "Search for Rides", true);
        dialog.setSize(450, 500);
        dialog.setLocationRelativeTo(this);
        
        // create panel
        JPanel panel = new JPanel();
        panel.setBackground(WHITE);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        
        // title
        JLabel titleLabel = new JLabel("Search for Rides");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(NAVY_BLUE);
        
        
        
        // create text fields
        JTextField originNameField = new JTextField();
        originNameField.setPreferredSize(new Dimension(300, 40));
        originNameField.setFont(new Font("Arial", Font.PLAIN, 13));
        originNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MID_BLUE, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        addPlaceholder(originNameField, "Origin Name");
        
        
        JTextField originAreaField = new JTextField();
        originAreaField.setPreferredSize(new Dimension(300, 40));
        originAreaField.setFont(new Font("Arial", Font.PLAIN, 13));
        originAreaField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MID_BLUE, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        addPlaceholder(originAreaField, "Origin Area");
        
        
        JTextField destNameField = new JTextField();
        destNameField.setPreferredSize(new Dimension(300, 40));
        destNameField.setFont(new Font("Arial", Font.PLAIN, 13));
        destNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MID_BLUE, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        addPlaceholder(destNameField, "Destination Name");
        
        
        JTextField destAreaField = new JTextField();
        destAreaField.setPreferredSize(new Dimension(300, 40));
        destAreaField.setFont(new Font("Arial", Font.PLAIN, 13));
        destAreaField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MID_BLUE, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        addPlaceholder(destAreaField, "Destination Area");
        
        
        JTextField timeField = new JTextField();
        timeField.setPreferredSize(new Dimension(300, 40));
        timeField.setFont(new Font("Arial", Font.PLAIN, 13));
        timeField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MID_BLUE, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        addPlaceholder(timeField, "Desired Time (HH:mm, e.g., 09:00)");
        
        
        
        // create buttons
        JButton searchButton = new JButton("SEARCH");
        
        searchButton.setPreferredSize(new Dimension(320, 45));
        searchButton.setMinimumSize(new Dimension(320, 45));
        searchButton.setMaximumSize(new Dimension(320, 45));
        searchButton.setBackground(MID_BLUE);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        searchButton.setFocusPainted(false);
        searchButton.setBorderPainted(true);
        searchButton.setBorder(BorderFactory.createLineBorder(MID_BLUE.darker(), 2));
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.setOpaque(true);
        
        
        JButton cancelButton = new JButton("CANCEL");
        
        cancelButton.setPreferredSize(new Dimension(320, 45));
        cancelButton.setMinimumSize(new Dimension(320, 45));
        cancelButton.setMaximumSize(new Dimension(320, 45));
        cancelButton.setBackground(new Color(70, 130, 180));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(true);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setOpaque(true);
        
        
        
        // add action to search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                try {
                    // get field values
                    String originName = getFieldText(originNameField);
                    String originArea = getFieldText(originAreaField);
                    String destName = getFieldText(destNameField);
                    String destArea = getFieldText(destAreaField);
                    String timeStr = getFieldText(timeField);
                    
                    // validate all fields are filled
                    if (originName.isEmpty() || originArea.isEmpty() || 
                        destName.isEmpty() || destArea.isEmpty() || timeStr.isEmpty()) {
                        
                        JOptionPane.showMessageDialog(
                            dialog, 
                            "Please fill in all fields!", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                    
                    // create locations
                    Location origin = new Location(originName, originArea);
                    
                    Location destination = new Location(destName, destArea);
                    
                    
                    // parse time
                    String[] timeParts = timeStr.split(":");
                    
                    LocalDateTime desiredTime = LocalDateTime.now()
                        .withHour(Integer.parseInt(timeParts[0]))
                        .withMinute(Integer.parseInt(timeParts[1]))
                        .withSecond(0);
                    
                    
                    // search for matches
                    ArrayList<Trip> matches = system.findMatches(origin, destination, desiredTime);
                    
                    // close dialog
                    dialog.dispose();
                    
                    // show results
                    showSearchResults(matches);
                    
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        dialog, 
                        "Error: Please enter time in HH:mm format (e.g., 09:00)", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        
        
        // add components
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(titleLabel, gbc);
        
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.gridy = 1;
        panel.add(originNameField, gbc);
        
        gbc.gridy = 2;
        panel.add(originAreaField, gbc);
        
        gbc.gridy = 3;
        panel.add(destNameField, gbc);
        
        gbc.gridy = 4;
        panel.add(destAreaField, gbc);
        
        gbc.gridy = 5;
        panel.add(timeField, gbc);
        
        
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(searchButton, gbc);
        
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(cancelButton, gbc);
        
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    
    
    // show search results
    private void showSearchResults(ArrayList<Trip> trips) {
        
        JDialog dialog = new JDialog(this, "Search Results", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        
        
        // title
        JLabel titleLabel = new JLabel("Found " + trips.size() + " matching trip(s)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(NAVY_BLUE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        
        if (trips.isEmpty()) {
            
            // no trips found
            JLabel noTripsLabel = new JLabel("No matching trips found. Try adjusting your search.");
            noTripsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            noTripsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            panel.add(noTripsLabel, BorderLayout.CENTER);
            
            
        } else {
            
            // create list model
            DefaultListModel<String> listModel = new DefaultListModel<String>();
            
            for (Trip trip : trips) {
                String tripInfo = String.format("%s: %s -> %s | Driver: %s", 
                    trip.getId(), 
                    trip.getOrigin().getName(), 
                    trip.getDestination().getName(),
                    trip.getDriver().getName()
                );
                
                listModel.addElement(tripInfo);
            }
            
            
            // create list
            JList<String> tripList = new JList<String>(listModel);
            tripList.setFont(new Font("Arial", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(tripList);
            panel.add(scrollPane, BorderLayout.CENTER);
            
            
            
            // create join button
            JButton joinButton = new JButton("JOIN SELECTED TRIP");
            
            joinButton.setPreferredSize(new Dimension(320, 45));
            joinButton.setBackground(MID_BLUE);
            joinButton.setForeground(Color.WHITE);
            joinButton.setFont(new Font("SansSerif", Font.BOLD, 13));
            joinButton.setFocusPainted(false);
            joinButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            
            joinButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    // get selected index
                    int selected = tripList.getSelectedIndex();
                    
                    if (selected >= 0) {
                        
                        // get the trip
                        Trip trip = trips.get(selected);
                        
                        // try to add passenger
                        if (trip.addPassenger(currentUser)) {
                            
                            JOptionPane.showMessageDialog(
                                dialog, 
                                "Successfully joined trip " + trip.getId() + "!", 
                                "Success", 
                                JOptionPane.INFORMATION_MESSAGE
                            );
                            
                            dialog.dispose();
                            
                        } else {
                            
                            JOptionPane.showMessageDialog(
                                dialog, 
                                "Could not join trip.", 
                                "Error", 
                                JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
            });
            
            
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(WHITE);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            buttonPanel.add(joinButton);
            
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }
        
        
        panel.add(titleLabel, BorderLayout.NORTH);
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    
    
    // show driver's trips
    private void showMyTrips() {
        
        Driver driver = (Driver) currentUser;
        
        ArrayList<Trip> myTrips = new ArrayList<Trip>();
        
        
        // find all trips for this driver
        for (Trip trip : system.getAllTrips()) {
            if (trip.getDriver().getId().equals(driver.getId())) {
                myTrips.add(trip);
            }
        }
        
        
        JDialog dialog = new JDialog(this, "My Trips", true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        
        
        JLabel titleLabel = new JLabel("My Trips (" + myTrips.size() + ") - Select a trip to complete");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(NAVY_BLUE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        
        if (myTrips.isEmpty()) {
            
            JLabel noTripsLabel = new JLabel("You haven't posted any trips yet.");
            noTripsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            noTripsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            panel.add(noTripsLabel, BorderLayout.CENTER);
            
            
        } else {
            
            // create list model
            DefaultListModel<String> listModel = new DefaultListModel<String>();
            
            for (Trip trip : myTrips) {
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String timeStr = trip.getDepartureTime().format(formatter);
                
                String tripInfo = String.format(
                    "<html><b>%s</b> [%s] - %s → %s<br>" +
                    "Time: %s | Passengers: %d | Fare: GHS %.2f per person</html>",
                    trip.getId(),
                    trip.getStatus(),
                    trip.getOrigin().getName(),
                    trip.getDestination().getName(),
                    timeStr,
                    trip.getPassengerCount(),
                    trip.calculateFarePerPerson()
                );
                
                listModel.addElement(tripInfo);
            }
            
            
            JList<String> tripList = new JList<String>(listModel);
            tripList.setFont(new Font("Arial", Font.PLAIN, 13));
            tripList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tripList.setFixedCellHeight(60);
            
            JScrollPane scrollPane = new JScrollPane(tripList);
            panel.add(scrollPane, BorderLayout.CENTER);
            
            
            
            // complete trip button
            JButton completeButton = new JButton("COMPLETE SELECTED TRIP");
            
            completeButton.setPreferredSize(new Dimension(320, 45));
            completeButton.setBackground(NAVY_BLUE);
            completeButton.setForeground(Color.WHITE);
            completeButton.setFont(new Font("SansSerif", Font.BOLD, 13));
            completeButton.setFocusPainted(false);
            completeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            
            completeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    int selected = tripList.getSelectedIndex();
                    
                    if (selected >= 0) {
                        
                        Trip trip = myTrips.get(selected);
                        
                        if (trip.getStatus().equals("Completed")) {
                            
                            JOptionPane.showMessageDialog(
                                dialog, 
                                "This trip is already completed!", 
                                "Already Completed", 
                                JOptionPane.INFORMATION_MESSAGE
                            );
                            
                        } else {
                            
                            trip.setStatus("Completed");
                            
                            String message = String.format(
                                "Trip %s marked as COMPLETED!\n%d passengers traveled with you.", 
                                trip.getId(), 
                                trip.getPassengerCount()
                            );
                            
                            JOptionPane.showMessageDialog(
                                dialog, 
                                message, 
                                "Trip Completed", 
                                JOptionPane.INFORMATION_MESSAGE
                            );
                            
                            dialog.dispose();
                            showDriverDashboard();
                        }
                        
                    } else {
                        
                        JOptionPane.showMessageDialog(
                            dialog, 
                            "Please select a trip first!", 
                            "No Selection", 
                            JOptionPane.WARNING_MESSAGE
                        );
                    }
                }
            });
            
            
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(WHITE);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            buttonPanel.add(completeButton);
            
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }
        
        
        panel.add(titleLabel, BorderLayout.NORTH);
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    
    
    // show available trips for riders
    private void showAvailableTrips() {
        
        ArrayList<Trip> trips = system.getAvailableTrips();
        
        JDialog dialog = new JDialog(this, "Available Trips", true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        
        
        JLabel titleLabel = new JLabel("Available Trips (" + trips.size() + ") - Click to select and join!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(NAVY_BLUE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        
        if (trips.isEmpty()) {
            
            JLabel noTripsLabel = new JLabel("No available trips at the moment.");
            noTripsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            noTripsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            panel.add(noTripsLabel, BorderLayout.CENTER);
            
            
        } else {
            
            // create list of trips
            DefaultListModel<String> listModel = new DefaultListModel<String>();
            
            for (Trip trip : trips) {
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String timeStr = trip.getDepartureTime().format(formatter);
                
                int seatsLeft = trip.getDriver().getCarCapacity() - 1 - trip.getPassengerCount();
                
                String tripInfo = String.format(
                    "<html><b>%s</b> - %s → %s<br>" +
                    "Time: %s | Driver: %s | Seats: %d left | Fare: GHS %.2f</html>",
                    trip.getId(),
                    trip.getOrigin().getName(),
                    trip.getDestination().getName(),
                    timeStr,
                    trip.getDriver().getName(),
                    seatsLeft,
                    trip.calculateFarePerPerson()
                );
                
                listModel.addElement(tripInfo);
            }
            
            
            JList<String> tripList = new JList<String>(listModel);
            tripList.setFont(new Font("Arial", Font.PLAIN, 13));
            tripList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tripList.setFixedCellHeight(60);
            
            JScrollPane scrollPane = new JScrollPane(tripList);
            panel.add(scrollPane, BorderLayout.CENTER);
            
            
            
            // join button
            JButton joinButton = new JButton("JOIN SELECTED TRIP");
            
            joinButton.setPreferredSize(new Dimension(320, 45));
            joinButton.setBackground(MID_BLUE);
            joinButton.setForeground(Color.WHITE);
            joinButton.setFont(new Font("SansSerif", Font.BOLD, 13));
            joinButton.setFocusPainted(false);
            joinButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            
            joinButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    int selected = tripList.getSelectedIndex();
                    
                    if (selected >= 0) {
                        
                        Trip trip = trips.get(selected);
                        
                        if (trip.addPassenger(currentUser)) {
                            
                            String message = String.format(
                                "Success! You joined trip %s\nFare: GHS %.2f\nDriver: %s", 
                                trip.getId(), 
                                trip.calculateFarePerPerson(), 
                                trip.getDriver().getName()
                            );
                            
                            JOptionPane.showMessageDialog(
                                dialog, 
                                message, 
                                "Trip Joined", 
                                JOptionPane.INFORMATION_MESSAGE
                            );
                            
                            dialog.dispose();
                            showRiderDashboard();
                            
                        } else {
                            
                            JOptionPane.showMessageDialog(
                                dialog, 
                                "Trip is full or you're already in this trip.", 
                                "Error", 
                                JOptionPane.ERROR_MESSAGE
                            );
                        }
                        
                    } else {
                        
                        JOptionPane.showMessageDialog(
                            dialog, 
                            "Please select a trip first!", 
                            "No Selection", 
                            JOptionPane.WARNING_MESSAGE
                        );
                    }
                }
            });
            
            
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(WHITE);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            buttonPanel.add(joinButton);
            
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }
        
        
        panel.add(titleLabel, BorderLayout.NORTH);
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    
    
    // show user statistics
    private void showStats() {
        
        JDialog dialog = new JDialog(this, "My Statistics", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel();
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setLayout(new GridBagLayout());
        
        
        JTextArea statsArea = new JTextArea(currentUser.toString());
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        statsArea.setBackground(LIGHT_GRAY);
        statsArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        
        panel.add(statsArea, gbc);
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    
    
    // show registration dialog
    private void showRegisterDialog(boolean isDriver) {
        
        String userType = isDriver ? "Driver" : "Rider";
        
        JDialog dialog = new JDialog(this, "Register New " + userType, true);
        
        int dialogHeight = isDriver ? 650 : 500;
        dialog.setSize(450, dialogHeight);
        dialog.setLocationRelativeTo(this);
        
        
        JPanel panel = new JPanel();
        panel.setBackground(WHITE);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 0);
        
        
        
        // create text fields
        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(300, 40));
        nameField.setFont(new Font("Arial", Font.PLAIN, 13));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MID_BLUE, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        addPlaceholder(nameField, "Full Name");
        
        
        JTextField contactField = new JTextField();
        contactField.setPreferredSize(new Dimension(300, 40));
        contactField.setFont(new Font("Arial", Font.PLAIN, 13));
        contactField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MID_BLUE, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        addPlaceholder(contactField, "Contact Info");
        
        
        JTextField ageField = new JTextField();
        ageField.setPreferredSize(new Dimension(300, 40));
        ageField.setFont(new Font("Arial", Font.PLAIN, 13));
        ageField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MID_BLUE, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        addPlaceholder(ageField, "Age");
        
        
        JTextField genderField = new JTextField();
        genderField.setPreferredSize(new Dimension(300, 40));
        genderField.setFont(new Font("Arial", Font.PLAIN, 13));
        genderField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MID_BLUE, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        addPlaceholder(genderField, "Gender");
        
        
        
        // driver-specific fields
        JTextField carModelField = null;
        JTextField carPlateField = null;
        JTextField capacityField = null;
        JTextField experienceField = null;
        
        // rider-specific field
        JTextField paymentField = null;
        
        
        if (isDriver) {
            
            carModelField = new JTextField();
            carModelField.setPreferredSize(new Dimension(300, 40));
            carModelField.setFont(new Font("Arial", Font.PLAIN, 13));
            carModelField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MID_BLUE, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            addPlaceholder(carModelField, "Car Model");
            
            
            carPlateField = new JTextField();
            carPlateField.setPreferredSize(new Dimension(300, 40));
            carPlateField.setFont(new Font("Arial", Font.PLAIN, 13));
            carPlateField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MID_BLUE, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            addPlaceholder(carPlateField, "License Plate");
            
            
            capacityField = new JTextField();
            capacityField.setPreferredSize(new Dimension(300, 40));
            capacityField.setFont(new Font("Arial", Font.PLAIN, 13));
            capacityField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MID_BLUE, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            addPlaceholder(capacityField, "Car Capacity");
            
            
            experienceField = new JTextField();
            experienceField.setPreferredSize(new Dimension(300, 40));
            experienceField.setFont(new Font("Arial", Font.PLAIN, 13));
            experienceField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MID_BLUE, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            addPlaceholder(experienceField, "Years of Experience");
            
            
        } else {
            
            paymentField = new JTextField();
            paymentField.setPreferredSize(new Dimension(300, 40));
            paymentField.setFont(new Font("Arial", Font.PLAIN, 13));
            paymentField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MID_BLUE, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            addPlaceholder(paymentField, "Payment Method");
        }
        
        
        
        // create buttons
        JButton registerButton = new JButton("REGISTER");
        
        registerButton.setPreferredSize(new Dimension(320, 45));
        registerButton.setBackground(MID_BLUE);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        
        JButton cancelButton = new JButton("CANCEL");
        
        cancelButton.setPreferredSize(new Dimension(320, 45));
        cancelButton.setBackground(new Color(70, 130, 180));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        
        
        // make final references for action listener
        final JTextField finalCarModel = carModelField;
        final JTextField finalCarPlate = carPlateField;
        final JTextField finalCapacity = capacityField;
        final JTextField finalExperience = experienceField;
        final JTextField finalPayment = paymentField;
        
        
        
        // add action to register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                try {
                    // get common field values
                    String name = getFieldText(nameField);
                    String contact = getFieldText(contactField);
                    String ageStr = getFieldText(ageField);
                    String gender = getFieldText(genderField);
                    
                    // validate common fields
                    if (name.isEmpty() || contact.isEmpty() || ageStr.isEmpty() || gender.isEmpty()) {
                        JOptionPane.showMessageDialog(
                            dialog, 
                            "Please fill in all fields!", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                    
                    // generate new ID
                    String prefix = isDriver ? "DRV" : "RDR";
                    String id = prefix + String.format("%03d", system.getAllUsers().size() + 1);
                    
                    
                    if (isDriver) {
                        
                        // get driver-specific fields
                        String carModel = getFieldText(finalCarModel);
                        String carPlate = getFieldText(finalCarPlate);
                        String capacityStr = getFieldText(finalCapacity);
                        String experienceStr = getFieldText(finalExperience);
                        
                        // validate driver fields
                        if (carModel.isEmpty() || carPlate.isEmpty() || 
                            capacityStr.isEmpty() || experienceStr.isEmpty()) {
                            
                            JOptionPane.showMessageDialog(
                                dialog, 
                                "Please fill in all fields!", 
                                "Error", 
                                JOptionPane.ERROR_MESSAGE
                            );
                            return;
                        }
                        
                        // create new driver
                        Driver driver = new Driver(
                            id, 
                            name, 
                            contact,
                            Integer.parseInt(ageStr), 
                            gender,
                            carModel, 
                            carPlate,
                            Integer.parseInt(capacityStr), 
                            Integer.parseInt(experienceStr)
                        );
                        
                        system.registerUser(driver);
                        
                        
                    } else {
                        
                        // get rider-specific field
                        String paymentMethod = getFieldText(finalPayment);
                        
                        // validate rider field
                        if (paymentMethod.isEmpty()) {
                            JOptionPane.showMessageDialog(
                                dialog, 
                                "Please fill in all fields!", 
                                "Error", 
                                JOptionPane.ERROR_MESSAGE
                            );
                            return;
                        }
                        
                        // create new rider
                        Rider rider = new Rider(
                            id, 
                            name, 
                            contact,
                            Integer.parseInt(ageStr), 
                            gender, 
                            paymentMethod
                        );
                        
                        system.registerUser(rider);
                    }
                    
                    
                    JOptionPane.showMessageDialog(
                        dialog, 
                        "Registration successful!\nYour ID: " + id, 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    
                    dialog.dispose();
                    
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                        dialog, 
                        "Error: Please enter valid numbers for Age, Capacity, and Experience!", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE
                    );
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        dialog, 
                        "Error: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        
        
        // add components to panel
        int row = 0;
        
        gbc.gridy = row++;
        panel.add(nameField, gbc);
        
        gbc.gridy = row++;
        panel.add(contactField, gbc);
        
        gbc.gridy = row++;
        panel.add(ageField, gbc);
        
        gbc.gridy = row++;
        panel.add(genderField, gbc);
        
        
        if (isDriver) {
            
            gbc.gridy = row++;
            panel.add(carModelField, gbc);
            
            gbc.gridy = row++;
            panel.add(carPlateField, gbc);
            
            gbc.gridy = row++;
            panel.add(capacityField, gbc);
            
            gbc.gridy = row++;
            panel.add(experienceField, gbc);
            
            
        } else {
            
            gbc.gridy = row++;
            panel.add(paymentField, gbc);
        }
        
        
        gbc.gridy = row++;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(registerButton, gbc);
        
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(cancelButton, gbc);
        
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    
    
    // setup sample data if CSV is empty
    private void setupSampleData() {
        
        // create locations
        Location ashesi = new Location("Ashesi University", "Berekuso");
        Location accraMan = new Location("Accra Mall", "Tetteh Quarshie");
        Location kotoka = new Location("Kotoka Airport", "Airport");
        Location osuOxford = new Location("Osu Oxford Street", "Osu");
        
        
        // create drivers
        Driver driver1 = new Driver(
            "DRV001", "Kwame Mensah", "0244123456", 
            28, "Male", "Toyota Corolla", "GR-4567-20", 5, 6
        );
        
        Driver driver2 = new Driver(
            "DRV002", "Ama Serwaa", "0201234567", 
            32, "Female", "Honda Civic", "AS-8901-19", 4, 8
        );
        
        
        // create riders
        Rider rider1 = new Rider(
            "RDR001", "Kofi Appiah", "0559876543", 
            22, "Male", "Mobile Money"
        );
        
        Rider rider2 = new Rider(
            "RDR002", "Abena Osei", "0267890123", 
            25, "Female", "Cash"
        );
        
        
        // register users
        system.registerUser(driver1);
        system.registerUser(driver2);
        system.registerUser(rider1);
        system.registerUser(rider2);
        
        
        
        // create sample trips
        LocalDateTime tomorrow9am = LocalDateTime.now()
            .plusDays(1)
            .withHour(9)
            .withMinute(0);
        
        LocalDateTime tomorrow2pm = LocalDateTime.now()
            .plusDays(1)
            .withHour(14)
            .withMinute(0);
        
        
        Trip trip1 = new Trip("TRIP001", driver1, ashesi, accraMan, tomorrow9am);
        Trip trip2 = new Trip("TRIP002", driver2, kotoka, osuOxford, tomorrow2pm);
        
        
        system.postTrip(trip1);
        system.postTrip(trip2);
    }
    
    
    
    // save data to CSV files
    private void saveData() {
        system.saveUsersToCSV();
        system.saveTripsToCSV();
    }
    
    
    
    // main method - entry point of the program
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RideLinkGUI();
            }
        });
    }
}
