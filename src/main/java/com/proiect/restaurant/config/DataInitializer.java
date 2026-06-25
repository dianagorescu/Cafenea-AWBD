package com.proiect.restaurant.config;

import com.proiect.restaurant.entity.*;
import com.proiect.restaurant.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    private final CafeTableRepository tableRepository;
    private final MenuItemRepository menuItemRepository;
    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CategoryRepository categoryRepository;
    private final ReceiptRepository receiptRepository;
    
    public DataInitializer(CafeTableRepository tableRepository,
                           MenuItemRepository menuItemRepository,
                           CustomerRepository customerRepository,
                           ReservationRepository reservationRepository,
                           OrderRepository orderRepository,
                           OrderItemRepository orderItemRepository,
                           CategoryRepository categoryRepository,
                           ReceiptRepository receiptRepository) {
        this.tableRepository = tableRepository;
        this.menuItemRepository = menuItemRepository;
        this.customerRepository = customerRepository;
        this.reservationRepository = reservationRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.categoryRepository = categoryRepository;
        this.receiptRepository = receiptRepository;
    }
    
    @Override
    public void run(String... args) {
        try {
            Path logsDir = Path.of("logs");
            if (!Files.exists(logsDir)) {
                Files.createDirectories(logsDir);
                logger.info("Created logs directory at {}", logsDir.toAbsolutePath());
            }
        } catch (Exception ex) {
            // If we can't create logs dir, log to console
            System.err.println("Could not create logs directory: " + ex.getMessage());
        }
        // Check if data already exists
        if (!tableRepository.findAll().isEmpty()) {
            logger.info("Database already initialized - skipping data initialization");
            return;
        }
        
        logger.info("Initializing database with sample data...");
        
        // MESE
        CafeTable table1 = tableRepository.save(new CafeTable(1, 2));
        CafeTable table2 = tableRepository.save(new CafeTable(2, 2));
        CafeTable table3 = tableRepository.save(new CafeTable(3, 4));
        CafeTable table4 = tableRepository.save(new CafeTable(4, 4));
        CafeTable table5 = tableRepository.save(new CafeTable(5, 6));
        CafeTable table6 = tableRepository.save(new CafeTable(6, 8));
        
        // PRODUSE
        MenuItem espresso = menuItemRepository.save(new MenuItem(
            "Espresso",
            "Strong and rich Italian espresso",
            3.50,
            true
        ));
        
        MenuItem cappuccino = menuItemRepository.save(new MenuItem(
            "Cappuccino",
            "Espresso with steamed milk and foam",
            4.50,
            true
        ));
        
        MenuItem latte = menuItemRepository.save(new MenuItem(
            "Caffe Latte",
            "Smooth espresso with steamed milk",
            4.75,
            true
        ));
        
        MenuItem americano = menuItemRepository.save(new MenuItem(
            "Americano",
            "Espresso diluted with hot water",
            3.75,
            true
        ));

        MenuItem matcha = menuItemRepository.save(new MenuItem(
            "Matcha Latte",
            "Japanese green tea powder with steamed milk",
            5.50,
            true
        ));
        
        MenuItem icedMatcha = menuItemRepository.save(new MenuItem(
            "Iced Matcha",
            "Refreshing cold matcha with milk over ice",
            5.75,
            true
        ));

        MenuItem earlGrey = menuItemRepository.save(new MenuItem(
            "Earl Grey Tea",
            "Classic black tea with bergamot",
            3.50,
            true
        ));
        
        MenuItem chamomile = menuItemRepository.save(new MenuItem(
            "Chamomile Tea",
            "Soothing herbal tea",
            3.25,
            false
        ));
        
        MenuItem greenTea = menuItemRepository.save(new MenuItem(
            "Green Tea",
            "Traditional Japanese sencha green tea",
            3.50,
            false
        ));
        
        MenuItem jasmineTea = menuItemRepository.save(new MenuItem(
            "Jasmine Tea",
            "Fragrant green tea with jasmine flowers",
            3.75,
            true
        ));

        MenuItem chocolateCake = menuItemRepository.save(new MenuItem(
            "Chocolate Cake",
            "Rich chocolate cake with chocolate ganache",
            6.50,
            true
        ));
        
        MenuItem cheesecake = menuItemRepository.save(new MenuItem(
            "Cheesecake",
            "Creamy New York style cheesecake",
            7.00,
            true
        ));
        
        MenuItem tiramisu = menuItemRepository.save(new MenuItem(
            "Tiramisu",
            "Classic Italian dessert with coffee and mascarpone",
            7.50,
            true
        ));
        
        MenuItem carrotCake = menuItemRepository.save(new MenuItem(
            "Carrot Cake",
            "Moist carrot cake with cream cheese frosting",
            6.75,
            true
        ));
        
        MenuItem matchaCake = menuItemRepository.save(new MenuItem(
            "Matcha Cake",
            "Light sponge cake with matcha cream",
            7.25,
            true
        ));

        // CATEGORII - Relație Many-to-Many
        Category drinks = categoryRepository.save(new Category("Drinks"));
        Category desserts = categoryRepository.save(new Category("Desserts"));

        drinks.getMenuItems().add(espresso);
        drinks.getMenuItems().add(cappuccino);
        drinks.getMenuItems().add(latte);
        drinks.getMenuItems().add(americano);
        drinks.getMenuItems().add(matcha);
        drinks.getMenuItems().add(icedMatcha);
        drinks.getMenuItems().add(earlGrey);
        drinks.getMenuItems().add(chamomile);
        drinks.getMenuItems().add(greenTea);
        drinks.getMenuItems().add(jasmineTea);
        categoryRepository.save(drinks);

        desserts.getMenuItems().add(chocolateCake);
        desserts.getMenuItems().add(cheesecake);
        desserts.getMenuItems().add(tiramisu);
        desserts.getMenuItems().add(carrotCake);
        desserts.getMenuItems().add(matchaCake);
        categoryRepository.save(desserts);
        
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
            customer1,
            table1
        ));
        
        reservationRepository.save(new Reservation(
            LocalDateTime.now().plusDays(2).withHour(18).withMinute(30),
            90,
            customer2,
            table3
        ));
        
        reservationRepository.save(new Reservation(
            LocalDateTime.now().plusDays(3).withHour(12).withMinute(0),
            60,
            customer3,
            table5
        ));
        
        // COMENZI
        Order order1 = orderRepository.save(new Order(customer1));
        orderItemRepository.save(new OrderItem(
            2,
            7.00,
            order1,
            espresso
        ));
        orderItemRepository.save(new OrderItem(
            1,
            6.50,
            order1,
            chocolateCake
        ));
        order1.setTotalPrice(13.50);
        orderRepository.save(order1);
        
        Order order2 = orderRepository.save(new Order(customer2));
        orderItemRepository.save(new OrderItem(
            1,
            5.50,
            order2,
            matcha
        ));
        orderItemRepository.save(new OrderItem(
            2,
            14.00,
            order2,
            cheesecake
        ));
        order2.setTotalPrice(19.50);
        orderRepository.save(order2);
        
        Order order3 = orderRepository.save(new Order(customer3));
        orderItemRepository.save(new OrderItem(
            3,
            13.50,
            order3,
            cappuccino
        ));
        orderItemRepository.save(new OrderItem(
            1,
            7.50,
            order3,
            tiramisu
        ));
        order3.setTotalPrice(21.00);
        orderRepository.save(order3);

        // BONURI FISCALE - Relație One-to-One
        receiptRepository.save(new Receipt("REC-SEED-1", order1.getTotalPrice(), order1));
        receiptRepository.save(new Receipt("REC-SEED-2", order2.getTotalPrice(), order2));
        receiptRepository.save(new Receipt("REC-SEED-3", order3.getTotalPrice(), order3));
        
        logger.info("Database initialized");
    }
}
