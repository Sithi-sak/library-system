package org.lkw.view.user;

import org.lkw.data.dao.BookDAO;
import org.lkw.data.dao.TransactionDAO;
import org.lkw.model.Book;
import org.lkw.model.Transaction;
import org.lkw.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class UserDashboardPanel extends JPanel {
    private final User currentUser;
    private final TransactionDAO transactionDAO;
    private final BookDAO bookDAO;

    // Design constants
    private static final int CONTAINER_WIDTH = 1200;
    private static final int CARD_HEIGHT = 120;
    private static final int SECTION_SPACING = 32;
    private static final int CARD_SPACING = 24;
    private static final Color BORDER_COLOR = new Color(226, 232, 240);
    private static final Color LABEL_COLOR = new Color(71, 85, 105);
    private static final Color HEADING_COLOR = new Color(15, 23, 42);
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private static final Color WARNING_COLOR = new Color(234, 179, 8);
    private static final Color DANGER_COLOR = new Color(239, 68, 68);
    private static final Font HEADING_FONT = new Font("Inter", Font.BOLD, 20);
    private static final Font CARD_TITLE_FONT = new Font("Inter", Font.BOLD, 16);
    private static final Font STAT_VALUE_FONT = new Font("Inter", Font.BOLD, 24);
    private static final Font STAT_LABEL_FONT = new Font("Inter", Font.PLAIN, 14);

    // Panels
    private JPanel statsPanel;
    private JPanel recentActivitiesPanel;
    private JPanel dueSoonPanel;

    public UserDashboardPanel(User user) {
        this.currentUser = user;
        this.transactionDAO = new TransactionDAO();
        this.bookDAO = new BookDAO();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Create main container
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(Color.WHITE);
        mainContainer.setBorder(new EmptyBorder(40, 0, 40, 0));

        // Center panel to hold all sections
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.setMaximumSize(new Dimension(CONTAINER_WIDTH, Integer.MAX_VALUE));

        // Welcome message
        JPanel welcomePanel = createWelcomePanel();
        centerPanel.add(welcomePanel);
        centerPanel.add(Box.createVerticalStrut(SECTION_SPACING));

        // Statistics cards
        statsPanel = createStatsPanel();
        centerPanel.add(statsPanel);
        centerPanel.add(Box.createVerticalStrut(SECTION_SPACING));

        // Recent activities and Due Soon sections in a row
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setMaximumSize(new Dimension(CONTAINER_WIDTH, 400));

        // Recent Activities (left)
        recentActivitiesPanel = createRecentActivitiesPanel();
        recentActivitiesPanel.setPreferredSize(new Dimension(CONTAINER_WIDTH / 2 - CARD_SPACING/2, 400));
        rowPanel.add(recentActivitiesPanel);
        
        rowPanel.add(Box.createHorizontalStrut(CARD_SPACING));

        // Due Soon (right)
        dueSoonPanel = createDueSoonPanel();
        dueSoonPanel.setPreferredSize(new Dimension(CONTAINER_WIDTH / 2 - CARD_SPACING/2, 400));
        rowPanel.add(dueSoonPanel);

        centerPanel.add(rowPanel);

        // Add center panel to main container with horizontal centering
        mainContainer.add(Box.createHorizontalGlue());
        mainContainer.add(centerPanel);
        mainContainer.add(Box.createHorizontalGlue());

        // Add to scroll pane
        JScrollPane scrollPane = new JScrollPane(mainContainer);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Load initial data
        refreshDashboard();
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            new EmptyBorder(24, 28, 24, 28)
        ));
        panel.setMaximumSize(new Dimension(CONTAINER_WIDTH, CARD_HEIGHT));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome back, " + currentUser.getFullName());
        welcomeLabel.setFont(HEADING_FONT);
        welcomeLabel.setForeground(HEADING_COLOR);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(welcomeLabel);

        // Date
        JLabel dateLabel = new JLabel(LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
        dateLabel.setFont(STAT_LABEL_FONT);
        dateLabel.setForeground(LABEL_COLOR);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(Box.createVerticalStrut(8));
        panel.add(dateLabel);

        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(CONTAINER_WIDTH, CARD_HEIGHT));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Books Currently Borrowed
        panel.add(createStatCard(
            "Currently Borrowed",
            "5",
            "books",
            Color.WHITE
        ));

        panel.add(Box.createHorizontalStrut(CARD_SPACING));

        // Books Due Soon
        panel.add(createStatCard(
            "Due Soon",
            "2",
            "books",
            new Color(254, 243, 199) // Yellow bg
        ));

        panel.add(Box.createHorizontalStrut(CARD_SPACING));

        // Overdue Books
        panel.add(createStatCard(
            "Overdue",
            "1",
            "book",
            new Color(254, 226, 226) // Red bg
        ));

        panel.add(Box.createHorizontalStrut(CARD_SPACING));

        // Total Books Read
        panel.add(createStatCard(
            "Total Books Read",
            "23",
            "books",
            new Color(220, 252, 231) // Green bg
        ));

        return panel;
    }

    private JPanel createStatCard(String title, String value, String unit, Color bgColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            new EmptyBorder(20, 24, 20, 24)
        ));
        int cardWidth = (CONTAINER_WIDTH - (CARD_SPACING * 3)) / 4;
        card.setPreferredSize(new Dimension(cardWidth, CARD_HEIGHT));
        card.setMaximumSize(new Dimension(cardWidth, CARD_HEIGHT));

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(STAT_LABEL_FONT);
        titleLabel.setForeground(LABEL_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLabel);

        card.add(Box.createVerticalStrut(12));

        // Value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(STAT_VALUE_FONT);
        valueLabel.setForeground(HEADING_COLOR);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(valueLabel);

        // Unit
        JLabel unitLabel = new JLabel(unit);
        unitLabel.setFont(STAT_LABEL_FONT);
        unitLabel.setForeground(LABEL_COLOR);
        unitLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(Box.createVerticalStrut(4));
        card.add(unitLabel);

        return card;
    }

    private JPanel createRecentActivitiesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            new EmptyBorder(24, 28, 24, 28)
        ));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel titleLabel = new JLabel("Recent Activities");
        titleLabel.setFont(CARD_TITLE_FONT);
        titleLabel.setForeground(HEADING_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));

        // Activity list (placeholder)
        String[] activities = {
            "Borrowed 'The Great Gatsby' - 2 days ago",
            "Returned 'To Kill a Mockingbird' - 5 days ago",
            "Renewed 'Pride and Prejudice' - 1 week ago"
        };

        for (String activity : activities) {
            JLabel activityLabel = new JLabel(activity);
            activityLabel.setFont(STAT_LABEL_FONT);
            activityLabel.setForeground(LABEL_COLOR);
            activityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(activityLabel);
            panel.add(Box.createVerticalStrut(16));
        }

        return panel;
    }

    private JPanel createDueSoonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            new EmptyBorder(24, 28, 24, 28)
        ));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel titleLabel = new JLabel("Due Soon");
        titleLabel.setFont(CARD_TITLE_FONT);
        titleLabel.setForeground(HEADING_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));

        // Due books list (placeholder)
        Object[][] dueBooks = {
            {"The Great Gatsby", "Due in 2 days", WARNING_COLOR},
            {"1984", "Overdue by 1 day", DANGER_COLOR}
        };

        for (Object[] book : dueBooks) {
            JPanel bookPanel = new JPanel();
            bookPanel.setLayout(new BorderLayout());
            bookPanel.setBackground(Color.WHITE);
            bookPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            bookPanel.setBorder(new EmptyBorder(8, 0, 8, 0));

            JLabel titleLabel2 = new JLabel((String) book[0]);
            titleLabel2.setFont(STAT_LABEL_FONT);
            titleLabel2.setForeground(HEADING_COLOR);
            bookPanel.add(titleLabel2, BorderLayout.WEST);

            JLabel dueLabel = new JLabel((String) book[1]);
            dueLabel.setFont(STAT_LABEL_FONT);
            dueLabel.setForeground((Color) book[2]);
            bookPanel.add(dueLabel, BorderLayout.EAST);

            panel.add(bookPanel);
            panel.add(Box.createVerticalStrut(8));
        }

        return panel;
    }

    private void refreshDashboard() {
        // Get current statistics
        int borrowedCount = transactionDAO.getBooksBorrowedCount(currentUser.getUserId());
        int readCount = transactionDAO.getBooksReadCount(currentUser.getUserId());
        List<Transaction> dueSoonList = transactionDAO.getDueSoonBooks(currentUser.getUserId());
        List<Transaction> overdueList = transactionDAO.getOverdueBooks(currentUser.getUserId());
        List<Transaction> recentTransactions = transactionDAO.getUserTransactions(currentUser.getUserId());

        // Update stats panel
        statsPanel.removeAll();
        statsPanel.add(createStatCard(
            "Currently Borrowed",
            String.valueOf(borrowedCount),
            borrowedCount == 1 ? "book" : "books",
            Color.WHITE
        ));
        statsPanel.add(Box.createHorizontalStrut(CARD_SPACING));
        statsPanel.add(createStatCard(
            "Due Soon",
            String.valueOf(dueSoonList.size()),
            dueSoonList.size() == 1 ? "book" : "books",
            new Color(254, 243, 199)
        ));
        statsPanel.add(Box.createHorizontalStrut(CARD_SPACING));
        statsPanel.add(createStatCard(
            "Overdue",
            String.valueOf(overdueList.size()),
            overdueList.size() == 1 ? "book" : "books",
            new Color(254, 226, 226)
        ));
        statsPanel.add(Box.createHorizontalStrut(CARD_SPACING));
        statsPanel.add(createStatCard(
            "Total Books Read",
            String.valueOf(readCount),
            readCount == 1 ? "book" : "books",
            new Color(220, 252, 231)
        ));

        // Update recent activities panel
        recentActivitiesPanel.removeAll();
        JLabel recentTitle = new JLabel("Recent Activities");
        recentTitle.setFont(CARD_TITLE_FONT);
        recentTitle.setForeground(HEADING_COLOR);
        recentTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        recentActivitiesPanel.add(recentTitle);
        recentActivitiesPanel.add(Box.createVerticalStrut(20));

        // Add recent transactions
        for (Transaction transaction : recentTransactions.subList(0, Math.min(5, recentTransactions.size()))) {
            JPanel activityPanel = new JPanel();
            activityPanel.setLayout(new BorderLayout());
            activityPanel.setBackground(Color.WHITE);
            activityPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            activityPanel.setBorder(new EmptyBorder(8, 0, 8, 0));

            Book book = transaction.getBook();
            String status = transaction.getStatus();
            String timeAgo = formatTimeAgo(transaction.getBorrowDate());

            JLabel bookLabel = new JLabel(book.getTitle());
            bookLabel.setFont(STAT_LABEL_FONT);
            bookLabel.setForeground(HEADING_COLOR);

            JLabel statusLabel = new JLabel(status + " • " + timeAgo);
            statusLabel.setFont(STAT_LABEL_FONT);
            statusLabel.setForeground(LABEL_COLOR);

            activityPanel.add(bookLabel, BorderLayout.WEST);
            activityPanel.add(statusLabel, BorderLayout.EAST);

            recentActivitiesPanel.add(activityPanel);
            recentActivitiesPanel.add(Box.createVerticalStrut(8));
        }

        // Update due soon panel
        dueSoonPanel.removeAll();
        JLabel dueSoonTitle = new JLabel("Due Soon");
        dueSoonTitle.setFont(CARD_TITLE_FONT);
        dueSoonTitle.setForeground(HEADING_COLOR);
        dueSoonTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        dueSoonPanel.add(dueSoonTitle);
        dueSoonPanel.add(Box.createVerticalStrut(20));

        // Add due soon books
        List<Transaction> allDueBooks = new ArrayList<>();
        allDueBooks.addAll(dueSoonList);
        allDueBooks.addAll(overdueList);

        for (Transaction transaction : allDueBooks) {
            JPanel bookPanel = new JPanel();
            bookPanel.setLayout(new BorderLayout());
            bookPanel.setBackground(Color.WHITE);
            bookPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            bookPanel.setBorder(new EmptyBorder(8, 0, 8, 0));

            Book book = transaction.getBook();
            boolean isOverdue = transaction.getDueDate().isBefore(LocalDateTime.now());
            
            String dueText;
            Color textColor;
            if (isOverdue) {
                int daysOverdue = getDaysOverdue(transaction.getDueDate());
                dueText = "Overdue by " + daysOverdue + (daysOverdue == 1 ? " day" : " days");
                textColor = DANGER_COLOR;
            } else {
                int daysUntilDue = getDaysUntilDue(transaction.getDueDate());
                dueText = "Due in " + daysUntilDue + (daysUntilDue == 1 ? " day" : " days");
                textColor = WARNING_COLOR;
            }

            JLabel titleLabel = new JLabel(book.getTitle());
            titleLabel.setFont(STAT_LABEL_FONT);
            titleLabel.setForeground(HEADING_COLOR);

            JLabel dueLabel = new JLabel(dueText);
            dueLabel.setFont(STAT_LABEL_FONT);
            dueLabel.setForeground(textColor);

            bookPanel.add(titleLabel, BorderLayout.WEST);
            bookPanel.add(dueLabel, BorderLayout.EAST);

            dueSoonPanel.add(bookPanel);
            dueSoonPanel.add(Box.createVerticalStrut(8));
        }

        // Revalidate and repaint all panels
        statsPanel.revalidate();
        statsPanel.repaint();
        recentActivitiesPanel.revalidate();
        recentActivitiesPanel.repaint();
        dueSoonPanel.revalidate();
        dueSoonPanel.repaint();
    }

    private String formatTimeAgo(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        long days = java.time.Duration.between(dateTime, now).toDays();
        long hours = java.time.Duration.between(dateTime, now).toHours();
        long minutes = java.time.Duration.between(dateTime, now).toMinutes();

        if (days > 0) {
            return days + (days == 1 ? " day ago" : " days ago");
        } else if (hours > 0) {
            return hours + (hours == 1 ? " hour ago" : " hours ago");
        } else {
            return minutes + (minutes == 1 ? " minute ago" : " minutes ago");
        }
    }

    private int getDaysOverdue(LocalDateTime dueDate) {
        return (int) java.time.Duration.between(dueDate, LocalDateTime.now()).toDays();
    }

    private int getDaysUntilDue(LocalDateTime dueDate) {
        return (int) java.time.Duration.between(LocalDateTime.now(), dueDate).toDays();
    }
} 