package com.dotcms.publisher.business;

import java.io.Serializable;

public class EndpointDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	private int status;
	private String info;
	private String stackTrace;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getStackTrace() {
		return stackTrace;
	}
	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}
}
