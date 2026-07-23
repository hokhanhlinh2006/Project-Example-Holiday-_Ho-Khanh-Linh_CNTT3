package com.hotelmanagement.service.impl;

import com.hotelmanagement.dto.request.ReviewRequest;
import com.hotelmanagement.dto.response.ReviewResponse;
import com.hotelmanagement.entity.Booking;
import com.hotelmanagement.entity.Review;
import com.hotelmanagement.entity.User;
import com.hotelmanagement.exception.ResourceNotFoundException;
import com.hotelmanagement.repository.BookingRepository;
import com.hotelmanagement.repository.ReviewRepository;
import com.hotelmanagement.repository.UserRepository;
import com.hotelmanagement.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    private ReviewResponse mapToResponse(Review rv) {
        ReviewResponse res = new ReviewResponse();
        res.setReviewId(rv.getReviewId());
        res.setBookingId(rv.getBooking().getBookingId());
        res.setUserFullName(rv.getUser().getFullName());
        res.setRating(rv.getRating());
        res.setComment(rv.getComment());
        res.setCreatedAt(rv.getCreatedAt());
        return res;
    }

    @Override
    @Transactional
    public ReviewResponse postReview(ReviewRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + request.getBookingId()));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUserId()));

        Review review = Review.builder()
                .booking(booking)
                .user(user)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        reviewRepository.save(review);
        return mapToResponse(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsForBooking(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));
        return booking.getReviews().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
