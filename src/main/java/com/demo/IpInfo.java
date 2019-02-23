package com.demo;

public class IpInfo {

	private String clientIp;
	private String remoteIp;
	private String port;

	public IpInfo() {
		super();
	}

	public IpInfo(String clientIp, String remoteIp, String port) {
		super();
		this.clientIp = clientIp;
		this.remoteIp = remoteIp;
		this.port = port;
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
