package com.proiect.restaurant.repository;

import com.proiect.restaurant.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    Customer save(Customer customer);
    Optional<Customer> findById(Long id);
    List<Customer> findAll();
    void deleteById(Long id);
    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);
}
