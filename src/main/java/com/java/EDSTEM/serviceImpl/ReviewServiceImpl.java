package com.java.EDSTEM.serviceImpl;

import com.java.EDSTEM.exception.ResourceNotFoundException;
import com.java.EDSTEM.model.Review;
import com.java.EDSTEM.repository.ReviewRepository;
import com.java.EDSTEM.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository repository;

    public ReviewServiceImpl(ReviewRepository repository) {
        this.repository = repository;
    }

    @Override
    public Review submitReview(Review review) {
        review.setStatus(Review.ReviewStatus.PENDING);
        logger.info("Submitting review for restaurant ID: {}", review.getRestaurantId());
        return repository.save(review);
    }

    @Override
    public Page<Review> getApprovedReviewsByRestaurant(Long restaurantId, Pageable pageable) {
        logger.info("Fetching approved reviews for restaurant ID: {}", restaurantId);
        return repository.findByRestaurantIdAndStatus(restaurantId, Review.ReviewStatus.APPROVED, pageable);
    }

    @Override
    public Review approveReview(Long id) {
        logger.info("Approving review with ID: {}", id);
        Review review = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));
        review.setStatus(Review.ReviewStatus.APPROVED);
        return repository.save(review);
    }


    public void approveAllPendingReviews() {
        logger.info("Approving all pending reviews");
        List<Review> pendingReviews = repository.findByStatus(Review.ReviewStatus.PENDING);
        if(pendingReviews!=null)
                pendingReviews.forEach(r -> r.setStatus(Review.ReviewStatus.APPROVED));
        repository.saveAll(pendingReviews);
    }
}
