package com.java.EDSTEM.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"ADMIN"})
class RestaurantControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        reviewRepository.deleteAll();
        restaurantRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /api/restaurants - Create restaurant")
    void shouldCreateRestaurant() throws Exception {
        Restaurant restaurant = new Restaurant(
                null,
                "Test Restaurant",
                "INDIAN",
                "Mumbai",
                Restaurant.PriceRange.LOW
        );

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurant)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Restaurant"))
                .andExpect(jsonPath("$.cuisineType").value("INDIAN"))
                .andExpect(jsonPath("$.address").value("Mumbai"))
                .andExpect(jsonPath("$.priceRange").value("LOW"));
    }


    @Test
    @DisplayName("GET /api/restaurants - Get all restaurants")
    void shouldGetAllRestaurants() throws Exception {
        Restaurant r1 = new Restaurant(null, "Rest 1", "Indian", "Delhi", Restaurant.PriceRange.HIGH);
        Restaurant r2 = new Restaurant(null, "Rest 2", "Chinese", "Chennai", Restaurant.PriceRange.MEDIUM);
        restaurantRepository.saveAll(List.of(r1, r2));

        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Rest 1")))
                .andExpect(jsonPath("$[1].cuisineType", is("Chinese")));
    }

    @Test
    @DisplayName("GET /api/restaurants/top3?cuisine=Indian - Top 3 by cuisine")
    void shouldGetTop3RestaurantsByCuisine() throws Exception {
        for (int i = 1; i <= 5; i++) {
            Restaurant r = new Restaurant(null, "Rest " + i, "Indian", "City " + i, Restaurant.PriceRange.LOW);
            r = restaurantRepository.save(r); // get generated ID after save

            Review review = new Review();
            review.setRestaurantId(r.getId()); // set real restaurant ID here
            review.setRating(6 - i); // descending ratings from 5 to 1
            review.setStatus(Review.ReviewStatus.APPROVED);
            review.setComment("Great");
            review.setVisitDate(LocalDate.now());

            reviewRepository.save(review);
        }

        mockMvc.perform(get("/api/restaurants/top3")
                        .param("cuisine", "Indian"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(3)))
                .andExpect(jsonPath("$[0].name", is("Rest 1")))
                .andExpect(jsonPath("$[2].name", is("Rest 3")));
    }



    @Test
    @DisplayName("GET /api/restaurants/{id}/average-rating - Average rating")
    void shouldGetAverageRating() throws Exception {
        Restaurant restaurant = new Restaurant(null, "Rest Avg", "Thai", "Bangalore", Restaurant.PriceRange.LOW);
        restaurant = restaurantRepository.save(restaurant);

        Review review = new Review();
        review.setRestaurantId(restaurant.getId());
        review.setRating(4);
        review.setStatus(Review.ReviewStatus.APPROVED);
        review.setComment("Nice place");
        review.setVisitDate(LocalDate.now());
        reviewRepository.save(review);

        mockMvc.perform(get("/api/restaurants/" + restaurant.getId() + "/average-rating"))
                .andExpect(status().isOk())
                .andExpect(content().string("4.0"));
    }


}
