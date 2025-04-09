package org.lkw.data.dao;

import org.lkw.data.util.DBConnection;
import org.lkw.model.Book;
import org.lkw.model.Category;
import org.lkw.model.Genre;
import org.lkw.model.Publisher;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

public class BookDAO {
    
    // Get all books
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY date_added DESC";
        
        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setPublisherId(rs.getInt("publisher_id"));
                book.setPublicationYear(rs.getInt("publication_year"));
                book.setCategoryId(rs.getInt("category_id"));
                book.setGenreId(rs.getInt("genre_id"));
                book.setCopiesAvailable(rs.getInt("copies_available"));
                book.setTotalCopies(rs.getInt("total_copies"));
                book.setDateAdded(rs.getString("date_added"));
                book.setCoverImage(rs.getBytes("cover_image"));
                
                books.add(book);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return books;
    }
    
    // Get all categories
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY category_name";
        
        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("category_id"));
                category.setCategoryName(rs.getString("category_name"));
                category.setDescription(rs.getString("description"));
                
                categories.add(category);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return categories;
    }
    
    // Get all genres
    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT * FROM genres ORDER BY genre_name";
        
        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Genre genre = new Genre();
                genre.setGenreId(rs.getInt("genre_id"));
                genre.setGenreName(rs.getString("genre_name"));
                
                genres.add(genre);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return genres;
    }
    
    // Add a new book
    public boolean addBook(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, publisher_id, publication_year, " +
                    "category_id, genre_id, copies_available, total_copies, cover_image) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setInt(4, book.getPublisherId());
            stmt.setInt(5, book.getPublicationYear());
            stmt.setInt(6, book.getCategoryId());
            stmt.setInt(7, book.getGenreId());
            stmt.setInt(8, book.getCopiesAvailable());
            stmt.setInt(9, book.getTotalCopies());
            stmt.setBytes(10, book.getCoverImage());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Update an existing book
    public void updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, publisher = ?, publication_year = ?, " +
                    "copies_available = ?, category_id = ?, genre_id = ? WHERE book_id = ?";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setString(4, book.getPublisher());
            stmt.setInt(5, book.getPublicationYear());
            stmt.setInt(6, book.getCopiesAvailable());
            stmt.setInt(7, book.getCategoryId());
            stmt.setInt(8, book.getGenreId());
            stmt.setInt(9, book.getBookId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update book", e);
        }
    }
    
    // Delete a book
    public boolean deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE book_id = ?";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Search books by title, author, or ISBN
    public List<Book> searchBooks(String query, String category, String genre) {
        List<Book> books = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT b.*, c.category_name, g.genre_name, p.publisher_name " +
            "FROM books b " +
            "LEFT JOIN categories c ON b.category_id = c.category_id " +
            "LEFT JOIN genres g ON b.genre_id = g.genre_id " +
            "LEFT JOIN publishers p ON b.publisher_id = p.publisher_id " +
            "WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        // Add search condition if query is not empty
        if (query != null && !query.trim().isEmpty()) {
            sql.append(" AND (b.title LIKE ? OR b.author LIKE ? OR b.isbn LIKE ?)");
            String searchPattern = "%" + query.trim() + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }

        // Add category filter if specified
        if (category != null && !category.isEmpty() && !"All Categories".equals(category)) {
            sql.append(" AND c.category_name = ?");
            params.add(category);
        }

        // Add genre filter if specified
        if (genre != null && !genre.isEmpty() && !"All Genres".equals(genre)) {
            sql.append(" AND g.genre_name = ?");
            params.add(genre);
        }

        // Order by title
        sql.append(" ORDER BY b.title");

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setPublisherId(rs.getInt("publisher_id"));
                book.setPublicationYear(rs.getInt("publication_year"));
                book.setCategoryId(rs.getInt("category_id"));
                book.setGenreId(rs.getInt("genre_id"));
                book.setCopiesAvailable(rs.getInt("copies_available"));
                book.setTotalCopies(rs.getInt("total_copies"));
                book.setDateAdded(rs.getString("date_added"));
                
                // Set names directly from the join results
                book.setCategoryName(rs.getString("category_name"));
                book.setGenreName(rs.getString("genre_name"));
                book.setPublisherName(rs.getString("publisher_name"));
                
                // Get cover image if exists
                byte[] coverData = rs.getBytes("cover_image");
                if (coverData != null) {
                    book.setCoverImage(coverData);
                }
                
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return books;
    }

    // Keep the original method for backward compatibility
    public List<Book> searchBooks(String query) {
        return searchBooks(query, null, null);
    }
    
    // Get books by category
    public List<Book> getBooksByCategory(int categoryId) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE category_id = ? ORDER BY date_added DESC";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoryId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setPublisherId(rs.getInt("publisher_id"));
                    book.setPublicationYear(rs.getInt("publication_year"));
                    book.setCategoryId(rs.getInt("category_id"));
                    book.setGenreId(rs.getInt("genre_id"));
                    book.setCopiesAvailable(rs.getInt("copies_available"));
                    book.setTotalCopies(rs.getInt("total_copies"));
                    book.setDateAdded(rs.getString("date_added"));
                    book.setCoverImage(rs.getBytes("cover_image"));
                    
                    books.add(book);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return books;
    }
    
    // Get books by genre
    public List<Book> getBooksByGenre(int genreId) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE genre_id = ? ORDER BY date_added DESC";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, genreId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setPublisherId(rs.getInt("publisher_id"));
                    book.setPublicationYear(rs.getInt("publication_year"));
                    book.setCategoryId(rs.getInt("category_id"));
                    book.setGenreId(rs.getInt("genre_id"));
                    book.setCopiesAvailable(rs.getInt("copies_available"));
                    book.setTotalCopies(rs.getInt("total_copies"));
                    book.setDateAdded(rs.getString("date_added"));
                    book.setCoverImage(rs.getBytes("cover_image"));
                    
                    books.add(book);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return books;
    }

    public List<Publisher> getAllPublishers() {
        List<Publisher> publishers = new ArrayList<>();
        String query = "SELECT publisher_id, publisher_name, contact_info FROM publishers ORDER BY publisher_name";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Publisher publisher = new Publisher(
                    rs.getInt("publisher_id"),
                    rs.getString("publisher_name"),
                    rs.getString("contact_info")
                );
                publishers.add(publisher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error loading publishers: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
        
        return publishers;
    }

    // Dashboard statistics methods
    public int getTotalBooks() {
        String sql = "SELECT COUNT(*) FROM books";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getAvailableBooks() {
        String sql = "SELECT COUNT(*) FROM books WHERE copies_available > 0";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getBorrowedBooks() {
        String sql = "SELECT COUNT(*) FROM books WHERE copies_available < total_copies";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getNewBooks() {
        String sql = "SELECT COUNT(*) FROM books WHERE DATEDIFF(CURRENT_DATE, date_added) <= 30";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getOverdueBooks() {
        // Return 0 since borrowings functionality is not implemented yet
        return 0;
    }

    public List<Map<String, String>> getRecentActivity(int limit) {
        List<Map<String, String>> activities = new ArrayList<>();
        String sql = "SELECT 'Book' as type, " +
                    "CONCAT('New book added: ''', title, '''') as description, " +
                    "date_added as date " +
                    "FROM books " +
                    "ORDER BY date_added DESC " +
                    "LIMIT ?";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, String> activity = new HashMap<>();
                activity.put("type", rs.getString("type"));
                activity.put("description", rs.getString("description"));
                
                // Format the date
                java.sql.Timestamp timestamp = rs.getTimestamp("date");
                String formattedDate;
                if (timestamp != null) {
                    long diff = System.currentTimeMillis() - timestamp.getTime();
                    if (diff < 24 * 60 * 60 * 1000) {
                        formattedDate = "Today";
                    } else if (diff < 48 * 60 * 60 * 1000) {
                        formattedDate = "Yesterday";
                    } else {
                        formattedDate = new SimpleDateFormat("MMM d, yyyy").format(timestamp);
                    }
                    activity.put("date", formattedDate);
                }
                
                activities.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activities;
    }

    public Map<String, Integer> getUserBorrowingStats(int userId) {
        Map<String, Integer> stats = new HashMap<>();
        String sql = """
            SELECT 
                COUNT(CASE WHEN return_date IS NULL THEN 1 END) as currently_borrowed,
                COUNT(CASE WHEN return_date IS NULL AND due_date < CURRENT_DATE THEN 1 END) as overdue,
                COUNT(*) as total_borrowed
            FROM borrowings 
            WHERE user_id = ?
            """;
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                stats.put("currently_borrowed", rs.getInt("currently_borrowed"));
                stats.put("overdue", rs.getInt("overdue"));
                stats.put("total_borrowed", rs.getInt("total_borrowed"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            // Return default values in case of error
            stats.put("currently_borrowed", 0);
            stats.put("overdue", 0);
            stats.put("total_borrowed", 0);
        }
        
        return stats;
    }

    public List<Map<String, String>> getUserBorrowedBooks(int userId) {
        List<Map<String, String>> borrowedBooks = new ArrayList<>();
        String sql = """
            SELECT 
                b.title,
                b.author,
                br.borrow_date,
                br.due_date,
                CASE 
                    WHEN br.return_date IS NOT NULL THEN 'Returned'
                    WHEN br.due_date < CURRENT_DATE THEN 'Overdue'
                    ELSE 'Borrowed'
                END as status
            FROM borrowings br
            JOIN books b ON b.book_id = br.book_id
            WHERE br.user_id = ? AND br.return_date IS NULL
            ORDER BY br.borrow_date DESC
            """;
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, String> book = new HashMap<>();
                book.put("title", rs.getString("title"));
                book.put("author", rs.getString("author"));
                book.put("borrow_date", rs.getDate("borrow_date").toString());
                book.put("due_date", rs.getDate("due_date").toString());
                book.put("status", rs.getString("status"));
                borrowedBooks.add(book);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return borrowedBooks;
    }

    public List<Map<String, String>> getUserActivity(int userId, int limit) {
        List<Map<String, String>> activities = new ArrayList<>();
        String sql = """
            SELECT 
                CASE 
                    WHEN return_date IS NOT NULL THEN 'Returned'
                    ELSE 'Borrowed'
                END as action,
                b.title as book,
                COALESCE(return_date, borrow_date) as date
            FROM borrowings br
            JOIN books b ON b.book_id = br.book_id
            WHERE br.user_id = ?
            ORDER BY COALESCE(return_date, borrow_date) DESC
            LIMIT ?
            """;
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, String> activity = new HashMap<>();
                activity.put("action", rs.getString("action"));
                activity.put("book", rs.getString("book"));
                activity.put("date", rs.getDate("date").toString());
                activities.add(activity);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return activities;
    }

    public boolean borrowBook(int bookId, int userId) {
        // First check if the book is available
        String checkSql = "SELECT copies_available FROM books WHERE book_id = ?";
        
        try (Connection conn = DBConnection.connect()) {
            // Check book availability
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, bookId);
                ResultSet rs = checkStmt.executeQuery();
                
                if (!rs.next() || rs.getInt("copies_available") <= 0) {
                    return false;
                }
            }
            
            // Start transaction
            conn.setAutoCommit(false);
            
            try {
                // Insert borrowing record
                String borrowSql = """
                    INSERT INTO borrowings (book_id, user_id, borrow_date, due_date)
                    VALUES (?, ?, CURRENT_TIMESTAMP, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 14 DAY))
                    """;
                
                try (PreparedStatement borrowStmt = conn.prepareStatement(borrowSql)) {
                    borrowStmt.setInt(1, bookId);
                    borrowStmt.setInt(2, userId);
                    borrowStmt.executeUpdate();
                }
                
                // Update book availability
                String updateSql = """
                    UPDATE books 
                    SET copies_available = copies_available - 1
                    WHERE book_id = ? AND copies_available > 0
                    """;
                
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, bookId);
                    updateStmt.executeUpdate();
                }
                
                // Commit transaction
                conn.commit();
                return true;
                
            } catch (SQLException e) {
                // Rollback transaction on error
                conn.rollback();
                e.printStackTrace();
                return false;
            } finally {
                // Reset auto-commit
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Book> searchBooksByTitle(String title) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ?";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + title + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
    
    public List<Book> searchBooksByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE author LIKE ?";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + author + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
    
    public List<Book> searchBooksByIsbn(String isbn) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE isbn LIKE ?";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + isbn + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBookId(rs.getInt("book_id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setIsbn(rs.getString("isbn"));
        book.setPublisherId(rs.getInt("publisher_id"));
        book.setPublicationYear(rs.getInt("publication_year"));
        book.setCategoryId(rs.getInt("category_id"));
        book.setGenreId(rs.getInt("genre_id"));
        book.setCopiesAvailable(rs.getInt("copies_available"));
        book.setTotalCopies(rs.getInt("total_copies"));
        book.setDateAdded(rs.getString("date_added"));
        book.setCoverImage(rs.getBytes("cover_image"));
        return book;
    }

    public Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE book_id = ?";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToBook(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Book getBookByTitle(String title) {
        String sql = "SELECT * FROM books WHERE title = ?";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToBook(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Book> searchBooksByCategory(String category) {
        String sql = "SELECT b.* FROM books b " +
                    "JOIN categories c ON b.category_id = c.category_id " +
                    "WHERE c.name = ?";
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Book> searchBooksByCategoryAndGenre(String category, String genre) {
        String sql = "SELECT b.* FROM books b " +
                    "JOIN categories c ON b.category_id = c.category_id " +
                    "JOIN genres g ON b.genre_id = g.genre_id " +
                    "WHERE c.name = ? AND g.name = ?";
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category);
            stmt.setString(2, genre);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
} 