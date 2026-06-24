package com.proiect.restaurant.repository;

import com.proiect.restaurant.entity.Reservation;
import com.proiect.restaurant.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByCustomer_Id(Long customerId);
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByCafeTable_IdAndStatusNotIn(Long tableId, Collection<ReservationStatus> statuses);
}
