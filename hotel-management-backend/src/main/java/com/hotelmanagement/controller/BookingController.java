package com.hotelmanagement.controller;

import com.hotelmanagement.dto.request.BookingRequest;
import com.hotelmanagement.dto.response.ApiResponse;
import com.hotelmanagement.dto.response.BookingResponse;
import com.hotelmanagement.service.RoomBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final RoomBookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(@Valid @RequestBody BookingRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Booking initialized pending confirmation", bookingService.createBooking(request)));
    }

    @PutMapping("/details/{detailId}/checkin")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
    public ResponseEntity<ApiResponse<BookingResponse>> checkIn(@PathVariable Integer detailId) {
        return ResponseEntity.ok(ApiResponse.success("Checked in successfully", bookingService.checkIn(detailId)));
    }

    @PutMapping("/details/{detailId}/checkout")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
    public ResponseEntity<ApiResponse<BookingResponse>> checkOut(@PathVariable Integer detailId) {
        return ResponseEntity.ok(ApiResponse.success("Checked out successfully", bookingService.checkOut(detailId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success("Fetched booking record", bookingService.getBookingById(id)));
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookings() {
        return ResponseEntity.ok(ApiResponse.success("Fetched my bookings", bookingService.getMyBookings()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getAllBookings() {
        return ResponseEntity.ok(ApiResponse.success("Fetched all bookings", bookingService.getAllBookings()));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelBooking(@PathVariable Integer id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok(ApiResponse.success("Booking cancelled", null));
    }
}
