package com.proiect.restaurant.config;

import com.proiect.restaurant.entity.*;
import com.proiect.restaurant.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final CafeTableRepository tableRepository;
    private final MenuItemRepository menuItemRepository;
    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    
    public DataInitializer(CafeTableRepository tableRepository,
                           MenuItemRepository menuItemRepository,
                           CustomerRepository customerRepository,
                           ReservationRepository reservationRepository,
                           OrderRepository orderRepository,
                           OrderItemRepository orderItemRepository) {
        this.tableRepository = tableRepository;
        this.menuItemRepository = menuItemRepository;
        this.customerRepository = customerRepository;
        this.reservationRepository = reservationRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }
    
    @Override
    public void run(String... args) {
        // Check if data already exists
        if (!tableRepository.findAll().isEmpty()) {
            System.out.println("Database already initialized - skipping data initialization");
            return;
        }
        
        System.out.println("Initializing database with sample data...");
        
        // MESE
        tableRepository.save(new CafeTable(1, 2));
        tableRepository.save(new CafeTable(2, 2));
        tableRepository.save(new CafeTable(3, 4));
        tableRepository.save(new CafeTable(4, 4));
        tableRepository.save(new CafeTable(5, 6));
        tableRepository.save(new CafeTable(6, 8));
        
        // PRODUSE
        menuItemRepository.save(new MenuItem(
            "Espresso",
            "Strong and rich Italian espresso",
            3.50,
            true
        ));
        
        menuItemRepository.save(new MenuItem(
            "Cappuccino",
            "Espresso with steamed milk and foam",
            4.50,
            true
        ));
        
        menuItemRepository.save(new MenuItem(
            "Caffe Latte",
            "Smooth espresso with steamed milk",
            4.75,
            true
        ));
        
        menuItemRepository.save(new MenuItem(
            "Americano",
            "Espresso diluted with hot water",
            3.75,
            true
        ));

        menuItemRepository.save(new MenuItem(
            "Matcha Latte",
            "Japanese green tea powder with steamed milk",
            5.50,
            true
        ));
        
        menuItemRepository.save(new MenuItem(
            "Iced Matcha",
            "Refreshing cold matcha with milk over ice",
            5.75,
            true
        ));

        menuItemRepository.save(new MenuItem(
            "Earl Grey Tea",
            "Classic black tea with bergamot",
            3.50,
            true
        ));
        
        menuItemRepository.save(new MenuItem(
            "Chamomile Tea",
            "Soothing herbal tea",
            3.25,
            false
        ));
        
        menuItemRepository.save(new MenuItem(
            "Green Tea",
            "Traditional Japanese sencha green tea",
            3.50,
            false
        ));
        
        menuItemRepository.save(new MenuItem(
            "Jasmine Tea",
            "Fragrant green tea with jasmine flowers",
            3.75,
            true
        ));

        menuItemRepository.save(new MenuItem(
            "Chocolate Cake",
            "Rich chocolate cake with chocolate ganache",
            6.50,
            true
        ));
        
        menuItemRepository.save(new MenuItem(
            "Cheesecake",
            "Creamy New York style cheesecake",
            7.00,
            true
        ));
        
        menuItemRepository.save(new MenuItem(
            "Tiramisu",
            "Classic Italian dessert with coffee and mascarpone",
            7.50,
            true
        ));
        
        menuItemRepository.save(new MenuItem(
            "Carrot Cake",
            "Moist carrot cake with cream cheese frosting",
            6.75,
            true
        ));
        
        menuItemRepository.save(new MenuItem(
            "Matcha Cake",
            "Light sponge cake with matcha cream",
            7.25,
            true
        ));
        
        // CLIENTI
        customerRepository.save(new Customer(
                "Default",
                "default@email.com",
                "0722222222"
        ));
        Customer customer1 = customerRepository.save(new Customer(
            "John Smith",
            "john.smith@email.com",
            "0712345678"
        ));

        Customer customer2 = customerRepository.save(new Customer(
            "Maria Popescu",
            "maria.popescu@email.com",
            "0723456789"
        ));
        
        Customer customer3 = customerRepository.save(new Customer(
            "Alex Johnson",
            "alex.johnson@email.com",
            "0734567890"
        ));
        
        // REZERVARI
        reservationRepository.save(new Reservation(
            LocalDateTime.now().plusDays(1).withHour(14).withMinute(0),
            120,
            customer1.getId(),
            1L
        ));
        
        reservationRepository.save(new Reservation(
            LocalDateTime.now().plusDays(2).withHour(18).withMinute(30),
            90,
            customer2.getId(),
            3L
        ));
        
        reservationRepository.save(new Reservation(
            LocalDateTime.now().plusDays(3).withHour(12).withMinute(0),
            60,
            customer3.getId(),
            5L
        ));
        
        // COMENZI
        Order order1 = orderRepository.save(new Order(customer1.getId()));
        orderItemRepository.save(new OrderItem(
            2,
            7.00,
            order1.getId(),
            1L // Espresso
        ));
        orderItemRepository.save(new OrderItem(
            1,
            6.50,
            order1.getId(),
            11L // Chocolate Cake
        ));
        order1.setTotalPrice(13.50);
        orderRepository.save(order1);
        
        Order order2 = orderRepository.save(new Order(customer2.getId()));
        orderItemRepository.save(new OrderItem(
            1,
            5.50,
            order2.getId(),
            5L // Matcha Latte
        ));
        orderItemRepository.save(new OrderItem(
            2,
            14.00,
            order2.getId(),
            12L // Cheesecake
        ));
        order2.setTotalPrice(19.50);
        orderRepository.save(order2);
        
        Order order3 = orderRepository.save(new Order(customer3.getId()));
        orderItemRepository.save(new OrderItem(
            3,
            13.50,
            order3.getId(),
            2L // Cappuccino
        ));
        orderItemRepository.save(new OrderItem(
            1,
            7.50,
            order3.getId(),
            13L // Tiramisu
        ));
        order3.setTotalPrice(21.00);
        orderRepository.save(order3);
        
        System.out.println("Database initialized");
    }
}
