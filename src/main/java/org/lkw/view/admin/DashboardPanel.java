package org.lkw.view.admin;

import org.lkw.data.dao.UserDAO;
import org.lkw.data.util.LaravelTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Dashboard panel for admin users displaying library statistics and quick actions
 */
public class DashboardPanel extends JPanel {
    
    // UI Components
    private JPanel statsPanel;
    private JPanel quickActionsPanel;
    private JPanel recentPanel;
    private JPanel alertsPanel;
    
    // Action buttons
    private JButton addBookButton;
    private JButton addUserButton;
    private JButton viewOverdueButton;
    
    // Stats labels
    private JLabel totalMembersValueLabel;
    private JLabel totalBooksValueLabel;
    private JLabel borrowedBooksValueLabel;
    private JLabel overdueBooksValueLabel;
    private JLabel availableBooksValueLabel;
    private JLabel newMembersValueLabel;
    private JLabel newBooksValueLabel;
    
    public DashboardPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(LaravelTheme.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Initialize all panels
        initializePanels();
        
        // Top row with heading and date
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Statistics cards - top left
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.65;
        gbc.weighty = 0.6;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 10, 10);
        mainPanel.add(statsPanel, gbc);
        
        // Quick actions - top right
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.35;
        gbc.weighty = 0.6;
        gbc.insets = new Insets(0, 10, 10, 0);
        mainPanel.add(quickActionsPanel, gbc);
        
        // Recent activity - bottom left
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.65;
        gbc.weighty = 0.4;
        gbc.insets = new Insets(10, 0, 0, 10);
        mainPanel.add(recentPanel, gbc);
        
        // Alerts - bottom right
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.35;
        gbc.weighty = 0.4;
        gbc.insets = new Insets(10, 10, 0, 0);
        mainPanel.add(alertsPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Load initial data
        updateDashboardData();
    }
    
    private void initializePanels() {
        // Initialize the statistics panel
        statsPanel = createStatsPanel();
        
        // Initialize the quick actions panel
        quickActionsPanel = createQuickActionsPanel();
        
        // Initialize the recent activity panel
        recentPanel = createRecentActivityPanel();
        
        // Initialize the alerts panel
        alertsPanel = createAlertsPanel();
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 24));
        titleLabel.setForeground(LaravelTheme.TEXT_DARK);
        panel.add(titleLabel, BorderLayout.WEST);
        
        // Add current date on the right
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy");
        JLabel dateLabel = new JLabel(dateFormat.format(new Date()));
        dateLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        dateLabel.setForeground(LaravelTheme.MUTED_TEXT);
        panel.add(dateLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3, 15, 15));
        panel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        
        // Create stat cards
        panel.add(createStatCard("Total Members", "0", "users"));
        panel.add(createStatCard("Total Books", "0", "books"));
        panel.add(createStatCard("Borrowed Books", "0", "borrowed"));
        panel.add(createStatCard("Overdue Books", "0", "overdue"));
        panel.add(createStatCard("Available Books", "0", "available"));
        panel.add(createStatCard("New Members", "0", "new-users"));
        panel.add(createStatCard("New Books", "0", "new-books"));
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, String type) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY, 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 14));
        titleLabel.setForeground(LaravelTheme.TEXT_DARK);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Inter", Font.BOLD, 24));
        valueLabel.setForeground(LaravelTheme.PRIMARY_RED);
        
        // Store reference to value labels
        if ("users".equals(type)) {
            totalMembersValueLabel = valueLabel;
        } else if ("books".equals(type)) {
            totalBooksValueLabel = valueLabel;
        } else if ("borrowed".equals(type)) {
            borrowedBooksValueLabel = valueLabel;
        } else if ("overdue".equals(type)) {
            overdueBooksValueLabel = valueLabel;
        } else if ("available".equals(type)) {
            availableBooksValueLabel = valueLabel;
        } else if ("new-users".equals(type)) {
            newMembersValueLabel = valueLabel;
        } else if ("new-books".equals(type)) {
            newBooksValueLabel = valueLabel;
        }
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY, 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel("Quick Actions");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 16));
        titleLabel.setForeground(LaravelTheme.TEXT_DARK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(2, 1, 0, 10));
        buttonsPanel.setBackground(Color.WHITE);
        
        // Add book button
        addBookButton = new JButton("Add New Book");
        styleActionButton(addBookButton);
        
        // View overdue button
        viewOverdueButton = new JButton("View Overdue Books");
        styleActionButton(viewOverdueButton);
        
        buttonsPanel.add(addBookButton);
        buttonsPanel.add(viewOverdueButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(buttonsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void styleActionButton(JButton button) {
        LaravelTheme.styleSecondaryButton(button);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Inter", Font.BOLD, 14));
    }
    
    private JPanel createRecentActivityPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY, 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel("Recent Activity");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 16));
        titleLabel.setForeground(LaravelTheme.TEXT_DARK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Create a model for recent activities
        String[] columnNames = {"Type", "Description", "Date"};
        Object[][] data = {
            {"Book", "New book added: 'Java Programming'", "Today"},
            {"User", "New user registered: 'John Doe'", "Yesterday"},
            {"Transaction", "Book borrowed: 'Design Patterns'", "Yesterday"},
            {"Transaction", "Book returned: 'Clean Code'", "Apr 10, 2023"}
        };
        
        JTable recentTable = new JTable(data, columnNames);
        recentTable.setFont(new Font("Inter", Font.PLAIN, 13));
        recentTable.setRowHeight(25);
        recentTable.getTableHeader().setFont(new Font("Inter", Font.BOLD, 13));
        recentTable.setShowGrid(false);
        recentTable.setIntercellSpacing(new Dimension(0, 0));
        
        JScrollPane scrollPane = new JScrollPane(recentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAlertsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY, 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel("Alerts");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 16));
        titleLabel.setForeground(LaravelTheme.TEXT_DARK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JPanel alertsListPanel = new JPanel();
        alertsListPanel.setLayout(new BoxLayout(alertsListPanel, BoxLayout.Y_AXIS));
        alertsListPanel.setBackground(Color.WHITE);
        
        // Example alerts
        alertsListPanel.add(createAlertItem("5 books are overdue", "warning"));
        alertsListPanel.add(createAlertItem("3 new member registrations pending approval", "info"));
        alertsListPanel.add(createAlertItem("Low inventory alert: 2 books with no copies left", "danger"));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(alertsListPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAlertItem(String message, String type) {
        JPanel alertPanel = new JPanel(new BorderLayout(10, 0));
        alertPanel.setBackground(Color.WHITE);
        alertPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        JLabel iconLabel = new JLabel("•");
        iconLabel.setFont(new Font("Inter", Font.BOLD, 18));
        
        // Set color based on alert type
        if ("warning".equals(type)) {
            iconLabel.setForeground(new Color(255, 152, 0)); // Orange
        } else if ("danger".equals(type)) {
            iconLabel.setForeground(LaravelTheme.PRIMARY_RED); // Red
        } else {
            iconLabel.setForeground(new Color(33, 150, 243)); // Blue
        }
        
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Inter", Font.PLAIN, 13));
        messageLabel.setForeground(LaravelTheme.TEXT_DARK);
        
        alertPanel.add(iconLabel, BorderLayout.WEST);
        alertPanel.add(messageLabel, BorderLayout.CENTER);
        
        return alertPanel;
    }
    
    // Set action listeners for the buttons
    public void setAddBookButtonListener(ActionListener listener) {
        addBookButton.addActionListener(listener);
    }
    
    public void setAddUserButtonListener(ActionListener listener) {
        addUserButton.addActionListener(listener);
    }
    
    public void setViewOverdueButtonListener(ActionListener listener) {
        viewOverdueButton.addActionListener(listener);
    }
    
    // Update the dashboard with real data
    public void updateDashboardData() {
        // Get real data from the database
        UserDAO userDAO = new UserDAO();
        
        // Count members (only users with role 'member')
        int totalMembers = userDAO.countUsersByRole("member");
        totalMembersValueLabel.setText(String.valueOf(totalMembers));
        
        // Count new members in the last 30 days
        int newMembers = userDAO.countNewMembersInLastDays(30);
        newMembersValueLabel.setText(String.valueOf(newMembers));
        
        // For now, using dummy data for book-related statistics
        // These would be replaced with real data from BookDAO in a complete implementation
        totalBooksValueLabel.setText("213");
        borrowedBooksValueLabel.setText("78");
        overdueBooksValueLabel.setText("5");
        availableBooksValueLabel.setText("135");
        newBooksValueLabel.setText("15");
    }
} 