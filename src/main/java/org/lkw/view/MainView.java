package org.lkw.view;

import com.formdev.flatlaf.FlatLightLaf;
import org.lkw.data.util.LaravelTheme;
import org.lkw.model.User;
import org.lkw.view.user.UserSettingsPanel;
import org.lkw.view.user.UserDashboardPanel;
import org.lkw.view.user.UserBooksPanel;

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
    private final JButton settingButton;
    private final JButton logoutButton;
    private final JPanel contentPanel;
    
    private JButton usersButton;
    
    private JButton activeButton;
    
    private User currentUser;
    
    public MainView() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        usernameLabel = new JLabel();
        roleLabel = new JLabel();
        dashboardButton = createSidebarButton("Dashboard", true);
        booksButton = createSidebarButton("Books", false);
        settingButton = createSidebarButton("Settings", false);
        logoutButton = createSidebarButton("Logout", false);
        contentPanel = new JPanel(new BorderLayout());
        
        activeButton = dashboardButton;
        
        setLayout(new BorderLayout());
        
        JPanel sidebarPanel = createSidebar();
        add(sidebarPanel, BorderLayout.WEST);
        
        contentPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        add(contentPanel, BorderLayout.CENTER);
        
        setupButtonActions();
    }
    
    private JPanel createSidebar() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BorderLayout());
        sidebarPanel.setBackground(Color.WHITE);
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, LaravelTheme.BORDER_GRAY));
        
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBackground(Color.WHITE);
        profilePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, LaravelTheme.BORDER_GRAY));

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBackground(Color.WHITE);
        userInfoPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        usernameLabel.setFont(new Font("Inter", Font.BOLD, 16));
        usernameLabel.setForeground(LaravelTheme.TEXT_DARK);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        roleLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        roleLabel.setForeground(LaravelTheme.MUTED_TEXT);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        roleLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        
        userInfoPanel.add(usernameLabel);
        userInfoPanel.add(roleLabel);
        
        profilePanel.add(userInfoPanel, BorderLayout.CENTER);
        
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        menuPanel.add(dashboardButton);
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
        
        return sidebarPanel;
    }
    
    private JButton createSidebarButton(String text, boolean isActive) {
        JButton button = new JButton(text);
        button.setFont(new Font("Inter", Font.PLAIN, 14));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        updateButtonStyle(button, isActive);
        
        return button;
    }
    
    private void updateButtonStyle(JButton button, boolean isActive) {
        if (isActive) {
            button.setForeground(LaravelTheme.PRIMARY_RED);
            button.setBackground(new Color(254, 242, 242));
            button.setOpaque(true);
        } else {
            button.setForeground(LaravelTheme.TEXT_DARK);
            button.setBackground(Color.WHITE);
            button.setOpaque(false);
        }
    }
    
    private void setupButtonActions() {
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

        settingButton.addActionListener(e -> {
            if (activeButton != settingButton) {
                updateButtonStyle(activeButton, false);
                updateButtonStyle(settingButton, true);
                activeButton = settingButton;
                showSettingsContent();
            }
        });
    }
    
    private void showDashboardContent() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(new UserDashboardPanel(currentUser), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showBooksContent() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(new UserBooksPanel(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showSettingsContent() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(new UserSettingsPanel(currentUser), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    public void setUser(User user) {
        this.currentUser = user;
        usernameLabel.setText(user.getUsername());
        
        String role = user.getRole();
        role = role.substring(0, 1).toUpperCase() + role.substring(1);
        roleLabel.setText("Role: " + role);
        
        showDashboardContent();
    }
    
    public void setLogoutListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }
    
    public void maximizeIfNeeded(boolean shouldMaximize) {
        if (shouldMaximize) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }
} 