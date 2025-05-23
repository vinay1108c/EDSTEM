package com.java.EDSTEM.service;

import com.java.EDSTEM.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ReviewService {

    public Review submitReview(Review review);

    public Page<Review> getApprovedReviewsByRestaurant(Long restaurantId, Pageable pageable);

    public Review approveReview(Long id);

    public void approveAllPendingReviews();
}
