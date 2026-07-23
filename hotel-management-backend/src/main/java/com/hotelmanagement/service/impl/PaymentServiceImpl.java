package com.hotelmanagement.service.impl;

import com.hotelmanagement.dto.request.PaymentRequest;
import com.hotelmanagement.dto.response.PaymentResponse;
import com.hotelmanagement.entity.Invoice;
import com.hotelmanagement.entity.Payment;
import com.hotelmanagement.exception.BadRequestException;
import com.hotelmanagement.exception.ResourceNotFoundException;
import com.hotelmanagement.repository.InvoiceRepository;
import com.hotelmanagement.repository.PaymentRepository;
import com.hotelmanagement.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    private PaymentResponse mapToResponse(Payment payment) {
        PaymentResponse res = new PaymentResponse();
        res.setPaymentId(payment.getPaymentId());
        res.setInvoiceId(payment.getInvoice().getInvoiceId());
        res.setPaymentMethod(payment.getPaymentMethod());
        res.setPaymentAmount(payment.getPaymentAmount());
        res.setTransactionId(payment.getTransactionId());
        res.setPaymentDate(payment.getPaymentDate());
        res.setStatus(payment.getStatus());
        return res;
    }

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + request.getInvoiceId()));

        if ("PAID".equals(invoice.getStatus())) {
            throw new BadRequestException("Invoice is already paid");
        }

        Payment payment = Payment.builder()
                .invoice(invoice)
                .paymentMethod(request.getPaymentMethod())
                .paymentAmount(request.getPaymentAmount())
                .transactionId(request.getTransactionId())
                .paymentDate(LocalDateTime.now())
                .status("SUCCESS")
                .build();

        paymentRepository.save(payment);

        invoice.setStatus("PAID");
        invoiceRepository.save(invoice);

        return mapToResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Integer id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));
        return mapToResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
