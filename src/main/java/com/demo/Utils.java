package com.demo;

public class Utils {

	public static String populateResponse(IpInfo ipInfo) {
		return "Your IP: " + ipInfo.getClientIp() + "  Remote IP: " + ipInfo.getRemoteIp() + ":" + ipInfo.getPort();
	}
}
