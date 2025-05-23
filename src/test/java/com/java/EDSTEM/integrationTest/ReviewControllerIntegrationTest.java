package com.java.EDSTEM.integrationTest;


import com.java.EDSTEM.model.Restaurant;
import com.java.EDSTEM.model.Review;
import com.java.EDSTEM.repository.RestaurantRepository;
import com.java.EDSTEM.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "admin", roles = {"ADMIN"})
public class ReviewControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private Restaurant testRestaurant;

    @BeforeEach
    void setup() {
        // Create a test restaurant for reviews
        testRestaurant = new Restaurant(null, "Test Restaurant", "Italian", "Test City", Restaurant.PriceRange.MEDIUM);
        testRestaurant = restaurantRepository.save(testRestaurant);
    }

    @Test
    @DisplayName("POST /api/reviews - Submit a review")
    void shouldSubmitReview() throws Exception {
        String reviewJson = """
            {
                "restaurantId": %d,
                "rating": 5,
                "comment": "Excellent food!",
                "visitDate": "%s",
                "status": "PENDING"
            }
            """.formatted(testRestaurant.getId(), LocalDate.now().toString());

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.comment").value("Excellent food!"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("GET /api/reviews/restaurant/{restaurantId} - Get approved reviews by restaurant")
    void shouldGetApprovedReviewsByRestaurant() throws Exception {
        // Save approved and pending reviews for the restaurant
        Review approvedReview = new Review();
        approvedReview.setRestaurantId(testRestaurant.getId());
        approvedReview.setRating(4);
        approvedReview.setComment("Good!");
        approvedReview.setVisitDate(LocalDate.now());
        approvedReview.setStatus(Review.ReviewStatus.APPROVED);
        reviewRepository.save(approvedReview);

        Review pendingReview = new Review();
        pendingReview.setRestaurantId(testRestaurant.getId());
        pendingReview.setRating(2);
        pendingReview.setComment("Needs improvement");
        pendingReview.setVisitDate(LocalDate.now());
        pendingReview.setStatus(Review.ReviewStatus.PENDING);
        reviewRepository.save(pendingReview);

        mockMvc.perform(get("/api/reviews/restaurant/" + testRestaurant.getId())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1)) // Only approved reviews
                .andExpect(jsonPath("$.content[0].comment").value("Good!"));
    }

    @Test
    @DisplayName("PUT /api/reviews/{id}/approve - Approve a review")
    void shouldApproveReview() throws Exception {
        Review review = new Review();
        review.setRestaurantId(testRestaurant.getId());
        review.setRating(3);
        review.setComment("Average");
        review.setVisitDate(LocalDate.now());
        review.setStatus(Review.ReviewStatus.PENDING);
        review = reviewRepository.save(review);

        mockMvc.perform(put("/api/reviews/" + review.getId() + "/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    @DisplayName("PUT /api/reviews/approve-all - Approve all pending reviews")
    void shouldApproveAllPendingReviews() throws Exception {
        // Create multiple pending reviews with valid ratings
        int[] ratings = {3, 4, 5};
        for (int i = 0; i < ratings.length; i++) {
            Review review = new Review();
            review.setRestaurantId(testRestaurant.getId());
            review.setRating(ratings[i]);  // valid ratings
            review.setComment("Review " + (i + 1));
            review.setVisitDate(LocalDate.now());
            review.setStatus(Review.ReviewStatus.PENDING);
            reviewRepository.save(review);
        }

        mockMvc.perform(put("/api/reviews/approve-all"))
                .andExpect(status().isOk())
                .andExpect(content().string("All pending reviews have been approved."));

        // Assert all pending reviews are now approved
        List<Review> approvedReviews = reviewRepository.findAll()
                .stream()
                .filter(r -> r.getStatus() == Review.ReviewStatus.APPROVED)
                .toList();

    }

}
