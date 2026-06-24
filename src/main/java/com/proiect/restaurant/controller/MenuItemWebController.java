package com.proiect.restaurant.controller;

import com.proiect.restaurant.dto.MenuItemRequest;
import com.proiect.restaurant.exception.BusinessException;
import com.proiect.restaurant.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/menu-items")
public class MenuItemWebController {

    private final MenuItemService menuItemService;

    public MenuItemWebController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public String listMenuItems(Model model) {
        model.addAttribute("menuItems", menuItemService.getAllMenuItems());
        return "menu-items/list";
    }

    @GetMapping("/new")
    public String createMenuItemForm(Model model) {
        var request = new MenuItemRequest();
        request.setAvailable(Boolean.TRUE);
        model.addAttribute("menuItemRequest", request);
        return "menu-items/form";
    }

    @PostMapping
    public String createMenuItem(@Valid @ModelAttribute("menuItemRequest") MenuItemRequest request,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            return "menu-items/form";
        }

        try {
            menuItemService.createMenuItem(request);
        } catch (BusinessException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "menu-items/form";
        }

        return "redirect:/menu-items";
    }

    @GetMapping("/{id}/edit")
    public String editMenuItemForm(@PathVariable Long id, Model model) {
        var menuItem = menuItemService.getMenuItemById(id);
        var request = new MenuItemRequest(menuItem.getName(), menuItem.getDescription(), menuItem.getPrice(), menuItem.getAvailable());
        model.addAttribute("menuItemRequest", request);
        model.addAttribute("menuItemId", id);
        return "menu-items/form";
    }

    @PostMapping("/{id}")
    public String updateMenuItem(@PathVariable Long id,
                                 @Valid @ModelAttribute("menuItemRequest") MenuItemRequest request,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("menuItemId", id);
            return "menu-items/form";
        }

        try {
            menuItemService.updateMenuItem(id, request);
        } catch (BusinessException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("menuItemId", id);
            return "menu-items/form";
        }

        return "redirect:/menu-items";
    }

    @PostMapping("/{id}/delete")
    public String deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return "redirect:/menu-items";
    }
}
