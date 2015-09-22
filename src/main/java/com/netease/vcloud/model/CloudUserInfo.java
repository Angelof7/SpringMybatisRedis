package com.netease.vcloud.model;

/**
 * @author hzgaochao
 * @version 创建时间：Sep 8, 2015 注册云计算返回的信息
 */
public class CloudUserInfo {
	private CloudRegResult result;

	private String requestId;

	private int code;

	public CloudRegResult getResult() {
		return result;
	}

	public void setResult(CloudRegResult result) {
		this.result = result;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}