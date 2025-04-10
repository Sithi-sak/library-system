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

public class AdminView extends JFrame {
    
    private final JButton dashboardButton;
    private final JButton usersButton;
    private final JButton booksButton;
    private final JButton settingButton;
    private final JButton logoutButton;
    
    private final JLabel usernameLabel;
    private final JLabel roleLabel;
    
    private final JPanel contentPanel;
    
    private final DashboardPanel dashboardPanel;
    private UsersPanel usersPanel;
    private BooksPanel booksPanel;
    private SettingsPanel settingsPanel;
    
    private User currentUser;
    
    private JButton activeButton;
    
    public AdminView() {
        FlatLightLaf.setup();
        
        setTitle("Library Management System - Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 800));

        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JPanel sidebarPanel = new JPanel(new BorderLayout());
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));
        sidebarPanel.setBackground(Color.WHITE);
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, LaravelTheme.BORDER_GRAY));
        
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBackground(LaravelTheme.LIGHT_GRAY);
        profilePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        profilePanel.setPreferredSize(new Dimension(200, 90));
        
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
        
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        dashboardButton = createMenuButton("Dashboard", true);
        usersButton = createMenuButton("Users", false);
        booksButton = createMenuButton("Books", false);
        settingButton = createMenuButton("Settings", false);
        logoutButton = createMenuButton("Logout", false);
        
        activeButton = dashboardButton;
        
        menuPanel.add(dashboardButton);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(usersButton);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(booksButton);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(settingButton);
        
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setForeground(LaravelTheme.DANGER_RED);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        sidebarPanel.add(profilePanel, BorderLayout.NORTH);
        sidebarPanel.add(menuPanel, BorderLayout.CENTER);
        sidebarPanel.add(logoutButton, BorderLayout.SOUTH);
        
        JPanel contentAreaPanel = new JPanel(new BorderLayout());
        contentAreaPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        
        dashboardPanel = new DashboardPanel();
        
        contentPanel.add(dashboardPanel, "dashboard");
        
        contentAreaPanel.add(contentPanel, BorderLayout.CENTER);
        
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(contentAreaPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        
        pack();
        setLocationRelativeTo(null);
        
        setupButtonActions();
    }
    
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
                
                contentPanel.removeAll();
                
                contentPanel.add(dashboardPanel, "dashboard");
                
                CardLayout cl = (CardLayout) contentPanel.getLayout();
                cl.show(contentPanel, "dashboard");
                
                dashboardPanel.updateDashboardData();
                
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });
        
        usersButton.addActionListener(e -> {
            if (activeButton != usersButton) {
                updateButtonStyle(activeButton, false);
                updateButtonStyle(usersButton, true);
                activeButton = usersButton;
                
                contentPanel.removeAll();
                
                if (usersPanel == null) {
                    usersPanel = new UsersPanel();
                }
                
                contentPanel.add(usersPanel, "users");
                
                CardLayout cl = (CardLayout) contentPanel.getLayout();
                cl.show(contentPanel, "users");
                
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });
        
        booksButton.addActionListener(e -> {
            if (activeButton != booksButton) {
                updateButtonStyle(activeButton, false);
                updateButtonStyle(booksButton, true);
                activeButton = booksButton;
                
                contentPanel.removeAll();
                
                if (booksPanel == null) {
                    booksPanel = new BooksPanel();
                }
                
                contentPanel.add(booksPanel, "books");
                
                CardLayout cl = (CardLayout) contentPanel.getLayout();
                cl.show(contentPanel, "books");
                
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });
        
        settingButton.addActionListener(e -> {
            if (activeButton != settingButton) {
                updateButtonStyle(activeButton, false);
                updateButtonStyle(settingButton, true);
                activeButton = settingButton;
                
                contentPanel.removeAll();
                
                if (settingsPanel == null) {
                    settingsPanel = new SettingsPanel();
                }
                
                contentPanel.add(settingsPanel, "settings");
                
                CardLayout cl = (CardLayout) contentPanel.getLayout();
                cl.show(contentPanel, "settings");
                
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });
    }
    
    public void setUser(User user) {
        this.currentUser = user;
        usernameLabel.setText(user.getUsername());
        
        String role = user.getRole();
        role = role.substring(0, 1).toUpperCase() + role.substring(1);
        roleLabel.setText("Role: " + role);
        
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