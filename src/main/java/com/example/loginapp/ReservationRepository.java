package com.example.loginapp;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByUserAndBook(User user, Book book);
    List<Reservation> findByBookOrderByReservationDateAsc(Book book);
}
