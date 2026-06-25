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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class MenuItemService {

    private static final Logger logger = LoggerFactory.getLogger(MenuItemService.class);
    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public MenuItemResponse createMenuItem(MenuItemRequest request) {
        logger.info("Creating menu item name={}", request.getName());
        MenuItem menuItem = new MenuItem(
            request.getName(),
            request.getDescription(),
            request.getPrice(),
            request.getAvailable()
        );

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        logger.debug("MenuItem created id={}", savedMenuItem.getId());
        return toResponse(savedMenuItem);
    }

    public MenuItemResponse updateMenuItem(Long id, MenuItemRequest request) {
        logger.info("Updating menu item id={}", id);
        MenuItem menuItem = menuItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));

        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setAvailable(request.getAvailable());

        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        logger.debug("MenuItem updated id={}", updatedMenuItem.getId());
        return toResponse(updatedMenuItem);
    }

    public MenuItemResponse getMenuItemById(Long id) {
        logger.debug("Fetching menu item by id={}", id);
        MenuItem menuItem = menuItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
        return toResponse(menuItem);
    }

    public List<MenuItemResponse> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public Page<MenuItemResponse> getMenuItems(Pageable pageable) {
        logger.debug("Listing menu items page={}, size={}, sort={}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return menuItemRepository.findAll(pageable)
            .map(this::toResponse);
    }

    public List<MenuItemResponse> getAvailableMenuItems() {
        return menuItemRepository.findByAvailable(true).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public void deleteMenuItem(Long id) {
        logger.info("Deleting menu item id={}", id);
        if (!menuItemRepository.findById(id).isPresent()) {
            logger.error("MenuItem delete failed: not found id={}", id);
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
