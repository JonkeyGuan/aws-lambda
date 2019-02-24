package com.demo;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class UtilsTest {

	@Test
	public void populateIpMessageExpected() {
		PageElements ipInfo = new PageElements("192.168.80.100", "192.168.90.100", "443", "dummy url");
		String ipMessage = Utils.populateIpMessage(ipInfo);
		Assert.assertTrue("Your IP: 192.168.80.100  Remote IP: 192.168.90.100:443".equals(ipMessage));
	}

	@Test
	public void populateHtmlOutputExpected() {
		PageElements ipInfo = new PageElements("192.168.80.100", "192.168.90.100", "443", "http://www.baidu.com/a/b/c");
		String html = Utils.populateHtmlOutput(ipInfo);
		assertThat(html, containsString("Your IP: 192.168.80.100  Remote IP: 192.168.90.100:443"));
		assertThat(html, containsString("http://www.baidu.com/a/b/c"));
	}
	
}
