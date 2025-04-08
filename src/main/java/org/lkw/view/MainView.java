package org.lkw.view;

import com.formdev.flatlaf.FlatLightLaf;
import org.lkw.data.util.LaravelTheme;
import org.lkw.model.User;
import org.lkw.view.user.UserSettingsPanel;
import org.lkw.view.user.UserDashboardPanel;
import org.lkw.view.user.UserBooksPanel;
import org.lkw.view.user.UserTransactionsPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainView extends JFrame {
    private final JLabel usernameLabel;
    private final JLabel roleLabel;
    private final JButton dashboardButton;
    private final JButton booksButton;
    private final JButton transactionsButton;
    private final JButton settingButton;
    private final JButton logoutButton;
    private final JPanel contentPanel;
    
    // Optional admin-only buttons
    private JButton usersButton;
    
    // Active button tracking
    private JButton activeButton;
    
    // User info
    private User currentUser;

    public MainView() {
        // Set FlatLaf look and feel
        FlatLightLaf.setup();
        
        // Set up the frame
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1024, 700));
        
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
        
        usernameLabel = new JLabel("User/Admin");
        usernameLabel.setFont(new Font("Inter", Font.BOLD, 16));
        usernameLabel.setForeground(LaravelTheme.TEXT_DARK);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        roleLabel = new JLabel("Role: Member");
        roleLabel.setFont(new Font("Inter", Font.ITALIC, 13));
        roleLabel.setForeground(LaravelTheme.MUTED_TEXT);
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        userInfoPanel.add(usernameLabel);
        userInfoPanel.add(Box.createVerticalStrut(5));
        userInfoPanel.add(roleLabel);
        
        profilePanel.add(userInfoPanel, BorderLayout.CENTER);
        
        // Menu items panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        // Create menu buttons
        dashboardButton = createMenuButton("Dashboard", true);
        booksButton = createMenuButton("Books", false);
        transactionsButton = createMenuButton("Transactions", false);
        settingButton = createMenuButton("Setting", false);
        
        // Set Dashboard as the active button initially
        activeButton = dashboardButton;
        
        // Add buttons to the menu panel
        menuPanel.add(dashboardButton);
        menuPanel.add(booksButton);
        menuPanel.add(transactionsButton);
        menuPanel.add(settingButton);
        
        // Add filler to push logout button to the bottom
        menuPanel.add(Box.createVerticalGlue());
        
        // Logout button at the bottom of the sidebar
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Inter", Font.BOLD, 14));
        logoutButton.setForeground(LaravelTheme.TEXT_DARK);
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, LaravelTheme.BORDER_GRAY),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setHorizontalAlignment(SwingConstants.CENTER);
        logoutButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, logoutButton.getPreferredSize().height));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add the panels to the sidebar
        sidebarPanel.add(profilePanel, BorderLayout.NORTH);
        
        // Add some spacing between profile and menu
        menuPanel.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
        
        sidebarPanel.add(menuPanel, BorderLayout.CENTER);
        sidebarPanel.add(logoutButton, BorderLayout.SOUTH);
        
        // Content area
        JPanel contentAreaPanel = new JPanel(new BorderLayout());
        contentAreaPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        
        // Main content panel
        contentPanel = new JPanel();
        contentPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
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
    }
    
    // Create a styled menu button
    private JButton createMenuButton(String text, boolean isActive) {
        JButton button = new JButton(text);
        button.setFont(new Font("Inter", Font.BOLD, 14));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Set initial styling
        updateButtonStyle(button, isActive);
        
        // Add padding
        button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Make sure the button spans the full width of the sidebar
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Add click handler to switch active state
        button.addActionListener(e -> {
            if (activeButton != button) {
                updateButtonStyle(activeButton, false);
                updateButtonStyle(button, true);
                activeButton = button;
                
                // Show the appropriate content
                if (button == dashboardButton) {
                    showDashboardContent();
                } else if (button == booksButton) {
                    showBooksContent();
                } else if (button == transactionsButton) {
                    showTransactionsContent();
                } else if (button == settingButton) {
                    showSettingsContent();
                } else if (button == usersButton) {
                    showUsersContent();
                }
            }
        });
        
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
    
    // Content display methods
    private void showDashboardContent() {
        if (currentUser == null) {
            return; // Don't show dashboard if user is not set
        }

        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        
        // Create and add the UserDashboardPanel
        UserDashboardPanel dashboardPanel = new UserDashboardPanel(currentUser);
        contentPanel.add(dashboardPanel, BorderLayout.CENTER);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showBooksContent() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        
        // Create and add the UserBooksPanel
        UserBooksPanel booksPanel = new UserBooksPanel();
        contentPanel.add(booksPanel, BorderLayout.CENTER);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showTransactionsContent() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(new UserTransactionsPanel(currentUser), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showSettingsContent() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        
        UserSettingsPanel settingsPanel = new UserSettingsPanel(currentUser);
        contentPanel.add(settingsPanel, BorderLayout.CENTER);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    // Set user info and initialize dashboard
    public void setUser(User user) {
        this.currentUser = user;
        usernameLabel.setText(user.getUsername());
        
        // Capitalize first letter of role
        String role = user.getRole();
        role = role.substring(0, 1).toUpperCase() + role.substring(1);
        roleLabel.setText("Role: " + role);
        
        // Check if the user is an admin and add the Users button if needed
        if ("admin".equalsIgnoreCase(user.getRole()) && usersButton == null) {
            // Create the Users button
            usersButton = createMenuButton("Users", false);
            
            // Add the button after the dashboard button
            JPanel menuPanel = (JPanel) dashboardButton.getParent();
            menuPanel.removeAll();
            
            // Re-add all buttons in correct order
            menuPanel.add(dashboardButton);
            menuPanel.add(usersButton);
            menuPanel.add(booksButton);
            menuPanel.add(transactionsButton);
            menuPanel.add(settingButton);
            menuPanel.add(Box.createVerticalGlue());  // Push logout to bottom
            
            // Add click handler for Users button
            usersButton.addActionListener(e -> {
                if (activeButton != usersButton) {
                    updateButtonStyle(activeButton, false);
                    updateButtonStyle(usersButton, true);
                    activeButton = usersButton;
                    showUsersContent();
                }
            });
            
            // Refresh the UI
            menuPanel.revalidate();
            menuPanel.repaint();
        }

        // Show the dashboard after user is set
        showDashboardContent();

        // Set up button actions
        dashboardButton.addActionListener(e -> {
            if (activeButton != dashboardButton) {
                updateButtonStyle(activeButton, false);
                updateButtonStyle(dashboardButton, true);
                activeButton = dashboardButton;
                showDashboardContent();
            }
        });

        booksButton.addActionListener(e -> {
            if (activeButton != booksButton) {
                updateButtonStyle(activeButton, false);
                updateButtonStyle(booksButton, true);
                activeButton = booksButton;
                showBooksContent();
            }
        });

        transactionsButton.addActionListener(e -> {
            if (activeButton != transactionsButton) {
                updateButtonStyle(activeButton, false);
                updateButtonStyle(transactionsButton, true);
                activeButton = transactionsButton;
                showTransactionsContent();
            }
        });

        settingButton.addActionListener(e -> {
            if (activeButton != settingButton) {
                updateButtonStyle(activeButton, false);
                updateButtonStyle(settingButton, true);
                activeButton = settingButton;
                showSettingsContent();
            }
        });
    }
    
    // Additional content display method for Users
    private void showUsersContent() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        
        JLabel usersLabel = new JLabel("Users Management");
        usersLabel.setFont(new Font("Inter", Font.BOLD, 24));
        usersLabel.setForeground(LaravelTheme.TEXT_DARK);
        
        contentPanel.add(usersLabel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    // Add action listeners
    public void setLogoutActionListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }
    
    // Window state methods
    public void maximizeIfNeeded(boolean shouldMaximize) {
        if (shouldMaximize) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }
} 