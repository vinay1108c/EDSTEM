package com.java.EDSTEM.repository;

import com.java.EDSTEM.model.Restaurant;
import com.java.EDSTEM.model.RestaurantWithRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query(value = """
    SELECT 
        r.id,
        r.name,
        r.cuisine_type AS cuisineType,
        r.address,
        r.price_range AS priceRange,
        ROUND(AVG(rev.rating)) AS averageRating
    FROM restaurant.restaurant r
    JOIN restaurant.review rev ON r.id = rev.restaurant_id
    WHERE r.cuisine_type = :cuisine
      AND rev.status = 'APPROVED'
    GROUP BY r.id, r.name, r.cuisine_type, r.address, r.price_range
    ORDER BY AVG(rev.rating) DESC
    LIMIT 3
""", nativeQuery = true)
    List<RestaurantWithRating> getTop3ByCuisineTypeOrderedByAvgRating(@Param("cuisine") String cuisine);



    List<Restaurant> findTop3ByCuisineTypeOrderByIdDesc(String cuisineType);

    @Query("SELECT ROUND(AVG(r.rating)) FROM Review r WHERE r.restaurantId = :restaurantId AND r.status = 'APPROVED'")
    double findAverageRatingByRestaurantId(@Param("restaurantId") Long restaurantId);
}
