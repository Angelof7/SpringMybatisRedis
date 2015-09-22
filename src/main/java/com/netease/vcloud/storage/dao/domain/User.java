package com.netease.vcloud.storage.dao.domain;

import java.io.Serializable;

/**
 * @author hzgaochao
 * @version 创建时间：Sep 6, 2015 类说明
 */
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4044690578272588203L;

	// tenantID
	private String uid;

	private String email;

	private String password;

	private String nce_password;

	private String name;

	private String phone;

	private Long createtime;

	private String token;

	private int stats;

	public int getStats() {
		return stats;
	}

	public void setStats(int stats) {
		this.stats = stats;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "UID: " + this.uid + " Email: " + this.email + " Password: " + this.password + " NCE_Password: "
				+ this.nce_password + " Name: " + this.name + " Phone: " + this.phone + " Createtime: "
				+ this.createtime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getNce_password() {
		return nce_password;
	}

	public void setNce_password(String nce_password) {
		this.nce_password = nce_password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Long createtime) {
		this.createtime = createtime;
	}

}
