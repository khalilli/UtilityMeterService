package com.example.utilitymeterservice.model.entity;

import com.example.utilitymeterservice.model.enums.MeterStatus;
import com.example.utilitymeterservice.model.enums.MeterType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "meters", indexes = {
        @Index(name = "idx_meters_user_id", columnList = "user_id"),
        @Index(name = "idx_meter_number", columnList = "meter_number")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Meter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "meter_type", columnDefinition = "meter_type", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private MeterType meterType;

    @Column(name = "meter_number", nullable = false, unique = true)
    private String meterNumber;

    @Column(name = "unit_of_measurement")
    private String unitOfMeasurement;

    @Column(name = "installation_date", nullable = false)
    private LocalDate installationDate;

    @Column(name = "status", columnDefinition = "meter_status", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private MeterStatus meterStatus;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "meter")
    private List<MeterReading> meterReadings = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
