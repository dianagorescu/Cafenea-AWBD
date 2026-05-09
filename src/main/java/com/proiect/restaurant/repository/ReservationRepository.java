package com.proiect.restaurant.repository;

import com.proiect.restaurant.entity.Reservation;
import com.proiect.restaurant.entity.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    Optional<Reservation> findById(Long id);
    List<Reservation> findAll();
    void deleteById(Long id);
    List<Reservation> findByCustomerId(Long customerId);
    List<Reservation> findOverlappingReservations(Long tableId, LocalDateTime startTime, LocalDateTime endTime);
    List<Reservation> findByStatus(ReservationStatus status);
}
