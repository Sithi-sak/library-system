package org.lkw.view.admin;

import org.lkw.data.util.LaravelTheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionsPanel extends JPanel {
    private final JTable transactionsTable;
    private final DefaultTableModel tableModel;
    private final JTextField searchField;
    private final JComboBox<String> typeFilter;
    private final JComboBox<String> statusFilter;
    private final JComboBox<String> dateFilter;
    private JPanel detailsPanel;
    
    // Date format for displaying timestamps
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
    
    public TransactionsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(LaravelTheme.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Initialize components
        searchField = new JTextField(20);
        typeFilter = new JComboBox<>(new String[]{"All Types", "Borrow", "Return", "Fine Payment"});
        statusFilter = new JComboBox<>(new String[]{"All Status", "Completed", "Pending", "Overdue"});
        dateFilter = new JComboBox<>(new String[]{"All Time", "Today", "This Week", "This Month", "Custom Range..."});
        
        // Add header section
        add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Create split pane for table and details
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.7); // Give more weight to the table
        
        // Create table
        tableModel = new DefaultTableModel(
            new String[]{"Transaction ID", "Book ID", "User ID", "Borrow Date", "Due Date", "Return Date", "Status", "Fine Amount"},
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        transactionsTable = new JTable(tableModel);
        setupTable();
        
        // Add table to left side of split pane
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY));
        
        JScrollPane scrollPane = new JScrollPane(transactionsTable);
        scrollPane.setBorder(null);
        tablePanel.add(scrollPane);
        
        splitPane.setLeftComponent(tablePanel);
        
        // Create and add details panel to right side
        detailsPanel = createDetailsPanel();
        splitPane.setRightComponent(detailsPanel);
        
        // Add split pane to main panel
        add(splitPane, BorderLayout.CENTER);
        
        // Load initial data
        loadTransactions();
        
        // Add table selection listener
        transactionsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = transactionsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    updateDetailsPanel(selectedRow);
                }
            }
        });
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 10));
        panel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        
        // Title
        JLabel titleLabel = new JLabel("Transaction History");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 24));
        titleLabel.setForeground(LaravelTheme.TEXT_DARK);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Filters panel
        JPanel filtersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filtersPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        
        // Style components
        LaravelTheme.styleTextField(searchField);
        searchField.setPreferredSize(new Dimension(200, 35));
        searchField.putClientProperty("JTextField.placeholderText", "Search transactions...");
        
        LaravelTheme.styleComboBox(typeFilter);
        LaravelTheme.styleComboBox(statusFilter);
        LaravelTheme.styleComboBox(dateFilter);
        
        // Add filter components
        filtersPanel.add(searchField);
        filtersPanel.add(typeFilter);
        filtersPanel.add(statusFilter);
        filtersPanel.add(dateFilter);
        
        // Add export button
        JButton exportButton = new JButton("Export");
        LaravelTheme.styleSecondaryButton(exportButton);
        filtersPanel.add(exportButton);
        
        panel.add(filtersPanel, BorderLayout.CENTER);
        
        // Add bottom border
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, LaravelTheme.BORDER_GRAY),
            new EmptyBorder(0, 0, 10, 0)
        ));
        
        return panel;
    }
    
    private void setupTable() {
        transactionsTable.setRowHeight(40);
        transactionsTable.setShowGrid(true);
        transactionsTable.setGridColor(LaravelTheme.BORDER_GRAY);
        transactionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Style header
        transactionsTable.getTableHeader().setBackground(Color.WHITE);
        transactionsTable.getTableHeader().setFont(new Font("Inter", Font.BOLD, 13));
        transactionsTable.setFont(new Font("Inter", Font.PLAIN, 13));
        
        // Set column widths
        transactionsTable.getColumnModel().getColumn(0).setPreferredWidth(100);  // Transaction ID
        transactionsTable.getColumnModel().getColumn(1).setPreferredWidth(80);   // Book ID
        transactionsTable.getColumnModel().getColumn(2).setPreferredWidth(80);   // User ID
        transactionsTable.getColumnModel().getColumn(3).setPreferredWidth(120);  // Borrow Date
        transactionsTable.getColumnModel().getColumn(4).setPreferredWidth(120);  // Due Date
        transactionsTable.getColumnModel().getColumn(5).setPreferredWidth(120);  // Return Date
        transactionsTable.getColumnModel().getColumn(6).setPreferredWidth(80);   // Status
        transactionsTable.getColumnModel().getColumn(7).setPreferredWidth(100);  // Fine Amount
    }
    
    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY));
        
        // Header
        JLabel headerLabel = new JLabel("Transaction Details");
        headerLabel.setFont(new Font("Inter", Font.BOLD, 16));
        headerLabel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.add(headerLabel, BorderLayout.NORTH);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(0, 15, 15, 15));
        
        // Add placeholder text
        JLabel placeholderLabel = new JLabel("Select a transaction to view details");
        placeholderLabel.setFont(new Font("Inter", Font.PLAIN, 13));
        placeholderLabel.setForeground(LaravelTheme.MUTED_TEXT);
        placeholderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(placeholderLabel);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void updateDetailsPanel(int selectedRow) {
        // Clear existing components
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(0, 15, 15, 15));
        
        // Get data from selected row
        String transactionId = tableModel.getValueAt(selectedRow, 0).toString();
        String bookId = tableModel.getValueAt(selectedRow, 1).toString();
        String userId = tableModel.getValueAt(selectedRow, 2).toString();
        String borrowDate = tableModel.getValueAt(selectedRow, 3).toString();
        String dueDate = tableModel.getValueAt(selectedRow, 4).toString();
        String returnDate = tableModel.getValueAt(selectedRow, 5).toString();
        String status = tableModel.getValueAt(selectedRow, 6).toString();
        String fineAmount = tableModel.getValueAt(selectedRow, 7).toString();
        
        // Add details
        addDetailRow(contentPanel, "Transaction ID:", transactionId);
        addDetailRow(contentPanel, "Book ID:", bookId);
        addDetailRow(contentPanel, "User ID:", userId);
        addDetailRow(contentPanel, "Borrow Date:", borrowDate);
        addDetailRow(contentPanel, "Due Date:", dueDate);
        addDetailRow(contentPanel, "Return Date:", returnDate);
        addDetailRow(contentPanel, "Status:", status);
        addDetailRow(contentPanel, "Fine Amount:", fineAmount);
        
        // Update the details panel
        detailsPanel.removeAll();
        JLabel headerLabel = new JLabel("Transaction Details");
        headerLabel.setFont(new Font("Inter", Font.BOLD, 16));
        headerLabel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        detailsPanel.add(headerLabel, BorderLayout.NORTH);
        detailsPanel.add(contentPanel, BorderLayout.CENTER);
        
        detailsPanel.revalidate();
        detailsPanel.repaint();
    }
    
    private void addDetailRow(JPanel panel, String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Inter", Font.BOLD, 13));
        labelComponent.setPreferredSize(new Dimension(120, labelComponent.getPreferredSize().height));
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Inter", Font.PLAIN, 13));
        
        row.add(labelComponent, BorderLayout.WEST);
        row.add(valueComponent, BorderLayout.CENTER);
        
        panel.add(row);
        panel.add(Box.createVerticalStrut(5));
    }
    
    private void loadTransactions() {
        // Clear existing data
        tableModel.setRowCount(0);
        // Data will be loaded from database
    }
} 