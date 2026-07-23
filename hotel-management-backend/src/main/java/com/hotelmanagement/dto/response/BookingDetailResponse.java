package com.hotelmanagement.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingDetailResponse {
    private Integer bookingDetailId;
    private RoomResponse room;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime checkInActual;
    private LocalDateTime checkOutActual;
    private BigDecimal priceApplied;
    private List<BookingServiceResponse> bookingServices;
}
