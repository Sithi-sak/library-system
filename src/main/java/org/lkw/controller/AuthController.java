package org.lkw.controller;

import org.lkw.data.dao.UserDAO;
import org.lkw.data.util.PasswordUtils;
import org.lkw.model.User;
import org.lkw.view.AdminView;
import org.lkw.view.LoginView;
import org.lkw.view.MainView;
import org.lkw.view.RegisterView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AuthController {
    private final LoginView loginView;
    private RegisterView registerView;
    private final UserDAO userDAO;
    private MainView mainView;
    
    public AuthController(LoginView loginView) {
        this.loginView = loginView;
        this.userDAO = new UserDAO();
        
        // Set up action listeners for login view
        this.loginView.setLoginActionListener(new LoginListener());
        this.loginView.setRegisterActionListener(new OpenRegisterListener());
    }
    
    class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();
            String role = loginView.getSelectedRole();
            
            // Validate inputs
            if (username.isEmpty() || password.isEmpty()) {
                loginView.showError("Username and password cannot be empty");
                return;
            }
            
            // Hash the password
            String hashedPassword = PasswordUtils.hashPassword(password);
            
            // Attempt login
            User user = userDAO.login(username, hashedPassword);
            
            if (user != null && user.getRole().equals(role)) {
                // Login successful
                loginView.showSuccess("Login successful!");
                loginView.setVisible(false);
                
                // Check if login window was maximized
                boolean wasMaximized = loginView.isWindowMaximized();
                
                // Open appropriate view based on role
                if ("admin".equalsIgnoreCase(user.getRole())) {
                    // Open admin view for admin users
                    AdminView adminView = new AdminView();
                    adminView.setUser(user);
                    adminView.setLogoutActionListener(e1 -> {
                        adminView.dispose();
                        loginView.clearFields();
                        loginView.setVisible(true);
                    });
                    
                    // Apply maximized state if needed
                    if (wasMaximized) {
                        adminView.maximizeIfNeeded(true);
                    }
                    
                    adminView.setVisible(true);
                } else {
                    // Open member view for regular users
                    mainView = new MainView();
                    mainView.setUser(user);
                    mainView.setLogoutActionListener(e1 -> logout());
                    
                    // Apply maximized state if needed
                    if (wasMaximized) {
                        mainView.maximizeIfNeeded(true);
                    }
                    
                    mainView.setVisible(true);
                }
            } else {
                // Login failed
                loginView.showError("Invalid username, password, or role. Please try again or register if you don't have an account.");
            }
        }
    }
    
    class OpenRegisterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Check if login window was maximized
            boolean wasMaximized = loginView.isWindowMaximized();
            
            registerView = new RegisterView();
            registerView.setRegisterActionListener(new RegisterListener());
            registerView.setCancelActionListener(e1 -> registerView.dispose());
            
            // Handle window closing
            registerView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loginView.setVisible(true);
                }
            });
            
            loginView.setVisible(false);
            
            // Apply maximized state if needed
            if (wasMaximized) {
                registerView.maximizeIfNeeded(true);
            }
            
            registerView.setVisible(true);
        }
    }
    
    class RegisterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get user input
            String username = registerView.getUsername();
            String password = registerView.getPassword();
            String confirmPassword = registerView.getConfirmPassword();
            String fullName = registerView.getFullName();
            String email = registerView.getEmail();
            String phoneNumber = registerView.getPhoneNumber();
            String role = registerView.getSelectedRole();
            
            // Validate inputs
            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || 
                fullName.isEmpty() || email.isEmpty()) {
                registerView.showError("Required fields cannot be empty");
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                registerView.showError("Passwords do not match");
                return;
            }
            
            // Check if username already exists
            if (userDAO.checkUsernameExists(username)) {
                registerView.showError("Username already exists. Please choose another.");
                return;
            }
            
            // Create user object
            User user = new User();
            user.setUsername(username);
            user.setPasswordHash(PasswordUtils.hashPassword(password));
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            user.setRole(role);
            
            // Register user
            boolean success = userDAO.register(user);
            
            if (success) {
                registerView.showSuccess("Registration successful! You can now login.");
                registerView.dispose();
                loginView.setVisible(true);
            } else {
                registerView.showError("Registration failed. Please try again.");
            }
        }
    }
    
    private void logout() {
        mainView.dispose();
        loginView.clearFields();
        loginView.setVisible(true);
    }
} 