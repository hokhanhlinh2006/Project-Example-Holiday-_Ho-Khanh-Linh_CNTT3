package com.hotelmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"rooms"})
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_type_id")
    private Integer roomTypeId;

    @NotBlank
    @Column(name = "name", length = 100, unique = true, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Min(1)
    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "base_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal basePrice;

    @NotNull
    @Min(1)
    @Column(name = "size_m2", nullable = false)
    private Integer sizeM2;

    @Column(name = "amenities", columnDefinition = "TEXT")
    private String amenities;

    @OneToMany(mappedBy = "roomType", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Room> rooms = new ArrayList<>();
}
