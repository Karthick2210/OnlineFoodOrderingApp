package com.initial.Service;

import com.initial.Request.CreateRestaurantRequest;
import com.initial.model.Restaurant;
import com.initial.model.User;

import java.util.List;

public interface RestaurantService {

    public Restaurant createRestaurant(CreateRestaurantRequest req , User user);

    public Restaurant updateRestaurant(Long restaurantId , CreateRestaurantRequest updateRestaurantRequest);

    public void deleteRestaurant(Long restaurantId);

    public List<Restaurant> getAllRestaurant();

    public List<Restaurant> searchRestaurant();

    public Restaurant findRestaurantById(Long id) throws  Exception;

    public  Restaurant getRestaurantByUserId(Long userId) throws  Exception;

    public  Restaurant addTOFavourites(Long restaurantId,User  user) throws Exception;

    public Restaurant updateRestaurantStatus(Long id) throws Exception;

}
