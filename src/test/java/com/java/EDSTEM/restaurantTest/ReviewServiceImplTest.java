package com.java.EDSTEM.restaurantTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.java.EDSTEM.exception.ResourceNotFoundException;
import com.java.EDSTEM.model.Review;
import com.java.EDSTEM.repository.ReviewRepository;
import com.java.EDSTEM.serviceImpl.ReviewServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository repository;

    @InjectMocks
    private ReviewServiceImpl service;

    private Review review;

    @BeforeEach
    void setUp() {
        review = new Review();
        review.setId(1L);
        review.setRestaurantId(100L);
        review.setRating(4);
        review.setComment("Great food");
        review.setVisitDate(LocalDate.now());
        review.setStatus(Review.ReviewStatus.PENDING);
    }

    @Test
    void testSubmitReview_setsStatusPendingAndSaves() {
        when(repository.save(any(Review.class))).thenReturn(review);

        Review submitted = service.submitReview(review);

        assertEquals(Review.ReviewStatus.PENDING, submitted.getStatus());
        verify(repository).save(review);
    }

    @Test
    void testGetApprovedReviewsByRestaurant_returnsPage() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Review> page = new PageImpl<>(List.of(review));
        when(repository.findByRestaurantIdAndStatus(100L, Review.ReviewStatus.APPROVED, pageable)).thenReturn(page);

        Page<Review> result = service.getApprovedReviewsByRestaurant(100L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository).findByRestaurantIdAndStatus(100L, Review.ReviewStatus.APPROVED, pageable);
    }

    @Test
    void testApproveReview_existingReview_approvesAndSaves() {
        review.setStatus(Review.ReviewStatus.PENDING);
        when(repository.findById(1L)).thenReturn(Optional.of(review));
        when(repository.save(any(Review.class))).thenReturn(review);

        Review approved = service.approveReview(1L);

        assertEquals(Review.ReviewStatus.APPROVED, approved.getStatus());
        verify(repository).findById(1L);
        verify(repository).save(review);
    }

    @Test
    void testApproveReview_notFound_throwsException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.approveReview(1L));
        verify(repository).findById(1L);
        verify(repository, never()).save(any());
    }

    @Test
    void testApproveAllPendingReviews_approvesAndSavesAll() {
        Review r1 = new Review();
        r1.setId(1L);
        r1.setStatus(Review.ReviewStatus.PENDING);

        Review r2 = new Review();
        r2.setId(2L);
        r2.setStatus(Review.ReviewStatus.PENDING);

        List<Review> pending = List.of(r1, r2);

        when(repository.findByStatus(Review.ReviewStatus.PENDING)).thenReturn(pending);
        when(repository.saveAll(anyList())).thenReturn(pending);

        service.approveAllPendingReviews();

        // Verify all pending reviews status set to APPROVED
        assertTrue(pending.stream().allMatch(r -> r.getStatus() == Review.ReviewStatus.APPROVED));

        verify(repository).findByStatus(Review.ReviewStatus.PENDING);
        verify(repository).saveAll(pending);
    }

    @Test
    void testApproveAllPendingReviews_handlesNullListGracefully() {
        when(repository.findByStatus(Review.ReviewStatus.PENDING)).thenReturn(null);

        // Should not throw NPE
        assertDoesNotThrow(() -> service.approveAllPendingReviews());

        verify(repository).findByStatus(Review.ReviewStatus.PENDING);
        verify(repository, never()).saveAll(anyList());
    }
}

