package com.hotelmanagement.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class InvoiceResponse {
    private Integer invoiceId;
    private Integer bookingId;
    private Integer employeeId;
    private LocalDateTime issueDate;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private String status;
    private List<PaymentResponse> payments;
}
