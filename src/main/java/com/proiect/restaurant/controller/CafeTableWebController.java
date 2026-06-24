package com.proiect.restaurant.controller;

import com.proiect.restaurant.dto.CafeTableRequest;
import com.proiect.restaurant.exception.BusinessException;
import com.proiect.restaurant.service.CafeTableService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tables")
public class CafeTableWebController {

    private final CafeTableService cafeTableService;

    public CafeTableWebController(CafeTableService cafeTableService) {
        this.cafeTableService = cafeTableService;
    }

    @GetMapping
    public String listTables(Model model) {
        model.addAttribute("tables", cafeTableService.getAllTablesWithAvailability());
        return "tables/list";
    }

    @GetMapping("/new")
    public String createTableForm(Model model) {
        model.addAttribute("tableRequest", new CafeTableRequest());
        return "tables/form";
    }

    @PostMapping
    public String createTable(@Valid @ModelAttribute("tableRequest") CafeTableRequest request,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            return "tables/form";
        }

        try {
            cafeTableService.createTable(request);
        } catch (BusinessException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "tables/form";
        }

        return "redirect:/tables";
    }

    @GetMapping("/{id}/edit")
    public String editTableForm(@PathVariable Long id, Model model) {
        var table = cafeTableService.getAllTablesWithAvailability().stream()
            .filter(t -> t.getId().equals(id))
            .findFirst()
            .orElseThrow();

        var request = new CafeTableRequest(table.getTableNumber(), table.getCapacity());
        model.addAttribute("tableRequest", request);
        model.addAttribute("tableId", id);
        return "tables/form";
    }

    @PostMapping("/{id}")
    public String updateTable(@PathVariable Long id,
                              @Valid @ModelAttribute("tableRequest") CafeTableRequest request,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tableId", id);
            return "tables/form";
        }

        try {
            cafeTableService.updateTable(id, request);
        } catch (BusinessException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("tableId", id);
            return "tables/form";
        }

        return "redirect:/tables";
    }

    @PostMapping("/{id}/delete")
    public String deleteTable(@PathVariable Long id) {
        cafeTableService.deleteTable(id);
        return "redirect:/tables";
    }
}
