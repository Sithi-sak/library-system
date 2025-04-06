package org.lkw.view;

import com.formdev.flatlaf.FlatLightLaf;
import org.lkw.data.util.LaravelTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class RegisterView extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;
    private final JTextField fullNameField;
    private final JTextField emailField;
    private final JTextField phoneField;
    private final JComboBox<String> roleComboBox;
    private final JButton registerButton;
    private final JButton cancelButton;

    public RegisterView() {
        // Set FlatLaf look and feel
        FlatLightLaf.setup();
        
        // Set up the frame
        setTitle("Library Management - Register");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Main panel with white background
        JPanel mainPanel = LaravelTheme.createPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        // Header
        JLabel headerLabel = new JLabel("Create Account", JLabel.CENTER);
        LaravelTheme.styleTitle(headerLabel);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        
        // Form panel - using GridBagLayout for proper alignment
        JPanel formPanel = LaravelTheme.createPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        
        // Username field
        JLabel usernameLabel = new JLabel("Username *");
        LaravelTheme.styleLabel(usernameLabel, false);
        gbc.insets = new Insets(0, 0, 4, 0);
        formPanel.add(usernameLabel, gbc);
        
        usernameField = new JTextField();
        LaravelTheme.styleTextField(usernameField);
        gbc.insets = new Insets(0, 0, 15, 0);
        formPanel.add(usernameField, gbc);
        
        // Password field
        JLabel passwordLabel = new JLabel("Password *");
        LaravelTheme.styleLabel(passwordLabel, false);
        gbc.insets = new Insets(0, 0, 4, 0);
        formPanel.add(passwordLabel, gbc);
        
        passwordField = new JPasswordField();
        LaravelTheme.stylePasswordField(passwordField);
        gbc.insets = new Insets(0, 0, 15, 0);
        formPanel.add(passwordField, gbc);
        
        // Confirm Password field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password *");
        LaravelTheme.styleLabel(confirmPasswordLabel, false);
        gbc.insets = new Insets(0, 0, 4, 0);
        formPanel.add(confirmPasswordLabel, gbc);
        
        confirmPasswordField = new JPasswordField();
        LaravelTheme.stylePasswordField(confirmPasswordField);
        gbc.insets = new Insets(0, 0, 15, 0);
        formPanel.add(confirmPasswordField, gbc);
        
        // Full Name field
        JLabel fullNameLabel = new JLabel("Full Name *");
        LaravelTheme.styleLabel(fullNameLabel, false);
        gbc.insets = new Insets(0, 0, 4, 0);
        formPanel.add(fullNameLabel, gbc);
        
        fullNameField = new JTextField();
        LaravelTheme.styleTextField(fullNameField);
        gbc.insets = new Insets(0, 0, 15, 0);
        formPanel.add(fullNameField, gbc);
        
        // Email field
        JLabel emailLabel = new JLabel("Email *");
        LaravelTheme.styleLabel(emailLabel, false);
        gbc.insets = new Insets(0, 0, 4, 0);
        formPanel.add(emailLabel, gbc);
        
        emailField = new JTextField();
        LaravelTheme.styleTextField(emailField);
        gbc.insets = new Insets(0, 0, 15, 0);
        formPanel.add(emailField, gbc);
        
        // Phone field
        JLabel phoneLabel = new JLabel("Phone Number");
        LaravelTheme.styleLabel(phoneLabel, false);
        gbc.insets = new Insets(0, 0, 4, 0);
        formPanel.add(phoneLabel, gbc);
        
        phoneField = new JTextField();
        LaravelTheme.styleTextField(phoneField);
        gbc.insets = new Insets(0, 0, 15, 0);
        formPanel.add(phoneField, gbc);
        
        // Role selection
        JLabel roleLabel = new JLabel("Register as");
        LaravelTheme.styleLabel(roleLabel, false);
        gbc.insets = new Insets(0, 0, 4, 0);
        formPanel.add(roleLabel, gbc);
        
        String[] roles = {"member", "admin"};
        roleComboBox = new JComboBox<>(roles);
        LaravelTheme.styleComboBox(roleComboBox);
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(roleComboBox, gbc);
        
        // Button panel
        JPanel buttonPanel = LaravelTheme.createPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 12, 0));
        
        registerButton = new JButton("Register");
        LaravelTheme.stylePrimaryButton(registerButton);
        
        cancelButton = new JButton("Cancel");
        LaravelTheme.styleSecondaryButton(cancelButton);
        
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        gbc.insets = new Insets(0, 0, 0, 0);
        formPanel.add(buttonPanel, gbc);
        
        // Wrap form panel in a scroll pane to handle overflow
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Add form to main panel
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Set the content pane
        setContentPane(mainPanel);
        
        // Size and center the window
        pack();
        setLocationRelativeTo(null);
    }
    
    public String getUsername() {
        return usernameField.getText();
    }
    
    public String getPassword() {
        return new String(passwordField.getPassword());
    }
    
    public String getConfirmPassword() {
        return new String(confirmPasswordField.getPassword());
    }
    
    public String getFullName() {
        return fullNameField.getText();
    }
    
    public String getEmail() {
        return emailField.getText();
    }
    
    public String getPhoneNumber() {
        return phoneField.getText();
    }
    
    public String getSelectedRole() {
        return (String) roleComboBox.getSelectedItem();
    }
    
    public void setRegisterActionListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }
    
    public void setCancelActionListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void maximizeIfNeeded(boolean shouldMaximize) {
        if (shouldMaximize) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }
} 