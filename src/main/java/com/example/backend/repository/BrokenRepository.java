package com.example.backend.repository;

import com.example.backend.entity.Broken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BrokenRepository extends JpaRepository<Broken, Integer> {
    Page<Broken> findByBrokenDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    @Query(value = "SELECT TO_CHAR(b.broken_date, 'YYYY-MM') as time_period, COUNT(b.id) as count " +
            "FROM broken b " +
            "WHERE b.broken_date >= ?1 AND b.broken_date <= ?2 " +
            "GROUP BY TO_CHAR(b.broken_date, 'YYYY-MM') " +
            "ORDER BY time_period", nativeQuery = true)
    List<Object[]> countBrokenByMonth(LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT TO_CHAR(b.broken_date, 'YYYY-\"Q\"Q') as time_period, COUNT(b.id) as count " +
            "FROM broken b " +
            "WHERE b.broken_date >= ?1 AND b.broken_date <= ?2 " +
            "GROUP BY TO_CHAR(b.broken_date, 'YYYY-\"Q\"Q') " +
            "ORDER BY time_period", nativeQuery = true)
    List<Object[]> countBrokenByQuarter(LocalDateTime startDate, LocalDateTime endDate);
}
