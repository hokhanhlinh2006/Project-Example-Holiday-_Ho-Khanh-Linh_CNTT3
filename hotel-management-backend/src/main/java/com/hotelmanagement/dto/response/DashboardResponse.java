package com.hotelmanagement.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class DashboardResponse {
    private long totalBookings;
    private BigDecimal totalRevenue;
    private double roomOccupancyRate;
    private long activeGuests;
    private Map<String, BigDecimal> revenueByService;
}
