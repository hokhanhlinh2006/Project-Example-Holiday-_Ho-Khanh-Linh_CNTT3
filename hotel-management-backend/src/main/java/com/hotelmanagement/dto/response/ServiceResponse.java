package com.hotelmanagement.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServiceResponse {
    private Integer serviceId;
    private String name;
    private String description;
    private BigDecimal price;
    private String unit;
}
