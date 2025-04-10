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
    private static final int MIN_GRID_COLUMNS = 7;
    private static final int GRID_HGAP = 20;
    private static final int GRID_VGAP = 20;
    private static final int BOOK_CARD_WIDTH = 180;
    private static final int BOOK_CARD_HEIGHT = 280;
    private static final int DETAILS_PANEL_WIDTH = 280;
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
            JScrollPane.VERTICAL_SCROLLBAR_NEVER,
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
        if (book == null) {
            detailsPanel.setVisible(false);
            return;
        }

        detailsPanel.removeAll();
        
        // Create a wrapper panel to center the content vertically
        JPanel contentWrapper = new JPanel();
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setBackground(Color.WHITE);
        contentWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentWrapper.setMaximumSize(new Dimension(DETAILS_PANEL_WIDTH, Integer.MAX_VALUE));

        // Create header panel with close button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(4, 8, 4, 8));
        headerPanel.setMaximumSize(new Dimension(DETAILS_PANEL_WIDTH, 32));
        
        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Inter", Font.PLAIN, 18));
        closeButton.setForeground(LaravelTheme.MUTED_TEXT);
        closeButton.setBorder(null);
        closeButton.setContentAreaFilled(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> detailsPanel.setVisible(false));
        headerPanel.add(closeButton, BorderLayout.EAST);
        
        contentWrapper.add(headerPanel);

        // Create a panel for the book cover
        JPanel coverPanel = new JPanel();
        coverPanel.setBackground(Color.WHITE);
        coverPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY),
            new EmptyBorder(4, 4, 4, 4)
        ));
        coverPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        coverPanel.setMaximumSize(new Dimension(220, 300));

        // Add book cover image
        if (book.getCoverImage() != null) {
            ImageIcon coverIcon = new ImageIcon(book.getCoverImage());
            Image scaledImage = coverIcon.getImage().getScaledInstance(200, 280, Image.SCALE_SMOOTH);
            JLabel coverLabel = new JLabel(new ImageIcon(scaledImage));
            coverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            coverPanel.add(coverLabel);
        }

        // Add cover panel with minimal padding
        JPanel coverWrapper = new JPanel();
        coverWrapper.setLayout(new BoxLayout(coverWrapper, BoxLayout.Y_AXIS));
        coverWrapper.setBackground(Color.WHITE);
        coverWrapper.setBorder(new EmptyBorder(8, 0, 8, 0));
        coverWrapper.setMaximumSize(new Dimension(DETAILS_PANEL_WIDTH, 316));  // Cover height + padding
        coverWrapper.add(coverPanel);
        contentWrapper.add(coverWrapper);

        // Title and author panel
        JPanel titleAuthorPanel = new JPanel();
        titleAuthorPanel.setLayout(new BoxLayout(titleAuthorPanel, BoxLayout.Y_AXIS));
        titleAuthorPanel.setBackground(Color.WHITE);
        titleAuthorPanel.setBorder(new EmptyBorder(0, 4, 4, 4));
        titleAuthorPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleAuthorPanel.setMaximumSize(new Dimension(DETAILS_PANEL_WIDTH - 8, 60));  // Fixed height for title area

        // Title
        JTextPane titlePane = new JTextPane();
        titlePane.setText(book.getTitle());
        titlePane.setFont(new Font("Inter", Font.BOLD, 14));
        titlePane.setEditable(false);
        titlePane.setBackground(Color.WHITE);
        titlePane.setBorder(new EmptyBorder(0, 0, 0, 0));
        titlePane.setMargin(new Insets(0, 0, 0, 0));
        
        StyledDocument doc = titlePane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        titlePane.setMaximumSize(new Dimension(DETAILS_PANEL_WIDTH - 16, 40));
        titleAuthorPanel.add(titlePane);

        // Author
        JLabel authorLabel = new JLabel("by " + book.getAuthor(), SwingConstants.CENTER);
        authorLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        authorLabel.setForeground(LaravelTheme.MUTED_TEXT);
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        authorLabel.setBorder(new EmptyBorder(2, 0, 0, 0));
        titleAuthorPanel.add(authorLabel);

        contentWrapper.add(titleAuthorPanel);

        // Book info panel
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(4, 12, 8, 12));
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.setMaximumSize(new Dimension(DETAILS_PANEL_WIDTH, 120));  // Fixed height for info area
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 0, 2, 8);

        addInfoRow(infoPanel, "ISBN:", book.getIsbn(), gbc);
        addInfoRow(infoPanel, "Category:", book.getCategory(), gbc);
        addInfoRow(infoPanel, "Genre:", book.getGenre(), gbc);
        addInfoRow(infoPanel, "Publication Year:", String.valueOf(book.getPublicationYear()), gbc);
        addInfoRow(infoPanel, "Available Copies:", String.valueOf(book.getCopiesAvailable()), gbc);

        contentWrapper.add(infoPanel);

        // Remove borrow button panel and related code
        detailsPanel.add(Box.createVerticalGlue());
        detailsPanel.add(contentWrapper);
        detailsPanel.add(Box.createVerticalGlue());

        detailsPanel.revalidate();
        detailsPanel.repaint();
    }

    private void addInfoRow(JPanel panel, String label, String value, GridBagConstraints gbc) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Inter", Font.BOLD, 12));
        panel.add(labelComponent, gbc);

        gbc.gridx = 1;
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Inter", Font.PLAIN, 12));
        panel.add(valueComponent, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240)));
        panel.setPreferredSize(new Dimension(DETAILS_PANEL_WIDTH, 0));
        panel.setMaximumSize(new Dimension(DETAILS_PANEL_WIDTH, Integer.MAX_VALUE));
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
        int lastRow = -1;
        for (Book book : currentBooks) {
            int currentRow = column / MIN_GRID_COLUMNS;
            gbc.gridx = column % MIN_GRID_COLUMNS;
            gbc.gridy = currentRow;
            
            // Reset weightx for each cell in the row
            gbc.weightx = 0.0;
            
            // Add the book card
            booksGridPanel.add(createBookCard(book), gbc);
            column++;
            lastRow = currentRow;
        }

        // Add filler component to push everything to the left and top
        gbc.gridx = MIN_GRID_COLUMNS;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridheight = lastRow + 1;  // Span all rows
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
} 