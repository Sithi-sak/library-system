package org.lkw.data.dao;

import org.lkw.data.util.DBConnection;
import org.lkw.model.Book;
import org.lkw.model.Transaction;
import org.lkw.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private final Connection connection;
    private final BookDAO bookDAO;

    public TransactionDAO() {
        this.connection = DBConnection.connect();
        this.bookDAO = new BookDAO();
    }

    public boolean addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (user_id, book_id, borrow_date, due_date, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, transaction.getUserId());
            stmt.setInt(2, transaction.getBookId());
            stmt.setTimestamp(3, Timestamp.valueOf(transaction.getBorrowDate()));
            stmt.setTimestamp(4, Timestamp.valueOf(transaction.getDueDate()));
            stmt.setString(5, transaction.getStatus());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    transaction.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTransaction(Transaction transaction) {
        String sql = "UPDATE transactions SET return_date = ?, status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, transaction.getReturnDate() != null ? 
                Timestamp.valueOf(transaction.getReturnDate()) : null);
            stmt.setString(2, transaction.getStatus());
            stmt.setInt(3, transaction.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Transaction> getUserTransactions(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = """
            SELECT t.*, b.title, b.author, b.isbn, b.cover_image
            FROM transactions t
            JOIN books b ON t.book_id = b.book_id
            WHERE t.user_id = ?
            ORDER BY t.borrow_date DESC
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rs.getInt("id"));
                transaction.setUserId(rs.getInt("user_id"));
                transaction.setBookId(rs.getInt("book_id"));
                transaction.setBorrowDate(rs.getTimestamp("borrow_date").toLocalDateTime());
                transaction.setDueDate(rs.getTimestamp("due_date").toLocalDateTime());
                
                Timestamp returnDate = rs.getTimestamp("return_date");
                if (returnDate != null) {
                    transaction.setReturnDate(returnDate.toLocalDateTime());
                }
                
                transaction.setStatus(rs.getString("status"));

                // Set book details
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setCoverImage(rs.getBytes("cover_image"));
                transaction.setBook(book);

                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public List<Transaction> getCurrentBorrowings(int userId) {
        List<Transaction> borrowings = new ArrayList<>();
        String sql = """
            SELECT t.*, b.title, b.author, b.isbn, b.cover_image
            FROM transactions t
            JOIN books b ON t.book_id = b.book_id
            WHERE t.user_id = ? AND t.status = 'BORROWED'
            ORDER BY t.due_date ASC
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rs.getInt("id"));
                transaction.setUserId(rs.getInt("user_id"));
                transaction.setBookId(rs.getInt("book_id"));
                transaction.setBorrowDate(rs.getTimestamp("borrow_date").toLocalDateTime());
                transaction.setDueDate(rs.getTimestamp("due_date").toLocalDateTime());
                transaction.setStatus(rs.getString("status"));

                // Set book details
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setCoverImage(rs.getBytes("cover_image"));
                transaction.setBook(book);

                borrowings.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowings;
    }

    public int getBooksBorrowedCount(int userId) {
        String sql = "SELECT COUNT(*) FROM transactions WHERE user_id = ? AND status = 'BORROWED'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getBooksReadCount(int userId) {
        String sql = "SELECT COUNT(*) FROM transactions WHERE user_id = ? AND status = 'RETURNED'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Transaction> getDueSoonBooks(int userId) {
        List<Transaction> dueSoon = new ArrayList<>();
        String sql = """
            SELECT t.*, b.title, b.author, b.isbn, b.cover_image
            FROM transactions t
            JOIN books b ON t.book_id = b.book_id
            WHERE t.user_id = ? 
            AND t.status = 'BORROWED'
            AND t.due_date BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 3 DAY)
            ORDER BY t.due_date ASC
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rs.getInt("id"));
                transaction.setUserId(rs.getInt("user_id"));
                transaction.setBookId(rs.getInt("book_id"));
                transaction.setBorrowDate(rs.getTimestamp("borrow_date").toLocalDateTime());
                transaction.setDueDate(rs.getTimestamp("due_date").toLocalDateTime());
                transaction.setStatus(rs.getString("status"));

                // Set book details
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setCoverImage(rs.getBytes("cover_image"));
                transaction.setBook(book);

                dueSoon.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dueSoon;
    }

    public List<Transaction> getOverdueBooks(int userId) {
        List<Transaction> overdue = new ArrayList<>();
        String sql = """
            SELECT t.*, b.title, b.author, b.isbn, b.cover_image
            FROM transactions t
            JOIN books b ON t.book_id = b.book_id
            WHERE t.user_id = ? 
            AND t.status = 'BORROWED'
            AND t.due_date < NOW()
            ORDER BY t.due_date ASC
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rs.getInt("id"));
                transaction.setUserId(rs.getInt("user_id"));
                transaction.setBookId(rs.getInt("book_id"));
                transaction.setBorrowDate(rs.getTimestamp("borrow_date").toLocalDateTime());
                transaction.setDueDate(rs.getTimestamp("due_date").toLocalDateTime());
                transaction.setStatus(rs.getString("status"));

                // Set book details
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setCoverImage(rs.getBytes("cover_image"));
                transaction.setBook(book);

                overdue.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return overdue;
    }
} 