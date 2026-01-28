package com.example.utilitymeterservice.repository;

import com.example.utilitymeterservice.model.entity.Meter;
import com.example.utilitymeterservice.model.enums.MeterStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MeterRepository extends JpaRepository<Meter, UUID> {
    // Find all meters for a specific user with pagination
//    Page<Meter> findByUserId(UUID userId, Pageable pageable);

    // Find a specific meter by ID and user ID (for authorization check)
    Optional<Meter> findByIdAndUserId(UUID id, UUID userId);

    // Check if meter number already exists
    boolean existsByMeterNumber(String meterNumber);

    // Find meters by user and status
//    Page<Meter> findByUserIdAndMeterStatus(UUID userId, MeterStatus status, Pageable pageable);

    // Count meters by user
//    long countByUserId(UUID userId);

    // Custom query with meter readings count
    @Query("SELECT m FROM Meter m LEFT JOIN FETCH m.meterReadings WHERE m.id = :meterId AND m.user.id = :userId")
    Optional<Meter> findByIdAndUserIdWithReadings(@Param("meterId") UUID meterId, @Param("userId") UUID userId);
}
