package org.lkw.view.user;

import org.lkw.data.dao.BookDAO;
import org.lkw.data.util.LaravelTheme;
import org.lkw.model.Book;
import org.lkw.model.Category;
import org.lkw.model.Genre;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.SimpleAttributeSet;

public class UserBooksPanel extends JPanel {
    private static final int MIN_GRID_COLUMNS = 5;
    private static final int GRID_HGAP = 20;
    private static final int GRID_VGAP = 20;
    private static final int BOOK_CARD_WIDTH = 180;
    private static final int BOOK_CARD_HEIGHT = 280;
    private static final int DETAILS_PANEL_WIDTH = 300;
    private static final int CONTAINER_PADDING = 20;
    
    private final BookDAO bookDAO;
    private JPanel booksGridPanel;
    private JPanel detailsPanel;
    private JTextField searchField;
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> genreComboBox;
    private List<Book> currentBooks;
    private Book selectedBook;

    public UserBooksPanel() {
        this.bookDAO = new BookDAO();
        setLayout(new BorderLayout());
        setBackground(LaravelTheme.BACKGROUND_COLOR);

        // Main panel with GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Create details panel first
        detailsPanel = createDetailsPanel();
        detailsPanel.setVisible(false);

        // Wrapper panel for content and details
        JPanel wrapperPanel = new JPanel(new BorderLayout(20, 0)); // Add 20px spacing between main content and details
        wrapperPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);
        wrapperPanel.setBorder(new EmptyBorder(CONTAINER_PADDING, CONTAINER_PADDING, CONTAINER_PADDING, CONTAINER_PADDING));

        // Add search panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, CONTAINER_PADDING, 0);
        JPanel searchPanel = createSearchPanel();
        mainPanel.add(searchPanel, gbc);

        // Add books panel
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel booksPanel = createBooksPanel();
        mainPanel.add(booksPanel, gbc);

        wrapperPanel.add(mainPanel, BorderLayout.CENTER);
        wrapperPanel.add(detailsPanel, BorderLayout.EAST);

        add(wrapperPanel, BorderLayout.CENTER);

        // Load initial data
        loadCategories();
        loadGenres();
        loadBooks();
    }

    private JPanel createSearchPanel() {
        // Main search panel with shadow-like border
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)), // Lighter border
            new EmptyBorder(0, 0, 0, 0)
        ));

        // Content wrapper with padding
        JPanel wrapper = new JPanel(new BorderLayout(15, 0));
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(new EmptyBorder(12, 15, 12, 15));

        // Search field with icon placeholder
        JPanel searchWrapper = new JPanel(new BorderLayout(8, 0));
        searchWrapper.setBackground(Color.WHITE);
        searchWrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(240, 240, 240)),
            new EmptyBorder(8, 12, 8, 12)
        ));
        
        // Search icon
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setForeground(LaravelTheme.MUTED_TEXT);
        searchWrapper.add(searchIcon, BorderLayout.WEST);

        // Search field
        searchField = new JTextField();
        searchField.setBorder(null);
        searchField.setFont(new Font("Inter", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(250, 24));
        searchField.addActionListener(e -> searchBooks());
        searchWrapper.add(searchField, BorderLayout.CENTER);

        // Filters panel
        JPanel filtersPanel = new JPanel();
        filtersPanel.setLayout(new BoxLayout(filtersPanel, BoxLayout.X_AXIS));
        filtersPanel.setBackground(Color.WHITE);

        // Category filter
        JLabel categoryLabel = new JLabel("Category");
        categoryLabel.setFont(new Font("Inter", Font.PLAIN, 13));
        categoryLabel.setForeground(LaravelTheme.TEXT_DARK);
        categoryComboBox = new JComboBox<>();
        categoryComboBox.setFont(new Font("Inter", Font.PLAIN, 13));
        categoryComboBox.setPreferredSize(new Dimension(160, 40));
        categoryComboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(240, 240, 240)),
            new EmptyBorder(4, 8, 4, 8)
        ));
        categoryComboBox.setBackground(Color.WHITE);
        categoryComboBox.addActionListener(e -> filterBooks());

        // Genre filter
        JLabel genreLabel = new JLabel("Genre");
        genreLabel.setFont(new Font("Inter", Font.PLAIN, 13));
        genreLabel.setForeground(LaravelTheme.TEXT_DARK);
        genreComboBox = new JComboBox<>();
        genreComboBox.setFont(new Font("Inter", Font.PLAIN, 13));
        genreComboBox.setPreferredSize(new Dimension(160, 40));
        genreComboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(240, 240, 240)),
            new EmptyBorder(4, 8, 4, 8)
        ));
        genreComboBox.setBackground(Color.WHITE);
        genreComboBox.addActionListener(e -> filterBooks());

        // Add components with consistent spacing
        filtersPanel.add(Box.createHorizontalStrut(20));
        filtersPanel.add(categoryLabel);
        filtersPanel.add(Box.createHorizontalStrut(8));
        filtersPanel.add(categoryComboBox);
        filtersPanel.add(Box.createHorizontalStrut(20));
        filtersPanel.add(genreLabel);
        filtersPanel.add(Box.createHorizontalStrut(8));
        filtersPanel.add(genreComboBox);

        wrapper.add(searchWrapper, BorderLayout.WEST);
        wrapper.add(filtersPanel, BorderLayout.CENTER);
        panel.add(wrapper, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBooksPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(LaravelTheme.BACKGROUND_COLOR);

        // Create books grid panel
        booksGridPanel = new JPanel(new GridBagLayout());
        booksGridPanel.setBackground(LaravelTheme.BACKGROUND_COLOR);

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(
            booksGridPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setBorder(null);
        scrollPane.setBackground(LaravelTheme.BACKGROUND_COLOR);
        scrollPane.getViewport().setBackground(LaravelTheme.BACKGROUND_COLOR);
        scrollPane.getViewport().setBorder(null);
        scrollPane.setViewportBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        container.add(scrollPane, BorderLayout.CENTER);
        return container;
    }

    private JPanel createBookCard(Book book) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setPreferredSize(new Dimension(BOOK_CARD_WIDTH, BOOK_CARD_HEIGHT));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                selectedBook = book;
                updateDetailsPanel(book);
                detailsPanel.setVisible(true);
            }
        });

        // Book cover
        if (book.getCoverImage() != null) {
            ImageIcon coverIcon = new ImageIcon(book.getCoverImage());
            Image scaledImage = coverIcon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
            JLabel coverLabel = new JLabel(new ImageIcon(scaledImage));
            coverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(coverLabel);
        }

        // Book info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Title (truncate if too long)
        String title = book.getTitle();
        if (title.length() > 25) {
            title = title.substring(0, 22) + "...";
        }
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(titleLabel);

        // Author
        JLabel authorLabel = new JLabel(book.getAuthor());
        authorLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        authorLabel.setForeground(LaravelTheme.MUTED_TEXT);
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(authorLabel);

        card.add(infoPanel);
        return card;
    }

    private void updateDetailsPanel(Book book) {
        detailsPanel.removeAll();

        // Main content area
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(Color.WHITE);

        // Minimal one-line header
        JPanel headerPanel = new JPanel(new BorderLayout(0, 0));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));
        headerPanel.setMaximumSize(new Dimension(DETAILS_PANEL_WIDTH, 28)); // Slightly taller for padding

        // Single line with text and close button
        JPanel titleRow = new JPanel(new BorderLayout(0, 0));
        titleRow.setBackground(Color.WHITE);
        titleRow.setBorder(new EmptyBorder(12, 12, 12, 12)); // Modern minimal padding

        JLabel detailsLabel = new JLabel("Book Details");
        detailsLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        detailsLabel.setForeground(new Color(75, 85, 99));

        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Inter", Font.PLAIN, 16));
        closeButton.setForeground(new Color(156, 163, 175));
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.setBorder(null);
        closeButton.setPreferredSize(new Dimension(16, 16)); // Smaller close button
        closeButton.addActionListener(e -> detailsPanel.setVisible(false));
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeButton.setForeground(LaravelTheme.TEXT_DARK);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeButton.setForeground(new Color(156, 163, 175));
            }
        });

        titleRow.add(detailsLabel, BorderLayout.WEST);
        titleRow.add(closeButton, BorderLayout.EAST);
        headerPanel.add(titleRow, BorderLayout.CENTER);
        mainContent.add(headerPanel);

        // Content wrapper with minimal top spacing
        JPanel contentWrapper = new JPanel();
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setBackground(Color.WHITE);
        contentWrapper.setBorder(new EmptyBorder(16, 12, 16, 12));

        // Book cover with minimal spacing
        if (book.getCoverImage() != null) {
            ImageIcon coverIcon = new ImageIcon(book.getCoverImage());
            Image scaledImage = coverIcon.getImage().getScaledInstance(180, -1, Image.SCALE_SMOOTH);
            JLabel coverLabel = new JLabel(new ImageIcon(scaledImage));
            coverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentWrapper.add(coverLabel);
            contentWrapper.add(Box.createVerticalStrut(16));  // Consistent spacing after cover
        }

        // Title and author section with fixed width wrapper
        JPanel titleAuthorWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        titleAuthorWrapper.setBackground(Color.WHITE);
        titleAuthorWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel titleAuthorPanel = new JPanel();
        titleAuthorPanel.setLayout(new BoxLayout(titleAuthorPanel, BoxLayout.Y_AXIS));
        titleAuthorPanel.setBackground(Color.WHITE);
        titleAuthorPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleAuthorPanel.setBorder(null);

        // Title with proper text wrapping and center alignment
        JTextPane titlePane = new JTextPane();
        titlePane.setText(book.getTitle());
        titlePane.setFont(new Font("Inter", Font.BOLD, 16));
        titlePane.setEditable(false);
        titlePane.setBackground(Color.WHITE);
        titlePane.setBorder(null);
        
        // Center align the text
        StyledDocument doc = titlePane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        
        // Set size constraints
        int titleWidth = 220;
        titlePane.setSize(titleWidth, Short.MAX_VALUE);
        int preferredHeight = titlePane.getPreferredSize().height;
        titlePane.setPreferredSize(new Dimension(titleWidth, preferredHeight));
        titlePane.setMaximumSize(new Dimension(titleWidth, preferredHeight));
        
        JLabel authorLabel = new JLabel("by " + book.getAuthor());
        authorLabel.setFont(new Font("Inter", Font.PLAIN, 13));
        authorLabel.setForeground(LaravelTheme.MUTED_TEXT);
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleAuthorPanel.add(titlePane);
        titleAuthorPanel.add(Box.createVerticalStrut(4));
        titleAuthorPanel.add(authorLabel);
        
        // Add the title-author panel to the fixed-width wrapper
        titleAuthorWrapper.add(titleAuthorPanel);
        
        contentWrapper.add(titleAuthorWrapper);
        contentWrapper.add(Box.createVerticalStrut(16));

        // Book info panel with minimal padding
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(250, 250, 250));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(240, 240, 240)),
                new EmptyBorder(12, 12, 12, 12)
        ));

        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.setMaximumSize(new Dimension(DETAILS_PANEL_WIDTH - 24, Integer.MAX_VALUE));

        addInfoRow(infoPanel, "ISBN", book.getIsbn());
        addInfoRow(infoPanel, "Category", book.getCategory());
        addInfoRow(infoPanel, "Genre", book.getGenre());
        addInfoRow(infoPanel, "Publication Year", String.valueOf(book.getPublicationYear()));
        addInfoRow(infoPanel, "Available Copies", String.valueOf(book.getCopiesAvailable()));

        contentWrapper.add(infoPanel);
        contentWrapper.add(Box.createVerticalStrut(12));

        // Action button section
        if (book.getCopiesAvailable() > 0) {
            JButton borrowButton = new JButton("Borrow Book");
            borrowButton.setFont(new Font("Inter", Font.BOLD, 13));
            borrowButton.setBackground(LaravelTheme.PRIMARY_RED);
            borrowButton.setForeground(Color.WHITE);
            borrowButton.setBorder(new EmptyBorder(10, 0, 10, 0));  // Increased vertical padding
            borrowButton.setFocusPainted(false);
            borrowButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            borrowButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            borrowButton.setMaximumSize(new Dimension(DETAILS_PANEL_WIDTH - 24, 40));  // Full width minus padding
            borrowButton.setPreferredSize(new Dimension(DETAILS_PANEL_WIDTH - 24, 40));  // Same as maximum size
            borrowButton.addActionListener(e -> borrowBook(book));
            contentWrapper.add(borrowButton);
        } else {
            JPanel unavailablePanel = new JPanel();
            unavailablePanel.setBackground(new Color(254, 242, 242));
            unavailablePanel.setBorder(new EmptyBorder(8, 12, 8, 12));
            unavailablePanel.setMaximumSize(new Dimension(DETAILS_PANEL_WIDTH - 24, 40));  // Match borrow button height
            
            JLabel unavailableLabel = new JLabel("Currently Unavailable");
            unavailableLabel.setFont(new Font("Inter", Font.BOLD, 13));
            unavailableLabel.setForeground(new Color(153, 27, 27));
            unavailablePanel.add(unavailableLabel);
            
            contentWrapper.add(unavailablePanel);
        }

        mainContent.add(contentWrapper);
        detailsPanel.add(mainContent);
        detailsPanel.revalidate();
        detailsPanel.repaint();
    }

    private void addInfoRow(JPanel panel, String label, String value) {
        JPanel rowPanel = new JPanel(new BorderLayout(8, 0));
        rowPanel.setBackground(new Color(250, 250, 250));
        rowPanel.setMaximumSize(new Dimension(DETAILS_PANEL_WIDTH - 24, 24));
        rowPanel.setBorder(new EmptyBorder(0, 0, 6, 0));

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Inter", Font.PLAIN, 13));
        labelComponent.setForeground(new Color(75, 85, 99));

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Inter", Font.PLAIN, 13));
        valueComponent.setForeground(LaravelTheme.TEXT_DARK);

        rowPanel.add(labelComponent, BorderLayout.WEST);
        rowPanel.add(valueComponent, BorderLayout.EAST);
        panel.add(rowPanel);
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240)));
        panel.setPreferredSize(new Dimension(DETAILS_PANEL_WIDTH, 0));
        return panel;
    }

    private void loadCategories() {
        categoryComboBox.addItem("All Categories");
        List<Category> categories = bookDAO.getAllCategories();
        for (Category category : categories) {
            categoryComboBox.addItem(category.getCategoryName());
        }
    }

    private void loadGenres() {
        genreComboBox.addItem("All Genres");
        List<Genre> genres = bookDAO.getAllGenres();
        for (Genre genre : genres) {
            genreComboBox.addItem(genre.getGenreName());
        }
    }

    private void loadBooks() {
        booksGridPanel.removeAll();
        String searchQuery = searchField.getText().trim();
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        String selectedGenre = (String) genreComboBox.getSelectedItem();

        currentBooks = bookDAO.searchBooks(
            searchQuery.isEmpty() ? null : searchQuery,
            "All Categories".equals(selectedCategory) ? null : selectedCategory,
            "All Genres".equals(selectedGenre) ? null : selectedGenre
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, GRID_VGAP, GRID_HGAP);

        int column = 0;
        for (Book book : currentBooks) {
            gbc.gridx = column % MIN_GRID_COLUMNS;
            gbc.gridy = column / MIN_GRID_COLUMNS;
            booksGridPanel.add(createBookCard(book), gbc);
            column++;
        }

        // Add filler component to push everything to the left
        gbc.gridx = MIN_GRID_COLUMNS;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        booksGridPanel.add(Box.createGlue(), gbc);

        booksGridPanel.revalidate();
        booksGridPanel.repaint();
    }

    private void searchBooks() {
        loadBooks();
    }

    private void filterBooks() {
        loadBooks();
    }

    private void borrowBook(Book book) {
        // TODO: Implement book borrowing functionality
        JOptionPane.showMessageDialog(
            this,
            "Borrowing functionality will be implemented soon.",
            "Not Implemented",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
} 