package com.netease.vcloud.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

/**
 * @author hzgaochao
 * @version 创建时间：Sep 9, 2015
 */
public class EmailAuthentication {
	private static Logger logger = Logger.getLogger(EmailAuthentication.class);

	private static String adminEmail; // 我们的邮箱
	private static String adminPassword;// 我们的邮箱密码
	private static String url;// 用户访问我们网站的网址
	private static Properties props;

	static {
		Properties prop = new Properties();
		InputStream in = EmailAuthentication.class.getClassLoader().getResourceAsStream("conf.properties");
		try {
			prop.load(in);
			adminEmail = prop.getProperty("mail.admin.address");
			adminPassword = prop.getProperty("mail.admin.password");
			url = prop.getProperty("vcloud.validation.url");

			props = new Properties();
			props.setProperty("mail.smtp.host", prop.getProperty("mail.smtp.host"));
			props.setProperty("mail.smtp.auth", prop.getProperty("mail.smtp.auth"));
			props.setProperty("mail.port", prop.getProperty("mail.port"));
			props.put("mail.smtp.starttls.enable", "true");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 向用户注册邮箱发送激活链接
	 */
	public static void sendAuthMail(String email, String token) throws MessagingException {
		String auth_url = url + "?verify=" + token;

		logger.info("[Begin SendAuthMail] >> " + email + " with auth_url: " + auth_url);

		Authenticator authenticator = new MyAuthenticator(adminEmail, adminPassword);
		javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, authenticator);
		session.setDebug(true);
		Address from = new InternetAddress(adminEmail);
		Address to = new InternetAddress(email);
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(from);
		msg.setSubject("网易视频云用户注册验证");
		msg.setSentDate(new Date());
		msg.setContent("<h3>欢迎注册网易视频云</h3><p>请点击下面的链接激活您的帐号</p><a href=\"" + auth_url + "\" target=\"_blank\">"
				+ auth_url + "</a><br/> 如果以上链接无法点击，请将它复制到你的浏览器地址栏中进入访问，该链接24小时内有效", "text/html;charset=utf-8");
		msg.setRecipient(RecipientType.TO, to);
		Transport.send(msg);

		logger.info("[SendAuthMail Succeed]");
	}

	/*
	 * 向用户密码重置验证码
	 */
	public static void sendCaptchaMail(String email, String captcha) throws MessagingException {

		logger.info("[Begin SendCaptchaMail] >> " + email + " with captcha: " + captcha);

		Authenticator authenticator = new MyAuthenticator(adminEmail, adminPassword);
		javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, authenticator);
		session.setDebug(true);
		Address from = new InternetAddress(adminEmail);
		Address to = new InternetAddress(email);
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(from);
		msg.setSubject("网易视频云用户密码重置验证");
		msg.setSentDate(new Date());
		msg.setContent("<h3>欢迎注册网易视频云</h3><p>这是您的密码重置验证码:" + captcha + "</p>", "text/html;charset=utf-8");
		msg.setRecipient(RecipientType.TO, to);
		Transport.send(msg);

		logger.info("[SendCaptchaMail Succeed]");
	}

}
