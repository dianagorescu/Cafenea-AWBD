package com.proiect.restaurant.repository;

import com.proiect.restaurant.entity.CafeTable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class CafeTableRepositoryImpl implements CafeTableRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<CafeTable> tableRowMapper = (rs, rowNum) -> {
        CafeTable table = new CafeTable();
        table.setId(rs.getLong("id"));
        table.setTableNumber(rs.getInt("table_number"));
        table.setCapacity(rs.getInt("capacity"));
        return table;
    };

    public CafeTableRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public CafeTable save(CafeTable table) {
        if (table.getId() == null) {
            return insert(table);
        } else {
            return update(table);
        }
    }

    private CafeTable insert(CafeTable table) {
        String sql = "INSERT INTO cafe_tables (table_number, capacity) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, table.getTableNumber());
            ps.setInt(2, table.getCapacity());
            return ps;
        }, keyHolder);

        table.setId(keyHolder.getKey().longValue());
        return table;
    }

    private CafeTable update(CafeTable table) {
        String sql = "UPDATE cafe_tables SET table_number = ?, capacity = ? WHERE id = ?";
        jdbcTemplate.update(sql, table.getTableNumber(), table.getCapacity(), table.getId());
        return table;
    }

    @Override
    public Optional<CafeTable> findById(Long id) {
        String sql = "SELECT * FROM cafe_tables WHERE id = ?";
        List<CafeTable> tables = jdbcTemplate.query(sql, tableRowMapper, id);
        return tables.isEmpty() ? Optional.empty() : Optional.of(tables.get(0));
    }

    @Override
    public List<CafeTable> findAll() {
        String sql = "SELECT * FROM cafe_tables";
        return jdbcTemplate.query(sql, tableRowMapper);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM cafe_tables WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<CafeTable> findByTableNumber(Integer tableNumber) {
        String sql = "SELECT * FROM cafe_tables WHERE table_number = ?";
        List<CafeTable> tables = jdbcTemplate.query(sql, tableRowMapper, tableNumber);
        return tables.isEmpty() ? Optional.empty() : Optional.of(tables.get(0));
    }

    @Override
    public List<CafeTable> findByCapacityGreaterThanEqual(Integer capacity) {
        String sql = "SELECT * FROM cafe_tables WHERE capacity >= ?";
        return jdbcTemplate.query(sql, tableRowMapper, capacity);
    }
}
