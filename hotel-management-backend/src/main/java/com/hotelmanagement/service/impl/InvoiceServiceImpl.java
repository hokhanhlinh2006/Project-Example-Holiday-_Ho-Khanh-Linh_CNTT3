package com.hotelmanagement.service.impl;

import com.hotelmanagement.dto.request.InvoiceRequest;
import com.hotelmanagement.dto.response.InvoiceResponse;
import com.hotelmanagement.dto.response.PaymentResponse;
import com.hotelmanagement.entity.Booking;
import com.hotelmanagement.entity.Employee;
import com.hotelmanagement.entity.Invoice;
import com.hotelmanagement.entity.Payment;
import com.hotelmanagement.exception.ResourceNotFoundException;
import com.hotelmanagement.repository.BookingRepository;
import com.hotelmanagement.repository.EmployeeRepository;
import com.hotelmanagement.repository.InvoiceRepository;
import com.hotelmanagement.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final BookingRepository bookingRepository;
    private final EmployeeRepository employeeRepository;

    private InvoiceResponse mapToResponse(Invoice invoice) {
        InvoiceResponse res = new InvoiceResponse();
        res.setInvoiceId(invoice.getInvoiceId());
        res.setBookingId(invoice.getBooking().getBookingId());
        res.setEmployeeId(invoice.getEmployee().getEmployeeId());
        res.setIssueDate(invoice.getIssueDate());
        res.setTaxAmount(invoice.getTaxAmount());
        res.setDiscountAmount(invoice.getDiscountAmount());
        res.setFinalAmount(invoice.getFinalAmount());
        res.setStatus(invoice.getStatus());
        
        List<PaymentResponse> payResList = invoice.getPayments().stream().map(p -> {
            PaymentResponse pr = new PaymentResponse();
            pr.setPaymentId(p.getPaymentId());
            pr.setInvoiceId(invoice.getInvoiceId());
            pr.setPaymentMethod(p.getPaymentMethod());
            pr.setPaymentAmount(p.getPaymentAmount());
            pr.setTransactionId(p.getTransactionId());
            pr.setPaymentDate(p.getPaymentDate());
            pr.setStatus(p.getStatus());
            return pr;
        }).collect(Collectors.toList());
        res.setPayments(payResList);
        return res;
    }

    @Override
    @Transactional
    public InvoiceResponse generateInvoice(InvoiceRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + request.getBookingId()));
        Employee emp = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + request.getEmployeeId()));

        BigDecimal discount = request.getDiscountAmount() != null ? request.getDiscountAmount() : BigDecimal.ZERO;
        BigDecimal tax = booking.getTotalAmount().multiply(BigDecimal.valueOf(0.1)); // 10% tax
        BigDecimal finalAmount = booking.getTotalAmount().add(tax).subtract(discount);

        Invoice invoice = Invoice.builder()
                .booking(booking)
                .employee(emp)
                .issueDate(LocalDateTime.now())
                .taxAmount(tax)
                .discountAmount(discount)
                .finalAmount(finalAmount)
                .status("UNPAID")
                .build();

        invoiceRepository.save(invoice);
        return mapToResponse(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getInvoiceById(Integer id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found: " + id));
        return mapToResponse(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
