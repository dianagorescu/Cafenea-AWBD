package com.proiect.restaurant.controller;

import com.proiect.restaurant.dto.ReservationRequest;
import com.proiect.restaurant.exception.BusinessException;
import com.proiect.restaurant.service.CustomerService;
import com.proiect.restaurant.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reservations")
public class ReservationWebController {

    private final ReservationService reservationService;
    private final CustomerService customerService;

    public ReservationWebController(ReservationService reservationService,
                                    CustomerService customerService) {
        this.reservationService = reservationService;
        this.customerService = customerService;
    }

    @GetMapping
    public String listReservations(Model model) {
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "reservations/list";
    }

    @GetMapping("/new")
    public String createReservationForm(Model model) {
        var request = new ReservationRequest();
        model.addAttribute("reservationRequest", request);
        model.addAttribute("customers", customerService.getAllCustomers());
        return "reservations/form";
    }

    @PostMapping
    public String createReservation(@Valid @ModelAttribute("reservationRequest") ReservationRequest request,
                                    BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("customers", customerService.getAllCustomers());
            return "reservations/form";
        }

        try {
            reservationService.createReservation(request);
        } catch (BusinessException ex) {
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("errorMessage", ex.getMessage());
            return "reservations/form";
        }

        return "redirect:/reservations";
    }

    @PostMapping("/{id}/cancel")
    public String cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return "redirect:/reservations";
    }

    @PostMapping("/{id}/complete")
    public String completeReservation(@PathVariable Long id) {
        reservationService.completeReservation(id);
        return "redirect:/reservations";
    }

    @PostMapping("/{id}/delete")
    public String deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return "redirect:/reservations";
    }
}
