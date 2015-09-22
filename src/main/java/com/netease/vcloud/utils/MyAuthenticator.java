package com.netease.vcloud.utils;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author hzgaochao
 * @version 创建时间：Sep 9, 2015 邮箱认证
 */
public class MyAuthenticator extends Authenticator {
	private String userName;
	private String password;

	public MyAuthenticator(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}
}
