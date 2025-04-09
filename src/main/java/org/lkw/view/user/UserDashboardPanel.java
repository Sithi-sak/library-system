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

        // Create main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);

        // Add welcome section
        contentPanel.add(createWelcomePanel(), BorderLayout.NORTH);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 24, 20, 24)
        ));

        JLabel welcomeLabel = new JLabel("Welcome back, " + currentUser.getUsername() + "!");
        welcomeLabel.setFont(new Font("Inter", Font.BOLD, 24));
        welcomeLabel.setForeground(LaravelTheme.TEXT_DARK);

        panel.add(welcomeLabel, BorderLayout.CENTER);
        return panel;
    }
} 