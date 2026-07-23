package com.hotelmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"bookingDetails"})
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer roomId;

    @NotBlank
    @Column(name = "room_number", length = 10, unique = true, nullable = false)
    private String roomNumber;

    @NotNull
    @Min(1)
    @Column(name = "floor", nullable = false)
    private Integer floor;

    @NotBlank
    @Column(name = "status", length = 50, nullable = false)
    @Builder.Default
    private String status = "VACANT_CLEAN";

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    @Builder.Default
    private List<BookingDetail> bookingDetails = new ArrayList<>();
}
