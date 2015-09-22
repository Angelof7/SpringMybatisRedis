package com.netease.vcloud.model;

/**
 * @author hzgaochao
 * @version 创建时间：Sep 8, 2015 注册云计算Post用户信息
 */
public class RegUserInfo {
	private String userName;

	private String password;

	private String email;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
