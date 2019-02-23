package com.demo;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import com.jayway.restassured.http.ContentType;

public class OnlineTest {

	private static String CLOUD_FRONT_URL = "https://d2c69zm52r5kbu.cloudfront.net/default/lambda";
	private static String API_GATEWAY_URL = "https://k5pqptkko9.execute-api.ap-southeast-1.amazonaws.com/default/lambda";
	private static String API_KEY_HEADER = "x-api-key";
	private static String API_KEY_VALUE = "usKx6jPHJH8LcnW7RYXXCMNKXzHUdVgarIGE906b";
	
    @Test
    public void cloudFrontTest() {
        given()
            .header(API_KEY_HEADER, API_KEY_VALUE).
        when()
            .post(CLOUD_FRONT_URL).
        then()
            .contentType(ContentType.JSON)
            .and()
            .statusCode(200)
            .and()
            .body(containsString("Your IP"))
            .and()
            .body(containsString("Remote IP"));
    }
	
    @Test
    public void apiGatewayTest() {
        given()
            .header(API_KEY_HEADER, API_KEY_VALUE).
        when()
            .post(API_GATEWAY_URL).
        then()
            .contentType(ContentType.JSON)
            .and()
            .statusCode(200)
            .and()
            .body(containsString("Your IP"))
            .and()
            .body(containsString("Remote IP"));
    }
}
