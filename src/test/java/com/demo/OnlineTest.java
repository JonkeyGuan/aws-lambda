package com.demo;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import com.jayway.restassured.http.ContentType;

public class OnlineTest {

	private static String CLOUD_FRONT_URL = "https://dpbv8kavqe9xj.cloudfront.net/default/myLambda";
	private static String API_GATEWAY_URL = "https://lq8tbapsxi.execute-api.ap-southeast-1.amazonaws.com/default/myLambda";
	private static String API_KEY_HEADER = "x-api-key";
	private static String API_KEY_VALUE = "xHDDKV6rle4mqHJ84uv2A2EdvMhN3KN5KxVQYo7a";
	
    @Test
    public void cloudFrontTest() {
        given()
            .header(API_KEY_HEADER, API_KEY_VALUE).
        when()
            .get(CLOUD_FRONT_URL).
        then()
            .contentType("text/html")
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
            .header(API_KEY_HEADER, API_KEY_VALUE)
            .header("X-Forwarded-For", "172.96.210.180, 216.137.44.18")
            .header("X-Forwarded-Port", "443")
            .header("X-Image-Url", "https://d23p6mqh85pir4.cloudfront.net/logo.png").
        when()
            .get(API_GATEWAY_URL).
        then()
            .contentType("text/html")
            .and()
            .statusCode(200)
            .and()
            .body(containsString("Your IP"))
            .and()
            .body(containsString("Remote IP"));
    }
}
