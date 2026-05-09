package com.proiect.restaurant.controller;

import com.proiect.restaurant.dto.*;
import com.proiect.restaurant.entity.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createOrder_Success() {
        OrderRequest request = new OrderRequest();
        request.setCustomerId(1L);
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setMenuItemId(1L);
        itemRequest.setQuantity(2);
        request.setItems(Arrays.asList(itemRequest));

        ResponseEntity<OrderResponse> response = restTemplate.postForEntity(
            "/api/orders",
            request,
            OrderResponse.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getCustomerId());
        assertNotNull(response.getBody().getId());
    }

    @Test
    void updateOrderStatus_Success() {
        
        OrderRequest createRequest = new OrderRequest();
        createRequest.setCustomerId(1L);
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setMenuItemId(1L);
        itemRequest.setQuantity(1);
        createRequest.setItems(Arrays.asList(itemRequest));
        
        ResponseEntity<OrderResponse> createResponse = restTemplate.postForEntity(
            "/api/orders",
            createRequest,
            OrderResponse.class
        );
        Long orderId = createResponse.getBody().getId();

        
        OrderStatusUpdateRequest updateRequest = new OrderStatusUpdateRequest();
        updateRequest.setStatus(OrderStatus.SERVED);

        ResponseEntity<OrderResponse> response = restTemplate.exchange(
            "/api/orders/" + orderId + "/status",
            HttpMethod.PATCH,
            new HttpEntity<>(updateRequest),
            OrderResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(OrderStatus.SERVED, response.getBody().getStatus());
    }

    @Test
    void getOrderById_Success() {

        OrderRequest createRequest = new OrderRequest();
        createRequest.setCustomerId(1L);
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setMenuItemId(1L);
        itemRequest.setQuantity(1);
        createRequest.setItems(Arrays.asList(itemRequest));
        
        ResponseEntity<OrderResponse> createResponse = restTemplate.postForEntity(
            "/api/orders",
            createRequest,
            OrderResponse.class
        );
        Long orderId = createResponse.getBody().getId();

        
        ResponseEntity<OrderResponse> response = restTemplate.getForEntity(
            "/api/orders/" + orderId,
            OrderResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(orderId, response.getBody().getId());
    }

    @Test
    void getAllOrders_Success() {
        ResponseEntity<OrderResponse[]> response = restTemplate.getForEntity(
            "/api/orders",
            OrderResponse[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 0);
    }

    @Test
    void getOrdersByCustomerId_Success() {
        ResponseEntity<OrderResponse[]> response = restTemplate.getForEntity(
            "/api/orders/customer/1",
            OrderResponse[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void removeItemFromOrder_Success() {

        OrderRequest createRequest = new OrderRequest();
        createRequest.setCustomerId(1L);
        OrderItemRequest item1 = new OrderItemRequest();
        item1.setMenuItemId(1L);
        item1.setQuantity(1);
        OrderItemRequest item2 = new OrderItemRequest();
        item2.setMenuItemId(2L);
        item2.setQuantity(1);
        createRequest.setItems(Arrays.asList(item1, item2));
        
        ResponseEntity<OrderResponse> createResponse = restTemplate.postForEntity(
            "/api/orders",
            createRequest,
            OrderResponse.class
        );
        Long orderId = createResponse.getBody().getId();
        Long itemId = createResponse.getBody().getItems().get(0).getId();


        ResponseEntity<Void> response = restTemplate.exchange(
            "/api/orders/" + orderId + "/items/" + itemId,
            HttpMethod.DELETE,
            null,
            Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
