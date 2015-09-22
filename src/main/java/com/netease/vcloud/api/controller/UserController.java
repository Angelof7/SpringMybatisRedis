package com.netease.vcloud.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.vcloud.model.NewUser;
import com.netease.vcloud.model.UpPwd;
import com.netease.vcloud.service.UserService;
import com.netease.vcloud.storage.cache.IRedisCached;
import com.netease.vcloud.storage.dao.domain.AuthToken;
import com.netease.vcloud.storage.dao.domain.User;
import com.netease.vcloud.utils.Constants;
import com.netease.vcloud.utils.EmailAuthentication;
import com.netease.vcloud.utils.RandomPassword;
import com.netease.vcloud.utils.SerializeUtil;

/**
 * @author hzgaochao
 * @version 创建时间：Sep 2, 2015
 */

@Controller
@RequestMapping("/usr")
public class UserController {
	private static Logger logger = Logger.getLogger(UserController.class);

	@Resource(name = "userServiceBean")
	private UserService userService;

	@Resource(name = "redisCachedImpl")
	private IRedisCached redisCached;

	/*
	 * 用户注册
	 */
	@RequestMapping(value = "/reg/{captcha_id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> register(@RequestBody NewUser newUser, @PathVariable String captcha_id) {
		logger.info(">>[Received Registration Message]: " + newUser);

		Map<String, Object> map = new HashMap<String, Object>();
		String captcha = newUser.getCaptcha();
		// 从redis获取相应验证码
		String redis_captcha = null;
		try {
			redis_captcha = (String) redisCached
					.getCached(SerializeUtil.serialize(Constants.LOGIN_CAPTCHA + captcha_id));
		} catch (Exception e) {
			logger.error("get the captcha from redis failed!>>id:" + captcha_id);
			e.printStackTrace();
		}
		logger.info("ID: " + Constants.LOGIN_CAPTCHA + captcha_id + " redis_captcha: " + redis_captcha);
		if (captcha == null || !captcha.equalsIgnoreCase(redis_captcha)) {
			logger.info("验证码错误");
			map.put("code", 4002);
			return map;
		}

		if (!newUser.valid()) {
			logger.info("注册信息不完整");
			map.put("code", 4003);
			return map;
		}

		if (userService.findUserByEmail(newUser.getEmail()) != null) {
			logger.info("用户已存在");
			map.put("code", 4004);
			return map;
		}

		User user = userService.register(newUser);
		if (user != null) {
			map.put("code", 200);
			Map<String, Object> ret = new HashMap<String, Object>();
			ret.put("ctime", user.getCreatetime());
			ret.put("uid", user.getUid());
			map.put("ret", ret);
			return map;
		} else {
			logger.info("注册失败");
			map.put("code", 5001);
			return map;
		}
	}

	/*
	 * 邮件激活
	 */
	@RequestMapping(value = "/mailreg/{token}", method = RequestMethod.GET)
	// @ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object> mailActivate(@PathVariable String token) {
		logger.info(">>[Received MailActivate Message]: Token>>" + token);
		Map<String, Object> map = new HashMap<String, Object>();

		AuthToken authToken = userService.activate(token);
		if (authToken == null) {
			logger.info("激活码不存在");
			map.put("code", 4005);
			return map;
		} else {
			map.put("code", 200);
			return map;
		}
	}

	/*
	 * 用户登录
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
		logger.info(">>[User Login]: " + user);
		Map<String, Object> map = new HashMap<String, Object>();

		if (user.getEmail() == null || user.getPassword() == null) {
			logger.info("登录信息不完整");
			map.put("code", 4003);
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.FORBIDDEN);
		}

		User usr = userService.findUserByEmail(user.getEmail());

		if (usr == null) {
			logger.info("用户不存在");
			map.put("code", 4007);
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.FORBIDDEN);
		}
		if (usr.getStats() == 0) {
			logger.info("用户未激活");
			map.put("code", 4006);
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.FORBIDDEN);
		}
		if (!usr.getPassword().equals(user.getPassword())) {
			logger.info("密码错误");
			map.put("code", 4008);
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.FORBIDDEN);
		} else {
			// 构造随机Session串sid并存入redis
			String sid = RandomPassword.genRandomNum(5) + System.currentTimeMillis();
			try {
				redisCached.updateCached(SerializeUtil.serialize(Constants.SESSION_LOGIN_NAME + sid),
						SerializeUtil.serialize(usr.getUid()), 1800L);
			} catch (Exception e) {
				logger.error("insert session to redis failed!" + usr.getEmail());
				e.printStackTrace();
			}

			map.put("code", 200);
			Map<String, Object> ret = new HashMap<String, Object>();
			ret.put("uid", usr.getUid());
			ret.put("sid", sid);
			map.put("ret", ret);
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}
	}

	/*
	 * 用户注销登录
	 */
	@RequestMapping(value = "/logout/{sid}", method = RequestMethod.DELETE)
	public ResponseEntity<Map<String, Object>> logout(@PathVariable String sid) {
		logger.info(">>[User Logout]: " + sid);
		Map<String, Object> map = new HashMap<String, Object>();

		if (sid == null) {
			logger.info("Seesion ID为空");
			map.put("code", 4003);
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.FORBIDDEN);
		}
		// 检查用户是否已登录
		Object obj = null;
		try {
			obj = redisCached.getCached(SerializeUtil.serialize(Constants.SESSION_LOGIN_NAME + sid));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (obj == null) {
			logger.info("用户未登录");
			map.put("code", 4009);
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.FORBIDDEN);
		}
		// 将用户的session从redis删除
		try {
			redisCached.deleteCached(SerializeUtil.serialize(Constants.SESSION_LOGIN_NAME + sid));
		} catch (Exception e) {
			logger.error("delete session from redis failed! " + sid);
			e.printStackTrace();
		}
		logger.info("用户成功退出, SID=>" + sid);
		map.put("code", 200);
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}

	/*
	 * 发起密码重置
	 */
	@RequestMapping(value = "/mailvcode", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> uppwdmail(@RequestBody User user) {
		logger.info(">>[Launch Password Update]: " + user);
		Map<String, Object> map = new HashMap<String, Object>();

		String email = user.getEmail();
		if (email == null) {
			logger.info("邮箱为空");
			map.put("code", 4003);
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.FORBIDDEN);
		}

		User usr = userService.findUserByEmail(email);
		if (usr == null) {
			logger.info("邮箱不存在");
			map.put("code", 4007);
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.FORBIDDEN);
		}

		// 生成四位验证码
		String captcha = RandomPassword.genRandomNum(4);

		// 将验证码存入redis缓存
		try {
			redisCached.updateCached(SerializeUtil.serialize(Constants.UPPWD_CAPTCHA + email),
					SerializeUtil.serialize(captcha), 1800L);
		} catch (Exception e1) {
			logger.error("Insert captcha to redis failed!");
			e1.printStackTrace();
		}
		// 发送邮件
		try {
			EmailAuthentication.sendCaptchaMail(email, captcha);
		} catch (MessagingException e) {
			logger.error(">> Send Captcha to" + user.getEmail() + "failed!");
			e.printStackTrace();
		}

		logger.info("密码重置验证码发送成功");
		map.put("code", 200);
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}

	/*
	 * 重置密码
	 */
	@RequestMapping(value = "/pwd", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> uppwd(@RequestBody UpPwd uppwd) {
		logger.info(">>[User Password Update]: " + uppwd);
		Map<String, Object> map = new HashMap<String, Object>();

		User user = userService.findUserByEmail(uppwd.getEmail());
		if (user == null) {
			logger.info("用户不存在");
			map.put("code", 4007);
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.NOT_FOUND);
		}

		// 根据email获取校验码
		String captcha = "";
		try {
			captcha = (String) redisCached
					.getCached(SerializeUtil.serialize(Constants.UPPWD_CAPTCHA + uppwd.getEmail()));
		} catch (Exception e1) {
			logger.error("Insert captcha to redis failed!");
			e1.printStackTrace();
		}
		if (captcha.equals(uppwd.getCaptcha())) {
			// 更新密码
			userService.updatePwd(user.getEmail(), user.getUid(), uppwd.getPwd());
			// 删除redis相应校验码
			try {
				redisCached.deleteCached(SerializeUtil.serialize(uppwd.getEmail()));
			} catch (Exception e) {
				logger.error("Delete the captcha in the redis failed!");
				e.printStackTrace();
			}
			map.put("code", 200);
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		} else {
			logger.info("验证码错误");
			map.put("code", 4002);
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.NOT_FOUND);
		}
	}

}