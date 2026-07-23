package com.hotelmanagement.controller;

import com.hotelmanagement.dto.request.ReviewRequest;
import com.hotelmanagement.dto.response.ApiResponse;
import com.hotelmanagement.dto.response.ReviewResponse;
import com.hotelmanagement.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> postReview(@Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Review posted successfully", reviewService.postReview(request)));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsForBooking(@PathVariable Integer bookingId) {
        return ResponseEntity.ok(ApiResponse.success("Fetched reviews for booking", reviewService.getReviewsForBooking(bookingId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getAllReviews() {
        return ResponseEntity.ok(ApiResponse.success("Fetched all reviews", reviewService.getAllReviews()));
    }
}
