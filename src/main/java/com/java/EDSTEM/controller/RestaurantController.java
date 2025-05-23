package com.java.EDSTEM.controller;

import com.java.EDSTEM.model.Restaurant;
import com.java.EDSTEM.model.RestaurantWithRating;
import com.java.EDSTEM.service.RestaurantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@Tag(name = "Restaurant", description = "Restaurant Management APIs")
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

    private final RestaurantService service;

    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    @PostMapping
    public Restaurant create(@RequestBody @Valid Restaurant restaurant) {
        logger.info("Received request to create restaurant");
        return service.createRestaurant(restaurant);

    }

    @GetMapping
    public List<Restaurant> getAll() {
        logger.info("Received request to get all restaurants");
        return service.getAllRestaurants();
    }

    @GetMapping("/top3")
    public List<RestaurantWithRating> getTop3(@RequestParam String cuisine) {
        logger.info("Received request to get top 3 restaurants for cuisine: {}", cuisine);
        return service.getTop3ByCuisine(cuisine);
    }

    @GetMapping("/{id}/average-rating")
    public double getAverageRating(@PathVariable Long id) {
        logger.info("Received request average rating: {}", id);
        return service.getAverageRating(id);
    }

}
