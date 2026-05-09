package com.proiect.restaurant.repository;

import com.proiect.restaurant.entity.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository {
    MenuItem save(MenuItem menuItem);
    Optional<MenuItem> findById(Long id);
    List<MenuItem> findAll();
    void deleteById(Long id);
    List<MenuItem> findByAvailable(Boolean available);
}
