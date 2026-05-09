package com.proiect.restaurant.service;

import com.proiect.restaurant.dto.CafeTableResponse;
import com.proiect.restaurant.entity.CafeTable;
import com.proiect.restaurant.entity.Reservation;
import com.proiect.restaurant.repository.CafeTableRepository;
import com.proiect.restaurant.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CafeTableService {

    private final CafeTableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    public CafeTableService(CafeTableRepository tableRepository,
                           ReservationRepository reservationRepository) {
        this.tableRepository = tableRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<CafeTableResponse> getAllTablesWithAvailability() {
        LocalDateTime now = LocalDateTime.now();
        List<CafeTable> allTables = tableRepository.findAll();
        
        return allTables.stream()
            .map(table -> {
                boolean isAvailable = isTableAvailableNow(table.getId(), now);
                return new CafeTableResponse(
                    table.getId(),
                    table.getTableNumber(),
                    table.getCapacity(),
                    isAvailable
                );
            })
            .collect(Collectors.toList());
    }
    
    public List<CafeTableResponse> getAvailableTables() {
        return getAllTablesWithAvailability().stream()
            .filter(CafeTableResponse::getAvailable)
            .collect(Collectors.toList());
    }
    
    private boolean isTableAvailableNow(Long tableId, LocalDateTime now) {
        
        // Ca sa fie libera ar trb: now >= reservationTime AND now < reservationTime + duration
        List<Reservation> activeReservations = reservationRepository.findOverlappingReservations(
            tableId,
            now,
            now.plusMinutes(1) // e ocupata in acest moment?
        );
        
        return activeReservations.isEmpty();
    }
}
