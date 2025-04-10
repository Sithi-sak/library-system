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

public class DashboardPanel extends JPanel {
    
    private JPanel statsPanel;
    private JPanel quickActionsPanel;
    private JPanel recentPanel;
    private JPanel alertsPanel;
    
    private JButton addBookButton;
    private JButton addUserButton;
    private JButton viewOverdueButton;
    
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
        
        initializePanels();
        
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.65;
        gbc.weighty = 0.45;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 8, 8);
        mainPanel.add(statsPanel, gbc);
        
        JPanel rightPanel = new JPanel(new BorderLayout(0, 8)); // 8px gap between components
        rightPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        
        rightPanel.add(quickActionsPanel, BorderLayout.NORTH);
        
        rightPanel.add(alertsPanel, BorderLayout.CENTER);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0.35;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 8, 0, 0);
        mainPanel.add(rightPanel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.65;
        gbc.weighty = 0.55;
        gbc.insets = new Insets(8, 0, 0, 8);
        mainPanel.add(recentPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        updateDashboardData();
    }
    
    private void initializePanels() {
        statsPanel = createStatsPanel();
        
        quickActionsPanel = createQuickActionsPanel();
        
        recentPanel = createRecentActivityPanel();
        
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
        
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
        buttonContainer.setBackground(Color.WHITE);
        
        addBookButton = new JButton("Add New Book");
        styleActionButton(addBookButton);
        addBookButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, addBookButton.getPreferredSize().height));
        
        viewOverdueButton = new JButton("View Overdue Books");
        styleActionButton(viewOverdueButton);
        viewOverdueButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, viewOverdueButton.getPreferredSize().height));
        
        buttonContainer.add(addBookButton);
        buttonContainer.add(Box.createVerticalStrut(6));
        buttonContainer.add(viewOverdueButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(buttonContainer, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void styleActionButton(JButton button) {
        LaravelTheme.styleSecondaryButton(button);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Inter", Font.BOLD, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
            button.getBorder(),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
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
        
        JPanel outerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        outerPanel.setBackground(Color.WHITE);
        
        JPanel alertsListPanel = new JPanel();
        alertsListPanel.setLayout(new BoxLayout(alertsListPanel, BoxLayout.Y_AXIS));
        alertsListPanel.setBackground(Color.WHITE);
        
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
    
    public void setAddBookButtonListener(ActionListener listener) {
        addBookButton.addActionListener(listener);
    }
    
    public void setAddUserButtonListener(ActionListener listener) {
        addUserButton.addActionListener(listener);
    }
    
    public void setViewOverdueButtonListener(ActionListener listener) {
        viewOverdueButton.addActionListener(listener);
    }
    
    public void updateDashboardData() {
        UserDAO userDAO = new UserDAO();
        BookDAO bookDAO = new BookDAO();
        
        int totalMembers = userDAO.countUsersByRole("member");
        totalMembersValueLabel.setText(String.valueOf(totalMembers));
        
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
        
        updateRecentActivity(bookDAO);
    }
    
    private void updateRecentActivity(BookDAO bookDAO) {
        List<Map<String, String>> activities = bookDAO.getRecentActivity(5);
        
        String[] columnNames = {"Type", "Description", "Date"};
        Object[][] data = new Object[activities.size()][3];
        
        for (int i = 0; i < activities.size(); i++) {
            Map<String, String> activity = activities.get(i);
            data[i][0] = activity.get("type");
            data[i][1] = activity.get("description");
            data[i][2] = activity.get("date");
        }
        
        if (activities.isEmpty()) {
            data = new Object[][]{{"", "No recent activity", ""}};
        }
        
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
        totalMembersValueLabel.setText("0");
    }

    private void updateTotalBooks() {
        totalBooksValueLabel.setText("0");
    }

    private void updateBorrowedBooks() {
        borrowedBooksValueLabel.setText("0");
    }

    private void updateOverdueBooks() {
        overdueBooksValueLabel.setText("0");
    }

    private void updateAvailableBooks() {
        availableBooksValueLabel.setText("0");
    }

    private void updateNewBooks() {
        newBooksValueLabel.setText("0");
    }

    private void updateRecentActivity() {
    }

    private void updateAlerts() {
    }
} 