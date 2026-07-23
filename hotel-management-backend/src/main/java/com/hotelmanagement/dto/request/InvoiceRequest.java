package com.hotelmanagement.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class InvoiceRequest {
    @NotNull(message = "Booking ID is required")
    private Integer bookingId;

    @NotNull(message = "Employee ID is required")
    private Integer employeeId;

    private BigDecimal discountAmount;
}
