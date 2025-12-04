package com.example.loginapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LibraryService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    public void borrowBook(Long bookId, User user) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalStateException("Book not found!"));

        if (book.getStatus() == BookStatus.BORROWED) {
            throw new IllegalStateException("Book is already borrowed!");
        }
        
        if (book.getStatus() == BookStatus.RESERVED && !user.equals(book.getBorrowedBy())) {
            throw new IllegalStateException("This book is reserved for another guest.");
        }

        book.setStatus(BookStatus.BORROWED);
        book.setBorrowedBy(user);
        bookRepository.save(book);

        BorrowingRecord record = new BorrowingRecord(user, book);
        borrowingRecordRepository.save(record);
    }

    @Transactional
    public void returnBook(Long bookId, User user) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalStateException("Book not found!"));

        if (book.getStatus() == BookStatus.AVAILABLE || !user.equals(book.getBorrowedBy())) {
            throw new IllegalStateException("You have not borrowed this book.");
        }

        // Update the borrowing record
        List<BorrowingRecord> records = borrowingRecordRepository.findByUserAndReturnDateIsNull(user);
        for (BorrowingRecord record : records) {
            if (record.getBook().equals(book)) {
                record.setReturnDate(LocalDate.now());
                borrowingRecordRepository.save(record);
                break;
            }
        }

        // Check for reservations
        List<Reservation> reservations = reservationRepository.findByBookOrderByReservationDateAsc(book);
        if (!reservations.isEmpty()) {
            // Book is now reserved for the next user in line
            Reservation nextReservation = reservations.get(0);
            book.setStatus(BookStatus.RESERVED);
            book.setBorrowedBy(nextReservation.getUser());
            
            // Create a notification for that user
            String message = "The book '" + book.getName() + "' you reserved is now available on the Shelf.";
            notificationRepository.save(new Notification(nextReservation.getUser(), message));
            
            // Remove the fulfilled reservation
            reservationRepository.delete(nextReservation);
        } else {
            // No reservations, book becomes available
            book.setStatus(BookStatus.AVAILABLE);
            book.setBorrowedBy(null);
        }
        bookRepository.save(book);
    }

    @Transactional
    public void reserveBook(Long bookId, User user) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalStateException("Book not found!"));

        if (book.getStatus() == BookStatus.AVAILABLE) {
            throw new IllegalStateException("This book is available to borrow, not reserve.");
        }

        if (reservationRepository.findByUserAndBook(user, book).isPresent()) {
            throw new IllegalStateException("You have already reserved this book.");
        }

        reservationRepository.save(new Reservation(user, book));
    }

    public List<BorrowingRecord> getBorrowingHistory(User user) {
        return borrowingRecordRepository.findByUser(user);
    }

    public List<Notification> getNotifications(User user) {
        return notificationRepository.findByUserOrderByCreatedDateDesc(user);
    }
}
