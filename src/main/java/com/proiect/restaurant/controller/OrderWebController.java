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
    @org.springframework.beans.factory.annotation.Value("${app.pagination.default-size:10}")
    private int defaultSize;

    public OrderWebController(OrderService orderService,
                              CustomerService customerService,
                              MenuItemService menuItemService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public String listOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "orderTime") String sort,
            @RequestParam(defaultValue = "desc") String dir,
            Model model) {

        int pageSize = (size == null) ? defaultSize : size;
        org.springframework.data.domain.Sort sortObj = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.fromString(dir), sort);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, pageSize, sortObj);

        var ordersPage = orderService.getOrders(pageable);
        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("page", page);
        model.addAttribute("size", pageSize);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
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
