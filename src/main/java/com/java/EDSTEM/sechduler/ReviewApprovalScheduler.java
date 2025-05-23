package com.example.restaurantreviewapi.scheduler;

import com.java.EDSTEM.service.ReviewService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReviewApprovalScheduler {

    private final ReviewService reviewService;

    public ReviewApprovalScheduler(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Scheduled to run every day at midnight (00:00)
    @Scheduled(cron = "0 0 0 * * ?")
    public void approvePendingReviewsAtMidnight() {
        reviewService.approveAllPendingReviews();
    }
}
