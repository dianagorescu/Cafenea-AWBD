package com.proiect.restaurant.controller;

import com.proiect.restaurant.dto.OrderRequest;
import com.proiect.restaurant.dto.OrderStatusUpdateRequest;
import com.proiect.restaurant.entity.OrderStatus;
import com.proiect.restaurant.exception.BusinessException;
import com.proiect.restaurant.service.CustomerService;
import com.proiect.restaurant.service.MenuItemService;
import com.proiect.restaurant.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderWebController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final MenuItemService menuItemService;

    public OrderWebController(OrderService orderService,
                              CustomerService customerService,
                              MenuItemService menuItemService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("statuses", OrderStatus.values());
        return "orders/list";
    }

    @GetMapping("/new")
    public String createOrderForm(Model model) {
        OrderRequest request = new OrderRequest();
        request.getItems().clear();
        request.getItems().add(new com.proiect.restaurant.dto.OrderItemRequest());
        model.addAttribute("orderRequest", request);
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("menuItems", menuItemService.getAllMenuItems());
        return "orders/form";
    }

    @PostMapping
    public String createOrder(@Valid @ModelAttribute("orderRequest") OrderRequest orderRequest,
                              BindingResult bindingResult,
                              Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("menuItems", menuItemService.getAllMenuItems());

        if (orderRequest.getItems() == null || orderRequest.getItems().isEmpty()) {
            orderRequest.getItems().add(new com.proiect.restaurant.dto.OrderItemRequest());
        }

        if (bindingResult.hasErrors()) {
            return "orders/form";
        }

        try {
            orderService.createOrder(orderRequest);
        } catch (BusinessException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "orders/form";
        }

        return "redirect:/orders";
    }

    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }

    @PostMapping("/{id}/status")
    public String updateOrderStatus(@PathVariable Long id,
                                    @Valid @ModelAttribute("statusRequest") OrderStatusUpdateRequest statusRequest,
                                    BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("orders", orderService.getAllOrders());
            model.addAttribute("statuses", OrderStatus.values());
            return "orders/list";
        }

        orderService.updateOrderStatus(id, statusRequest);
        return "redirect:/orders";
    }
}
