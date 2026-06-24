package com.proiect.restaurant.service;

import com.proiect.restaurant.dto.CafeTableRequest;
import com.proiect.restaurant.dto.CafeTableResponse;
import com.proiect.restaurant.entity.CafeTable;
import com.proiect.restaurant.entity.Reservation;
import com.proiect.restaurant.entity.ReservationStatus;
import com.proiect.restaurant.exception.BusinessException;
import com.proiect.restaurant.exception.ResourceNotFoundException;
import com.proiect.restaurant.repository.CafeTableRepository;
import com.proiect.restaurant.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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

    public CafeTableResponse createTable(CafeTableRequest request) {
        if (tableRepository.findByTableNumber(request.getTableNumber()).isPresent()) {
            throw new BusinessException("Table number " + request.getTableNumber() + " already exists");
        }

        CafeTable table = new CafeTable(
            request.getTableNumber(),
            request.getCapacity()
        );

        CafeTable savedTable = tableRepository.save(table);
        return new CafeTableResponse(savedTable.getId(), savedTable.getTableNumber(), savedTable.getCapacity(), true);
    }

    public CafeTableResponse updateTable(Long id, CafeTableRequest request) {
        CafeTable table = tableRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));

        java.util.Optional<CafeTable> existingTable = tableRepository.findByTableNumber(request.getTableNumber());
        if (existingTable.isPresent() && !existingTable.get().getId().equals(id)) {
            throw new BusinessException("Table number " + request.getTableNumber() + " already exists");
        }

        table.setTableNumber(request.getTableNumber());
        table.setCapacity(request.getCapacity());

        CafeTable updatedTable = tableRepository.save(table);
        boolean isAvailable = isTableAvailableNow(updatedTable.getId(), LocalDateTime.now());
        return new CafeTableResponse(updatedTable.getId(), updatedTable.getTableNumber(), updatedTable.getCapacity(), isAvailable);
    }

    public void deleteTable(Long id) {
        if (!tableRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Table not found with id: " + id);
        }
        tableRepository.deleteById(id);
    }
    
    private boolean isTableAvailableNow(Long tableId, LocalDateTime now) {
        LocalDateTime endTime = now.plusMinutes(1);
        List<Reservation> activeReservations = reservationRepository.findByCafeTable_IdAndStatusNotIn(
            tableId,
            List.of(ReservationStatus.CANCELLED, ReservationStatus.COMPLETED)
        );
        
        boolean hasOverlap = activeReservations.stream().anyMatch(r -> 
            r.getReservationTime().isBefore(endTime) && r.getEndTime().isAfter(now)
        );
        
        return !hasOverlap;
    }
}
