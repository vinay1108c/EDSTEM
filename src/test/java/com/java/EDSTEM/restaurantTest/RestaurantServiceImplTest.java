package com.java.EDSTEM.restaurantTest;

import com.java.EDSTEM.model.Restaurant;
import com.java.EDSTEM.model.RestaurantWithRating;
import com.java.EDSTEM.repository.RestaurantRepository;
import com.java.EDSTEM.serviceImpl.RestaurantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository repository;

    @InjectMocks
    private RestaurantServiceImpl service;

    private Restaurant sampleRestaurant;

    @BeforeEach
    void setUp() {
        sampleRestaurant = new Restaurant();
        sampleRestaurant.setId(1L);
        sampleRestaurant.setName("Testaurant");
        sampleRestaurant.setCuisineType("Italian");
    }

    @Test
    void testCreateRestaurant() {
        when(repository.save(any(Restaurant.class))).thenReturn(sampleRestaurant);

        Restaurant result = service.createRestaurant(sampleRestaurant);

        assertNotNull(result);
        assertEquals("Testaurant", result.getName());
        verify(repository, times(1)).save(sampleRestaurant);
    }

    @Test
    void testGetAllRestaurants() {
        List<Restaurant> list = List.of(sampleRestaurant);
        when(repository.findAll()).thenReturn(list);

        List<Restaurant> result = service.getAllRestaurants();

        assertEquals(1, result.size());
        assertEquals("Testaurant", result.get(0).getName());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetTop3ByCuisine() {
        List<Restaurant> list = List.of(sampleRestaurant);
        when(repository.findTop3ByCuisineTypeOrderByIdDesc("Italian")).thenReturn(list);

        List<RestaurantWithRating> result = service.getTop3ByCuisine("Italian");

        assertEquals(1, result.size());
        assertEquals("Italian", result.get(0).getCuisineType());
        verify(repository, times(1)).findTop3ByCuisineTypeOrderByIdDesc("Italian");
    }

    @Test
    void testGetAverageRating() {
        when(repository.findAverageRatingByRestaurantId(1L)).thenReturn(4.2);

        double result = service.getAverageRating(1L);

        assertEquals(4.2, result);
        verify(repository, times(1)).findAverageRatingByRestaurantId(1L);
    }
}
