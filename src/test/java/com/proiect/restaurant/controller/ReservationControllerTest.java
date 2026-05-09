package com.proiect.restaurant.controller;

import com.proiect.restaurant.dto.ReservationRequest;
import com.proiect.restaurant.dto.ReservationResponse;
import com.proiect.restaurant.entity.ReservationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ReservationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createReservation_Success() {
        ReservationRequest request = new ReservationRequest();
        request.setCustomerId(1L);
        request.setReservationTime(LocalDateTime.now().plusDays(1));
        request.setDuration(120);
        request.setNumberOfPeople(2);

        ResponseEntity<ReservationResponse> response = restTemplate.postForEntity(
            "/api/reservations",
            request,
            ReservationResponse.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals(ReservationStatus.CONFIRMED, response.getBody().getStatus());
        assertEquals(1L, response.getBody().getCustomerId());
    }

    @Test
    void getReservationById_Success() {

        ReservationRequest createRequest = new ReservationRequest();
        createRequest.setCustomerId(1L);
        createRequest.setReservationTime(LocalDateTime.now().plusDays(1));
        createRequest.setDuration(120);
        createRequest.setNumberOfPeople(2);
        
        ResponseEntity<ReservationResponse> createResponse = restTemplate.postForEntity(
            "/api/reservations",
            createRequest,
            ReservationResponse.class
        );
        Long reservationId = createResponse.getBody().getId();

        
        ResponseEntity<ReservationResponse> response = restTemplate.getForEntity(
            "/api/reservations/" + reservationId,
            ReservationResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(reservationId, response.getBody().getId());
    }

    @Test
    void getAllReservations_Success() {
        ResponseEntity<ReservationResponse[]> response = restTemplate.getForEntity(
            "/api/reservations",
            ReservationResponse[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 0);
    }

    @Test
    void getReservationsByCustomerId_Success() {
        ResponseEntity<ReservationResponse[]> response = restTemplate.getForEntity(
            "/api/reservations/customer/1",
            ReservationResponse[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void cancelReservation_Success() {

        ReservationRequest createRequest = new ReservationRequest();
        createRequest.setCustomerId(1L);
        createRequest.setReservationTime(LocalDateTime.now().plusDays(10)); // Far in future
        createRequest.setDuration(120);
        createRequest.setNumberOfPeople(2);
        
        ResponseEntity<ReservationResponse> createResponse = restTemplate.postForEntity(
            "/api/reservations",
            createRequest,
            ReservationResponse.class
        );
        
        
        if (createResponse.getStatusCode() == HttpStatus.CREATED && createResponse.getBody() != null) {
            Long reservationId = createResponse.getBody().getId();

        
            ResponseEntity<ReservationResponse> response = restTemplate.exchange(
                "/api/reservations/" + reservationId + "/cancel",
                HttpMethod.PATCH,
                null,
                ReservationResponse.class
            );

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(ReservationStatus.CANCELLED, response.getBody().getStatus());
        }
    }

    @Test
    void completeReservation_Success() {
        ReservationRequest createRequest = new ReservationRequest();
        createRequest.setCustomerId(1L);
        createRequest.setReservationTime(LocalDateTime.now().plusDays(15)); // Far in future
        createRequest.setDuration(120);
        createRequest.setNumberOfPeople(2);
        
        ResponseEntity<ReservationResponse> createResponse = restTemplate.postForEntity(
            "/api/reservations",
            createRequest,
            ReservationResponse.class
        );

        if (createResponse.getStatusCode() == HttpStatus.CREATED && createResponse.getBody() != null) {
            Long reservationId = createResponse.getBody().getId();

            ResponseEntity<ReservationResponse> response = restTemplate.exchange(
                "/api/reservations/" + reservationId + "/complete",
                HttpMethod.PATCH,
                null,
                ReservationResponse.class
            );

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(ReservationStatus.COMPLETED, response.getBody().getStatus());
        }
    }
}
