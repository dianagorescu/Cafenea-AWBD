package com.proiect.restaurant.config;

import com.proiect.restaurant.repository.*;
import com.proiect.restaurant.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public CustomerService customerService(CustomerRepository customerRepository) {
        return new CustomerService(customerRepository);
    }

    @Bean
    public MenuItemService menuItemService(MenuItemRepository menuItemRepository) {
        return new MenuItemService(menuItemRepository);
    }

    @Bean
    public OrderService orderService(OrderRepository orderRepository,
                                     OrderItemRepository orderItemRepository,
                                     CustomerRepository customerRepository,
                                     MenuItemRepository menuItemRepository) {
        return new OrderService(orderRepository, orderItemRepository, customerRepository, menuItemRepository);
    }

    @Bean
    public ReservationService reservationService(ReservationRepository reservationRepository,
                                                 CafeTableRepository tableRepository,
                                                 CustomerRepository customerRepository) {
        return new ReservationService(reservationRepository, tableRepository, customerRepository);
    }

    @Bean
    public CafeTableService cafeTableService(CafeTableRepository tableRepository,
                                             ReservationRepository reservationRepository) {
        return new CafeTableService(tableRepository, reservationRepository);
    }
}
