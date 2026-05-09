package com.proiect.restaurant.service;

import com.proiect.restaurant.dto.ReservationRequest;
import com.proiect.restaurant.dto.ReservationResponse;
import com.proiect.restaurant.entity.ReservationStatus;
import com.proiect.restaurant.exception.BusinessException;
import com.proiect.restaurant.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    void createReservation_Success() {
        ReservationRequest request = new ReservationRequest();
        request.setCustomerId(1L); 
        request.setReservationTime(LocalDateTime.now().plusDays(5));
        request.setDuration(120);
        request.setNumberOfPeople(2);

        ReservationResponse response = reservationService.createReservation(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(1L, response.getCustomerId());
        assertEquals(ReservationStatus.CONFIRMED, response.getStatus());
    }

    @Test
    void createReservation_CustomerNotFound() {
        ReservationRequest request = new ReservationRequest();
        request.setCustomerId(999L);
        request.setReservationTime(LocalDateTime.now().plusDays(1));
        request.setDuration(120);
        request.setNumberOfPeople(2);

        assertThrows(ResourceNotFoundException.class, () -> reservationService.createReservation(request));
    }

    @Test
    void createReservation_NoSuitableTables() {
        ReservationRequest request = new ReservationRequest();
        request.setCustomerId(1L);
        request.setReservationTime(LocalDateTime.now().plusDays(1));
        request.setDuration(120);
        request.setNumberOfPeople(100); 

        assertThrows(BusinessException.class, () -> reservationService.createReservation(request));
    }

    @Test
    void getReservationById_Success() {
        
        ReservationRequest request = new ReservationRequest();
        request.setCustomerId(1L);
        request.setReservationTime(LocalDateTime.now().plusDays(3));
        request.setDuration(120);
        request.setNumberOfPeople(2);
        
        ReservationResponse created = reservationService.createReservation(request);

        ReservationResponse response = reservationService.getReservationById(created.getId());

        assertNotNull(response);
        assertEquals(created.getId(), response.getId());
    }

    @Test
    void getReservationById_NotFound() {
        assertThrows(ResourceNotFoundException.class, () -> reservationService.getReservationById(999L));
    }

    @Test
    void getAllReservations_Success() {
        List<ReservationResponse> responses = reservationService.getAllReservations();

        assertNotNull(responses);
        
        assertTrue(responses.size() >= 0);
    }

    @Test
    void cancelReservation_Success() {
        
        ReservationRequest request = new ReservationRequest();
        request.setCustomerId(1L);
        request.setReservationTime(LocalDateTime.now().plusDays(7));
        request.setDuration(120);
        request.setNumberOfPeople(2);
        
        ReservationResponse created = reservationService.createReservation(request);

        ReservationResponse response = reservationService.cancelReservation(created.getId());

        assertNotNull(response);
        assertEquals(ReservationStatus.CANCELLED, response.getStatus());
    }

    @Test
    void completeReservation_Success() {
        
        ReservationRequest request = new ReservationRequest();
        request.setCustomerId(1L);
        request.setReservationTime(LocalDateTime.now().plusDays(8));
        request.setDuration(120);
        request.setNumberOfPeople(2);
        
        ReservationResponse created = reservationService.createReservation(request);

        ReservationResponse response = reservationService.completeReservation(created.getId());

        assertNotNull(response);
        assertEquals(ReservationStatus.COMPLETED, response.getStatus());
    }
}
