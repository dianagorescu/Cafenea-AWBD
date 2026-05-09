package com.proiect.restaurant.service;

import com.proiect.restaurant.dto.CafeTableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CafeTableServiceTest {

    @Autowired
    private CafeTableService cafeTableService;

    @Test
    void getAllTablesWithAvailability_Success() {
        List<CafeTableResponse> responses = cafeTableService.getAllTablesWithAvailability();

        assertNotNull(responses);
        assertTrue(responses.size() >= 5);
    }

    @Test
    void getAllTablesWithAvailability_ReturnsAllTables() {
        List<CafeTableResponse> responses = cafeTableService.getAllTablesWithAvailability();

        assertNotNull(responses);
        assertTrue(responses.size() >= 5);
        responses.forEach(table -> assertNotNull(table.getAvailable()));
    }

    @Test
    void getAvailableTables_Success() {
        List<CafeTableResponse> responses = cafeTableService.getAvailableTables();

        assertNotNull(responses);
        assertTrue(responses.size() >= 5); 
        responses.forEach(table -> assertTrue(table.getAvailable()));
    }

    @Test
    void getAvailableTables_OnlyReturnsAvailableTables() {
        List<CafeTableResponse> responses = cafeTableService.getAvailableTables();

        assertNotNull(responses);

        responses.forEach(table -> {
            assertTrue(table.getAvailable());
            assertNotNull(table.getId());
            assertNotNull(table.getTableNumber());
        });
    }
}
