package com.hotelmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking_services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BookingService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_service_id")
    private Integer bookingServiceId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_detail_id", nullable = false)
    private BookingDetail bookingDetail;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private HotelService service;

    @NotNull
    @Min(1)
    @Column(name = "quantity", nullable = false)
    @Builder.Default
    private Integer quantity = 1;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "price_applied", precision = 15, scale = 2, nullable = false)
    private BigDecimal priceApplied;

    @NotNull
    @Column(name = "service_date", nullable = false)
    private LocalDateTime serviceDate;
}
