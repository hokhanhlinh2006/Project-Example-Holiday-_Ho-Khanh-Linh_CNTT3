package com.hotelmanagement.service;

import com.hotelmanagement.dto.request.BookingRequest;
import com.hotelmanagement.dto.response.BookingResponse;
import java.util.List;

public interface RoomBookingService {
    BookingResponse createBooking(BookingRequest request);
    BookingResponse checkIn(Integer bookingDetailId);
    BookingResponse checkOut(Integer bookingDetailId);
    BookingResponse getBookingById(Integer id);
    List<BookingResponse> getAllBookings();
    List<BookingResponse> getMyBookings();
    void cancelBooking(Integer id);
}
