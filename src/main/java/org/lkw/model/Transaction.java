package org.lkw.model;

import java.time.LocalDateTime;

public class Transaction {
    private int id;
    private int userId;
    private int bookId;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private String status; // BORROWED, RETURNED, RENEWED, OVERDUE
    private Book book;
    private User user;

    public Transaction() {
    }

    public Transaction(int userId, int bookId, LocalDateTime borrowDate, LocalDateTime dueDate) {
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = "BORROWED";
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Helper methods
    public boolean isOverdue() {
        return status.equals("BORROWED") && LocalDateTime.now().isAfter(dueDate);
    }

    public boolean isDueSoon() {
        if (!status.equals("BORROWED")) return false;
        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(dueDate) && now.plusDays(3).isAfter(dueDate);
    }
} 