package com.proiect.restaurant.controller;

import com.proiect.restaurant.dto.CafeTableResponse;
import com.proiect.restaurant.service.CafeTableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class CafeTableController {
    
    private final CafeTableService cafeTableService;
    
    public CafeTableController(CafeTableService cafeTableService) {
        this.cafeTableService = cafeTableService;
    }
    
    @GetMapping
    public ResponseEntity<List<CafeTableResponse>> getAllTables() {
        List<CafeTableResponse> tables = cafeTableService.getAllTablesWithAvailability();
        return ResponseEntity.ok(tables);
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<CafeTableResponse>> getAvailableTables() {
        List<CafeTableResponse> availableTables = cafeTableService.getAvailableTables();
        return ResponseEntity.ok(availableTables);
    }
}
