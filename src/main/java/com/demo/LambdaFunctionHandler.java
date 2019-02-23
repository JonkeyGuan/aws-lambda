package com.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import sun.net.spi.nameservice.dns.DNSNameService;

@SuppressWarnings("restriction")
public class LambdaFunctionHandler implements RequestStreamHandler {

	private final static String HEADERS = "headers";
	private final static String X_FORWARD_FOR = "X-Forwarded-For";
	private final static String HOST = "Host";
	private final static String X_FORWARD_PORT = "X-Forwarded-Port";
	private final static String MISSING_HEADER = "missing header";

	JSONParser parser = new JSONParser();

	@SuppressWarnings("unchecked")
	@Override
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
		LambdaLogger logger = context.getLogger();

		logger.log("Loading Java Lambda handler of ProxyWithStream");

		String responseCode = "200";
		String errorMessage = "";
		IpInfo ipInfo = null;
		try {
			ipInfo = extractIpInfo(inputStream);
		} catch (Exception e) {
			errorMessage = e.getMessage();
			logger.log(e.getMessage());
			responseCode = "400";
		}

		JSONObject responseBody = new JSONObject();
		if (!errorMessage.isEmpty()) {
			responseBody.put("error", errorMessage);
		} else {
			responseBody.put("message", Utils.populateResponse(ipInfo));
		}

		JSONObject responseJson = new JSONObject();
		JSONObject headerJson = new JSONObject();
		headerJson.put("Access-Control-Allow-Origin", "*");
		responseJson.put("isBase64Encoded", false);
		responseJson.put("statusCode", responseCode);
		responseJson.put("headers", headerJson);
		responseJson.put("body", responseBody.toString());

		OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
		writer.write(responseJson.toJSONString());
		writer.close();
	}

	private IpInfo extractIpInfo(InputStream inputStream) throws Exception {
		IpInfo result = new IpInfo();

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		JSONObject inputInfo = null;

		try {
			inputInfo = (JSONObject) parser.parse(reader);
		} catch (Exception e) {
			throw e;
		}

		if (inputInfo.get(HEADERS) == null) {
			throw new Exception(MISSING_HEADER + "s");
		}

		JSONObject headers = (JSONObject) inputInfo.get(HEADERS);
		if (headers.containsKey(X_FORWARD_FOR)) {
			result.setClientIp((String) headers.get(X_FORWARD_FOR));
		} else {
			throw new Exception(MISSING_HEADER + ": " + X_FORWARD_FOR);
		}

		if (headers.containsKey(HOST)) {
			String host = (String) headers.get(HOST);
			InetAddress[] ipAddress = new DNSNameService().lookupAllHostAddr(host);
			result.setRemoteIp(ipAddress[0].getHostAddress());
		} else {
			throw new Exception(MISSING_HEADER + ": " + HOST);
		}

		if (headers.containsKey(X_FORWARD_PORT)) {
			result.setPort((String) headers.get(X_FORWARD_PORT));
		} else {
			throw new Exception(MISSING_HEADER + ": " + X_FORWARD_PORT);
		}

		return result;
	}
}
