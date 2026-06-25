package com.proiect.restaurant.service;

import com.proiect.restaurant.dto.CustomerRequest;
import com.proiect.restaurant.dto.CustomerResponse;
import com.proiect.restaurant.entity.Customer;
import com.proiect.restaurant.exception.BusinessException;
import com.proiect.restaurant.exception.ResourceNotFoundException;
import com.proiect.restaurant.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    
    private final CustomerRepository customerRepository;
    
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    public CustomerResponse createCustomer(CustomerRequest request) {
        logger.info("Creating customer with email={}", request.getEmail());
        if (customerRepository.existsByEmail(request.getEmail())) {
            logger.error("Customer creation failed: email already exists={}", request.getEmail());
            throw new BusinessException("Customer with email " + request.getEmail() + " already exists");
        }
        
        Customer customer = new Customer(
            request.getName(),
            request.getEmail(),
            request.getPhone()
        );
        
        Customer savedCustomer = customerRepository.save(customer);
        logger.debug("Customer created with id={}", savedCustomer.getId());
        return toResponse(savedCustomer);
    }
    
    public CustomerResponse getCustomerById(Long id) {
        logger.debug("Fetching customer by id={}", id);
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return toResponse(customer);
    }
    
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public Page<CustomerResponse> getCustomers(Pageable pageable) {
        logger.debug("Listing customers page={}, size={}, sort={}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return customerRepository.findAll(pageable)
            .map(this::toResponse);
    }
    
    public Customer findCustomerEntityById(Long id) {
        return customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        logger.info("Updating customer id={}", id);
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        if (!customer.getEmail().equals(request.getEmail()) && customerRepository.existsByEmail(request.getEmail())) {
            logger.error("Customer update failed: email already exists={}", request.getEmail());
            throw new BusinessException("Customer with email " + request.getEmail() + " already exists");
        }

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        Customer updatedCustomer = customerRepository.save(customer);
        logger.debug("Customer updated id={}", updatedCustomer.getId());
        return toResponse(updatedCustomer);
    }

    public void deleteCustomer(Long id) {
        logger.info("Deleting customer id={}", id);
        if (!customerRepository.findById(id).isPresent()) {
            logger.error("Customer delete failed: not found id={}", id);
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }
    
    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
            customer.getId(),
            customer.getName(),
            customer.getEmail(),
            customer.getPhone()
        );
    }
}
