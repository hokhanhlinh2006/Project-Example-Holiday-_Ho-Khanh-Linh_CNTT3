package com.hotelmanagement.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class BookingRequest {
    private Integer userId;

    @NotEmpty(message = "Booking must contain at least one room itinerary detail")
    private List<BookingDetailRequest> details;
}
