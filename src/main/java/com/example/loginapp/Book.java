package com.example.loginapp;

import javax.persistence.*;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String author;
    private String category;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @ManyToOne
    @JoinColumn(name = "borrowed_by_user_id")
    private User borrowedBy;
    
    @ManyToOne
    @JoinColumn(name = "reserved_by_user_id")
    private User reservedBy;

    public Book() {
    }

    public Book(String name, String author, String category) {
        this.name = name;
        this.author = author;
        this.category = category;
        this.status = BookStatus.AVAILABLE;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BookStatus getStatus() { return status; }
    public void setStatus(BookStatus status) { this.status = status; }
    public User getBorrowedBy() { return borrowedBy; }
    public void setBorrowedBy(User borrowedBy) { this.borrowedBy = borrowedBy; }
    public User getReservedBy() { return reservedBy; }
    public void setReservedBy(User reservedBy) { this.reservedBy = reservedBy; }
}
