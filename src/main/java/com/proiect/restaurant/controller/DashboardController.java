package com.proiect.restaurant.controller;

import com.proiect.restaurant.service.CafeTableService;
import com.proiect.restaurant.service.CustomerService;
import com.proiect.restaurant.service.MenuItemService;
import com.proiect.restaurant.service.OrderService;
import com.proiect.restaurant.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final CustomerService customerService;
    private final CafeTableService cafeTableService;
    private final MenuItemService menuItemService;
    private final ReservationService reservationService;
    private final OrderService orderService;

    public DashboardController(CustomerService customerService,
                               CafeTableService cafeTableService,
                               MenuItemService menuItemService,
                               ReservationService reservationService,
                               OrderService orderService) {
        this.customerService = customerService;
        this.cafeTableService = cafeTableService;
        this.menuItemService = menuItemService;
        this.reservationService = reservationService;
        this.orderService = orderService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("customerCount", customerService.getAllCustomers().size());
        model.addAttribute("tableCount", cafeTableService.getAllTablesWithAvailability().size());
        model.addAttribute("menuItemCount", menuItemService.getAllMenuItems().size());
        model.addAttribute("reservationCount", reservationService.getAllReservations().size());
        model.addAttribute("orderCount", orderService.getAllOrders().size());
        return "dashboard";
    }
}
