package com.java.EDSTEM.model;

public interface RestaurantWithRating {
    Long getId();
    String getName();
    String getCuisineType();
    String getAddress();
    String getPriceRange();
    Double getAverageRating();
}
