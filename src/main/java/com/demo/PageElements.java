package com.demo;

public class PageElements {

	private String clientIp;
	private String remoteIp;
	private String port;
	private String imageUrl;

	public PageElements() {
		super();
	}

	public PageElements(String clientIp, String remoteIp, String port, String imageUrl) {
		super();
		this.clientIp = clientIp;
		this.remoteIp = remoteIp;
		this.port = port;
		this.imageUrl = imageUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

}
