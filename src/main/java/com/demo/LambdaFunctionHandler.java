package com.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

public class LambdaFunctionHandler implements RequestStreamHandler {

	private final static String HEADERS = "headers";
	private final static String X_FORWARD_FOR = "X-Forwarded-For";
	private final static String X_FORWARD_PORT = "X-Forwarded-Port";
	private final static String X_IMAGE_URL = "X-Image-Url";
	private final static String MISSING_HEADER = "missing header";

	JSONParser parser = new JSONParser();

	@SuppressWarnings("unchecked")
	@Override
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
		LambdaLogger logger = context.getLogger();

		logger.log("Loading Java Lambda handler of ProxyWithStream");

		String responseCode = "200";
		String errorMessage = "";
		PageElements pageElements = null;
		try {
			pageElements = generatePageElements(inputStream, logger);
		} catch (Exception e) {
			errorMessage = e.getMessage();
			logger.log(e.getMessage());
			responseCode = "400";
		}

		String body = "";
		if (!errorMessage.isEmpty()) {
			body = errorMessage;
		} else {
			body = Utils.populateHtmlOutput(pageElements);
		}

		JSONObject responseJson = new JSONObject();
		JSONObject headerJson = new JSONObject();
		headerJson.put("Access-Control-Allow-Origin", "*");
		headerJson.put("Content-Type", "text/html");
		responseJson.put("isBase64Encoded", false);
		responseJson.put("statusCode", responseCode);
		responseJson.put("headers", headerJson);
		responseJson.put("body", body);

		outputStream.write(responseJson.toJSONString().getBytes());
	}

	private PageElements generatePageElements(InputStream inputStream, LambdaLogger logger) throws Exception {
		PageElements result = new PageElements();

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		JSONObject inputInfo = null;

		try {
			inputInfo = (JSONObject) parser.parse(reader);
			logger.log("input: " + inputInfo);
		} catch (Exception e) {
			throw e;
		}

		if (inputInfo.get(HEADERS) == null) {
			throw new Exception(MISSING_HEADER + "s");
		}

		JSONObject headers = (JSONObject) inputInfo.get(HEADERS);
		if (headers.containsKey(X_FORWARD_FOR)) {
			String ips = (String) headers.get(X_FORWARD_FOR);
			result.setClientIp(ips.split(",")[0]);
		} else {
			throw new Exception(MISSING_HEADER + ": " + X_FORWARD_FOR);
		}

		if (headers.containsKey(X_FORWARD_PORT)) {
			result.setPort((String) headers.get(X_FORWARD_PORT));
		} else {
			throw new Exception(MISSING_HEADER + ": " + X_FORWARD_PORT);
		}

		if (headers.containsKey(X_IMAGE_URL)) {
			result.setImageUrl((String) headers.get(X_IMAGE_URL));
		} else {
			throw new Exception(MISSING_HEADER + ": " + X_IMAGE_URL);
		}
		
		InetAddress ip = InetAddress.getByName(new URL(result.getImageUrl()).getHost()); 
		result.setRemoteIp(ip.getHostAddress());
		
		return result;
	}
}
