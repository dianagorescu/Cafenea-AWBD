package com.proiect.restaurant.repository;

import com.proiect.restaurant.entity.Reservation;
import com.proiect.restaurant.entity.ReservationStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ReservationRepositoryImpl implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) -> {
        Reservation reservation = new Reservation();
        reservation.setId(rs.getLong("id"));
        reservation.setReservationTime(rs.getTimestamp("reservation_time").toLocalDateTime());
        reservation.setDuration(rs.getInt("duration"));
        reservation.setStatus(ReservationStatus.valueOf(rs.getString("status")));
        reservation.setCustomerId(rs.getLong("customer_id"));
        reservation.setTableId(rs.getLong("table_id"));
        return reservation;
    };

    public ReservationRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            return insert(reservation);
        } else {
            return update(reservation);
        }
    }

    private Reservation insert(Reservation reservation) {
        String sql = "INSERT INTO reservations (reservation_time, duration, status, customer_id, table_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, Timestamp.valueOf(reservation.getReservationTime()));
            ps.setInt(2, reservation.getDuration());
            ps.setString(3, reservation.getStatus().name());
            ps.setLong(4, reservation.getCustomerId());
            ps.setLong(5, reservation.getTableId());
            return ps;
        }, keyHolder);

        reservation.setId(keyHolder.getKey().longValue());
        return reservation;
    }

    private Reservation update(Reservation reservation) {
        String sql = "UPDATE reservations SET reservation_time = ?, duration = ?, status = ?, customer_id = ?, table_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, Timestamp.valueOf(reservation.getReservationTime()), reservation.getDuration(),
                           reservation.getStatus().name(), reservation.getCustomerId(),
                           reservation.getTableId(), reservation.getId());
        return reservation;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        List<Reservation> reservations = jdbcTemplate.query(sql, reservationRowMapper, id);
        return reservations.isEmpty() ? Optional.empty() : Optional.of(reservations.get(0));
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT * FROM reservations";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservations WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Reservation> findByCustomerId(Long customerId) {
        String sql = "SELECT * FROM reservations WHERE customer_id = ?";
        return jdbcTemplate.query(sql, reservationRowMapper, customerId);
    }

    @Override
    public List<Reservation> findOverlappingReservations(Long tableId, LocalDateTime startTime, LocalDateTime endTime) {
        String sql = "SELECT * FROM reservations WHERE table_id = ? " +
                    "AND status NOT IN ('CANCELLED', 'COMPLETED') " +
                    "AND (reservation_time < ? AND DATEADD(MINUTE, duration, reservation_time) > ?)";
        return jdbcTemplate.query(sql, reservationRowMapper, tableId,
                                 Timestamp.valueOf(endTime), Timestamp.valueOf(startTime));
    }

    @Override
    public List<Reservation> findByStatus(ReservationStatus status) {
        String sql = "SELECT * FROM reservations WHERE status = ?";
        return jdbcTemplate.query(sql, reservationRowMapper, status.name());
    }
}
