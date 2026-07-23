package com.hotelmanagement.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RoomTypeRequest {
    @NotBlank(message = "Room type name is required")
    private String name;

    private String description;

    @NotNull(message = "Max capacity is required")
    @Min(value = 1, message = "Max capacity must be at least 1")
    private Integer maxCapacity;

    @NotNull(message = "Base price is required")
    private BigDecimal basePrice;

    @NotNull(message = "Room size is required")
    @Min(value = 1, message = "Size must be at least 1")
    private Integer sizeM2;

    private String amenities;
}
