package com.proiect.restaurant.controller;

import com.proiect.restaurant.dto.MenuItemRequest;
import com.proiect.restaurant.exception.BusinessException;
import com.proiect.restaurant.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/menu-items")
public class MenuItemWebController {

    private final MenuItemService menuItemService;
    @Value("${app.pagination.default-size:10}")
    private int defaultSize;

    public MenuItemWebController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public String listMenuItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            Model model) {

        int pageSize = (size == null) ? defaultSize : size;
        org.springframework.data.domain.Sort sortObj = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.fromString(dir), sort);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, pageSize, sortObj);

        var itemsPage = menuItemService.getMenuItems(pageable);
        model.addAttribute("menuItemsPage", itemsPage);
        model.addAttribute("page", page);
        model.addAttribute("size", pageSize);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
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
