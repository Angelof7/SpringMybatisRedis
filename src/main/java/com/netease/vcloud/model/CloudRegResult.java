package com.netease.vcloud.model;

/**
 * @author hzgaochao
 * @version 创建时间：Sep 8, 2015
 * 
 */
public class CloudRegResult {
	private String tenantId;

	private String userName;

	private String password;

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
