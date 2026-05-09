package com.proiect.restaurant.controller;

import com.proiect.restaurant.dto.CafeTableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CafeTableControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getAllTables_Success() {
        ResponseEntity<CafeTableResponse[]> response = restTemplate.getForEntity(
            "/api/tables",
            CafeTableResponse[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        
        
        CafeTableResponse firstTable = response.getBody()[0];
        assertNotNull(firstTable.getId());
        assertNotNull(firstTable.getTableNumber());
        assertNotNull(firstTable.getCapacity());
        assertNotNull(firstTable.getAvailable());
    }

    @Test
    void getAvailableTables_Success() {
        ResponseEntity<CafeTableResponse[]> response = restTemplate.getForEntity(
            "/api/tables/available",
            CafeTableResponse[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        

        for (CafeTableResponse table : response.getBody()) {
            assertTrue(table.getAvailable());
        }
    }

    @Test
    void getAvailableTables_ReturnsOnlyAvailable() {
        
        ResponseEntity<CafeTableResponse[]> allTablesResponse = restTemplate.getForEntity(
            "/api/tables",
            CafeTableResponse[].class
        );
        
        ResponseEntity<CafeTableResponse[]> availableTablesResponse = restTemplate.getForEntity(
            "/api/tables/available",
            CafeTableResponse[].class
        );

        assertEquals(HttpStatus.OK, availableTablesResponse.getStatusCode());
        assertNotNull(availableTablesResponse.getBody());
        
        
        assertTrue(availableTablesResponse.getBody().length <= allTablesResponse.getBody().length);
    }
}
