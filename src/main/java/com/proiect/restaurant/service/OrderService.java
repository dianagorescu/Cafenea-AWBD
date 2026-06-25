package com.proiect.restaurant.service;

import com.proiect.restaurant.dto.*;
import com.proiect.restaurant.entity.*;
import com.proiect.restaurant.exception.BusinessException;
import com.proiect.restaurant.exception.ResourceNotFoundException;
import com.proiect.restaurant.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final MenuItemRepository menuItemRepository;
    private final ReceiptRepository receiptRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        CustomerRepository customerRepository,
                        MenuItemRepository menuItemRepository,
                        ReceiptRepository receiptRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.customerRepository = customerRepository;
        this.menuItemRepository = menuItemRepository;
        this.receiptRepository = receiptRepository;
    }
    
    public OrderResponse createOrder(OrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));
        logger.info("Creating order for customerId={}", request.getCustomerId());

        Order order = new Order(customer);
        Order savedOrder = orderRepository.save(order);

        double totalPrice = 0.0;
        for (OrderItemRequest itemRequest : request.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemRequest.getMenuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + itemRequest.getMenuItemId()));

            if (!menuItem.getAvailable()) {
                logger.error("Menu item not available: id={} name={}", menuItem.getId(), menuItem.getName());
                throw new BusinessException("Menu item '" + menuItem.getName() + "' is not available");
            }

            double itemPrice = menuItem.getPrice() * itemRequest.getQuantity();
            OrderItem orderItem = new OrderItem(
                itemRequest.getQuantity(),
                itemPrice,
                savedOrder,
                menuItem
            );
            OrderItem savedItem = orderItemRepository.save(orderItem);
            savedOrder.getOrderItems().add(savedItem);
            totalPrice += itemPrice;
        }

        savedOrder.setTotalPrice(totalPrice);
        
        // Creare automată bon fiscal (Receipt) pentru relația One-to-One
        Receipt receipt = new Receipt("REC-" + System.currentTimeMillis() + "-" + savedOrder.getId(), totalPrice, savedOrder);
        receiptRepository.save(receipt);
        savedOrder.setReceipt(receipt);

        savedOrder = orderRepository.save(savedOrder);
        logger.debug("Order created id={} totalPrice={}", savedOrder.getId(), savedOrder.getTotalPrice());
        return toResponse(savedOrder);
    }
    
    public OrderResponse updateOrderStatus(Long id, OrderStatusUpdateRequest request) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        logger.info("Updating order status id={} status={}", id, request.getStatus());
        
        order.setStatus(request.getStatus());
        Order updatedOrder = orderRepository.save(order);
        return toResponse(updatedOrder);
    }
    
    public OrderResponse getOrderById(Long id) {
        logger.debug("Fetching order by id={}", id);
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return toResponse(order);
    }
    
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public Page<OrderResponse> getOrders(Pageable pageable) {
        logger.debug("Listing orders page={}, size={}, sort={}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return orderRepository.findAll(pageable)
            .map(this::toResponse);
    }
    
    public List<OrderResponse> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomer_Id(customerId).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
    
    public void removeItemFromOrder(Long orderId, Long orderItemId) {
        logger.info("Removing item {} from order {}", orderItemId, orderId);
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
            .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id: " + orderItemId));
        
        if (!orderItem.getOrder().getId().equals(orderId)) {
            throw new BusinessException("Order item " + orderItemId + " does not belong to order " + orderId);
        }
        
        orderItemRepository.deleteById(orderItemId);
        order.getOrderItems().remove(orderItem);
        
        // Recalculare pret
        double newTotalPrice = order.getOrderItems().stream()
            .mapToDouble(OrderItem::getPrice)
            .sum();
        
        order.setTotalPrice(newTotalPrice);
        
        // Actualizare bon fiscal
        receiptRepository.findByOrder_Id(orderId).ifPresent(receipt -> {
            receipt.setTotalPrice(newTotalPrice);
            receiptRepository.save(receipt);
        });

        orderRepository.save(order);
        logger.debug("Item {} removed from order {}. New total={}", orderItemId, orderId, order.getTotalPrice());
    }

    public void deleteOrder(Long id) {
        logger.info("Deleting order id={}", id);
        if (!orderRepository.findById(id).isPresent()) {
            logger.error("Order delete failed: not found id={}", id);
            throw new ResourceNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }
    
    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getOrderItems().stream()
            .map(item -> new OrderItemResponse(
                item.getId(),
                item.getMenuItem().getName(),
                item.getQuantity(),
                item.getPrice()
            ))
            .collect(Collectors.toList());

        return new OrderResponse(
            order.getId(),
            order.getOrderTime(),
            order.getStatus(),
            order.getTotalPrice(),
            order.getCustomer().getId(),
            order.getCustomer().getName(),
            items
        );
    }
}
