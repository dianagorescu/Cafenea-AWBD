package com.proiect.restaurant.service;

import com.proiect.restaurant.dto.*;
import com.proiect.restaurant.entity.*;
import com.proiect.restaurant.exception.BusinessException;
import com.proiect.restaurant.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    void createOrder_Success() {
        OrderRequest request = new OrderRequest();
        request.setCustomerId(1L);
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setMenuItemId(1L); 
        itemRequest.setQuantity(2);
        request.setItems(Arrays.asList(itemRequest));

        OrderResponse response = orderService.createOrder(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(1L, response.getCustomerId());
        assertEquals(1, response.getItems().size());
        assertTrue(response.getTotalPrice() > 0);
    }

    @Test
    void createOrder_CustomerNotFound() {
        OrderRequest request = new OrderRequest();
        request.setCustomerId(999L);
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setMenuItemId(1L);
        itemRequest.setQuantity(1);
        request.setItems(Arrays.asList(itemRequest));

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(request));
    }

    @Test
    void createOrder_MenuItemNotFound() {
        OrderRequest request = new OrderRequest();
        request.setCustomerId(1L);
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setMenuItemId(999L);
        itemRequest.setQuantity(2);
        request.setItems(Arrays.asList(itemRequest));

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(request));
    }

    @Test
    void updateOrderStatus_Success() {
        
        OrderRequest createRequest = new OrderRequest();
        createRequest.setCustomerId(1L);
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setMenuItemId(1L);
        itemRequest.setQuantity(1);
        createRequest.setItems(Arrays.asList(itemRequest));
        
        OrderResponse createdOrder = orderService.createOrder(createRequest);
        
        
        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();
        request.setStatus(OrderStatus.SERVED);

        OrderResponse response = orderService.updateOrderStatus(createdOrder.getId(), request);

        assertNotNull(response);
        assertEquals(OrderStatus.SERVED, response.getStatus());
    }

    @Test
    void getOrderById_Success() {
        
        OrderRequest request = new OrderRequest();
        request.setCustomerId(1L);
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setMenuItemId(1L);
        itemRequest.setQuantity(1);
        request.setItems(Arrays.asList(itemRequest));
        
        OrderResponse createdOrder = orderService.createOrder(request);

        OrderResponse response = orderService.getOrderById(createdOrder.getId());

        assertNotNull(response);
        assertEquals(createdOrder.getId(), response.getId());
    }

    @Test
    void getOrderById_NotFound() {
        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(999L));
    }

    @Test
    void getAllOrders_Success() {
        List<OrderResponse> responses = orderService.getAllOrders();

        assertNotNull(responses);
        
        assertTrue(responses.size() >= 0);
    }

    @Test
    void removeItemFromOrder_Success() {
        
        OrderRequest request = new OrderRequest();
        request.setCustomerId(1L);
        OrderItemRequest item1 = new OrderItemRequest();
        item1.setMenuItemId(1L);
        item1.setQuantity(1);
        OrderItemRequest item2 = new OrderItemRequest();
        item2.setMenuItemId(2L);
        item2.setQuantity(1);
        request.setItems(Arrays.asList(item1, item2));
        
        OrderResponse order = orderService.createOrder(request);
        Long orderItemId = order.getItems().get(0).getId();

        orderService.removeItemFromOrder(order.getId(), orderItemId);

        OrderResponse updatedOrder = orderService.getOrderById(order.getId());
        assertEquals(1, updatedOrder.getItems().size());
    }

    @Test
    void removeItemFromOrder_OrderNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> 
            orderService.removeItemFromOrder(999L, 1L));
    }
}
