package com.example.demo;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class DemoApplicationTests {

	private static final String m_conn = "jdbc:sqlite:C:/SQLite/restaurant.db";

	public void insertRestaurantData(){
		try (Connection conn = DriverManager.getConnection(m_conn)){
			if(conn != null) {
				String sql = String.format("INSERT INTO Restaurants (id, name, address, foodtype, mealprice)" +
								" VALUES (1, '%s', '%s', '%s', %f);" +
						" INSERT INTO Restaurants (id, name, address, foodtype, mealprice)" +
						" VALUES (2, '%s', '%s', '%s', %f);" +
						" INSERT INTO Restaurants (id, name, address, foodtype, mealprice)" +
						" VALUES (3, '%s', '%s', '%s', %f);",
						TestData.row_1_name, TestData.row_1_address, TestData.row_1_foodtype, TestData.row_1_mealprice,
						TestData.row_2_name, TestData.row_2_address, TestData.row_2_foodtype, TestData.row_2_mealprice,
						TestData.row_3_name, TestData.row_3_address, TestData.row_3_foodtype, TestData.row_3_mealprice);
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(sql);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	public void cleanData(){
		try (Connection conn = DriverManager.getConnection(m_conn)){
			if(conn != null) {
				String sql = String.format("DELETE FROM Restaurants;");
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(sql);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void test_get_all_restaurants() {

		cleanData();
		insertRestaurantData();

		ClientConfig clientConfig = new DefaultClientConfig();
		Client client = Client.create(clientConfig);
		WebResource webResource = client.resource(UriBuilder.fromUri("http://localhost:8080/restaurant").build());

		String result =  webResource.path("").path("").accept(String.valueOf(MediaType.APPLICATION_JSON)).get(String.class);
		System.out.println(result);

		Gson gson = new Gson();
		JsonArray ja = JsonParser.parseString(result).getAsJsonArray();
		RestaurantDAO[] restaurants = gson.fromJson(ja, RestaurantDAO[].class);

		assertEquals(3, restaurants.length);
	}

	@Test
	public void test_get_by_id_2() {

		cleanData();
		insertRestaurantData();

		ClientConfig clientConfig = new DefaultClientConfig();
		Client client = Client.create(clientConfig);
		WebResource webResource =
				client.resource(UriBuilder.fromUri("http://localhost:8080/restaurant/2").build());
		String result =  webResource.path("").accept(String.valueOf(MediaType.APPLICATION_JSON)).get(String.class);
		System.out.println(result);

		Gson gson = new Gson();
		JsonObject jo = JsonParser.parseString(result).getAsJsonObject();
		RestaurantDTO restaurant_id2 = gson.fromJson(JsonParser.parseString(result).getAsJsonObject(), RestaurantDTO.class);

		assert restaurant_id2.getM_id() == 2;
		assert restaurant_id2.getM_name().equals(TestData.row_2_name);
		assert restaurant_id2.getM_address().equals(TestData.row_2_address);
		assert restaurant_id2.getM_foodtype().equals(TestData.row_2_foodtype);
		assert restaurant_id2.getM_mealprice() == TestData.row_2_mealprice;
	}

	@Test
	public void test_post() {

		cleanData();
		insertRestaurantData();

		Gson gson = new Gson();
		ClientConfig clientConfig = new DefaultClientConfig();

		Client client = Client.create(clientConfig);
		WebResource webResource =  client.resource(UriBuilder.fromUri("http://localhost:8080/restaurant").build());

		RestaurantDTO restaurant = new RestaurantDTO(4, TestData.post_name, TestData.post_address,
				TestData.post_foodtype, TestData.post_mealprice);

		ClientResponse resp = webResource.path("").accept("application/json").type("application/json").post(ClientResponse.class,
				gson.toJson(restaurant));

		assertEquals(200, resp.getStatus());

// --- not ideal ---

//		webResource = client.resource(UriBuilder.fromUri("http://localhost:8080/restaurant").build());
//		String result =  webResource.path("").path("").accept(String.valueOf(MediaType.APPLICATION_JSON)).get(String.class);
//		System.out.println(result);
//
//		JsonArray ja = JsonParser.parseString(result).getAsJsonArray();
//		RestaurantDAO[] restaurants = gson.fromJson(ja, RestaurantDAO[].class);
//
//		assertEquals(4, restaurants.length);
	}

	@Test
	public void test_put() {

		cleanData();
		insertRestaurantData();

		Gson gson = new Gson();
		ClientConfig clientConfig = new DefaultClientConfig();

		Client client = Client.create(clientConfig);
		WebResource webResource =  client.resource(UriBuilder.fromUri("http://localhost:8080/restaurant/3").build());

		RestaurantDTO restaurant = new RestaurantDTO(3, TestData.row_3_name, TestData.put_address,
				TestData.row_3_foodtype, TestData.row_3_mealprice);

		ClientResponse resp = webResource.accept("application/json").type("application/json").put(ClientResponse.class,
				gson.toJson(restaurant));

		assertEquals(200, resp.getStatus());
	}

	@Test
	public void test_delete_by_id_3() {

		cleanData();
		insertRestaurantData();

		Gson gson = new Gson();
		ClientConfig clientConfig = new DefaultClientConfig();

		Client client = Client.create(clientConfig);
		WebResource webResource =  client.resource(UriBuilder.fromUri("http://localhost:8080/restaurant/3").build());
		webResource.accept("application/json").type("application/json").delete();

		//getAll
		// --- not ideal ---
		webResource = client.resource(UriBuilder.fromUri("http://localhost:8080/restaurant").build());
		String result =  webResource.path("").path("").accept(String.valueOf(MediaType.APPLICATION_JSON)).get(String.class);

		JsonArray ja = JsonParser.parseString(result).getAsJsonArray();
		RestaurantDAO[] restaurants = gson.fromJson(ja, RestaurantDAO[].class);

		assertEquals(2, restaurants.length);

		//getById
		// --- not ideal ---
		webResource = client.resource(UriBuilder.fromUri("http://localhost:8080/restaurant/3").build());
		result =  webResource.path("").accept(String.valueOf(MediaType.APPLICATION_JSON)).get(String.class);
		assert result.equals("") == true;
	}
}
