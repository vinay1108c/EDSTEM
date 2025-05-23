package com.java.EDSTEM.repository;

import com.java.EDSTEM.model.Review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByRestaurantIdAndStatus(Long restaurantId, Review.ReviewStatus status, Pageable pageable);

    List<Review> findByStatus(Review.ReviewStatus status);
}
