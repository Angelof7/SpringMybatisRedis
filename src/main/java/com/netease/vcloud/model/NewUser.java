package com.netease.vcloud.model;

import com.netease.vcloud.storage.dao.domain.User;

/**
 * @author hzgaochao
 * @version 创建时间：Sep 7, 2015 注册用户基本信息
 */
public class NewUser extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String captcha;

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public boolean valid() {
		if (this.getEmail() == null)
			return false;
		if (this.getPassword() == null)
			return false;
		if (this.getName() == null)
			return false;
		if (this.getPhone() == null)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return super.toString() + " Captcha: " + captcha;
	}

}
