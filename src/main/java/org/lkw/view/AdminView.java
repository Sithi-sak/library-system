package org.lkw.view;

import com.formdev.flatlaf.FlatLightLaf;
import org.lkw.data.util.LaravelTheme;
import org.lkw.model.User;
import org.lkw.view.admin.DashboardPanel;
import org.lkw.view.admin.UsersPanel;
import org.lkw.view.admin.BooksPanel;
import org.lkw.view.admin.SettingsPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Main view for administrators with advanced management features
 */
public class AdminView extends JFrame {
    
    // Sidebar buttons
    private final JButton dashboardButton;
    private final JButton usersButton;
    private final JButton booksButton;
    private final JButton settingButton;
    private final JButton logoutButton;
    
    // User info
    private final JLabel usernameLabel;
    private final JLabel roleLabel;
    
    // Content area
    private final JPanel contentPanel;
    
    // Panels
    private final DashboardPanel dashboardPanel;
    private UsersPanel usersPanel;
    private BooksPanel booksPanel;
    private SettingsPanel settingsPanel;
    
    // User info
    private User currentUser;
    
    // Active button tracking
    private JButton activeButton;
    
    public AdminView() {
        // Set FlatLaf look and feel
        FlatLightLaf.setup();
        
        // Set up the frame
        setTitle("Library Management System - Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 800));
        
        // Main split layout - sidebar and content
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Create sidebar panel
        JPanel sidebarPanel = new JPanel(new BorderLayout());
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));
        sidebarPanel.setBackground(Color.WHITE);
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, LaravelTheme.BORDER_GRAY));
        
        // Profile panel at the top of the sidebar
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBackground(LaravelTheme.LIGHT_GRAY);
        profilePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        profilePanel.setPreferredSize(new Dimension(200, 90));
        
        // Create user info panel
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBackground(LaravelTheme.LIGHT_GRAY);
        
        usernameLabel = new JLabel("Admin");
        usernameLabel.setFont(new Font("Inter", Font.BOLD, 16));
        usernameLabel.setForeground(LaravelTheme.TEXT_DARK);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        roleLabel = new JLabel("Role: Administrator");
        roleLabel.setFont(new Font("Inter", Font.ITALIC, 13));
        roleLabel.setForeground(LaravelTheme.MUTED_TEXT);
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        userInfoPanel.add(usernameLabel);
        userInfoPanel.add(Box.createVerticalStrut(5));
        userInfoPanel.add(roleLabel);
        
        profilePanel.add(userInfoPanel, BorderLayout.CENTER);
        
        // Create menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Create menu buttons
        dashboardButton = createMenuButton("Dashboard", true);
        usersButton = createMenuButton("Users", false);
        booksButton = createMenuButton("Books", false);
        settingButton = createMenuButton("Settings", false);
        logoutButton = createMenuButton("Logout", false);
        
        // Set initial active button
        activeButton = dashboardButton;
        
        // Add buttons to menu panel
        menuPanel.add(dashboardButton);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(usersButton);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(booksButton);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(settingButton);
        
        // Style logout button
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setForeground(LaravelTheme.DANGER_RED);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add the panels to the sidebar
        sidebarPanel.add(profilePanel, BorderLayout.NORTH);
        sidebarPanel.add(menuPanel, BorderLayout.CENTER);
        sidebarPanel.add(logoutButton, BorderLayout.SOUTH);
        
        // Content area
        JPanel contentAreaPanel = new JPanel(new BorderLayout());
        contentAreaPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        
        // Main content panel for showing different views
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        
        // Initialize panels
        dashboardPanel = new DashboardPanel();
        
        // Add all panels to card layout
        contentPanel.add(dashboardPanel, "dashboard");
        
        // Add components to content area
        contentAreaPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add sidebar and content area to main panel
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(contentAreaPanel, BorderLayout.CENTER);
        
        // Set content pane
        setContentPane(mainPanel);
        
        // Size and center the window
        pack();
        setLocationRelativeTo(null);
        
        // Set up button action listeners
        setupButtonActions();
    }
    
    // Create a styled menu button
    private JButton createMenuButton(String text, boolean isActive) {
        JButton button = new JButton(text);
        button.setFont(new Font("Inter", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
        
        updateButtonStyle(button, isActive);
        
        return button;
    }
    
    private void updateButtonStyle(JButton button, boolean isActive) {
        if (isActive) {
            button.setBackground(Color.BLACK);
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(LaravelTheme.TEXT_DARK);
            button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        }
    }
    
    private void setupButtonActions() {
        dashboardButton.addActionListener(e -> {
            if (activeButton != dashboardButton) {
                updateButtonStyle(activeButton, false);
                updateButtonStyle(dashboardButton, true);
                activeButton = dashboardButton;
                
                // Clear the content panel
                contentPanel.removeAll();
                
                // Add the dashboard panel back
                contentPanel.add(dashboardPanel, "dashboard");
                
                // Show the dashboard panel
                CardLayout cl = (CardLayout) contentPanel.getLayout();
                cl.show(contentPanel, "dashboard");
                
                // Update dashboard data
                dashboardPanel.updateDashboardData();
                
                // Revalidate and repaint
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });
        
        usersButton.addActionListener(e -> {
            if (activeButton != usersButton) {
                updateButtonStyle(activeButton, false);
                updateButtonStyle(usersButton, true);
                activeButton = usersButton;
                
                // Clear the content panel
                contentPanel.removeAll();
                
                // Create the users panel if it doesn't exist
                if (usersPanel == null) {
                    usersPanel = new UsersPanel();
                }
                
                // Add the users panel
                contentPanel.add(usersPanel, "users");
                
                // Show the users panel
                CardLayout cl = (CardLayout) contentPanel.getLayout();
                cl.show(contentPanel, "users");
                
                // Revalidate and repaint
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });
        
        booksButton.addActionListener(e -> {
            if (activeButton != booksButton) {
                updateButtonStyle(activeButton, false);
                updateButtonStyle(booksButton, true);
                activeButton = booksButton;
                
                // Clear the content panel
                contentPanel.removeAll();
                
                // Create the books panel if it doesn't exist
                if (booksPanel == null) {
                    booksPanel = new BooksPanel();
                }
                
                // Add the books panel
                contentPanel.add(booksPanel, "books");
                
                // Show the books panel
                CardLayout cl = (CardLayout) contentPanel.getLayout();
                cl.show(contentPanel, "books");
                
                // Revalidate and repaint
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });
        
        settingButton.addActionListener(e -> {
            if (activeButton != settingButton) {
                updateButtonStyle(activeButton, false);
                updateButtonStyle(settingButton, true);
                activeButton = settingButton;
                
                // Clear the content panel
                contentPanel.removeAll();
                
                // Create the settings panel if it doesn't exist
                if (settingsPanel == null) {
                    settingsPanel = new SettingsPanel();
                }
                
                // Add the settings panel
                contentPanel.add(settingsPanel, "settings");
                
                // Show the settings panel
                CardLayout cl = (CardLayout) contentPanel.getLayout();
                cl.show(contentPanel, "settings");
                
                // Revalidate and repaint
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });
    }
    
    public void setUser(User user) {
        this.currentUser = user;
        usernameLabel.setText(user.getUsername());
        
        // Capitalize first letter of role
        String role = user.getRole();
        role = role.substring(0, 1).toUpperCase() + role.substring(1);
        roleLabel.setText("Role: " + role);
        
        // Show the dashboard after user is set
        showDashboardContent();
    }
    
    public void setLogoutActionListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }
    
    public void maximizeIfNeeded(boolean shouldMaximize) {
        if (shouldMaximize) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }
    
    private void showDashboardContent() {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "dashboard");
    }
    
    public void refreshDashboard() {
        if (dashboardPanel != null) {
            dashboardPanel.refreshData();
        }
    }
} 