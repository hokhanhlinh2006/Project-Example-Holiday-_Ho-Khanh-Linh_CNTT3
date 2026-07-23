package com.hotelmanagement.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingResponse {
    private Integer bookingId;
    private UserResponse user;
    private BigDecimal totalAmount;
    private String bookingStatus;
    private List<BookingDetailResponse> bookingDetails;
    private LocalDateTime createdAt;
}
