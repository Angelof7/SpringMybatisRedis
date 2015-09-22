package com.netease.vcloud.utils;

/**
 * @author hzgaochao
 * @version 创建时间：Sep 8, 2015 类说明
 */
public class Constants {
	// redis里存放用户名的key
	public static final String SESSION_LOGIN_NAME = "SESSION_LOGIN_NAME:";
	// redis里存放登录验证码的key
	public static final String LOGIN_CAPTCHA = "LOGIN_CAPTCHA:";
	// redis里存放重置密码的验证码的key
	public static final String UPPWD_CAPTCHA = "UPPWD_CAPTCHA:";
	// 登录URL
	public static final String LOGIN_URL = "/login.htm";
	// 云计算URL
	public static final String CLOUD_REG_URL = "http://http://10.180.11.18:8191/register/user";
	// 云计算获取AccessKey和SecretKey的URL
	public static final String CLOUD_CREDENTIALS_URL = "http://10.180.11.18:8191/auth";
	// Rdis中userinfo的key
	public static final String REDIS_USERINFO = "VCLOUD_USER_INFO:";

}
