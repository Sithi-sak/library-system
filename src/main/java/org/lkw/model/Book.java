package org.lkw.model;

import org.lkw.data.dao.BookDAO;
import java.util.List;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private String isbn;
    private int publisherId;
    private int publicationYear;
    private int categoryId;
    private int genreId;
    private int copiesAvailable;
    private int totalCopies;
    private String dateAdded;
    private byte[] coverImage;
    private String description;
    private String coverImagePath;
    
    private String categoryName;
    private String genreName;
    private String publisherName;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }

    public void setCopiesAvailable(int copiesAvailable) {
        this.copiesAvailable = copiesAvailable;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public byte[] getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public String getCategory() {
        if (categoryName == null) {
            BookDAO dao = new BookDAO();
            List<Category> categories = dao.getAllCategories();
            for (Category category : categories) {
                if (category.getCategoryId() == this.categoryId) {
                    categoryName = category.getCategoryName();
                    break;
                }
            }
            if (categoryName == null) {
                categoryName = "Unknown";
            }
        }
        return categoryName;
    }

    public String getGenre() {
        if (genreName == null) {
            BookDAO dao = new BookDAO();
            List<Genre> genres = dao.getAllGenres();
            for (Genre genre : genres) {
                if (genre.getGenreId() == this.genreId) {
                    genreName = genre.getGenreName();
                    break;
                }
            }
            if (genreName == null) {
                genreName = "Unknown";
            }
        }
        return genreName;
    }

    public String getPublisher() {
        if (publisherName == null) {
            BookDAO dao = new BookDAO();
            List<Publisher> publishers = dao.getAllPublishers();
            for (Publisher publisher : publishers) {
                if (publisher.getPublisherId() == this.publisherId) {
                    publisherName = publisher.getPublisherName();
                    break;
                }
            }
            if (publisherName == null) {
                publisherName = "Unknown";
            }
        }
        return publisherName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public boolean isAvailable() {
        return copiesAvailable > 0;
    }
} 