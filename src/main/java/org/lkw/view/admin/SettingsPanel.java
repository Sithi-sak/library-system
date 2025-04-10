package org.lkw.view.admin;

import org.lkw.data.util.LaravelTheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.prefs.Preferences;

public class SettingsPanel extends JPanel {
    private final Preferences prefs;
    
    // Library Information Fields
    private final JTextField libraryNameField;
    private final JTextField addressField;
    private final JTextField contactNumberField;
    private final JTextField emailField;
    
    // System Settings Fields
    private final JCheckBox enableEmailNotificationsCheckbox;
    private final JCheckBox enableAutoRenewalCheckbox;
    private final JCheckBox enableReservationsCheckbox;
    private final JSpinner backupFrequencySpinner;
    
    public SettingsPanel() {
        prefs = Preferences.userRoot().node("LibraryManagementSystem");
        
        // Initialize all fields first
        libraryNameField = new JTextField(20);
        addressField = new JTextField(20);
        contactNumberField = new JTextField(20);
        emailField = new JTextField(20);
        
        enableEmailNotificationsCheckbox = new JCheckBox("Enable Email Notifications");
        enableAutoRenewalCheckbox = new JCheckBox("Enable Auto-Renewal");
        enableReservationsCheckbox = new JCheckBox("Enable Book Reservations");
        backupFrequencySpinner = new JSpinner(new SpinnerNumberModel(7, 1, 30, 1));
        
        // Style all spinners
        styleSpinners();
        
        // Set up the panel layout
        setLayout(new BorderLayout(10, 10));
        setBackground(LaravelTheme.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Create main content panel with FlowLayout for left alignment
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        
        // Create sections panel that will hold all sections
        JPanel sectionsPanel = new JPanel();
        sectionsPanel.setLayout(new BoxLayout(sectionsPanel, BoxLayout.Y_AXIS));
        sectionsPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        
        // Add sections with left alignment
        JPanel[] sections = {
            createLibraryInfoSection(),
            createSystemSettingsSection()
        };
        
        for (JPanel section : sections) {
            // Create a wrapper panel for left alignment
            JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
            wrapper.setBackground(LaravelTheme.BACKGROUND_COLOR);
            wrapper.add(section);
            
            // Set the preferred size of the section to control its width
            section.setPreferredSize(new Dimension(600, section.getPreferredSize().height));
            
            sectionsPanel.add(wrapper);
            sectionsPanel.add(Box.createVerticalStrut(15));
        }
        
        // Add sections panel to content panel
        contentPanel.add(sectionsPanel);
        
        // Add some padding at the bottom for better scrolling
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Create scroll pane with improved scrolling
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(LaravelTheme.BACKGROUND_COLOR);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Improve scroll speed
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
        
        // Add save button at bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        JButton saveButton = new JButton("Save Settings");
        LaravelTheme.stylePrimaryButton(saveButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Load saved settings
        loadSettings();
        
        // Add save button action
        saveButton.addActionListener(e -> saveSettings());
    }
    
    private void styleSpinners() {
        JSpinner[] spinners = {
            backupFrequencySpinner
        };
        
        for (JSpinner spinner : spinners) {
            spinner.setPreferredSize(new Dimension(100, 35));
            JComponent editor = spinner.getEditor();
            if (editor instanceof JSpinner.DefaultEditor) {
                JTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
                textField.setFont(new Font("Inter", Font.PLAIN, 13));
            }
        }
    }
    
    private JPanel createLibraryInfoSection() {
        JPanel panel = LaravelTheme.createCard();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make components fill horizontal space

        // Section title
        JLabel titleLabel = new JLabel("Library Information");
        titleLabel.setForeground(LaravelTheme.PRIMARY_RED);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Reset gridwidth
        gbc.gridwidth = 1;
        
        // Add fields
        addFormField(panel, "Library Name:", libraryNameField, gbc, 1);
        addFormField(panel, "Address:", addressField, gbc, 2);
        addFormField(panel, "Contact Number:", contactNumberField, gbc, 3);
        addFormField(panel, "Email:", emailField, gbc, 4);
        
        return panel;
    }
    
    private JPanel createSystemSettingsSection() {
        JPanel panel = LaravelTheme.createCard();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make components fill horizontal space
        
        // Section title
        JLabel titleLabel = new JLabel("System Settings");
        titleLabel.setForeground(LaravelTheme.PRIMARY_RED);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Reset gridwidth
        gbc.gridwidth = 1;
        
        // Add checkboxes with proper spacing
        gbc.gridy = 1;
        panel.add(enableEmailNotificationsCheckbox, gbc);
        
        gbc.gridy = 2;
        panel.add(enableAutoRenewalCheckbox, gbc);
        
        gbc.gridy = 3;
        panel.add(enableReservationsCheckbox, gbc);
        
        // Reset insets for the spinner
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy = 4;
        addFormField(panel, "Backup Frequency (days):", backupFrequencySpinner, gbc, 4);
        
        return panel;
    }
    
    private void addFormField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row) {
        if (field == null) {
            return;
        }
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.4; // Give less weight to label
        JLabel jLabel = new JLabel(label);
        LaravelTheme.styleLabel(jLabel, false);
        panel.add(jLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.6; // Give more weight to field
        if (field instanceof JTextField) {
            LaravelTheme.styleTextField((JTextField) field);
        }
        panel.add(field, gbc);
    }
    
    private void loadSettings() {
        // Load Library Information
        libraryNameField.setText(prefs.get("libraryName", "My Library"));
        addressField.setText(prefs.get("address", ""));
        contactNumberField.setText(prefs.get("contactNumber", ""));
        emailField.setText(prefs.get("email", ""));
        
        // Load System Settings
        enableEmailNotificationsCheckbox.setSelected(prefs.getBoolean("enableEmailNotifications", true));
        enableAutoRenewalCheckbox.setSelected(prefs.getBoolean("enableAutoRenewal", false));
        enableReservationsCheckbox.setSelected(prefs.getBoolean("enableReservations", true));
        backupFrequencySpinner.setValue(prefs.getInt("backupFrequency", 7));
    }
    
    private void saveSettings() {
        // Save Library Information
        prefs.put("libraryName", libraryNameField.getText());
        prefs.put("address", addressField.getText());
        prefs.put("contactNumber", contactNumberField.getText());
        prefs.put("email", emailField.getText());
        
        // Save System Settings
        prefs.putBoolean("enableEmailNotifications", enableEmailNotificationsCheckbox.isSelected());
        prefs.putBoolean("enableAutoRenewal", enableAutoRenewalCheckbox.isSelected());
        prefs.putBoolean("enableReservations", enableReservationsCheckbox.isSelected());
        prefs.putInt("backupFrequency", (Integer) backupFrequencySpinner.getValue());
        
        // Show success message
        JOptionPane.showMessageDialog(this,
            "Settings saved successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
    }
} 