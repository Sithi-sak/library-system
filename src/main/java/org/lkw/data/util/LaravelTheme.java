package org.lkw.data.util;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LaravelTheme {
    // Laravel theme colors
    public static final Color PRIMARY_RED = new Color(220, 38, 38);
    public static final Color DANGER_RED = new Color(239, 68, 68);
    public static final Color DARK_RED = new Color(227, 36, 43);
    public static final Color LIGHT_GRAY = new Color(249, 250, 251);
    public static final Color BORDER_GRAY = new Color(229, 231, 235);
    public static final Color TEXT_DARK = new Color(17, 24, 39);
    public static final Color MUTED_TEXT = new Color(107, 114, 128);
    public static final Color LABEL_COLOR = new Color(77, 77, 77);
    public static final Color BACKGROUND_COLOR = new Color(248, 250, 252);
    
    // paddings
    private static final int FIELD_HEIGHT = 38;
    private static final int BUTTON_HEIGHT = 40;
    private static final Insets BUTTON_PADDING = new Insets(10, 20, 10, 20);
    private static final int MAX_CONTENT_WIDTH = 600;

    public static void styleTextField(JTextField textField) {
        textField.setBorder(createInputBorder());
        textField.setPreferredSize(new Dimension(300, FIELD_HEIGHT));
        textField.setMinimumSize(new Dimension(250, FIELD_HEIGHT));
        textField.setFont(new Font("Inter", Font.PLAIN, 13));
    }

    public static void stylePasswordField(JPasswordField passwordField) {
        passwordField.setBorder(createInputBorder());
        passwordField.setPreferredSize(new Dimension(300, FIELD_HEIGHT));
        passwordField.setMinimumSize(new Dimension(250, FIELD_HEIGHT));
        passwordField.setFont(new Font("Inter", Font.PLAIN, 13));
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setBorder(BorderFactory.createLineBorder(BORDER_GRAY, 1, true));
        comboBox.setBackground(Color.WHITE);
        comboBox.setPreferredSize(new Dimension(300, FIELD_HEIGHT));
        comboBox.setMinimumSize(new Dimension(250, FIELD_HEIGHT));
        comboBox.setFont(new Font("Inter", Font.PLAIN, 13));
        
        comboBox.setMaximumRowCount(10);
    }

    public static void styleLabel(JLabel label, boolean isBold) {
        label.setFont(new Font("Inter", isBold ? Font.BOLD : Font.PLAIN, 13));
        label.setForeground(LABEL_COLOR);
    }
    
    public static void styleTitle(JLabel label) {
        label.setFont(new Font("Inter", Font.BOLD, 20));
        label.setForeground(PRIMARY_RED);
    }
    
    public static void stylePrimaryButton(JButton button) {
        button.setFont(new Font("Inter", Font.BOLD, 13));
        button.setBackground(PRIMARY_RED);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(
            BUTTON_PADDING.top, 
            BUTTON_PADDING.left, 
            BUTTON_PADDING.bottom, 
            BUTTON_PADDING.right
        ));
        button.setPreferredSize(new Dimension(button.getPreferredSize().width, BUTTON_HEIGHT));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(DARK_RED);
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(PRIMARY_RED);
            }
        });
    }
    
    public static void styleSecondaryButton(JButton button) {
        button.setFont(new Font("Inter", Font.BOLD, 13));
        button.setBackground(LIGHT_GRAY);
        button.setForeground(TEXT_DARK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GRAY, 1, true),
            BorderFactory.createEmptyBorder(
                BUTTON_PADDING.top - 1, 
                BUTTON_PADDING.left - 1, 
                BUTTON_PADDING.bottom - 1, 
                BUTTON_PADDING.right - 1
            )
        ));
        button.setPreferredSize(new Dimension(button.getPreferredSize().width, BUTTON_HEIGHT));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    public static Border createInputBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GRAY, 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        );
    }
    
    public static JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        return panel;
    }
    
    public static JPanel createCard() {
        JPanel panel = new JPanel();
        panel.setBackground(LIGHT_GRAY);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GRAY, 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        return panel;
    }
    
    public static JPanel createResponsiveContainer(JPanel contentPanel) {
        JPanel outerContainer = new JPanel();
        outerContainer.setBackground(BACKGROUND_COLOR);
        outerContainer.setLayout(new GridBagLayout());
        
        contentPanel.setBackground(Color.WHITE);
        
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        wrapperPanel.add(contentPanel, BorderLayout.CENTER);
        wrapperPanel.setMaximumSize(new Dimension(MAX_CONTENT_WIDTH, Integer.MAX_VALUE));
        wrapperPanel.setPreferredSize(new Dimension(
            Math.min(contentPanel.getPreferredSize().width, MAX_CONTENT_WIDTH),
            contentPanel.getPreferredSize().height
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        
        outerContainer.add(wrapperPanel, gbc);
        return outerContainer;
    }
    
    public static void configureFrame(JFrame frame) {
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(350, 300));
        frame.pack();
    }
} 