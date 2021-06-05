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
import org.springframework.http.MediaType;

import javax.ws.rs.core.UriBuilder;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class DemoApplicationTests {

	private ArrayList<MyCoupon> get_m_coupons(CouponController coupon) {
		try {
			ArrayList<MyCoupon> m_coupons = null;
			Field privateStringField = null;
			privateStringField = CouponController.class.
					getDeclaredField("m_coupons");
			privateStringField.setAccessible(true);
			m_coupons = (ArrayList<MyCoupon>) privateStringField.get(coupon);

			return m_coupons;
		}
		catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void test_get_all_jersey() {

		CouponController couponController = new CouponController();
		ArrayList<MyCoupon> m_coupons = get_m_coupons(couponController);

		ClientConfig clientConfig = new DefaultClientConfig();
		Client client = Client.create(clientConfig);
		WebResource webResource =
				client.resource(UriBuilder.fromUri("http://localhost:8080/coupon").  build());
		String result =  webResource.path("").accept(String.valueOf(MediaType.APPLICATION_JSON)).get(String.class);
		System.out.println(result);

		Gson gson = new Gson();
		JsonArray ja = JsonParser.parseString(result).getAsJsonArray();
		MyCoupon[] coupons = gson.fromJson(ja, MyCoupon[].class);

		for (int i = 0; i < m_coupons.size(); i++){
			assertEquals(coupons[i].getId(), m_coupons.get(i).getId() );
			assertEquals(coupons[i].getTitle(), m_coupons.get(i).getTitle() );
		}
		assertEquals(coupons.length, m_coupons.size());
	}

	@Test
	public void test_get_by_1_jersey() {

		CouponController couponController = new CouponController();
		ArrayList<MyCoupon> m_coupons = get_m_coupons(couponController);

		ClientConfig clientConfig = new DefaultClientConfig();
		Client client = Client.create(clientConfig);
		WebResource webResource =
				client.resource(UriBuilder.fromUri("http://localhost:8080/coupon/1").  build());
		String result =  webResource.path("").accept(String.valueOf(MediaType.APPLICATION_JSON)).get(String.class);
		System.out.println(result);

		Gson gson = new Gson();
		JsonObject jo = JsonParser.parseString(result).getAsJsonObject();
		MyCoupon coupon = gson.fromJson(jo, MyCoupon.class);

		assertEquals(m_coupons.get(0).getId(), coupon.getId());
		assertEquals(m_coupons.get(0).getTitle(), coupon.getTitle());
	}

	@Test
	public void test_post() {
		ClientConfig clientConfig = new DefaultClientConfig();

		// better to use DTO and gson

		String coupon = "{ \"id\": 5, \"title\":\"new coupon\" }";

		Client client = Client.create(clientConfig);


		WebResource webResource =  client.resource(UriBuilder.fromUri("http://localhost:8080/coupon").build());
		ClientResponse resp = webResource.accept("application/json").type("application/json").post(ClientResponse.class, coupon);

		System.out.println(resp.getStatus());

		if(resp.getStatus() != 200) {
			assert false;
		}
	}

	@Test
	public void test_post_coupon() {

		Gson gson = new Gson();

		ClientConfig clientConfig = new DefaultClientConfig();

		Client client = Client.create(clientConfig);
		WebResource webResource =
				client.resource(UriBuilder.fromUri("http://localhost:8080/coupon").build());

		MyCoupon coupon = new MyCoupon(4, "launch");

		ClientResponse resp =
				webResource.path("").accept("application/json").type("application/json").post(ClientResponse.class, gson.toJson(coupon));

//		if(resp.getStatus() != 201) {
//			assert false;}

		// ---- get for assert

		// not ideal

		webResource = client.resource(UriBuilder.fromUri("http://localhost:8080/coupon/4").build());
		String result =  webResource.path("").accept(String.valueOf(MediaType.APPLICATION_JSON)).get(String.class);

		System.out.println(result);

		MyCoupon coupon_id4 = gson.fromJson(JsonParser.parseString(result).getAsJsonObject(), MyCoupon.class);
		assert coupon_id4.getId() == 4;
		assert coupon_id4.getTitle().equals("launch") == true;
	}

	@Test
	public void test_put_coupon() {
		Gson gson = new Gson();

		ClientConfig clientConfig = new DefaultClientConfig();

		Client client = Client.create(clientConfig);
		WebResource webResource =  client.resource(UriBuilder.fromUri("http://localhost:8080/coupon/2").build());

		MyCoupon coupon = new MyCoupon(2, "Movie VIP stars");

		ClientResponse resp = webResource.accept("application/json").type("application/json").put(ClientResponse.class,
				gson.toJson(coupon));

		if(resp.getStatus() != 200) { assert false;
		}

		// ---- get for assert

		// not ideal

		webResource = client.resource(UriBuilder.fromUri("http://localhost:8080/coupon/2").build());
		String result =  webResource.path("").accept(String.valueOf(MediaType.APPLICATION_JSON)).get(String.class);

		System.out.println(result);

		MyCoupon coupon_id2 = gson.fromJson(JsonParser.parseString(result).getAsJsonObject(), MyCoupon.class);

		assert coupon_id2.getId() == 2;
		assert coupon_id2.getTitle().equals("Movie VIP stars") == true;
	}

	@Test
	public void test_delete_coupon() {
		Gson gson = new Gson();

		ClientConfig clientConfig = new DefaultClientConfig();

		Client client = Client.create(clientConfig);
		WebResource webResource =  client.resource(UriBuilder.fromUri("http://localhost:8080/coupon").build());

		MyCoupon coupon = new MyCoupon(10, "burger");

		ClientResponse resp = webResource.path("").accept("application/json").type("application/json").post(ClientResponse.class,
				gson.toJson(coupon));

//		if(resp.getStatus() != 201) {assert false;}

		webResource =  client.resource(UriBuilder.fromUri("http://localhost:8080/coupon/10").build());
		webResource.accept("application/json").type("application/json").delete();


		// ---- get for assert
		// not ideal

		webResource = client.resource(UriBuilder.fromUri("http://localhost:8080/coupon/10").build());

		String result =  webResource.path("").accept(String.valueOf(MediaType.APPLICATION_JSON)).get(String.class);

		System.out.println(result);

		assert result.equals("") == true;
	}
}
