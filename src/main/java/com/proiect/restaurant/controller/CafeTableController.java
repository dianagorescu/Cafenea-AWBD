package com.proiect.restaurant.controller;

import com.proiect.restaurant.dto.CafeTableRequest;
import com.proiect.restaurant.dto.CafeTableResponse;
import com.proiect.restaurant.service.CafeTableService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    public ResponseEntity<CafeTableResponse> createTable(@Valid @RequestBody CafeTableRequest request) {
        CafeTableResponse response = cafeTableService.createTable(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CafeTableResponse> updateTable(@PathVariable Long id, @Valid @RequestBody CafeTableRequest request) {
        CafeTableResponse response = cafeTableService.updateTable(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        cafeTableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }
}
