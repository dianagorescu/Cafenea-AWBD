package com.proiect.restaurant.controller;

import com.proiect.restaurant.dto.CustomerRequest;
import com.proiect.restaurant.exception.BusinessException;
import com.proiect.restaurant.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customers")
public class CustomerWebController {

    private final CustomerService customerService;

    public CustomerWebController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String listCustomers(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        return "customers/list";
    }

    @GetMapping("/new")
    public String createCustomerForm(Model model) {
        model.addAttribute("customerRequest", new CustomerRequest());
        return "customers/form";
    }

    @PostMapping
    public String createCustomer(@Valid @ModelAttribute("customerRequest") CustomerRequest request,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            return "customers/form";
        }

        try {
            customerService.createCustomer(request);
        } catch (BusinessException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "customers/form";
        }

        return "redirect:/customers";
    }

    @GetMapping("/{id}/edit")
    public String editCustomerForm(@PathVariable Long id, Model model) {
        var customer = customerService.getCustomerById(id);
        var request = new CustomerRequest(customer.getName(), customer.getEmail(), customer.getPhone());
        model.addAttribute("customerRequest", request);
        model.addAttribute("customerId", id);
        return "customers/form";
    }

    @PostMapping("/{id}")
    public String updateCustomer(@PathVariable Long id,
                                 @Valid @ModelAttribute("customerRequest") CustomerRequest request,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("customerId", id);
            return "customers/form";
        }

        try {
            customerService.updateCustomer(id, request);
        } catch (BusinessException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("customerId", id);
            return "customers/form";
        }

        return "redirect:/customers";
    }

    @PostMapping("/{id}/delete")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }
}
