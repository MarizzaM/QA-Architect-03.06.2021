package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RestController
public class RestaurantController {

    private static List<RestaurantDTO> m_restaurants = new ArrayList<>();
    RestaurantDAO restaurantDAO = new RestaurantDAO("jdbc:sqlite:C:/SQLite/restaurant.db");

    @GetMapping("/restaurant")
    public List<RestaurantDTO> getRestaurants()
    {
        return restaurantDAO.getAllRestaurants();
    }
    @GetMapping("/restaurant/{id}")
    public RestaurantDTO doGetRestaurantById(@PathVariable("id") int id){
        return restaurantDAO.getRestaurantById(id);
    }

    @PostMapping("/restaurant")
    public void addRestaurant(@RequestBody RestaurantDTO restaurant){
        restaurantDAO.insertRestaurant(restaurant);;
    }

    @PutMapping("/restaurant/{id}")
    public void doUpdateRestaurantByID(@PathVariable("id") int id, @RequestBody RestaurantDTO sent){
        restaurantDAO.updateRestaurant(sent, id);
    }

    @DeleteMapping("/restaurant/{id}")
    public void deleteRestaurantById(@PathVariable("id") int id){
        restaurantDAO.deleteRestaurant(id);
    }

}
