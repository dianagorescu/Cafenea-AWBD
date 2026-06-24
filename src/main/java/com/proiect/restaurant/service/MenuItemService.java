package com.proiect.restaurant.service;

import com.proiect.restaurant.dto.MenuItemRequest;
import com.proiect.restaurant.dto.MenuItemResponse;
import com.proiect.restaurant.entity.MenuItem;
import com.proiect.restaurant.exception.ResourceNotFoundException;
import com.proiect.restaurant.repository.MenuItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public MenuItemResponse createMenuItem(MenuItemRequest request) {
        MenuItem menuItem = new MenuItem(
            request.getName(),
            request.getDescription(),
            request.getPrice(),
            request.getAvailable()
        );

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return toResponse(savedMenuItem);
    }

    public MenuItemResponse updateMenuItem(Long id, MenuItemRequest request) {
        MenuItem menuItem = menuItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));

        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setAvailable(request.getAvailable());

        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        return toResponse(updatedMenuItem);
    }

    public MenuItemResponse getMenuItemById(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
        return toResponse(menuItem);
    }

    public List<MenuItemResponse> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public List<MenuItemResponse> getAvailableMenuItems() {
        return menuItemRepository.findByAvailable(true).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public void deleteMenuItem(Long id) {
        if (!menuItemRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Menu item not found with id: " + id);
        }
        menuItemRepository.deleteById(id);
    }

    private MenuItemResponse toResponse(MenuItem menuItem) {
        return new MenuItemResponse(
            menuItem.getId(),
            menuItem.getName(),
            menuItem.getDescription(),
            menuItem.getPrice(),
            menuItem.getAvailable()
        );
    }
}
