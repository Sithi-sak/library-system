package org.lkw.view;

import com.formdev.flatlaf.FlatLightLaf;
import org.lkw.data.util.LaravelTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton registerButton;
    private final JComboBox<String> roleComboBox;

    public LoginView() {
        FlatLightLaf.setup();

        setTitle("Library Management - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = LaravelTheme.createPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel headerLabel = new JLabel("Library System", JLabel.CENTER);
        LaravelTheme.styleTitle(headerLabel);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        JPanel formPanel = LaravelTheme.createPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;

        JLabel usernameLabel = new JLabel("Username");
        LaravelTheme.styleLabel(usernameLabel, false);
        gbc.insets = new Insets(0, 0, 4, 0);
        formPanel.add(usernameLabel, gbc);

        usernameField = new JTextField();
        LaravelTheme.styleTextField(usernameField);
        gbc.insets = new Insets(0, 0, 15, 0);
        formPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password");
        LaravelTheme.styleLabel(passwordLabel, false);
        gbc.insets = new Insets(0, 0, 4, 0);
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField();
        LaravelTheme.stylePasswordField(passwordField);
        gbc.insets = new Insets(0, 0, 15, 0);
        formPanel.add(passwordField, gbc);

        JLabel roleLabel = new JLabel("Login as");
        LaravelTheme.styleLabel(roleLabel, false);
        gbc.insets = new Insets(0, 0, 4, 0);
        formPanel.add(roleLabel, gbc);

        String[] roles = {"member", "admin"};
        roleComboBox = new JComboBox<>(roles);
        LaravelTheme.styleComboBox(roleComboBox);
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(roleComboBox, gbc);

        JPanel buttonPanel = LaravelTheme.createPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 12, 0));

        loginButton = new JButton("Login");
        LaravelTheme.stylePrimaryButton(loginButton);

        registerButton = new JButton("Register");
        LaravelTheme.styleSecondaryButton(registerButton);

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        gbc.insets = new Insets(0, 0, 0, 0);
        formPanel.add(buttonPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);

        pack();
        setLocationRelativeTo(null);
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getSelectedRole() {
        return (String) roleComboBox.getSelectedItem();
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }

    public void setLoginActionListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void setRegisterActionListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean isWindowMaximized() {
        return (getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH;
    }
} 