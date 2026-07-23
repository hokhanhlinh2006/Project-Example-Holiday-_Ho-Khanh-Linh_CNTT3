package com.hotelmanagement.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewResponse {
    private Integer reviewId;
    private Integer bookingId;
    private String userFullName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
