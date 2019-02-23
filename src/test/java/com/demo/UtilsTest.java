package com.demo;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

	@Test
	public void populateResponseExpected() {
		IpInfo ipInfo = new IpInfo("192.168.80.100", "192.168.90.100", "443");
		String message = Utils.populateResponse(ipInfo);
		Assert.assertTrue("Your IP: 192.168.80.100  Remote IP: 192.168.90.100:443".equals(message));
	}

}
