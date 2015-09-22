package com.netease.vcloud.model;

/**
 * @author hzgaochao
 * @version 创建时间：Sep 10, 2015 请求AccessKey和SecretKey返回的Bean
 */
public class CloudCredentials {
	private int code;

	private String requestId;

	public CloudCredResult result;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public CloudCredResult getResult() {
		return result;
	}

	public void setResult(CloudCredResult result) {
		this.result = result;
	}
}
