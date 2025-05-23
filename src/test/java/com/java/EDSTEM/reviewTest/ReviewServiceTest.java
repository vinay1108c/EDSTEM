package com.java.EDSTEM.reviewTest;

import com.java.EDSTEM.exception.ResourceNotFoundException;
import com.java.EDSTEM.model.Review;
import com.java.EDSTEM.model.Restaurant;
import com.java.EDSTEM.repository.ReviewRepository;
import com.java.EDSTEM.serviceImpl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceImplTest {

    @Mock
    private ReviewRepository repository;

    @InjectMocks
    private ReviewServiceImpl service;

    private Review sampleReview;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        restaurant = new Restaurant();
        restaurant.setId(1L);

        sampleReview = new Review();
        sampleReview.setId(100L);
        sampleReview.setRestaurantId(1L);
        sampleReview.setStatus(Review.ReviewStatus.PENDING);
    }

    @Test
    void testSubmitReview() {
        when(repository.save(any(Review.class))).thenReturn(sampleReview);

        Review result = service.submitReview(sampleReview);

        assertNotNull(result);
        assertEquals(Review.ReviewStatus.PENDING, result.getStatus());
        verify(repository, times(1)).save(sampleReview);
    }

    @Test
    void testGetApprovedReviewsByRestaurant() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Review> page = new PageImpl<>(List.of(sampleReview));
        when(repository.findByRestaurantIdAndStatus(1L, Review.ReviewStatus.APPROVED, pageable)).thenReturn(page);

        Page<Review> result = service.getApprovedReviewsByRestaurant(1L, pageable);

        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findByRestaurantIdAndStatus(1L, Review.ReviewStatus.APPROVED, pageable);
    }

    @Test
    void testApproveReview_Success() {
        when(repository.findById(100L)).thenReturn(Optional.of(sampleReview));
        when(repository.save(any(Review.class))).thenReturn(sampleReview);

        Review result = service.approveReview(100L);

        assertEquals(Review.ReviewStatus.APPROVED, result.getStatus());
        verify(repository, times(1)).findById(100L);
        verify(repository, times(1)).save(sampleReview);
    }

    @Test
    void testApproveReview_NotFound() {
        when(repository.findById(200L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.approveReview(200L);
        });

        assertEquals("Review not found with ID: 200", exception.getMessage());
        verify(repository, times(1)).findById(200L);
    }

    @Test
    void testApproveAllPendingReviews() {
        List<Review> pendingReviews = List.of(sampleReview);
        when(repository.findByStatus(Review.ReviewStatus.PENDING)).thenReturn(pendingReviews);

        service.approveAllPendingReviews();

        assertEquals(Review.ReviewStatus.APPROVED, sampleReview.getStatus());
        verify(repository, times(1)).findByStatus(Review.ReviewStatus.PENDING);
        verify(repository, times(1)).saveAll(pendingReviews);
    }

    @Test
    void testApproveAllPendingReviews_EmptyList() {
        when(repository.findByStatus(Review.ReviewStatus.PENDING)).thenReturn(Collections.emptyList());

        service.approveAllPendingReviews();

        verify(repository, times(1)).findByStatus(Review.ReviewStatus.PENDING);
        verify(repository, times(1)).saveAll(Collections.emptyList());
    }
}
