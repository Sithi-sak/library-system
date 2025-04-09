package org.lkw.view.admin;

import org.lkw.data.dao.BookDAO;
import org.lkw.data.dao.CategoryDAO;
import org.lkw.data.dao.GenreDAO;
import org.lkw.data.dao.PublisherDAO;
import org.lkw.data.util.LaravelTheme;
import org.lkw.model.Book;
import org.lkw.model.Category;
import org.lkw.model.Genre;
import org.lkw.model.Publisher;
import org.lkw.view.AdminView;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class BooksPanel extends JPanel {
    private final BookDAO bookDAO;
    private final CategoryDAO categoryDAO;
    private final GenreDAO genreDAO;
    private final PublisherDAO publisherDAO;
    private final JTable booksTable;
    private final DefaultTableModel tableModel;
    private final JComboBox<String> categoryFilterBox;
    private final JComboBox<String> genreFilterBox;
    private final JTextField titleField;
    private final JTextField authorField;
    private final JTextField isbnField;
    private final JComboBox<String> categoryComboBox;
    private final JComboBox<String> genreComboBox;
    private final JSpinner copiesSpinner;
    private final JLabel coverImageLabel;
    private final JButton addButton;
    private final JButton editButton;
    private final JButton deleteButton;
    private String selectedImagePath;
    private int selectedBookId = -1;
    private static final Font TITLE_FONT = new Font("Inter", Font.BOLD, 20);
    private final JComboBox<String> publisherComboBox;
    private final JTextField yearField;
    private boolean isEditing = false;
    private final JButton saveButton;
    private final JButton cancelButton;

    public BooksPanel() {
        bookDAO = new BookDAO();
        categoryDAO = new CategoryDAO();
        genreDAO = new GenreDAO();
        publisherDAO = new PublisherDAO();

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // Top panel with filters
        JPanel filterPanel = LaravelTheme.createPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, LaravelTheme.BORDER_GRAY),
            new EmptyBorder(0, 0, 15, 0)
        ));

        // Title
        JLabel titleLabel = new JLabel("Books Management");
        LaravelTheme.styleTitle(titleLabel);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.add(titleLabel);
        filterPanel.add(Box.createVerticalStrut(15));

        // Filters row
        JPanel filtersRow = LaravelTheme.createPanel();
        filtersRow.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));
        filtersRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Category filter
        JLabel categoryLabel = new JLabel("Filter by:");
        LaravelTheme.styleLabel(categoryLabel, true);
        categoryFilterBox = new JComboBox<>();
        LaravelTheme.styleComboBox(categoryFilterBox);
        categoryFilterBox.setPreferredSize(new Dimension(200, 35));
        
        // Genre filter
        genreFilterBox = new JComboBox<>();
        LaravelTheme.styleComboBox(genreFilterBox);
        genreFilterBox.setPreferredSize(new Dimension(200, 35));

        // Search field
        JTextField searchField = new JTextField(20);
        LaravelTheme.styleTextField(searchField);
        searchField.setPreferredSize(new Dimension(250, 35));
        searchField.putClientProperty("JTextField.placeholderText", "Search by title or author...");

        filtersRow.add(categoryLabel);
        filtersRow.add(categoryFilterBox);
        filtersRow.add(Box.createHorizontalStrut(10));
        filtersRow.add(genreFilterBox);
        filtersRow.add(Box.createHorizontalStrut(15));
        filtersRow.add(searchField);

        filterPanel.add(filtersRow);

        // Main content split
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.8);
        splitPane.setEnabled(false);
        splitPane.setBorder(null);
        splitPane.setDividerSize(0); // Remove the divider completely
        splitPane.setBackground(Color.WHITE);

        // Left panel with table
        JPanel leftPanel = LaravelTheme.createPanel();
        leftPanel.setLayout(new BorderLayout());
        
        // Table setup
        tableModel = new DefaultTableModel(
            new String[]{"Title", "Author", "Category", "Genre", "Available", "Total"},
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        booksTable = new JTable(tableModel);
        booksTable.setRowHeight(40);
        booksTable.setShowGrid(true);
        booksTable.setGridColor(LaravelTheme.BORDER_GRAY);
        booksTable.setBackground(Color.WHITE);
        booksTable.setSelectionBackground(LaravelTheme.LIGHT_GRAY);
        booksTable.setSelectionForeground(LaravelTheme.TEXT_DARK);
        booksTable.getTableHeader().setBackground(Color.WHITE);
        booksTable.getTableHeader().setFont(new Font("Inter", Font.BOLD, 13));
        booksTable.setFont(new Font("Inter", Font.PLAIN, 13));

        // Set column widths
        booksTable.getColumnModel().getColumn(0).setPreferredWidth(300); // Title
        booksTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Author
        booksTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Category
        booksTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Genre
        booksTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Available
        booksTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Total
        
        JScrollPane tableScrollPane = new JScrollPane(booksTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY));
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        leftPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = LaravelTheme.createPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        addButton = new JButton("Add New Book");
        editButton = new JButton("Edit Book");
        deleteButton = new JButton("Delete Book");
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        
        LaravelTheme.stylePrimaryButton(addButton);
        LaravelTheme.styleSecondaryButton(editButton);
        LaravelTheme.styleSecondaryButton(deleteButton);
        LaravelTheme.stylePrimaryButton(saveButton);
        LaravelTheme.styleSecondaryButton(cancelButton);
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Right panel with book details
        JPanel rightPanel = LaravelTheme.createPanel();
        rightPanel.setLayout(new BorderLayout(0, 20));
        rightPanel.setBorder(new EmptyBorder(0, 20, 0, 0));

        // Book details form
        JPanel detailsPanel = LaravelTheme.createCard();
        detailsPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Form fields
        titleField = createFormField(detailsPanel, "Title:", gbc, 0);
        authorField = createFormField(detailsPanel, "Author:", gbc, 1);
        isbnField = createFormField(detailsPanel, "ISBN:", gbc, 2);
        
        // Publisher combo
        publisherComboBox = createFormComboBox(detailsPanel, "Publisher:", gbc, 3);
        loadPublishers(); // Load publishers into combo box
        
        // Publication Year field
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel yearLabel = new JLabel("Publication Year:");
        LaravelTheme.styleLabel(yearLabel, false);
        detailsPanel.add(yearLabel, gbc);
        
        gbc.gridx = 1;
        yearField = new JTextField(20);
        LaravelTheme.styleTextField(yearField);
        detailsPanel.add(yearField, gbc);
        
        // Category and Genre combos
        categoryComboBox = createFormComboBox(detailsPanel, "Category:", gbc, 5);
        genreComboBox = createFormComboBox(detailsPanel, "Genre:", gbc, 6);
        loadCategories(); // Load categories into combo box
        loadGenres(); // Load genres into combo box
        
        // Copies spinner
        gbc.gridx = 0;
        gbc.gridy = 7;
        JLabel copiesLabel = new JLabel("Copies:");
        LaravelTheme.styleLabel(copiesLabel, false);
        detailsPanel.add(copiesLabel, gbc);
        
        gbc.gridx = 1;
        SpinnerNumberModel copiesModel = new SpinnerNumberModel(1, 0, 999, 1);
        copiesSpinner = new JSpinner(copiesModel);
        copiesSpinner.setBorder(LaravelTheme.createInputBorder());
        ((JSpinner.DefaultEditor) copiesSpinner.getEditor()).getTextField().setFont(new Font("Inter", Font.PLAIN, 13));
        detailsPanel.add(copiesSpinner, gbc);

        // Cover image panel
        JPanel coverPanel = LaravelTheme.createCard();
        coverPanel.setLayout(new BorderLayout(5, 10));
        
        coverImageLabel = new JLabel();
        coverImageLabel.setPreferredSize(new Dimension(200, 250));
        coverImageLabel.setBorder(BorderFactory.createLineBorder(LaravelTheme.BORDER_GRAY));
        coverImageLabel.setHorizontalAlignment(JLabel.CENTER);
        coverImageLabel.setBackground(LaravelTheme.LIGHT_GRAY);
        coverImageLabel.setOpaque(true);
        
        JButton chooseImageButton = new JButton("Choose Image");
        LaravelTheme.styleSecondaryButton(chooseImageButton);
        
        coverPanel.add(coverImageLabel, BorderLayout.CENTER);
        coverPanel.add(chooseImageButton, BorderLayout.SOUTH);

        // Add components to right panel
        rightPanel.add(detailsPanel, BorderLayout.NORTH);
        rightPanel.add(coverPanel, BorderLayout.CENTER);

        // Add panels to split pane
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        // Add components to main panel
        add(filterPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        // Load initial data
        loadBooks();
        updateButtonStates();

        // Update event listeners
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                performSearch(searchField.getText());
            }
        });

        categoryFilterBox.addActionListener(e -> {
            String selectedCategory = (String) categoryFilterBox.getSelectedItem();
            if (selectedCategory != null) {
                // Update genre filter based on category
                genreFilterBox.removeAllItems();
                genreFilterBox.addItem("All Genres");
                
                if (!selectedCategory.equals("All Categories")) {
                    List<Genre> genres = genreDAO.getAllGenres();
                    for (Genre genre : genres) {
                        genreFilterBox.addItem(genre.getGenreName());
                    }
                }
                performSearch(searchField.getText());
            }
        });

        genreFilterBox.addActionListener(e -> performSearch(searchField.getText()));
        
        booksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = booksTable.getSelectedRow();
                if (row != -1) {
                    String title = (String) booksTable.getValueAt(row, 0);
                    Book book = bookDAO.getBookByTitle(title);
                    if (book != null) {
                        selectedBookId = book.getBookId();
                        showBookDetails(book);
                        updateButtonStates();
                    }
                }
            }
        });

        addButton.addActionListener(e -> {
            startNewBook();
        });
        
        editButton.addActionListener(e -> {
            if (selectedBookId != -1) {
                startEditing();
            }
        });
        
        saveButton.addActionListener(e -> saveBook());
        
        cancelButton.addActionListener(e -> {
            if (selectedBookId != -1) {
                // If editing existing book, restore its details
                Book book = bookDAO.getBookById(selectedBookId);
                if (book != null) {
                    showBookDetails(book);
                }
            } else {
                // If adding new book, just clear the form
                clearForm();
            }
            setEditingMode(false);
        });
        
        deleteButton.addActionListener(e -> deleteBook());
        chooseImageButton.addActionListener(e -> chooseImage());

        // Initialize button states
        setEditingMode(false);
    }

    private JTextField createFormField(JPanel panel, String label, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel jLabel = new JLabel(label);
        LaravelTheme.styleLabel(jLabel, false);
        panel.add(jLabel, gbc);
        
        gbc.gridx = 1;
        JTextField field = new JTextField(20);
        LaravelTheme.styleTextField(field);
        panel.add(field, gbc);
        return field;
    }

    private JComboBox<String> createFormComboBox(JPanel panel, String label, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel jLabel = new JLabel(label);
        LaravelTheme.styleLabel(jLabel, false);
        panel.add(jLabel, gbc);
        
        gbc.gridx = 1;
        JComboBox<String> comboBox = new JComboBox<>();
        LaravelTheme.styleComboBox(comboBox);
        panel.add(comboBox, gbc);
        return comboBox;
    }

    private void showBookDetails(Book book) {
        if (book != null) {
            titleField.setText(book.getTitle());
            authorField.setText(book.getAuthor());
            isbnField.setText(book.getIsbn());
            copiesSpinner.setValue(book.getCopiesAvailable());
            
            Publisher publisher = publisherDAO.getPublisherById(book.getPublisherId());
            if (publisher != null) {
                publisherComboBox.setSelectedItem(publisher.getName());
            }
            
            yearField.setText(String.valueOf(book.getPublicationYear()));
            
            Category category = categoryDAO.getCategoryById(book.getCategoryId());
            if (category != null) {
                categoryComboBox.setSelectedItem(category.getCategoryName());
            }
            
            Genre genre = genreDAO.getGenreById(book.getGenreId());
            if (genre != null) {
                genreComboBox.setSelectedItem(genre.getGenreName());
            }
            
            // Load cover image
            byte[] coverImage = book.getCoverImage();
            if (coverImage != null && coverImage.length > 0) {
                try {
                    ImageIcon icon = new ImageIcon(coverImage);
                    Image img = icon.getImage();
                    Image scaledImg = img.getScaledInstance(200, 250, Image.SCALE_SMOOTH);
                    coverImageLabel.setIcon(new ImageIcon(scaledImg));
                } catch (Exception e) {
                    e.printStackTrace();
                    coverImageLabel.setIcon(null);
                }
            } else {
                coverImageLabel.setIcon(null);
            }
            
            // Disable fields if not in editing mode
            setEditingMode(false);
        }
    }

    private void loadBooks() {
        tableModel.setRowCount(0);
        String selectedCategory = (String) categoryFilterBox.getSelectedItem();
        String selectedGenre = (String) genreFilterBox.getSelectedItem();
        
        List<Book> books = bookDAO.getAllBooks();
        
        for (Book book : books) {
            Category bookCategory = categoryDAO.getCategoryById(book.getCategoryId());
            Genre bookGenre = genreDAO.getGenreById(book.getGenreId());
            
            // Skip if category doesn't match filter
            if (selectedCategory != null && !selectedCategory.equals("All Categories") && 
                (bookCategory == null || !bookCategory.getCategoryName().equals(selectedCategory))) {
                continue;
            }
            
            // Skip if genre doesn't match filter
            if (selectedGenre != null && !selectedGenre.equals("All Genres") && 
                (bookGenre == null || !bookGenre.getGenreName().equals(selectedGenre))) {
                continue;
            }
            
            tableModel.addRow(new Object[]{
                book.getTitle(),
                book.getAuthor(),
                bookCategory != null ? bookCategory.getCategoryName() : "",
                bookGenre != null ? bookGenre.getGenreName() : "",
                book.getCopiesAvailable(),
                book.getTotalCopies()
            });
        }
    }

    private void loadCategories() {
        categoryFilterBox.removeAllItems();
        categoryComboBox.removeAllItems();
        categoryFilterBox.addItem("All Categories");
        
        List<Category> categories = categoryDAO.getAllCategories();
        for (Category category : categories) {
            categoryFilterBox.addItem(category.getCategoryName());
            categoryComboBox.addItem(category.getCategoryName());
        }
    }

    private void loadGenres() {
        genreFilterBox.removeAllItems();
        genreComboBox.removeAllItems();
        genreFilterBox.addItem("All Genres");
        
        List<Genre> genres = genreDAO.getAllGenres();
        for (Genre genre : genres) {
            genreFilterBox.addItem(genre.getGenreName());
            genreComboBox.addItem(genre.getGenreName());
        }
    }

    private void loadPublishers() {
        publisherComboBox.removeAllItems();
        List<Publisher> publishers = publisherDAO.getAllPublishers();
        for (Publisher publisher : publishers) {
            publisherComboBox.addItem(publisher.getName());
        }
    }

    private void saveBook() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String isbn = isbnField.getText().trim();
        int copies = (int) copiesSpinner.getValue();
        
        // Validate year input
        String yearText = yearField.getText().trim();
        int year;
        try {
            year = Integer.parseInt(yearText);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (year < 1800 || year > currentYear) {
                JOptionPane.showMessageDialog(this,
                    "Publication year must be between 1800 and " + currentYear,
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid publication year",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all required fields (Title, Author, ISBN)",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get IDs from selected names
        String categoryName = (String) categoryComboBox.getSelectedItem();
        String genreName = (String) genreComboBox.getSelectedItem();
        String publisherName = (String) publisherComboBox.getSelectedItem();
        
        if (categoryName == null || genreName == null || publisherName == null) {
            JOptionPane.showMessageDialog(this,
                "Please select a category, genre, and publisher",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Category category = categoryDAO.getCategoryByName(categoryName);
        Genre genre = genreDAO.getGenreByName(genreName);
        Publisher publisher = publisherDAO.getPublisherByName(publisherName);
        
        if (category == null || genre == null || publisher == null) {
            JOptionPane.showMessageDialog(this,
                "Invalid category, genre, or publisher selected",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setCopiesAvailable(copies);
        book.setTotalCopies(copies); // For new books, total copies equals available copies
        book.setPublicationYear(year);
        book.setCategoryId(category.getCategoryId());
        book.setGenreId(genre.getGenreId());
        book.setPublisherId(publisher.getPublisherId());
        
        // Handle image
        if (selectedImagePath != null) {
            try {
                // Read the image file into a byte array
                byte[] imageData = Files.readAllBytes(Paths.get(selectedImagePath));
                book.setCoverImage(imageData); // Set the actual image data
                
                // Save a copy in the covers directory for backup
                String fileName = Paths.get(selectedImagePath).getFileName().toString();
                Path targetPath = Paths.get("covers", fileName);
                Files.createDirectories(Paths.get("covers"));
                Files.copy(Paths.get(selectedImagePath), targetPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Error saving cover image",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else if (selectedBookId != -1) {
            // If editing and no new image selected, keep the existing image
            Book existingBook = bookDAO.getBookById(selectedBookId);
            if (existingBook != null) {
                book.setCoverImage(existingBook.getCoverImage());
            }
        }

        boolean success;
        if (selectedBookId == -1) {
            // Add new book
            success = bookDAO.addBook(book);
        } else {
            // Update existing book
            book.setBookId(selectedBookId);
            Book existingBook = bookDAO.getBookById(selectedBookId);
            if (existingBook != null) {
                // When editing, update total copies if available copies increased
                int newTotal = Math.max(existingBook.getTotalCopies(), copies);
                book.setTotalCopies(newTotal);
            }
            try {
                bookDAO.updateBook(book);
                success = true;
            } catch (RuntimeException e) {
                success = false;
            }
        }

        if (success) {
            loadBooks();
            setEditingMode(false);
            if (selectedBookId != -1) {
                // If we were editing, reload the book details
                Book updatedBook = bookDAO.getBookById(selectedBookId);
                if (updatedBook != null) {
                    showBookDetails(updatedBook);
                }
            } else {
                // If we were adding, clear the form
                clearForm();
                selectedBookId = -1;
            }
            
            // Refresh dashboard if this panel is part of AdminView
            Container parent = getParent();
            while (parent != null && !(parent instanceof AdminView)) {
                parent = parent.getParent();
            }
            if (parent != null) {
                ((AdminView) parent).refreshDashboard();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Error saving book",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBook() {
        if (selectedBookId != -1) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this book?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = bookDAO.deleteBook(selectedBookId);
                if (success) {
                    loadBooks();
                    clearForm();
                    selectedBookId = -1;
                    updateButtonStates();
                    
                    // Refresh dashboard if this panel is part of AdminView
                    Container parent = getParent();
                    while (parent != null && !(parent instanceof AdminView)) {
                        parent = parent.getParent();
                    }
                    if (parent instanceof AdminView) {
                        ((AdminView) parent).refreshDashboard();
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Error deleting book",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void clearForm() {
        titleField.setText("");
        authorField.setText("");
        isbnField.setText("");
        copiesSpinner.setValue(1);
        yearField.setText("");
        categoryComboBox.setSelectedIndex(0);
        genreComboBox.setSelectedIndex(0);
        publisherComboBox.setSelectedIndex(0);
        coverImageLabel.setIcon(null);
        selectedImagePath = null;
    }

    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(
            "Image files", "jpg", "jpeg", "png", "gif"));
            
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                BufferedImage img = ImageIO.read(selectedFile);
                if (img != null) {
                    Image scaledImg = img.getScaledInstance(200, 250, Image.SCALE_SMOOTH);
                    coverImageLabel.setIcon(new ImageIcon(scaledImg));
                    selectedImagePath = selectedFile.getAbsolutePath();
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Error loading image",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateButtonStates() {
        boolean isBookSelected = selectedBookId != -1;
        
        if (!isEditing) {
            addButton.setEnabled(true);
            editButton.setEnabled(isBookSelected);
            deleteButton.setEnabled(isBookSelected);
        }
    }

    private void performSearch(String searchText) {
        searchText = searchText.toLowerCase().trim();
        String selectedCategory = (String) categoryFilterBox.getSelectedItem();
        String selectedGenre = (String) genreFilterBox.getSelectedItem();
        
        tableModel.setRowCount(0);
        List<Book> books = bookDAO.getAllBooks();
        
        for (Book book : books) {
            Category bookCategory = categoryDAO.getCategoryById(book.getCategoryId());
            Genre bookGenre = genreDAO.getGenreById(book.getGenreId());
            
            // Skip if category doesn't match filter
            if (selectedCategory != null && !selectedCategory.equals("All Categories") && 
                (bookCategory == null || !bookCategory.getCategoryName().equals(selectedCategory))) {
                continue;
            }
            
            // Skip if genre doesn't match filter
            if (selectedGenre != null && !selectedGenre.equals("All Genres") && 
                (bookGenre == null || !bookGenre.getGenreName().equals(selectedGenre))) {
                continue;
            }
            
            // Skip if search text doesn't match
            if (!searchText.isEmpty() && 
                !book.getTitle().toLowerCase().contains(searchText) && 
                !book.getAuthor().toLowerCase().contains(searchText)) {
                continue;
            }
            
            tableModel.addRow(new Object[]{
                book.getTitle(),
                book.getAuthor(),
                bookCategory != null ? bookCategory.getCategoryName() : "",
                bookGenre != null ? bookGenre.getGenreName() : "",
                book.getCopiesAvailable(),
                book.getTotalCopies()
            });
        }
        
        clearForm();
        selectedBookId = -1;
        updateButtonStates();
    }

    private void startNewBook() {
        clearForm();
        selectedBookId = -1;
        setEditingMode(true);
    }

    private void startEditing() {
        setEditingMode(true);
    }

    private void setEditingMode(boolean editing) {
        isEditing = editing;
        
        // Enable/disable form fields
        titleField.setEnabled(editing);
        authorField.setEnabled(editing);
        isbnField.setEnabled(editing);
        publisherComboBox.setEnabled(editing);
        yearField.setEnabled(editing);
        categoryComboBox.setEnabled(editing);
        genreComboBox.setEnabled(editing);
        copiesSpinner.setEnabled(editing);
        
        // Show/hide appropriate buttons
        addButton.setVisible(!editing);
        editButton.setVisible(!editing);
        deleteButton.setVisible(!editing);
        saveButton.setVisible(editing);
        cancelButton.setVisible(editing);
        
        // Update button states
        updateButtonStates();
    }
} 