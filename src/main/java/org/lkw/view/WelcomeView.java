package org.lkw.view;

import com.formdev.flatlaf.FlatLightLaf;
import org.lkw.data.util.LaravelTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class WelcomeView extends JFrame {
    
    private final JLabel welcomeLabel;
    private final JLabel roleLabel;
    private final JButton logoutButton;
    
    public WelcomeView() {
        // Set FlatLaf look and feel
        FlatLightLaf.setup();
        
        // Set up the frame
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Main panel with white background
        JPanel mainPanel = LaravelTheme.createPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        // Header panel
        JPanel headerPanel = LaravelTheme.createPanel();
        headerPanel.setLayout(new GridBagLayout());
        GridBagConstraints headerGbc = new GridBagConstraints();
        headerGbc.gridwidth = GridBagConstraints.REMAINDER;
        headerGbc.anchor = GridBagConstraints.CENTER;
        headerGbc.insets = new Insets(0, 0, 8, 0);
        
        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 22));
        titleLabel.setForeground(LaravelTheme.PRIMARY_RED);
        headerPanel.add(titleLabel, headerGbc);
        
        welcomeLabel = new JLabel("Welcome, User!");
        welcomeLabel.setFont(new Font("Inter", Font.PLAIN, 16));
        welcomeLabel.setForeground(LaravelTheme.TEXT_DARK);
        headerGbc.insets = new Insets(10, 0, 5, 0);
        headerPanel.add(welcomeLabel, headerGbc);
        
        roleLabel = new JLabel("Role: Member");
        roleLabel.setFont(new Font("Inter", Font.ITALIC, 14));
        roleLabel.setForeground(LaravelTheme.TEXT_DARK);
        headerGbc.insets = new Insets(0, 0, 20, 0);
        headerPanel.add(roleLabel, headerGbc);
        
        // Center content - info card
        JPanel infoCard = LaravelTheme.createCard();
        infoCard.setLayout(new GridBagLayout());
        
        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.gridwidth = GridBagConstraints.REMAINDER;
        cardGbc.anchor = GridBagConstraints.CENTER;
        cardGbc.insets = new Insets(0, 0, 12, 0);
        
        JLabel infoLabel = new JLabel("Welcome to the Library Management System");
        infoLabel.setFont(new Font("Inter", Font.BOLD, 14));
        infoLabel.setForeground(LaravelTheme.TEXT_DARK);
        infoCard.add(infoLabel, cardGbc);
        
        JLabel descLabel = new JLabel("You have successfully logged in");
        descLabel.setFont(new Font("Inter", Font.PLAIN, 13));
        descLabel.setForeground(LaravelTheme.TEXT_DARK);
        cardGbc.insets = new Insets(0, 0, 20, 0);
        infoCard.add(descLabel, cardGbc);
        
        // Add version label
        JLabel versionLabel = new JLabel("Library v1.0");
        versionLabel.setFont(new Font("Inter", Font.PLAIN, 11));
        versionLabel.setForeground(LaravelTheme.MUTED_TEXT);
        cardGbc.insets = new Insets(0, 0, 0, 0);
        infoCard.add(versionLabel, cardGbc);
        
        // Add infoCard directly to main panel
        JPanel centerPanel = LaravelTheme.createPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints centerGbc = new GridBagConstraints();
        centerGbc.gridwidth = GridBagConstraints.REMAINDER;
        centerGbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(infoCard, centerGbc);
        
        // Logout button with consistent styling
        JPanel footerPanel = LaravelTheme.createPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        logoutButton = new JButton("Logout");
        LaravelTheme.stylePrimaryButton(logoutButton);
        
        footerPanel.add(logoutButton);
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // Set the content pane
        setContentPane(mainPanel);
        
        // Size and center the window
        pack();
        setLocationRelativeTo(null);
    }
    
    public void setWelcomeMessage(String username) {
        welcomeLabel.setText("Welcome, " + username + "!");
    }
    
    public void setRole(String role) {
        roleLabel.setText("Role: " + role);
    }
    
    public void setUserInfo(String username, String role) {
        welcomeLabel.setText("Welcome, " + username + "!");
        roleLabel.setText("Role: " + role.substring(0, 1).toUpperCase() + role.substring(1));
    }
    
    public void setLogoutActionListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }
    
    public void maximizeIfNeeded(boolean shouldMaximize) {
        if (shouldMaximize) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }
} 