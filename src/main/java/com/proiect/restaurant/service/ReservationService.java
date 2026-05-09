package com.proiect.restaurant.service;

import com.proiect.restaurant.dto.ReservationRequest;
import com.proiect.restaurant.dto.ReservationResponse;
import com.proiect.restaurant.entity.Customer;
import com.proiect.restaurant.entity.Reservation;
import com.proiect.restaurant.entity.ReservationStatus;
import com.proiect.restaurant.entity.CafeTable;
import com.proiect.restaurant.exception.BusinessException;
import com.proiect.restaurant.exception.ResourceNotFoundException;
import com.proiect.restaurant.repository.CustomerRepository;
import com.proiect.restaurant.repository.ReservationRepository;
import com.proiect.restaurant.repository.CafeTableRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CafeTableRepository tableRepository;
    private final CustomerRepository customerRepository;

    public ReservationService(ReservationRepository reservationRepository,
                             CafeTableRepository tableRepository,
                             CustomerRepository customerRepository) {
        this.reservationRepository = reservationRepository;
        this.tableRepository = tableRepository;
        this.customerRepository = customerRepository;
    }
    
    public ReservationResponse createReservation(ReservationRequest request) {
   
        Customer customer = customerRepository.findById(request.getCustomerId())
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

        // cautare mese disponibile
        List<CafeTable> suitableTables = tableRepository
            .findByCapacityGreaterThanEqual(request.getNumberOfPeople());

        if (suitableTables.isEmpty()) {
            throw new BusinessException("No tables available with capacity for " +
                request.getNumberOfPeople() + " people");
        }

        // nu permite suprapuneri
        LocalDateTime endTime = request.getReservationTime().plusMinutes(request.getDuration());
        CafeTable availableTable = null;

        for (CafeTable table : suitableTables) {
            List<Reservation> overlapping = reservationRepository.findOverlappingReservations(
                table.getId(),
                request.getReservationTime(),
                endTime
            );

            if (overlapping.isEmpty()) {
                availableTable = table;
                break;
            }
        }

        if (availableTable == null) {
            throw new BusinessException("No tables available for the requested time slot");
        }

        Reservation reservation = new Reservation(
            request.getReservationTime(),
            request.getDuration(),
            customer.getId(),
            availableTable.getId()
        );

        Reservation savedReservation = reservationRepository.save(reservation);
        return toResponse(savedReservation);
    }
    
    public ReservationResponse getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        return toResponse(reservation);
    }
    
    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
    
    public List<ReservationResponse> getReservationsByCustomerId(Long customerId) {
        return reservationRepository.findByCustomerId(customerId).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
    
    public ReservationResponse cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        
        reservation.setStatus(ReservationStatus.CANCELLED);
        Reservation updatedReservation = reservationRepository.save(reservation);
        return toResponse(updatedReservation);
    }
    
    public ReservationResponse completeReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        
        reservation.setStatus(ReservationStatus.COMPLETED);
        Reservation updatedReservation = reservationRepository.save(reservation);
        return toResponse(updatedReservation);
    }
    
    private ReservationResponse toResponse(Reservation reservation) {
        // Fetch customer and table separately
        Customer customer = customerRepository.findById(reservation.getCustomerId())
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + reservation.getCustomerId()));

        CafeTable table = tableRepository.findById(reservation.getTableId())
            .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + reservation.getTableId()));

        return new ReservationResponse(
            reservation.getId(),
            reservation.getReservationTime(),
            reservation.getDuration(),
            reservation.getStatus(),
            customer.getId(),
            customer.getName(),
            table.getId(),
            table.getTableNumber()
        );
    }
}
