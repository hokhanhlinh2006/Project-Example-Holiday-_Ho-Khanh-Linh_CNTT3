package com.hotelmanagement.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RoomTypeResponse {
    private Integer roomTypeId;
    private String name;
    private String description;
    private Integer maxCapacity;
    private BigDecimal basePrice;
    private Integer sizeM2;
    private String amenities;
}
