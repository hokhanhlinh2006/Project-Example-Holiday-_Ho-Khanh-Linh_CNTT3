package com.hotelmanagement.service;

import com.hotelmanagement.dto.request.ServiceRequest;
import com.hotelmanagement.dto.request.BookServiceRequest;
import com.hotelmanagement.dto.response.ServiceResponse;
import com.hotelmanagement.dto.response.BookingServiceResponse;
import java.util.List;

public interface HotelServiceService {
    ServiceResponse createService(ServiceRequest request);
    ServiceResponse updateService(Integer id, ServiceRequest request);
    ServiceResponse getServiceById(Integer id);
    List<ServiceResponse> getAllServices();
    void deleteService(Integer id);
    BookingServiceResponse orderService(BookServiceRequest request);
}
