package org.lkw.view.admin;

import org.lkw.data.dao.UserDAO;
import org.lkw.data.util.LaravelTheme;
import org.lkw.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.text.ParseException;

/**
 * UsersPanel for admin to view and manage registered users
 */
public class UsersPanel extends JPanel {
    
    // UI Components
    private JPanel userListPanel;
    private JPanel userDetailsPanel;
    private JTable usersTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private JList<String> activityList;
    
    // User action buttons
    private JButton toggleStatusButton;
    private JButton resetPasswordButton;
    
    // Details panel components
    private JLabel userNameValueLabel;
    private JLabel userEmailValueLabel;
    private JLabel userRoleValueLabel;
    private JLabel userStatusValueLabel;
    private JLabel userRegisteredValueLabel;
    private JLabel userLastLoginValueLabel;
    
    // User statistics labels
    private JLabel totalBorrowedLabel;
    private JLabel currentlyBorrowedLabel;
    private JLabel overdueLabel;
    private JLabel totalTransactionsLabel;
    
    // Data access
    private final UserDAO userDAO;
    
    // Currently selected user
    private User selectedUser;
    
    public UsersPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(LaravelTheme.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Initialize DAO
        userDAO = new UserDAO();
        
        // Initialize all panels
        initializePanels();
        
        // Top row with heading and search
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content panel with GridBagLayout for better control
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // User list panel (left side - 65%)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.65;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 8);
        mainPanel.add(userListPanel, gbc);
        
        // User details panel (right side - 35%)
        gbc.gridx = 1;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 8, 0, 0);
        mainPanel.add(userDetailsPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Load initial data
        loadUserData();
        
        // Add action listeners
        setupActionListeners();
    }
    
    private void initializePanels() {
        // User list panel (left side)
        userListPanel = createUserListPanel();
        
        // User details panel (right side)
        userDetailsPanel = createUserDetailsPanel();
    }
    
    private void setupActionListeners() {
        // Search button action
        JButton searchButton = (JButton) ((JPanel) searchField.getParent()).getComponent(2);
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim();
            if (!searchTerm.isEmpty() && !searchTerm.equals("Search")) {
                filterUsers(searchTerm);
            } else {
                loadUserData(); // Reset to show all
            }
        });
        
        // Filter combobox action
        filterComboBox.addActionListener(e -> {
            String selectedFilter = (String) filterComboBox.getSelectedItem();
            if (selectedFilter != null && !selectedFilter.equals("All Members")) {
                applyFilter(selectedFilter);
            } else {
                loadUserData(); // Reset to show all
            }
        });
        
        // Toggle status button
        toggleStatusButton.addActionListener(e -> {
            if (selectedUser != null) {
                // In a real implementation, this would update the database
                JOptionPane.showMessageDialog(this, 
                    "Status toggle functionality would update user status in the database", 
                    "Toggle Status", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Reset password button
        resetPasswordButton.addActionListener(e -> {
            if (selectedUser != null) {
                // In a real implementation, this would reset the password in the database
                JOptionPane.showMessageDialog(this, 
                    "Password reset functionality would send a reset email or generate a new password", 
                    "Reset Password", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Add table selection listener
        usersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = usersTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String username = (String) usersTable.getValueAt(selectedRow, 0);
                    selectedUser = userDAO.getUserByUsername(username);
                    updateUserDetails(selectedUser);
                } else {
                    selectedUser = null;
                    updateUserDetails(null);
                }
            }
        });
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Title section
        JLabel titleLabel = new JLabel("Member Management");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 24));
        titleLabel.setForeground(LaravelTheme.TEXT_DARK);
        panel.add(titleLabel, BorderLayout.WEST);
        
        // Search and filter section
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        
        // Filter dropdown
        filterComboBox = new JComboBox<>(new String[] {"All Members", "Active", "Inactive"});
        filterComboBox.setFont(new Font("Inter", Font.PLAIN, 13));
        filterComboBox.setPreferredSize(new Dimension(120, 30));
        
        // Search field
        searchField = new JTextField();
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY, 1, true),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setFont(new Font("Inter", Font.PLAIN, 13));
        
        JButton searchButton = new JButton("Search");
        LaravelTheme.styleSecondaryButton(searchButton);
        searchButton.setPreferredSize(new Dimension(90, 30));
        
        searchPanel.add(filterComboBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        panel.add(searchPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createUserListPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY, 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Table data model
        String[] columnNames = {"Username", "Full Name", "Role", "Status", "Registration Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        usersTable = new JTable(tableModel);
        usersTable.setRowHeight(35);
        usersTable.setFont(new Font("Inter", Font.PLAIN, 13));
        usersTable.setShowVerticalLines(false);
        usersTable.setGridColor(LaravelTheme.BORDER_GRAY);
        usersTable.getTableHeader().setFont(new Font("Inter", Font.BOLD, 13));
        usersTable.getTableHeader().setBackground(LaravelTheme.LIGHT_GRAY);
        usersTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, LaravelTheme.BORDER_GRAY));
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add mouse listener to show user details when clicked
        usersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = usersTable.getSelectedRow();
                if (selectedRow >= 0) {
                    showUserDetails(selectedRow);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, LaravelTheme.BORDER_GRAY),
            BorderFactory.createEmptyBorder(10, 0, 0, 0)
        ));
        
        toggleStatusButton = new JButton("Toggle Status");
        LaravelTheme.styleSecondaryButton(toggleStatusButton);
        
        resetPasswordButton = new JButton("Reset Password");
        LaravelTheme.styleSecondaryButton(resetPasswordButton);
        
        actionPanel.add(toggleStatusButton);
        actionPanel.add(resetPasswordButton);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createUserDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY, 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Create a top panel to hold all sections
        JPanel topPanel = new JPanel(new BorderLayout(0, 0));
        topPanel.setBackground(Color.WHITE);
        
        // User Details Section
        JPanel detailsSection = new JPanel(new BorderLayout(0, 0));
        detailsSection.setBackground(Color.WHITE);
        
        JLabel detailsTitle = new JLabel("User Details");
        detailsTitle.setFont(new Font("Inter", Font.BOLD, 16));
        detailsTitle.setForeground(LaravelTheme.TEXT_DARK);
        detailsTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        
        JPanel detailsContent = new JPanel();
        detailsContent.setLayout(new BoxLayout(detailsContent, BoxLayout.Y_AXIS));
        detailsContent.setBackground(Color.WHITE);
        
        // Initialize value labels
        userNameValueLabel = createValueLabel("Select a user to view details");
        userEmailValueLabel = createValueLabel("");
        userRoleValueLabel = createValueLabel("");
        userStatusValueLabel = createValueLabel("");
        userRegisteredValueLabel = createValueLabel("");
        userLastLoginValueLabel = createValueLabel("");
        
        // Add field rows with minimal spacing
        detailsContent.add(createDetailRow("Name:", userNameValueLabel));
        detailsContent.add(Box.createVerticalStrut(4));
        detailsContent.add(createDetailRow("Email:", userEmailValueLabel));
        detailsContent.add(Box.createVerticalStrut(4));
        detailsContent.add(createDetailRow("Role:", userRoleValueLabel));
        detailsContent.add(Box.createVerticalStrut(4));
        detailsContent.add(createDetailRow("Status:", userStatusValueLabel));
        detailsContent.add(Box.createVerticalStrut(4));
        detailsContent.add(createDetailRow("Registered:", userRegisteredValueLabel));
        detailsContent.add(Box.createVerticalStrut(4));
        detailsContent.add(createDetailRow("Last Login:", userLastLoginValueLabel));
        
        detailsSection.add(detailsTitle, BorderLayout.NORTH);
        detailsSection.add(detailsContent, BorderLayout.CENTER);
        
        // First separator
        JSeparator firstSeparator = new JSeparator();
        firstSeparator.setForeground(LaravelTheme.BORDER_GRAY);
        JPanel firstSeparatorPanel = new JPanel(new BorderLayout());
        firstSeparatorPanel.setBackground(Color.WHITE);
        firstSeparatorPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        firstSeparatorPanel.add(firstSeparator, BorderLayout.CENTER);
        
        // User Statistics Section
        JPanel statsSection = new JPanel(new BorderLayout(0, 8));
        statsSection.setBackground(Color.WHITE);
        
        JLabel statsTitle = new JLabel("User Statistics");
        statsTitle.setFont(new Font("Inter", Font.BOLD, 16));
        statsTitle.setForeground(LaravelTheme.TEXT_DARK);
        
        // Stats grid
        JPanel statsGrid = new JPanel(new GridLayout(2, 2, 8, 8));
        statsGrid.setBackground(Color.WHITE);
        
        // Add stat cards directly
        statsGrid.add(createStatCard("Total Borrowed", "0"));
        statsGrid.add(createStatCard("Currently Borrowed", "0"));
        statsGrid.add(createStatCard("Overdue Books", "0"));
        statsGrid.add(createStatCard("Total Transactions", "0"));
        
        statsSection.add(statsTitle, BorderLayout.NORTH);
        statsSection.add(statsGrid, BorderLayout.CENTER);
        
        // Second separator
        JSeparator secondSeparator = new JSeparator();
        secondSeparator.setForeground(LaravelTheme.BORDER_GRAY);
        JPanel secondSeparatorPanel = new JPanel(new BorderLayout());
        secondSeparatorPanel.setBackground(Color.WHITE);
        secondSeparatorPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        secondSeparatorPanel.add(secondSeparator, BorderLayout.CENTER);
        
        // Recent Activity Section
        JPanel activityPanel = new JPanel(new BorderLayout(0, 8));
        activityPanel.setBackground(Color.WHITE);
        
        JLabel activityTitle = new JLabel("Recent Activity");
        activityTitle.setFont(new Font("Inter", Font.BOLD, 16));
        activityTitle.setForeground(LaravelTheme.TEXT_DARK);
        
        DefaultListModel<String> activityModel = new DefaultListModel<>();
        activityModel.addElement("No activity data available");
        
        activityList = new JList<>(activityModel);
        activityList.setFont(new Font("Inter", Font.PLAIN, 13));
        activityList.setFixedCellHeight(30);
        
        JScrollPane activityScroll = new JScrollPane(activityList);
        activityScroll.setBorder(BorderFactory.createEmptyBorder());
        
        activityPanel.add(activityTitle, BorderLayout.NORTH);
        activityPanel.add(activityScroll, BorderLayout.CENTER);
        
        // Add all sections to the top panel
        JPanel sectionsPanel = new JPanel();
        sectionsPanel.setLayout(new BoxLayout(sectionsPanel, BoxLayout.Y_AXIS));
        sectionsPanel.setBackground(Color.WHITE);
        
        sectionsPanel.add(detailsSection);
        sectionsPanel.add(firstSeparatorPanel);
        sectionsPanel.add(statsSection);
        sectionsPanel.add(secondSeparatorPanel);
        
        // Main panel layout
        panel.add(sectionsPanel, BorderLayout.NORTH);
        panel.add(activityPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createDetailRow(String label, JLabel valueLabel) {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setBackground(Color.WHITE);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Inter", Font.BOLD, 13));
        labelComponent.setForeground(LaravelTheme.TEXT_DARK);
        labelComponent.setPreferredSize(new Dimension(80, labelComponent.getPreferredSize().height));
        
        // Create a fixed-width panel for the value
        JPanel valuePanel = new JPanel(new BorderLayout());
        valuePanel.setBackground(Color.WHITE);
        valuePanel.setPreferredSize(new Dimension(200, valueLabel.getPreferredSize().height));
        valuePanel.add(valueLabel, BorderLayout.WEST); // Align text to the left
        
        panel.add(labelComponent, BorderLayout.WEST);
        panel.add(valuePanel, BorderLayout.CENTER);
        
        // Fix the height of the row
        int height = Math.max(labelComponent.getPreferredSize().height, valueLabel.getPreferredSize().height);
        panel.setPreferredSize(new Dimension(panel.getPreferredSize().width, height));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        
        return panel;
    }
    
    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Inter", Font.PLAIN, 13));
        label.setForeground(LaravelTheme.TEXT_DARK);
        // Fix the size of the label
        Dimension preferredSize = label.getPreferredSize();
        label.setPreferredSize(new Dimension(200, preferredSize.height));
        label.setMinimumSize(new Dimension(0, preferredSize.height));
        label.setMaximumSize(new Dimension(200, preferredSize.height));
        return label;
    }
    
    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY, 1, true),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 12));
        titleLabel.setForeground(LaravelTheme.TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Inter", Font.BOLD, 20));
        valueLabel.setForeground(LaravelTheme.PRIMARY_RED);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        
        // Store reference to value label
        if (title.equals("Total Borrowed")) {
            totalBorrowedLabel = valueLabel;
        } else if (title.equals("Currently Borrowed")) {
            currentlyBorrowedLabel = valueLabel;
        } else if (title.equals("Overdue Books")) {
            overdueLabel = valueLabel;
        } else if (title.equals("Total Transactions")) {
            totalTransactionsLabel = valueLabel;
        }
        
        card.add(titleLabel);
        card.add(valueLabel);
        
        return card;
    }
    
    private void showUserDetails(int selectedRow) {
        String username = (String) tableModel.getValueAt(selectedRow, 0);
        
        // Fetch the user list and find the selected user
        List<User> users = userDAO.getAllUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                selectedUser = user;
                
                // Update details panel with real data
                userNameValueLabel.setText(user.getFullName() + " (" + user.getUsername() + ")");
                userEmailValueLabel.setText(user.getEmail() != null ? user.getEmail() : "N/A");
                
                // Capitalize first letter of role
                String role = user.getRole();
                role = role.substring(0, 1).toUpperCase() + role.substring(1);
                userRoleValueLabel.setText(role);
                
                // Capitalize first letter of status
                String status = user.getMemberStatus();
                status = status.substring(0, 1).toUpperCase() + status.substring(1);
                userStatusValueLabel.setText(status);
                
                // Format dates nicely
                userRegisteredValueLabel.setText(formatDate(user.getJoinDate()));
                userLastLoginValueLabel.setText(user.getLastLogin() != null ? formatDate(user.getLastLogin()) : "Never");
                
                // Update button text based on status
                toggleStatusButton.setText(status.equalsIgnoreCase("Active") ? "Deactivate User" : "Activate User");
                
                // Set all statistics to 0 since borrowing is not implemented yet
                totalBorrowedLabel.setText("0");
                currentlyBorrowedLabel.setText("0");
                overdueLabel.setText("0");
                totalTransactionsLabel.setText("0");
                
                break;
            }
        }
    }
    
    // Format SQL datetime to a more readable format
    private String formatDate(String sqlDate) {
        if (sqlDate == null || sqlDate.isEmpty()) {
            return "N/A";
        }
        
        try {
            // Parse the SQL date string
            SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sqlFormat.parse(sqlDate);
            
            // Format to a nice readable date
            SimpleDateFormat niceFormat = new SimpleDateFormat("MMM d, yyyy 'at' h:mm a");
            return niceFormat.format(date);
        } catch (ParseException e) {
            // If there's an error parsing, just return the original
            return sqlDate;
        }
    }
    
    // Load user data from the database
    private void loadUserData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get all non-admin users from the database
        List<User> users = userDAO.getNonAdminUsers();
        
        // Add each user to the table
        for (User user : users) {
            String username = user.getUsername();
            String fullName = user.getFullName();
            
            // Capitalize first letter of role
            String role = user.getRole();
            role = role.substring(0, 1).toUpperCase() + role.substring(1);
            
            // Capitalize first letter of status
            String status = user.getMemberStatus();
            status = status.substring(0, 1).toUpperCase() + status.substring(1);
            
            // Format the registration date
            String regDate = formatDate(user.getJoinDate());
            
            addUserRow(username, fullName, role, status, regDate);
        }
    }
    
    private void filterUsers(String searchTerm) {
        // Clear table
        tableModel.setRowCount(0);
        
        // Get all non-admin users
        List<User> users = userDAO.getNonAdminUsers();
        searchTerm = searchTerm.toLowerCase();
        
        // Add matching users to the table
        for (User user : users) {
            if (user.getUsername().toLowerCase().contains(searchTerm) || 
                user.getFullName().toLowerCase().contains(searchTerm) ||
                user.getEmail().toLowerCase().contains(searchTerm)) {
                
                String username = user.getUsername();
                String fullName = user.getFullName();
                
                // Capitalize first letter of role
                String role = user.getRole();
                role = role.substring(0, 1).toUpperCase() + role.substring(1);
                
                // Capitalize first letter of status
                String status = user.getMemberStatus();
                status = status.substring(0, 1).toUpperCase() + status.substring(1);
                
                // Format the registration date
                String regDate = formatDate(user.getJoinDate());
                
                addUserRow(username, fullName, role, status, regDate);
            }
        }
    }
    
    private void applyFilter(String filter) {
        // Clear table
        tableModel.setRowCount(0);
        
        // Get all non-admin users
        List<User> users = userDAO.getNonAdminUsers();
        
        // Add matching users to the table
        for (User user : users) {
            boolean shouldAdd = false;
            
            // Check if user matches the filter
            if (filter.equalsIgnoreCase("Active") && user.getMemberStatus().equalsIgnoreCase("active")) {
                shouldAdd = true;
            } else if (filter.equalsIgnoreCase("Inactive") && user.getMemberStatus().equalsIgnoreCase("inactive")) {
                shouldAdd = true;
            }
            
            if (shouldAdd) {
                String username = user.getUsername();
                String fullName = user.getFullName();
                
                // Capitalize first letter of role
                String role = user.getRole();
                role = role.substring(0, 1).toUpperCase() + role.substring(1);
                
                // Capitalize first letter of status
                String status = user.getMemberStatus();
                status = status.substring(0, 1).toUpperCase() + status.substring(1);
                
                // Format the registration date
                String regDate = formatDate(user.getJoinDate());
                
                addUserRow(username, fullName, role, status, regDate);
            }
        }
    }
    
    private void addUserRow(String username, String fullName, String role, String status, String regDate) {
        tableModel.addRow(new Object[]{username, fullName, role, status, regDate});
    }
    
    // Method to refresh data (would be called when data changes)
    public void refreshData() {
        loadUserData();
    }

    private void updateUserDetails(User user) {
        if (user != null) {
            // Update basic user information
            userNameValueLabel.setText(user.getUsername() + " (" + user.getFullName() + ")");
            userEmailValueLabel.setText(user.getEmail());
            userRoleValueLabel.setText(user.getRole());
            userStatusValueLabel.setText(user.getMemberStatus());
            userRegisteredValueLabel.setText(formatDate(user.getJoinDate()));
            userLastLoginValueLabel.setText(user.getLastLogin() != null ? formatDate(user.getLastLogin()) : "Never");

            // Set all statistics to 0 since borrowing is not implemented yet
            totalBorrowedLabel.setText("0");
            currentlyBorrowedLabel.setText("0");
            overdueLabel.setText("0");
            totalTransactionsLabel.setText("0");

            // Update button states
            boolean isActive = "active".equalsIgnoreCase(user.getMemberStatus());
            toggleStatusButton.setText(isActive ? "Deactivate Account" : "Activate Account");
            toggleStatusButton.setEnabled(true);
            resetPasswordButton.setEnabled(true);

            // Clear activity data
            DefaultListModel<String> model = (DefaultListModel<String>) activityList.getModel();
            model.clear();
            model.addElement("No activity data available");
        } else {
            // Clear all fields if no user is selected
            userNameValueLabel.setText("");
            userEmailValueLabel.setText("");
            userRoleValueLabel.setText("");
            userStatusValueLabel.setText("");
            userRegisteredValueLabel.setText("");
            userLastLoginValueLabel.setText("");
            
            totalBorrowedLabel.setText("0");
            currentlyBorrowedLabel.setText("0");
            overdueLabel.setText("0");
            totalTransactionsLabel.setText("0");

            // Disable buttons
            toggleStatusButton.setEnabled(false);
            resetPasswordButton.setEnabled(false);

            // Clear activity data
            DefaultListModel<String> model = (DefaultListModel<String>) activityList.getModel();
            model.clear();
        }
    }

    private void updateStatistics(User user) {
        // Set all statistics to 0 since borrowing is not implemented yet
        totalBorrowedLabel.setText("0");
        currentlyBorrowedLabel.setText("0");
        overdueLabel.setText("0");
        totalTransactionsLabel.setText("0");
    }
} 