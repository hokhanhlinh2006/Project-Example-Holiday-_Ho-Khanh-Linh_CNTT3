package com.hotelmanagement.service.impl;

import com.hotelmanagement.dto.request.BookingRequest;
import com.hotelmanagement.dto.request.BookingDetailRequest;
import com.hotelmanagement.dto.response.*;
import com.hotelmanagement.entity.*;
import com.hotelmanagement.exception.BadRequestException;
import com.hotelmanagement.exception.ResourceNotFoundException;
import com.hotelmanagement.repository.*;
import com.hotelmanagement.service.RoomBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomBookingServiceImpl implements RoomBookingService {

    private final BookingRepository bookingRepository;
    private final BookingDetailRepository bookingDetailRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    private UserResponse mapUser(User user) {
        if (user == null) return null;
        UserResponse ur = new UserResponse();
        ur.setUserId(user.getUserId());
        ur.setEmail(user.getEmail());
        ur.setFullName(user.getFullName());
        ur.setPhoneNumber(user.getPhoneNumber());
        ur.setCccdPassport(user.getCccdPassport());
        ur.setLoyaltyPoints(user.getLoyaltyPoints());
        ur.setRoleName(user.getRole().getRoleName());
        ur.setCreatedAt(user.getCreatedAt());
        return ur;
    }

    private RoomResponse mapRoom(Room room) {
        RoomResponse rr = new RoomResponse();
        rr.setRoomId(room.getRoomId());
        rr.setRoomNumber(room.getRoomNumber());
        rr.setFloor(room.getFloor());
        rr.setStatus(room.getStatus());

        RoomType rt = room.getRoomType();
        RoomTypeResponse rtr = new RoomTypeResponse();
        rtr.setRoomTypeId(rt.getRoomTypeId());
        rtr.setName(rt.getName());
        rtr.setBasePrice(rt.getBasePrice());
        rtr.setMaxCapacity(rt.getMaxCapacity());
        rtr.setSizeM2(rt.getSizeM2());
        rtr.setAmenities(rt.getAmenities());
        rr.setRoomType(rtr);
        return rr;
    }

    private BookingDetailResponse mapDetail(BookingDetail detail) {
        BookingDetailResponse bdr = new BookingDetailResponse();
        bdr.setBookingDetailId(detail.getBookingDetailId());
        bdr.setRoom(mapRoom(detail.getRoom()));
        bdr.setStartDate(detail.getStartDate());
        bdr.setEndDate(detail.getEndDate());
        bdr.setCheckInActual(detail.getCheckInActual());
        bdr.setCheckOutActual(detail.getCheckOutActual());
        bdr.setPriceApplied(detail.getPriceApplied());
        
        List<BookingServiceResponse> bsrList = detail.getBookingServices().stream().map(bs -> {
            BookingServiceResponse bsr = new BookingServiceResponse();
            bsr.setBookingServiceId(bs.getBookingServiceId());
            bsr.setQuantity(bs.getQuantity());
            bsr.setPriceApplied(bs.getPriceApplied());
            bsr.setServiceDate(bs.getServiceDate());
            
            ServiceResponse sr = new ServiceResponse();
            sr.setServiceId(bs.getService().getServiceId());
            sr.setName(bs.getService().getName());
            sr.setPrice(bs.getService().getPrice());
            sr.setUnit(bs.getService().getUnit());
            bsr.setService(sr);
            return bsr;
        }).collect(Collectors.toList());
        
        bdr.setBookingServices(bsrList);
        return bdr;
    }

    private BookingResponse mapToResponse(Booking bk) {
        BookingResponse br = new BookingResponse();
        br.setBookingId(bk.getBookingId());
        br.setUser(mapUser(bk.getUser()));
        br.setTotalAmount(bk.getTotalAmount());
        br.setBookingStatus(bk.getBookingStatus());
        br.setCreatedAt(bk.getCreatedAt());
        br.setBookingDetails(bk.getBookingDetails().stream().map(this::mapDetail).collect(Collectors.toList()));
        return br;
    }

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        User user = null;
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));
        }

        Booking booking = Booking.builder()
                .user(user)
                .bookingStatus("PENDING")
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal grandTotal = BigDecimal.ZERO;
        List<BookingDetail> details = new ArrayList<>();

        for (BookingDetailRequest detailReq : request.getDetails()) {
            Room room = roomRepository.findById(detailReq.getRoomId())
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + detailReq.getRoomId()));

            long days = ChronoUnit.DAYS.between(detailReq.getStartDate(), detailReq.getEndDate());
            if (days <= 0) {
                throw new BadRequestException("End date must be after start date");
            }

            BigDecimal roomCost = room.getRoomType().getBasePrice().multiply(BigDecimal.valueOf(days));
            grandTotal = grandTotal.add(roomCost);

            BookingDetail detail = BookingDetail.builder()
                    .booking(booking)
                    .room(room)
                    .startDate(detailReq.getStartDate())
                    .endDate(detailReq.getEndDate())
                    .priceApplied(room.getRoomType().getBasePrice())
                    .build();

            details.add(detail);
        }

        booking.setTotalAmount(grandTotal);
        booking.setBookingDetails(details);
        bookingRepository.save(booking);

        return mapToResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse checkIn(Integer bookingDetailId) {
        BookingDetail detail = bookingDetailRepository.findById(bookingDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking Detail not found with ID: " + bookingDetailId));
        
        detail.setCheckInActual(LocalDateTime.now());
        detail.getRoom().setStatus("OCCUPIED");
        roomRepository.save(detail.getRoom());
        
        Booking booking = detail.getBooking();
        booking.setBookingStatus("CHECKED_IN");
        bookingRepository.save(booking);
        
        return mapToResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse checkOut(Integer bookingDetailId) {
        BookingDetail detail = bookingDetailRepository.findById(bookingDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking Detail not found with ID: " + bookingDetailId));
        
        detail.setCheckOutActual(LocalDateTime.now());
        detail.getRoom().setStatus("VACANT_DIRTY");
        roomRepository.save(detail.getRoom());
        
        Booking booking = detail.getBooking();
        booking.setBookingStatus("CHECKED_OUT");
        bookingRepository.save(booking);
        
        return mapToResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));
        return mapToResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return bookingRepository.findAll().stream()
                .filter(b -> b.getUser() != null && email.equalsIgnoreCase(b.getUser().getEmail()))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelBooking(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));
        booking.setBookingStatus("CANCELLED");
        bookingRepository.save(booking);
    }
}
