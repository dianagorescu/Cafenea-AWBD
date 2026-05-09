package com.proiect.restaurant.repository;

import com.proiect.restaurant.entity.CafeTable;

import java.util.List;
import java.util.Optional;

public interface CafeTableRepository {
    CafeTable save(CafeTable table);
    Optional<CafeTable> findById(Long id);
    List<CafeTable> findAll();
    void deleteById(Long id);
    Optional<CafeTable> findByTableNumber(Integer tableNumber);
    List<CafeTable> findByCapacityGreaterThanEqual(Integer capacity);
}
