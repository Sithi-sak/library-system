package org.lkw.view.admin;

import org.lkw.data.dao.UserDAO;
import org.lkw.data.dao.BookDAO;
import org.lkw.data.util.LaravelTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private JLabel newBooksValueLabel;
    
    public DashboardPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(LaravelTheme.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
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
        gbc.weighty = 0.45;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 8, 8);
        mainPanel.add(statsPanel, gbc);
        
        // Quick actions and Alerts - right column
        JPanel rightPanel = new JPanel(new BorderLayout(0, 8)); // 8px gap between components
        rightPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        
        // Quick actions - top right (natural height)
        rightPanel.add(quickActionsPanel, BorderLayout.NORTH);
        
        // Alerts - fills remaining space
        rightPanel.add(alertsPanel, BorderLayout.CENTER);
        
        // Add right panel to main panel
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0.35;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 8, 0, 0);
        mainPanel.add(rightPanel, gbc);
        
        // Recent activity - bottom left
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.65;
        gbc.weighty = 0.55;
        gbc.insets = new Insets(8, 0, 0, 8);
        mainPanel.add(recentPanel, gbc);
        
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
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
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
        panel.setLayout(new GridLayout(2, 3, 8, 8)); // Changed to 2x3 grid
        panel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        
        // Create stat cards in a more logical order
        panel.add(createStatCard("Total Members", "0", "users"));
        panel.add(createStatCard("Total Books", "0", "books"));
        panel.add(createStatCard("Available Books", "0", "available"));
        panel.add(createStatCard("Borrowed Books", "0", "borrowed"));
        panel.add(createStatCard("Overdue Books", "0", "overdue"));
        panel.add(createStatCard("New Books", "0", "new-books"));
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, String type) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY, 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
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
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel titleLabel = new JLabel("Quick Actions");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 16));
        titleLabel.setForeground(LaravelTheme.TEXT_DARK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        
        // Container for buttons with minimum spacing
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
        buttonContainer.setBackground(Color.WHITE);
        
        // Add book button
        addBookButton = new JButton("Add New Book");
        styleActionButton(addBookButton);
        addBookButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, addBookButton.getPreferredSize().height));
        
        // View overdue button
        viewOverdueButton = new JButton("View Overdue Books");
        styleActionButton(viewOverdueButton);
        viewOverdueButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, viewOverdueButton.getPreferredSize().height));
        
        // Add buttons to container with minimal spacing
        buttonContainer.add(addBookButton);
        buttonContainer.add(Box.createVerticalStrut(6)); // Reduced spacing between buttons
        buttonContainer.add(viewOverdueButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(buttonContainer, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void styleActionButton(JButton button) {
        LaravelTheme.styleSecondaryButton(button);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Inter", Font.BOLD, 14));
        // Make the button more compact
        button.setBorder(BorderFactory.createCompoundBorder(
            button.getBorder(),
            BorderFactory.createEmptyBorder(4, 12, 4, 12) // Reduced vertical padding
        ));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
    
    private JPanel createRecentActivityPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY, 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel titleLabel = new JLabel("Recent Activity");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 16));
        titleLabel.setForeground(LaravelTheme.TEXT_DARK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        
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
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel titleLabel = new JLabel("Alerts");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 16));
        titleLabel.setForeground(LaravelTheme.TEXT_DARK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        
        // Use FlowLayout to align items to the left
        JPanel outerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        outerPanel.setBackground(Color.WHITE);
        
        JPanel alertsListPanel = new JPanel();
        alertsListPanel.setLayout(new BoxLayout(alertsListPanel, BoxLayout.Y_AXIS));
        alertsListPanel.setBackground(Color.WHITE);
        
        // Example alerts with alignment wrappers
        JPanel alert1 = createAlertItem("5 books are overdue", "warning");
        JPanel alert1Wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        alert1Wrapper.setBackground(Color.WHITE);
        alert1Wrapper.add(alert1);
        
        JPanel alert2 = createAlertItem("3 new member registrations pending approval", "info");
        JPanel alert2Wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        alert2Wrapper.setBackground(Color.WHITE);
        alert2Wrapper.add(alert2);
        
        JPanel alert3 = createAlertItem("Low inventory alert: 2 books with no copies left", "danger");
        JPanel alert3Wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        alert3Wrapper.setBackground(Color.WHITE);
        alert3Wrapper.add(alert3);
        
        // Add alerts to the vertical panel
        alertsListPanel.add(alert1Wrapper);
        alertsListPanel.add(Box.createVerticalStrut(8));
        alertsListPanel.add(alert2Wrapper);
        alertsListPanel.add(Box.createVerticalStrut(8));
        alertsListPanel.add(alert3Wrapper);
        
        outerPanel.add(alertsListPanel);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(outerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAlertItem(String message, String type) {
        JPanel alertPanel = new JPanel(new BorderLayout(6, 0));
        alertPanel.setBackground(Color.WHITE);
        
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
        BookDAO bookDAO = new BookDAO();
        
        // Count members (only users with role 'member')
        int totalMembers = userDAO.countUsersByRole("member");
        totalMembersValueLabel.setText(String.valueOf(totalMembers));
        
        // Get book statistics
        int totalBooks = bookDAO.getTotalBooks();
        int borrowedBooks = bookDAO.getBorrowedBooks();
        int overdueBooks = bookDAO.getOverdueBooks();
        int availableBooks = bookDAO.getAvailableBooks();
        int newBooks = bookDAO.getNewBooks();
        
        totalBooksValueLabel.setText(String.valueOf(totalBooks));
        borrowedBooksValueLabel.setText(String.valueOf(borrowedBooks));
        overdueBooksValueLabel.setText(String.valueOf(overdueBooks));
        availableBooksValueLabel.setText(String.valueOf(availableBooks));
        newBooksValueLabel.setText(String.valueOf(newBooks));
        
        // Update recent activity
        updateRecentActivity(bookDAO);
    }
    
    private void updateRecentActivity(BookDAO bookDAO) {
        // Get recent activities
        List<Map<String, String>> activities = bookDAO.getRecentActivity(5);
        
        // Create table model for recent activities
        String[] columnNames = {"Type", "Description", "Date"};
        Object[][] data = new Object[activities.size()][3];
        
        for (int i = 0; i < activities.size(); i++) {
            Map<String, String> activity = activities.get(i);
            data[i][0] = activity.get("type");
            data[i][1] = activity.get("description");
            data[i][2] = activity.get("date");
        }
        
        // If no activities, show a default message
        if (activities.isEmpty()) {
            data = new Object[][]{{"", "No recent activity", ""}};
        }
        
        // Find the recent activity table and update its model
        Component[] components = recentPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) component;
                if (scrollPane.getViewport().getView() instanceof JTable) {
                    JTable table = (JTable) scrollPane.getViewport().getView();
                    table.setModel(new DefaultTableModel(data, columnNames));
                    break;
                }
            }
        }
    }

    public void refreshData() {
        // Refresh all dashboard statistics and data
        updateTotalMembers();
        updateTotalBooks();
        updateBorrowedBooks();
        updateOverdueBooks();
        updateAvailableBooks();
        updateNewBooks();
        updateRecentActivity();
        updateAlerts();
    }

    private void updateTotalMembers() {
        // TODO: Implement total members update
        totalMembersValueLabel.setText("0");
    }

    private void updateTotalBooks() {
        // TODO: Implement total books update
        totalBooksValueLabel.setText("0");
    }

    private void updateBorrowedBooks() {
        // TODO: Implement borrowed books update
        borrowedBooksValueLabel.setText("0");
    }

    private void updateOverdueBooks() {
        // TODO: Implement overdue books update
        overdueBooksValueLabel.setText("0");
    }

    private void updateAvailableBooks() {
        // TODO: Implement available books update
        availableBooksValueLabel.setText("0");
    }

    private void updateNewBooks() {
        // TODO: Implement new books update
        newBooksValueLabel.setText("0");
    }

    private void updateRecentActivity() {
        // TODO: Implement recent activity update
    }

    private void updateAlerts() {
        // TODO: Implement alerts update
    }
} 