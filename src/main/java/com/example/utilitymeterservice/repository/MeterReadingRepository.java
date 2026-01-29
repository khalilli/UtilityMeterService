package com.example.utilitymeterservice.repository;

import com.example.utilitymeterservice.model.entity.MeterReading;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MeterReadingRepository extends JpaRepository<MeterReading, UUID> {
    Page<MeterReading> findAllByMeterId(UUID meterId, Pageable pageable);
}
