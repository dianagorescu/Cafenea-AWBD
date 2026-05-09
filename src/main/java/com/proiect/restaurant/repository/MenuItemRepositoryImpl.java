package com.proiect.restaurant.repository;

import com.proiect.restaurant.entity.MenuItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class MenuItemRepositoryImpl implements MenuItemRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<MenuItem> menuItemRowMapper = (rs, rowNum) -> {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(rs.getLong("id"));
        menuItem.setName(rs.getString("name"));
        menuItem.setDescription(rs.getString("description"));
        menuItem.setPrice(rs.getDouble("price"));
        menuItem.setAvailable(rs.getBoolean("available"));
        return menuItem;
    };

    public MenuItemRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        if (menuItem.getId() == null) {
            return insert(menuItem);
        } else {
            return update(menuItem);
        }
    }

    private MenuItem insert(MenuItem menuItem) {
        String sql = "INSERT INTO menu_items (name, description, price, available) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, menuItem.getName());
            ps.setString(2, menuItem.getDescription());
            ps.setDouble(3, menuItem.getPrice());
            ps.setBoolean(4, menuItem.getAvailable());
            return ps;
        }, keyHolder);

        menuItem.setId(keyHolder.getKey().longValue());
        return menuItem;
    }

    private MenuItem update(MenuItem menuItem) {
        String sql = "UPDATE menu_items SET name = ?, description = ?, price = ?, available = ? WHERE id = ?";
        jdbcTemplate.update(sql, menuItem.getName(), menuItem.getDescription(),
                           menuItem.getPrice(), menuItem.getAvailable(), menuItem.getId());
        return menuItem;
    }

    @Override
    public Optional<MenuItem> findById(Long id) {
        String sql = "SELECT * FROM menu_items WHERE id = ?";
        List<MenuItem> menuItems = jdbcTemplate.query(sql, menuItemRowMapper, id);
        return menuItems.isEmpty() ? Optional.empty() : Optional.of(menuItems.get(0));
    }

    @Override
    public List<MenuItem> findAll() {
        String sql = "SELECT * FROM menu_items";
        return jdbcTemplate.query(sql, menuItemRowMapper);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM menu_items WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<MenuItem> findByAvailable(Boolean available) {
        String sql = "SELECT * FROM menu_items WHERE available = ?";
        return jdbcTemplate.query(sql, menuItemRowMapper, available);
    }
}
