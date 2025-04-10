package org.lkw.view.user;

import org.lkw.data.util.LaravelTheme;
import org.lkw.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UserDashboardPanel extends JPanel {
    private static final int CONTAINER_PADDING = 20;
    private final User currentUser;

    public UserDashboardPanel(User user) {
        this.currentUser = user;
        
        setLayout(new BorderLayout());
        setBackground(LaravelTheme.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(CONTAINER_PADDING, CONTAINER_PADDING, CONTAINER_PADDING, CONTAINER_PADDING));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);

        contentPanel.add(createWelcomePanel());
        contentPanel.add(Box.createVerticalStrut(20));

        contentPanel.add(createStatisticsPanel());
        contentPanel.add(Box.createVerticalStrut(20));

        contentPanel.add(createQuickActionsPanel());

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        wrapperPanel.add(contentPanel, BorderLayout.NORTH);

        add(wrapperPanel, BorderLayout.CENTER);
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY, 1, true),
            new EmptyBorder(24, 24, 24, 24)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel welcomeLabel = new JLabel("Welcome back, " + currentUser.getUsername() + "!");
        welcomeLabel.setFont(new Font("Inter", Font.BOLD, 24));
        welcomeLabel.setForeground(LaravelTheme.TEXT_DARK);

        JLabel subtitleLabel = new JLabel("Here's an overview of your library activity");
        subtitleLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        subtitleLabel.setForeground(LaravelTheme.MUTED_TEXT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(welcomeLabel);
        textPanel.add(subtitleLabel);

        panel.add(textPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 16, 0));
        panel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        panel.add(createStatCard("Currently Borrowed", "0", "Books you have checked out"));
        panel.add(createStatCard("Overdue", "0", "Books past their due date"));

        return panel;
    }

    private JPanel createStatCard(String title, String value, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY, 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 14));
        titleLabel.setForeground(LaravelTheme.TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Inter", Font.BOLD, 24));
        valueLabel.setForeground(LaravelTheme.PRIMARY_RED);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Inter", Font.PLAIN, 13));
        descLabel.setForeground(LaravelTheme.MUTED_TEXT);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(valueLabel);
        card.add(descLabel);

        return card;
    }

    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY, 1, true),
            new EmptyBorder(20, 24, 24, 24)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JLabel titleLabel = new JLabel("Quick Actions");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 16));
        titleLabel.setForeground(LaravelTheme.TEXT_DARK);

        JPanel actionsGrid = new JPanel(new GridLayout(1, 2, 16, 0));
        actionsGrid.setBackground(Color.WHITE);
        actionsGrid.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));

        actionsGrid.add(createActionButton("Browse Books", "Explore our collection of books"));
        actionsGrid.add(createActionButton("Update Profile", "Manage your account settings"));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(actionsGrid, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActionButton(String title, String description) {
        JPanel button = new JPanel();
        button.setLayout(new BoxLayout(button, BoxLayout.Y_AXIS));
        button.setBackground(new Color(249, 250, 251));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY, 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 14));
        titleLabel.setForeground(LaravelTheme.TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Inter", Font.PLAIN, 13));
        descLabel.setForeground(LaravelTheme.MUTED_TEXT);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        descLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        button.add(titleLabel);
        button.add(descLabel);

        return button;
    }
} 