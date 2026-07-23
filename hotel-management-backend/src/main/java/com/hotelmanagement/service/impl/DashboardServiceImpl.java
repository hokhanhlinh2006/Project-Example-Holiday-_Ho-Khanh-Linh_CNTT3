package com.hotelmanagement.service.impl;

import com.hotelmanagement.dto.response.DashboardResponse;
import com.hotelmanagement.entity.Booking;
import com.hotelmanagement.entity.Invoice;
import com.hotelmanagement.entity.Room;
import com.hotelmanagement.entity.BookingService;
import com.hotelmanagement.repository.BookingRepository;
import com.hotelmanagement.repository.InvoiceRepository;
import com.hotelmanagement.repository.RoomRepository;
import com.hotelmanagement.repository.BookingServiceRepository;
import com.hotelmanagement.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final BookingRepository bookingRepository;
    private final InvoiceRepository invoiceRepository;
    private final RoomRepository roomRepository;
    private final BookingServiceRepository bookingServiceRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse getDashboardStats() {
        long totalBookings = bookingRepository.count();
        
        List<Invoice> invoices = invoiceRepository.findAll();
        BigDecimal totalRevenue = invoices.stream()
                .filter(i -> "PAID".equals(i.getStatus()))
                .map(Invoice::getFinalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Room> rooms = roomRepository.findAll();
        long totalRoomsCount = rooms.size();
        long occupiedRoomsCount = rooms.stream()
                .filter(r -> "OCCUPIED".equals(r.getStatus()))
                .count();

        double occupancyRate = totalRoomsCount > 0 ? ((double) occupiedRoomsCount / totalRoomsCount) * 100.0 : 0.0;

        // Group services revenue by service name
        List<BookingService> orderedServices = bookingServiceRepository.findAll();
        Map<String, BigDecimal> serviceRevenueMap = new HashMap<>();
        
        for (BookingService bs : orderedServices) {
            String serviceName = bs.getService().getName();
            BigDecimal cost = bs.getPriceApplied().multiply(BigDecimal.valueOf(bs.getQuantity()));
            serviceRevenueMap.put(serviceName, serviceRevenueMap.getOrDefault(serviceName, BigDecimal.ZERO).add(cost));
        }

        return DashboardResponse.builder()
                .totalBookings(totalBookings)
                .totalRevenue(totalRevenue)
                .roomOccupancyRate(occupancyRate)
                .activeGuests(occupiedRoomsCount * 2) // Rough estimate of 2 guests per occupied room
                .revenueByService(serviceRevenueMap)
                .build();
    }
}
