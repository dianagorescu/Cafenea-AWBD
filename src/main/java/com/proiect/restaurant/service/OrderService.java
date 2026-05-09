package com.proiect.restaurant.service;

import com.proiect.restaurant.dto.*;
import com.proiect.restaurant.entity.Customer;
import com.proiect.restaurant.entity.MenuItem;
import com.proiect.restaurant.entity.Order;
import com.proiect.restaurant.entity.OrderItem;
import com.proiect.restaurant.exception.BusinessException;
import com.proiect.restaurant.exception.ResourceNotFoundException;
import com.proiect.restaurant.repository.CustomerRepository;
import com.proiect.restaurant.repository.MenuItemRepository;
import com.proiect.restaurant.repository.OrderItemRepository;
import com.proiect.restaurant.repository.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderService(OrderRepository orderRepository,
                       OrderItemRepository orderItemRepository,
                       CustomerRepository customerRepository,
                       MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.customerRepository = customerRepository;
        this.menuItemRepository = menuItemRepository;
    }
    
    public OrderResponse createOrder(OrderRequest request) {
        // exista clientul?
        Customer customer = customerRepository.findById(request.getCustomerId())
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

        // creare comanda
        Order order = new Order(customer.getId());
        Order savedOrder = orderRepository.save(order);

        // creare bon si calculare pret total
        double totalPrice = 0.0;
        for (OrderItemRequest itemRequest : request.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemRequest.getMenuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + itemRequest.getMenuItemId()));

            if (!menuItem.getAvailable()) {
                throw new BusinessException("Menu item '" + menuItem.getName() + "' is not available");
            }

            double itemPrice = menuItem.getPrice() * itemRequest.getQuantity();
            OrderItem orderItem = new OrderItem(
                itemRequest.getQuantity(),
                itemPrice,
                savedOrder.getId(),
                menuItem.getId()
            );
            orderItemRepository.save(orderItem);
            totalPrice += itemPrice;
        }

        // actualizare pret total comanda
        savedOrder.setTotalPrice(totalPrice);
        savedOrder = orderRepository.save(savedOrder);

        return toResponse(savedOrder);
    }
    
    public OrderResponse updateOrderStatus(Long id, OrderStatusUpdateRequest request) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        
        order.setStatus(request.getStatus());
        Order updatedOrder = orderRepository.save(order);
        return toResponse(updatedOrder);
    }
    
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return toResponse(order);
    }
    
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
    
    public List<OrderResponse> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
    
    public void removeItemFromOrder(Long orderId, Long orderItemId) {
        // exista comanda?
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        // exista produsul in comanda?
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
            .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id: " + orderItemId));
        
        if (!orderItem.getOrderId().equals(orderId)) {
            throw new BusinessException("Order item " + orderItemId + " does not belong to order " + orderId);
        }
        
        // stergere produs
        orderItemRepository.deleteById(orderItemId);
        
        // recaluclez pretul
        List<OrderItem> remainingItems = orderItemRepository.findByOrderId(orderId);
        double newTotalPrice = remainingItems.stream()
            .mapToDouble(OrderItem::getPrice)
            .sum();
        
        order.setTotalPrice(newTotalPrice);
        orderRepository.save(order);
    }
    
    private OrderResponse toResponse(Order order) {
    
        Customer customer = customerRepository.findById(order.getCustomerId())
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + order.getCustomerId()));

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());

        List<OrderItemResponse> items = orderItems.stream()
            .map(item -> {
                MenuItem menuItem = menuItemRepository.findById(item.getMenuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + item.getMenuItemId()));

                return new OrderItemResponse(
                    item.getId(),
                    menuItem.getName(),
                    item.getQuantity(),
                    item.getPrice()
                );
            })
            .collect(Collectors.toList());

        return new OrderResponse(
            order.getId(),
            order.getOrderTime(),
            order.getStatus(),
            order.getTotalPrice(),
            customer.getId(),
            customer.getName(),
            items
        );
    }
}
