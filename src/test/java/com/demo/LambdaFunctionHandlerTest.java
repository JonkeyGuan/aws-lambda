package com.demo;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

public class LambdaFunctionHandlerTest {

	private LambdaFunctionHandler handler;
	private Context context;
	private ByteArrayOutputStream outputStream;

	@Before
	public void setup() {
		handler = new LambdaFunctionHandler();
		context = mock(Context.class);
		when(context.getLogger()).thenReturn(System.out::println);
		outputStream = new ByteArrayOutputStream();
	}

	@Test
	public void handleRequest_succeed() throws Exception {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("succeed.json");
		handler.handleRequest(inputStream, outputStream, context);
		String result = outputStream.toString();
		assertThat(result, containsString("Your IP"));
		assertThat(result, containsString("Remote IP"));
		assertThat(result, containsString("200"));
	}

	@Test
	public void handleRequest_failed_missing_headers() throws Exception {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("missing_headers.json");
		handler.handleRequest(inputStream, outputStream, context);
		String result = outputStream.toString();
		assertThat(result, containsString("400"));
		assertThat(result, containsString("missing headers"));
	}

	@Test
	public void handleRequest_failed_missing_x_forwarded_for() throws Exception {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("missing_x_forwarded_for.json");
		handler.handleRequest(inputStream, outputStream, context);
		String result = outputStream.toString();
		assertThat(result, containsString("400"));
		assertThat(result, containsString("missing header: X-Forwarded-For"));
	}

	@Test
	public void handleRequest_failed_missing_x_forwarded_port() throws Exception {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("missing_x_forwarded_port.json");
		handler.handleRequest(inputStream, outputStream, context);
		String result = outputStream.toString();
		assertThat(result, containsString("400"));
		assertThat(result, containsString("missing header: X-Forwarded-Port"));
	}
	
	@Test
	public void handleRequest_failed_missing_x_image_url() throws Exception {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("missing_x_image_url.json");
		handler.handleRequest(inputStream, outputStream, context);
		String result = outputStream.toString();
		assertThat(result, containsString("400"));
		assertThat(result, containsString("missing header: X-Image-Url"));
	}
}
