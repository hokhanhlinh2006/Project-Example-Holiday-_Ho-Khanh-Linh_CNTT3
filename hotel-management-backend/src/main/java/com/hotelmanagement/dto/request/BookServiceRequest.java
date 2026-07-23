package com.hotelmanagement.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookServiceRequest {
    @NotNull(message = "Booking detail ID is required")
    private Integer bookingDetailId;

    @NotNull(message = "Service ID is required")
    private Integer serviceId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
