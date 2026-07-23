package com.hotelmanagement.controller;

import com.hotelmanagement.dto.request.BookServiceRequest;
import com.hotelmanagement.dto.request.ServiceRequest;
import com.hotelmanagement.dto.response.ApiResponse;
import com.hotelmanagement.dto.response.BookingServiceResponse;
import com.hotelmanagement.dto.response.ServiceResponse;
import com.hotelmanagement.service.HotelServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
public class ServiceController {

    private final HotelServiceService hotelService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ServiceResponse>> createService(@Valid @RequestBody ServiceRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Service created successfully", hotelService.createService(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ServiceResponse>> updateService(@PathVariable Integer id, @Valid @RequestBody ServiceRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Service updated successfully", hotelService.updateService(id, request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceResponse>> getServiceById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success("Fetched service details", hotelService.getServiceById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ServiceResponse>>> getAllServices() {
        return ResponseEntity.ok(ApiResponse.success("Fetched all services", hotelService.getAllServices()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteService(@PathVariable Integer id) {
        hotelService.deleteService(id);
        return ResponseEntity.ok(ApiResponse.success("Service deleted successfully", null));
    }

    @PostMapping("/order")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
    public ResponseEntity<ApiResponse<BookingServiceResponse>> orderService(@Valid @RequestBody BookServiceRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Service ordered to room", hotelService.orderService(request)));
    }
}
