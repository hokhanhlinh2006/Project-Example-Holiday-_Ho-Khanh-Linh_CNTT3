package com.hotelmanagement.service;

import com.hotelmanagement.dto.request.InvoiceRequest;
import com.hotelmanagement.dto.response.InvoiceResponse;
import java.util.List;

public interface InvoiceService {
    InvoiceResponse generateInvoice(InvoiceRequest request);
    InvoiceResponse getInvoiceById(Integer id);
    List<InvoiceResponse> getAllInvoices();
}
