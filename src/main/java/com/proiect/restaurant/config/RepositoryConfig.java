package com.proiect.restaurant.config;

import com.proiect.restaurant.repository.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class RepositoryConfig {

    @Bean
    public CustomerRepository customerRepository(JdbcTemplate jdbcTemplate) {
        return new CustomerRepositoryImpl(jdbcTemplate);
    }

    @Bean
    public OrderRepository orderRepository(JdbcTemplate jdbcTemplate) {
        return new OrderRepositoryImpl(jdbcTemplate);
    }

    @Bean
    public OrderItemRepository orderItemRepository(JdbcTemplate jdbcTemplate) {
        return new OrderItemRepositoryImpl(jdbcTemplate);
    }

    @Bean
    public MenuItemRepository menuItemRepository(JdbcTemplate jdbcTemplate) {
        return new MenuItemRepositoryImpl(jdbcTemplate);
    }

    @Bean
    public ReservationRepository reservationRepository(JdbcTemplate jdbcTemplate) {
        return new ReservationRepositoryImpl(jdbcTemplate);
    }

    @Bean
    public CafeTableRepository cafeTableRepository(JdbcTemplate jdbcTemplate) {
        return new CafeTableRepositoryImpl(jdbcTemplate);
    }
}
