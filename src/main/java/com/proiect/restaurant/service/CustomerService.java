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

@Service
@Transactional
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    public CustomerResponse createCustomer(CustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Customer with email " + request.getEmail() + " already exists");
        }
        
        Customer customer = new Customer(
            request.getName(),
            request.getEmail(),
            request.getPhone()
        );
        
        Customer savedCustomer = customerRepository.save(customer);
        return toResponse(savedCustomer);
    }
    
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return toResponse(customer);
    }
    
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
    
    public Customer findCustomerEntityById(Long id) {
        return customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        if (!customer.getEmail().equals(request.getEmail()) && customerRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Customer with email " + request.getEmail() + " already exists");
        }

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        Customer updatedCustomer = customerRepository.save(customer);
        return toResponse(updatedCustomer);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.findById(id).isPresent()) {
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
