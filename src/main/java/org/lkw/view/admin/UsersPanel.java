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
    
    // User action buttons
    private JButton viewDetailsButton;
    private JButton toggleStatusButton;
    private JButton resetPasswordButton;
    
    // Details panel components
    private JLabel userNameValueLabel;
    private JLabel userEmailValueLabel;
    private JLabel userRoleValueLabel;
    private JLabel userStatusValueLabel;
    private JLabel userRegisteredValueLabel;
    private JLabel userLastLoginValueLabel;
    
    // Data access
    private final UserDAO userDAO;
    
    // Currently selected user
    private User selectedUser;
    
    public UsersPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(LaravelTheme.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Initialize DAO
        userDAO = new UserDAO();
        
        // Initialize all panels
        initializePanels();
        
        // Top row with heading and search
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content panel - split view
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, userListPanel, userDetailsPanel);
        splitPane.setDividerLocation(700);
        splitPane.setResizeWeight(0.7);
        splitPane.setBorder(null);
        
        add(splitPane, BorderLayout.CENTER);
        
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
        
        // View details button
        viewDetailsButton.addActionListener(e -> {
            int selectedRow = usersTable.getSelectedRow();
            if (selectedRow >= 0) {
                showUserDetails(selectedRow);
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
        searchButton.setPreferredSize(new Dimension(80, 30));
        
        searchPanel.add(filterComboBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        panel.add(searchPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createUserListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY, 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Table data model
        String[] columnNames = {"Username", "Full Name", "Role", "Status", "Registration Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
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
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, LaravelTheme.BORDER_GRAY));
        
        viewDetailsButton = new JButton("View Details");
        LaravelTheme.styleSecondaryButton(viewDetailsButton);
        
        toggleStatusButton = new JButton("Toggle Status");
        LaravelTheme.styleSecondaryButton(toggleStatusButton);
        
        resetPasswordButton = new JButton("Reset Password");
        LaravelTheme.styleSecondaryButton(resetPasswordButton);
        
        actionPanel.add(viewDetailsButton);
        actionPanel.add(toggleStatusButton);
        actionPanel.add(resetPasswordButton);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createUserDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY, 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Header
        JLabel detailsTitle = new JLabel("User Details");
        detailsTitle.setFont(new Font("Inter", Font.BOLD, 18));
        detailsTitle.setForeground(LaravelTheme.TEXT_DARK);
        detailsTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Details panel
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
        
        // Add field rows
        detailsContent.add(createDetailRow("Name:", userNameValueLabel));
        detailsContent.add(Box.createVerticalStrut(10));
        detailsContent.add(createDetailRow("Email:", userEmailValueLabel));
        detailsContent.add(Box.createVerticalStrut(10));
        detailsContent.add(createDetailRow("Role:", userRoleValueLabel));
        detailsContent.add(Box.createVerticalStrut(10));
        detailsContent.add(createDetailRow("Status:", userStatusValueLabel));
        detailsContent.add(Box.createVerticalStrut(10));
        detailsContent.add(createDetailRow("Registered:", userRegisteredValueLabel));
        detailsContent.add(Box.createVerticalStrut(10));
        detailsContent.add(createDetailRow("Last Login:", userLastLoginValueLabel));
        
        // User activity section
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBackground(Color.WHITE);
        activityPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, LaravelTheme.BORDER_GRAY),
            BorderFactory.createEmptyBorder(15, 0, 0, 0)
        ));
        
        JLabel activityTitle = new JLabel("Recent Activity");
        activityTitle.setFont(new Font("Inter", Font.BOLD, 16));
        activityTitle.setForeground(LaravelTheme.TEXT_DARK);
        activityTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        DefaultListModel<String> activityModel = new DefaultListModel<>();
        activityModel.addElement("No activity data available");
        
        JList<String> activityList = new JList<>(activityModel);
        activityList.setFont(new Font("Inter", Font.PLAIN, 13));
        activityList.setFixedCellHeight(30);
        
        JScrollPane activityScroll = new JScrollPane(activityList);
        activityScroll.setBorder(BorderFactory.createEmptyBorder());
        
        activityPanel.add(activityTitle, BorderLayout.NORTH);
        activityPanel.add(activityScroll, BorderLayout.CENTER);
        
        panel.add(detailsTitle, BorderLayout.NORTH);
        panel.add(detailsContent, BorderLayout.CENTER);
        panel.add(activityPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createDetailRow(String label, JLabel valueLabel) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Inter", Font.BOLD, 13));
        labelComponent.setForeground(LaravelTheme.TEXT_DARK);
        labelComponent.setPreferredSize(new Dimension(100, labelComponent.getPreferredSize().height));
        
        panel.add(labelComponent, BorderLayout.WEST);
        panel.add(valueLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Inter", Font.PLAIN, 13));
        label.setForeground(LaravelTheme.TEXT_DARK);
        return label;
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
} 