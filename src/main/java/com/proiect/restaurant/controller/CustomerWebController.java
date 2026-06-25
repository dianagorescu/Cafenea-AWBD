package com.proiect.restaurant.controller;

import com.proiect.restaurant.dto.CustomerRequest;
import com.proiect.restaurant.exception.BusinessException;
import com.proiect.restaurant.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customers")
public class CustomerWebController {

    private final CustomerService customerService;
    @Value("${app.pagination.default-size:10}")
    private int defaultSize;

    public CustomerWebController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String listCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            Model model) {

        int pageSize = (size == null) ? defaultSize : size;
        org.springframework.data.domain.Sort sortObj = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.fromString(dir), sort);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, pageSize, sortObj);

        var customersPage = customerService.getCustomers(pageable);
        model.addAttribute("customersPage", customersPage);
        model.addAttribute("page", page);
        model.addAttribute("size", pageSize);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
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
