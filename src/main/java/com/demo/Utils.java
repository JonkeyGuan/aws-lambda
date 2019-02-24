package com.demo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Utils {

	public static String populateIpMessage(PageElements pageElements) {
		return "Your IP: " + pageElements.getClientIp() + "  Remote IP: " + pageElements.getRemoteIp() + ":" + pageElements.getPort();
	}
	
	public static String populateHtmlOutput(PageElements pageElements)  {
		String result = "";
		
		String ipMessage = Utils.populateIpMessage(pageElements);
		
		InputStream is = Utils.class.getClassLoader().getResourceAsStream("index.html");
	    if (is != null) {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	        result = reader.lines().collect(Collectors.joining(System.lineSeparator()));
	        result = result.replaceFirst("@@message@@", ipMessage);
	        result = result.replaceFirst("@@image@@", pageElements.getImageUrl());
	    }

		return result;
	}
}
