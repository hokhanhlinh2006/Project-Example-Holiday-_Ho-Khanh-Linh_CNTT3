package com.hotelmanagement.service.impl;

import com.hotelmanagement.dto.request.BookServiceRequest;
import com.hotelmanagement.dto.request.ServiceRequest;
import com.hotelmanagement.dto.response.BookingServiceResponse;
import com.hotelmanagement.dto.response.ServiceResponse;
import com.hotelmanagement.entity.BookingDetail;
import com.hotelmanagement.entity.BookingService;
import com.hotelmanagement.entity.HotelService;
import com.hotelmanagement.exception.ResourceNotFoundException;
import com.hotelmanagement.repository.BookingDetailRepository;
import com.hotelmanagement.repository.BookingServiceRepository;
import com.hotelmanagement.repository.ServiceRepository;
import com.hotelmanagement.service.HotelServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceServiceImpl implements HotelServiceService {

    private final ServiceRepository serviceRepository;
    private final BookingDetailRepository bookingDetailRepository;
    private final BookingServiceRepository bookingServiceRepository;

    private ServiceResponse mapToResponse(HotelService service) {
        ServiceResponse res = new ServiceResponse();
        res.setServiceId(service.getServiceId());
        res.setName(service.getName());
        res.setDescription(service.getDescription());
        res.setPrice(service.getPrice());
        res.setUnit(service.getUnit());
        return res;
    }

    @Override
    @Transactional
    public ServiceResponse createService(ServiceRequest request) {
        HotelService service = HotelService.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .unit(request.getUnit())
                .build();
        serviceRepository.save(service);
        return mapToResponse(service);
    }

    @Override
    @Transactional
    public ServiceResponse updateService(Integer id, ServiceRequest request) {
        HotelService service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + id));
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setUnit(request.getUnit());
        serviceRepository.save(service);
        return mapToResponse(service);
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResponse getServiceById(Integer id) {
        HotelService service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found: " + id));
        return mapToResponse(service);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceResponse> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteService(Integer id) {
        HotelService service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found: " + id));
        serviceRepository.delete(service);
    }

    @Override
    @Transactional
    public BookingServiceResponse orderService(BookServiceRequest request) {
        BookingDetail detail = bookingDetailRepository.findById(request.getBookingDetailId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking detail not found: " + request.getBookingDetailId()));
        HotelService service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found: " + request.getServiceId()));

        BookingService bookingService = BookingService.builder()
                .bookingDetail(detail)
                .service(service)
                .quantity(request.getQuantity())
                .priceApplied(service.getPrice())
                .serviceDate(LocalDateTime.now())
                .build();

        bookingServiceRepository.save(bookingService);

        BookingServiceResponse response = new BookingServiceResponse();
        response.setBookingServiceId(bookingService.getBookingServiceId());
        response.setQuantity(bookingService.getQuantity());
        response.setPriceApplied(bookingService.getPriceApplied());
        response.setServiceDate(bookingService.getServiceDate());
        
        ServiceResponse sr = new ServiceResponse();
        sr.setServiceId(service.getServiceId());
        sr.setName(service.getName());
        sr.setPrice(service.getPrice());
        sr.setUnit(service.getUnit());
        response.setService(sr);

        return response;
    }
}
