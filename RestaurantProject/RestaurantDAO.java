package com.example.demo;

import java.sql.*;
import java.util.ArrayList;

public class RestaurantDAO {

    private String m_conn;

    public RestaurantDAO(String m_conn) {
        this.m_conn = m_conn;
    }

    public ArrayList<RestaurantDTO> getAllRestaurants (){

        ArrayList<RestaurantDTO> m_restaurants = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(m_conn)){

            if(conn != null){
                String sql = "SELECT * FROM Restaurants";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()){
                    RestaurantDTO el = new RestaurantDTO(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("foodtype"),
                            rs.getFloat("mealprice"));
                    m_restaurants.add(el);
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return m_restaurants;
    }

    public RestaurantDTO getRestaurantById (int id){
        try (Connection conn = DriverManager.getConnection(m_conn)){
            if(conn != null) {
                String sql = "SELECT * FROM Restaurants WHERE id = " + id;
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()){
                    RestaurantDTO restaurant = new RestaurantDTO(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("foodtype"),
                            rs.getFloat("mealprice"));
                    return restaurant;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void insertRestaurant(RestaurantDTO restaurant){
        try (Connection conn = DriverManager.getConnection(m_conn)){
            if(conn != null) {
                String sql = String.format("INSERT INTO Restaurants (id, name, address, foodtype, mealprice)" +
                                " VALUES (%d, '%s', '%s', '%s', %f)", restaurant.getM_id(),
                        restaurant.getM_name(), restaurant.getM_address(),
                        restaurant.getM_foodtype(), restaurant.getM_mealprice());
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateRestaurant(RestaurantDTO restaurant, int id){
        try (Connection conn = DriverManager.getConnection(m_conn)){
            if(conn != null) {
                String sql = String.format("UPDATE Restaurants SET " +
                                "name = '%s', " +
                                "address = '%s', " +
                                "foodtype = '%s', " +
                                "mealprice = %f" +
                                " WHERE id = %d", restaurant.getM_name(), restaurant.getM_address(),
                        restaurant.getM_foodtype(), restaurant.getM_mealprice(), restaurant.getM_id());
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteRestaurant(int id){
        try (Connection conn = DriverManager.getConnection(m_conn)){
            if(conn != null) {
                String sql = "DELETE FROM Restaurants WHERE id = " + id;
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
