package com.netease.vcloud.model;

/**
 * @author hzgaochao
 * @version 创建时间：Sep 10, 2015 重置密码bean
 */
public class UpPwd {
	private String email;

	private String captcha;

	private String pwd;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@Override
	public String toString() {
		return this.email + "; " + this.captcha + "; " + this.pwd;
	}
}
