package com.netease.vcloud.model;

/**
 * @author hzgaochao
 * @version 创建时间：Sep 10, 2015 类说明
 */
public class CloudCredResult {
	private String accessKey;

	private String secretKey;

	private String tenantId;

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
