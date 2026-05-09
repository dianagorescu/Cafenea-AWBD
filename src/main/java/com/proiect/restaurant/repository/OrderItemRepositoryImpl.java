package com.proiect.restaurant.repository;

import com.proiect.restaurant.entity.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<OrderItem> orderItemRowMapper = (rs, rowNum) -> {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(rs.getLong("id"));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setPrice(rs.getDouble("price"));
        orderItem.setOrderId(rs.getLong("order_id"));
        orderItem.setMenuItemId(rs.getLong("menu_item_id"));
        return orderItem;
    };

    public OrderItemRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        if (orderItem.getId() == null) {
            return insert(orderItem);
        } else {
            return update(orderItem);
        }
    }

    private OrderItem insert(OrderItem orderItem) {
        String sql = "INSERT INTO order_items (quantity, price, order_id, menu_item_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, orderItem.getQuantity());
            ps.setDouble(2, orderItem.getPrice());
            ps.setLong(3, orderItem.getOrderId());
            ps.setLong(4, orderItem.getMenuItemId());
            return ps;
        }, keyHolder);

        orderItem.setId(keyHolder.getKey().longValue());
        return orderItem;
    }

    private OrderItem update(OrderItem orderItem) {
        String sql = "UPDATE order_items SET quantity = ?, price = ?, order_id = ?, menu_item_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, orderItem.getQuantity(), orderItem.getPrice(),
                           orderItem.getOrderId(), orderItem.getMenuItemId(), orderItem.getId());
        return orderItem;
    }

    @Override
    public Optional<OrderItem> findById(Long id) {
        String sql = "SELECT * FROM order_items WHERE id = ?";
        List<OrderItem> orderItems = jdbcTemplate.query(sql, orderItemRowMapper, id);
        return orderItems.isEmpty() ? Optional.empty() : Optional.of(orderItems.get(0));
    }

    @Override
    public List<OrderItem> findAll() {
        String sql = "SELECT * FROM order_items";
        return jdbcTemplate.query(sql, orderItemRowMapper);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM order_items WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<OrderItem> findByOrderId(Long orderId) {
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        return jdbcTemplate.query(sql, orderItemRowMapper, orderId);
    }
}
