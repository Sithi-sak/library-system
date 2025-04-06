package org.lkw;

import com.formdev.flatlaf.FlatLightLaf;
import org.lkw.controller.AuthController;
import org.lkw.data.util.DBConnection;
import org.lkw.view.LoginView;

import javax.swing.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        // Set up FlatLaf look and feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf");
        }
        
        // Test database connection
        Connection conn = DBConnection.connect();
        if (conn != null) {
            // Start the application
            SwingUtilities.invokeLater(() -> {
                LoginView loginView = new LoginView();
                new AuthController(loginView);
                loginView.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(null, 
                    "Could not connect to database. Please check your database connection.", 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}