package com.hotelmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"bookingServices"})
public class HotelService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Integer serviceId;

    @NotBlank
    @Column(name = "name", length = 100, unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "price", precision = 15, scale = 2, nullable = false)
    private BigDecimal price;

    @NotBlank
    @Column(name = "unit", length = 20, nullable = false)
    private String unit;

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    @Builder.Default
    private List<BookingService> bookingServices = new ArrayList<>();
}
