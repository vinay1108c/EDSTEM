package com.java.EDSTEM.service;

import com.java.EDSTEM.model.Restaurant;
import com.java.EDSTEM.model.RestaurantWithRating;

import java.util.List;

public interface RestaurantService {
    public Restaurant createRestaurant(Restaurant restaurant);

    public List<Restaurant> getAllRestaurants() ;

    public List<RestaurantWithRating> getTop3ByCuisine(String cuisine);

    public double getAverageRating(Long restaurantId);

}
