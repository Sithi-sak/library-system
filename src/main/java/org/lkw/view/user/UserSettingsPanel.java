package org.lkw.view.user;

import org.lkw.data.dao.UserDAO;
import org.lkw.data.util.LaravelTheme;
import org.lkw.data.util.PasswordUtils;
import org.lkw.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.prefs.Preferences;

public class UserSettingsPanel extends JPanel {
    private final User currentUser;
    private final UserDAO userDAO;
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JCheckBox emailNotificationsCheck;
    private JCheckBox dueDateRemindersCheck;
    private JCheckBox newArrivalsCheck;
    private final Preferences userPrefs;

    private static final int CONTAINER_WIDTH = 640;
    private static final int FIELD_HEIGHT = 36;
    private static final int SECTION_SPACING = 32;
    private static final int FIELD_SPACING = 24;
    private static final Color BORDER_COLOR = new Color(226, 232, 240);
    private static final Color LABEL_COLOR = new Color(71, 85, 105);
    private static final Color INPUT_BORDER = new Color(203, 213, 225);
    private static final Font HEADING_FONT = new Font("Inter", Font.BOLD, 16);
    private static final Font LABEL_FONT = new Font("Inter", Font.PLAIN, 13);
    private static final Font INPUT_FONT = new Font("Inter", Font.PLAIN, 14);
    private static final int LABEL_BOTTOM_MARGIN = 8;

    public UserSettingsPanel(User user) {
        this.currentUser = user;
        this.userDAO = new UserDAO();
        this.userPrefs = Preferences.userNodeForPackage(UserSettingsPanel.class).node(user.getUsername());
        
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(Color.WHITE);
        mainContainer.setBorder(new EmptyBorder(40, 0, 40, 0));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.setMaximumSize(new Dimension(CONTAINER_WIDTH, Integer.MAX_VALUE));

        centerPanel.add(createSection("Personal Information", createPersonalInfoPanel()));
        centerPanel.add(Box.createVerticalStrut(SECTION_SPACING));
        centerPanel.add(createSection("Change Password", createPasswordPanel()));
        centerPanel.add(Box.createVerticalStrut(SECTION_SPACING));
        centerPanel.add(createSection("Notification Preferences", createNotificationPanel()));
        centerPanel.add(Box.createVerticalStrut(SECTION_SPACING));

        JButton saveButton = new JButton("Save Changes");
        styleButton(saveButton);
        saveButton.addActionListener(e -> saveSettings());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(CONTAINER_WIDTH, 50));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(saveButton);

        centerPanel.add(buttonPanel);

        mainContainer.add(Box.createHorizontalGlue());
        mainContainer.add(centerPanel);
        mainContainer.add(Box.createHorizontalGlue());

        JScrollPane scrollPane = new JScrollPane(mainContainer);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createSection(String title, JPanel content) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            new EmptyBorder(24, 28, 24, 28)
        ));
        section.setMaximumSize(new Dimension(CONTAINER_WIDTH, Integer.MAX_VALUE));
        section.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(HEADING_FONT);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(titleLabel);
        section.add(Box.createVerticalStrut(20));

        content.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(content);

        return section;
    }

    private JPanel createFormField(String labelText, JComponent field) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(Color.WHITE);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, field.getPreferredSize().height + LABEL_BOTTOM_MARGIN + 20));

        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        label.setForeground(LABEL_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(label);
        wrapper.add(Box.createVerticalStrut(LABEL_BOTTOM_MARGIN));

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (field instanceof JTextField || field instanceof JPasswordField) {
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, FIELD_HEIGHT));
        }
        wrapper.add(field);

        return wrapper;
    }

    private JPanel createPersonalInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        fullNameField = createStyledTextField(currentUser.getFullName());
        emailField = createStyledTextField(currentUser.getEmail());
        phoneField = createStyledTextField(currentUser.getPhoneNumber());

        panel.add(createFormField("Full Name", fullNameField));
        panel.add(Box.createVerticalStrut(FIELD_SPACING));
        panel.add(createFormField("Email", emailField));
        panel.add(Box.createVerticalStrut(FIELD_SPACING));
        panel.add(createFormField("Phone", phoneField));

        return panel;
    }

    private JPanel createPasswordPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        currentPasswordField = createStyledPasswordField();
        newPasswordField = createStyledPasswordField();
        confirmPasswordField = createStyledPasswordField();

        panel.add(createFormField("Current Password", currentPasswordField));
        panel.add(Box.createVerticalStrut(FIELD_SPACING));
        panel.add(createFormField("New Password", newPasswordField));
        panel.add(Box.createVerticalStrut(FIELD_SPACING));
        panel.add(createFormField("Confirm Password", confirmPasswordField));

        return panel;
    }

    private JPanel createNotificationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        emailNotificationsCheck = createCheckbox("Email Notifications", 
            userPrefs.getBoolean("emailNotifications", true));
        dueDateRemindersCheck = createCheckbox("Due Date Reminders", 
            userPrefs.getBoolean("dueDateReminders", true));
        newArrivalsCheck = createCheckbox("New Book Arrivals", 
            userPrefs.getBoolean("newArrivals", true));

        panel.add(emailNotificationsCheck);
        panel.add(Box.createVerticalStrut(16));
        panel.add(dueDateRemindersCheck);
        panel.add(Box.createVerticalStrut(16));
        panel.add(newArrivalsCheck);

        return panel;
    }

    private JTextField createStyledTextField(String initialValue) {
        JTextField field = new JTextField(initialValue);
        field.setFont(INPUT_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(INPUT_BORDER),
            new EmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(INPUT_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(INPUT_BORDER),
            new EmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    private JCheckBox createCheckbox(String text, boolean selected) {
        JCheckBox checkbox = new JCheckBox(text);
        checkbox.setSelected(selected);
        checkbox.setBackground(Color.WHITE);
        checkbox.setFont(INPUT_FONT);
        checkbox.setForeground(Color.BLACK);
        checkbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkbox.setBorder(new EmptyBorder(2, 0, 2, 0));
        return checkbox;
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Inter", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(239, 68, 68)); // Laravel red
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 38, 38)); // Darker red on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(239, 68, 68));
            }
        });
    }

    private void saveSettings() {
        if (fullNameField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty() || 
            phoneField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all required fields",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentUser.setFullName(fullNameField.getText().trim());
        currentUser.setEmail(emailField.getText().trim());
        currentUser.setPhoneNumber(phoneField.getText().trim());

        String currentPassword = new String(currentPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (!currentPassword.isEmpty()) {
            if (!PasswordUtils.verifyPassword(currentPassword, currentUser.getPasswordHash())) {
                JOptionPane.showMessageDialog(this,
                    "Current password is incorrect",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this,
                    "New passwords do not match",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (newPassword.length() < 6) {
                JOptionPane.showMessageDialog(this,
                    "New password must be at least 6 characters long",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            currentUser.setPasswordHash(PasswordUtils.hashPassword(newPassword));
        }

        userPrefs.putBoolean("emailNotifications", emailNotificationsCheck.isSelected());
        userPrefs.putBoolean("dueDateReminders", dueDateRemindersCheck.isSelected());
        userPrefs.putBoolean("newArrivals", newArrivalsCheck.isSelected());

        if (userDAO.updateUser(currentUser)) {
            JOptionPane.showMessageDialog(this,
                "Settings saved successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            currentPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to save settings",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 