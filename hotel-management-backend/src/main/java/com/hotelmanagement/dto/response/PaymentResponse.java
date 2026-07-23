package com.hotelmanagement.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private Integer paymentId;
    private Integer invoiceId;
    private String paymentMethod;
    private BigDecimal paymentAmount;
    private String transactionId;
    private LocalDateTime paymentDate;
    private String status;
}
