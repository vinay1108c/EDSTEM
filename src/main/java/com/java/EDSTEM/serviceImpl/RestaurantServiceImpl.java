package com.java.EDSTEM.serviceImpl;

import com.java.EDSTEM.model.Restaurant;
import com.java.EDSTEM.model.RestaurantWithRating;
import com.java.EDSTEM.repository.RestaurantRepository;
import com.java.EDSTEM.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class RestaurantServiceImpl implements RestaurantService {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

    private final RestaurantRepository repository;

    public RestaurantServiceImpl(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Override
    public Restaurant createRestaurant(Restaurant restaurant) {
        logger.info("Creating restaurant: {}", restaurant.getName());
        return repository.save(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        logger.info("Fetching all restaurants");
        return repository.findAll();
    }

    @Override
    public List<RestaurantWithRating> getTop3ByCuisine(String cuisine) {
        logger.info("Fetching top 3 restaurants by cuisine: {}", cuisine);
        List<RestaurantWithRating> list =  repository.getTop3ByCuisineTypeOrderedByAvgRating(cuisine);
        return list;
    }

    public double getAverageRating(Long restaurantId) {
        logger.info("Fetching Average Rate : {}", restaurantId);
        return repository.findAverageRatingByRestaurantId(restaurantId);
    }
}
