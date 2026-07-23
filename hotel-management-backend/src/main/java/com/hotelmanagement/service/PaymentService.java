package com.hotelmanagement.service;

import com.hotelmanagement.dto.request.PaymentRequest;
import com.hotelmanagement.dto.response.PaymentResponse;
import java.util.List;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);
    PaymentResponse getPaymentById(Integer id);
    List<PaymentResponse> getAllPayments();
}
