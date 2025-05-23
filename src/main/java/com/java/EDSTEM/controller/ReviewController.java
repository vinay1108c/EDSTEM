package com.java.EDSTEM.controller;

import com.java.EDSTEM.model.Review;
import com.java.EDSTEM.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

//@Tag(name = "Review", description = "Review Management APIs")
@RestController
@RequestMapping("api/reviews")
@Validated
public class ReviewController {

    private final ReviewService service;

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @PostMapping
    public Review submit(@RequestBody @Valid Review review) {
        logger.info("Received request to submit review");
        return service.submitReview(review);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public Page<Review> getApprovedByRestaurant(@PathVariable Long restaurantId, Pageable pageable) {
        logger.info("Received request to fetch approved reviews for restaurant ID: {}", restaurantId);
        return service.getApprovedReviewsByRestaurant(restaurantId, pageable);
    }

    @PutMapping("/{id}/approve")
    public Review approve(@PathVariable Long id) {
        logger.info("Received request to approve review with ID: {}", id);
        return service.approveReview(id);
    }

    @PutMapping("/approve-all")
    public ResponseEntity<String> approveAllPendingReviews() {
        logger.info("Received request for updating all pending reviews: {}");
        service.approveAllPendingReviews();
        return ResponseEntity.ok("All pending reviews have been approved.");
    }


}

