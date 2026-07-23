package com.hotelmanagement.controller;

import com.hotelmanagement.dto.request.InvoiceRequest;
import com.hotelmanagement.dto.response.ApiResponse;
import com.hotelmanagement.dto.response.InvoiceResponse;
import com.hotelmanagement.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<ApiResponse<InvoiceResponse>> generateInvoice(@Valid @RequestBody InvoiceRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Invoice generated successfully", invoiceService.generateInvoice(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getInvoiceById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success("Fetched invoice details", invoiceService.getInvoiceById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<InvoiceResponse>>> getAllInvoices() {
        return ResponseEntity.ok(ApiResponse.success("Fetched all invoices", invoiceService.getAllInvoices()));
    }
}
