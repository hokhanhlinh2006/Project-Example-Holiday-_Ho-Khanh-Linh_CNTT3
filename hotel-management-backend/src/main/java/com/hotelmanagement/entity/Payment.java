package com.hotelmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @NotBlank
    @Column(name = "payment_method", length = 50, nullable = false)
    private String paymentMethod;

    @NotNull
    @DecimalMin("0.01")
    @Column(name = "payment_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal paymentAmount;

    @Column(name = "transaction_id", length = 100)
    private String transactionId;

    @NotNull
    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @NotBlank
    @Column(name = "status", length = 50, nullable = false)
    @Builder.Default
    private String status = "PENDING";
}
