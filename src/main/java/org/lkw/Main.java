package org.lkw;

import com.formdev.flatlaf.FlatLightLaf;
import org.lkw.controller.AuthController;
import org.lkw.data.util.DBConnection;
import org.lkw.view.LoginView;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf");
        }
        
        try (Connection conn = DBConnection.connect()) {
            if (conn != null) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}