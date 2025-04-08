package org.lkw.view.user;

import org.lkw.data.dao.TransactionDAO;
import org.lkw.data.util.LaravelTheme;
import org.lkw.model.Transaction;
import org.lkw.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class UserTransactionsPanel extends JPanel {
    private static final int CONTAINER_PADDING = 20;
    
    private final TransactionDAO transactionDAO;
    private JTable transactionsTable;
    private DefaultTableModel tableModel;
    private JLabel totalFinesLabel;
    private User currentUser;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public UserTransactionsPanel(User user) {
        this.currentUser = user;
        this.transactionDAO = new TransactionDAO();
        
        setLayout(new BorderLayout());
        setBackground(LaravelTheme.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(CONTAINER_PADDING, CONTAINER_PADDING, CONTAINER_PADDING, CONTAINER_PADDING));

        // Create main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);

        // Add header section
        contentPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Add transactions table
        contentPanel.add(createTransactionsPanel(), BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
        
        // Load initial data
        loadTransactions();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 24, 20, 24)
        ));

        // Title and subtitle
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("My Transactions");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 18));
        titleLabel.setForeground(LaravelTheme.TEXT_DARK);
        
        JLabel subtitleLabel = new JLabel("View your borrowed books and any outstanding fines");
        subtitleLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        subtitleLabel.setForeground(LaravelTheme.MUTED_TEXT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitleLabel);

        // Total fines section
        totalFinesLabel = new JLabel("Total Fines: $0.00");
        totalFinesLabel.setFont(new Font("Inter", Font.BOLD, 16));
        totalFinesLabel.setForeground(LaravelTheme.PRIMARY_RED);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(totalFinesLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        // Create table model with columns
        String[] columns = {
            "Book Title",
            "Borrow Date",
            "Due Date",
            "Status",
            "Fine Amount"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create table with custom renderer
        transactionsTable = new JTable(tableModel);
        transactionsTable.setFont(new Font("Inter", Font.PLAIN, 14));
        transactionsTable.setRowHeight(50);
        transactionsTable.setShowVerticalLines(false);
        transactionsTable.setGridColor(new Color(240, 240, 240));
        transactionsTable.getTableHeader().setFont(new Font("Inter", Font.BOLD, 14));
        transactionsTable.getTableHeader().setBackground(new Color(249, 250, 251));
        transactionsTable.getTableHeader().setForeground(LaravelTheme.TEXT_DARK);
        transactionsTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        
        // Custom cell renderer for status and fine amount
        TableCellRenderer customRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (column == 3) { // Status column
                    setHorizontalAlignment(JLabel.CENTER);
                    String status = value.toString();
                    if ("overdue".equalsIgnoreCase(status)) {
                        setForeground(new Color(220, 38, 38));
                        setText("⚠ OVERDUE");
                    } else if ("returned".equalsIgnoreCase(status)) {
                        setForeground(new Color(22, 163, 74));
                        setText("✓ RETURNED");
                    } else if ("borrowed".equalsIgnoreCase(status)) {
                        setForeground(new Color(88, 80, 236));
                        setText("◷ BORROWED");
                    }
                } else if (column == 4) { // Fine amount column
                    setHorizontalAlignment(JLabel.RIGHT);
                    if (value != null && !value.toString().equals("$0.00")) {
                        setForeground(LaravelTheme.PRIMARY_RED);
                    } else {
                        setForeground(LaravelTheme.TEXT_DARK);
                    }
                } else {
                    setForeground(LaravelTheme.TEXT_DARK);
                    setHorizontalAlignment(JLabel.LEFT);
                }
                
                return c;
            }
        };

        // Apply custom renderer to all columns
        for (int i = 0; i < transactionsTable.getColumnCount(); i++) {
            transactionsTable.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
        }

        // Set column widths
        transactionsTable.getColumnModel().getColumn(0).setPreferredWidth(300); // Book Title
        transactionsTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Borrow Date
        transactionsTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Due Date
        transactionsTable.getColumnModel().getColumn(3).setPreferredWidth(120); // Status
        transactionsTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Fine Amount

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(transactionsTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void loadTransactions() {
        // Clear existing data
        tableModel.setRowCount(0);
        double totalFines = 0.0;

        try {
            // Get transactions from DAO using userId as int
            int userId = currentUser.getUserId();
            List<Transaction> transactions = transactionDAO.getUserTransactions(userId);
            
            if (transactions.isEmpty()) {
                showEmptyMessage();
                return;
            }

            for (Transaction transaction : transactions) {
                // The fine_amount is stored directly in the database
                double fineAmount = 0.0;
                
                // If the transaction has a fine amount in the database, use it
                if (transaction.getBook() != null) {
                    String bookTitle = transaction.getBook().getTitle();
                    String borrowDate = transaction.getBorrowDate().toLocalDate().format(dateFormatter);
                    String dueDate = transaction.getDueDate().toLocalDate().format(dateFormatter);
                    String status = transaction.getStatus();
                    
                    // Use the fine_amount from database if it exists, or calculate it
                    try {
                        // Assuming your DAO populates this from the fine_amount column
                        fineAmount = getFineAmount(transaction);
                        totalFines += fineAmount;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    Object[] rowData = {
                        bookTitle,
                        borrowDate,
                        dueDate,
                        status,
                        String.format("$%.2f", fineAmount)
                    };
                    
                    tableModel.addRow(rowData);
                }
            }

            // Update total fines label
            totalFinesLabel.setText(String.format("Total Fines: $%.2f", totalFines));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            showEmptyMessage();
        }
    }
    
    private double getFineAmount(Transaction transaction) {
        // This is just a placeholder - in your actual implementation, 
        // you'll want to access the fine_amount column from your database
        
        // For now, let's calculate based on overdue status
        if ("borrowed".equalsIgnoreCase(transaction.getStatus()) && 
            transaction.getDueDate().isBefore(LocalDateTime.now())) {
            // Calculate days overdue
            long daysLate = ChronoUnit.DAYS.between(transaction.getDueDate(), LocalDateTime.now());
            return daysLate * 1.0; // $1 per day late
        } else if ("returned".equalsIgnoreCase(transaction.getStatus()) && 
                  transaction.getReturnDate() != null && 
                  transaction.getReturnDate().isAfter(transaction.getDueDate())) {
            // Calculate days returned late
            long daysLate = ChronoUnit.DAYS.between(transaction.getDueDate(), transaction.getReturnDate());
            return daysLate * 1.0; // $1 per day late
        }
        
        return 0.0;
    }
    
    private void showEmptyMessage() {
        tableModel.setRowCount(0);
        JPanel emptyPanel = new JPanel(new GridBagLayout());
        emptyPanel.setBackground(Color.WHITE);
        
        JLabel emptyLabel = new JLabel("You don't have any transactions yet");
        emptyLabel.setFont(new Font("Inter", Font.BOLD, 16));
        emptyLabel.setForeground(LaravelTheme.MUTED_TEXT);
        
        emptyPanel.add(emptyLabel);
        
        // Remove the existing table
        Container parent = transactionsTable.getParent().getParent().getParent();
        parent.removeAll();
        parent.add(emptyPanel, BorderLayout.CENTER);
        parent.revalidate();
        parent.repaint();
    }
} 