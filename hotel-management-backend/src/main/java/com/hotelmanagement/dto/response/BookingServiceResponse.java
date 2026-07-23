package com.hotelmanagement.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingServiceResponse {
    private Integer bookingServiceId;
    private ServiceResponse service;
    private Integer quantity;
    private BigDecimal priceApplied;
    private LocalDateTime serviceDate;
}
