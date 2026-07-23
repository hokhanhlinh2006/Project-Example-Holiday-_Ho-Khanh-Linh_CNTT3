package com.hotelmanagement.service;

import com.hotelmanagement.dto.request.ReviewRequest;
import com.hotelmanagement.dto.response.ReviewResponse;
import java.util.List;

public interface ReviewService {
    ReviewResponse postReview(ReviewRequest request);
    List<ReviewResponse> getReviewsForBooking(Integer bookingId);
    List<ReviewResponse> getAllReviews();
}
