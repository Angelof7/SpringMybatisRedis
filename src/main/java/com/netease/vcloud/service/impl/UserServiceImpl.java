package com.netease.vcloud.service.impl;
/**
* @author hzgaochao
* @version 创建时间：Sep 6, 2015
* 类说明
*/

import java.util.UUID;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.netease.vcloud.net.RestClient;
import com.netease.vcloud.service.ServiceException;
import com.netease.vcloud.service.UserService;
import com.netease.vcloud.storage.cache.IRedisCached;
import com.netease.vcloud.storage.dao.domain.AuthToken;
import com.netease.vcloud.storage.dao.domain.User;
import com.netease.vcloud.storage.dao.mapper.TokenMapper;
import com.netease.vcloud.storage.dao.mapper.UserMapper;
import com.netease.vcloud.utils.Constants;
import com.netease.vcloud.utils.EmailAuthentication;
import com.netease.vcloud.utils.MD5Utils;
import com.netease.vcloud.utils.RandomPassword;
import com.netease.vcloud.utils.SerializeUtil;

@Service("userServiceBean")
public class UserServiceImpl implements UserService {
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired
	UserMapper userMapper;

	@Autowired
	TokenMapper tokenMapper;

	@Autowired
	RestClient restClient;

	@Resource(name = "redisCachedImpl")
	private IRedisCached redisCached;

	@Override
	public User save(User o) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User update(User o) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(String id) throws ServiceException {
		// TODO Auto-generated method stub

	}

	/*
	 * 调用云计算接口，申请云计算帐号
	 * 
	 * @see
	 * com.netease.vcloud.service.UserService#register(com.netease.vcloud.model.
	 * User)
	 */
	@Transactional
	@Override
	public User register(User user) {
		// 调用云计算接口获取tenantId
		String nce_password = RandomPassword.genRandomNum(15);
		// String tenantId = restClient.register(user.getName(), nce_password,
		// user.getEmail());
		String tenantId = UUID.randomUUID().toString();
		logger.info(">>[Get tenantId]: " + tenantId + " from cloud for User: " + user.getEmail());
		// 持久化UserInfo到DB
		user.setUid(tenantId);
		user.setCreatetime(System.currentTimeMillis() / 1000);
		user.setNce_password(nce_password);
		userMapper.addUser(user);
		// 持久化Token到DB
		String token = MD5Utils.encoderByMd5With32Bit(user.getUid() + user.getCreatetime());
		AuthToken authToken = new AuthToken();
		authToken.setUid(tenantId);
		authToken.setToken(token);
		tokenMapper.addToken(authToken);
		// 发送邮件
		try {
			EmailAuthentication.sendAuthMail(user.getEmail(), token);
		} catch (MessagingException e) {
			logger.error(">> Send AuthMail to" + user.getEmail() + "failed!");
			e.printStackTrace();
		}
		return user;
	}

	/*
	 * 激活用户帐号
	 * 
	 * @see
	 * com.netease.vcloud.service.UserService#activate(com.netease.vcloud.model.
	 * User)
	 */
	@Override
	public AuthToken activate(String token) {
		// 验证token是否有效
		AuthToken authToken = tokenMapper.selectUserByToken(token);
		if (authToken == null) {
			logger.info("The unauthed token " + token + "does not exist!");
			return null;
		}
		// 调用云计算接口获取AccessKey和Secretey
		// CloudCredentials credit = restClient.credentials(authToken.getUid());
		// if (credit != null) {
		// String accessKey = credit.getResult().getAccessKey();
		// String secretKey = credit.getResult().getSecretKey();
		// logger.info(">>[Get accessKey and secretKey]: " + accessKey + ";" + "
		// from cloud for User: "
		// + authToken.getUid());
		// // 更新User表激活用户
		// userMapper.activeUser(authToken.getUid(), accessKey, secretKey);
		// logger.info("[User Activated] >> " + authToken.getUid());
		// return authToken;
		// } else {
		// logger.error("获取Credentials失败" + ">>UID:" + authToken.getUid());
		// return null;
		// }
		// 通过UUID来生成32位的accessKey和secretKey
		String accessKey = UUID.randomUUID().toString().replaceAll("-", "");
		String secretKey = UUID.randomUUID().toString().replaceAll("-", "");
		logger.info(">>[Get accessKey and secretKey]: " + accessKey + ";" + " by UUID for User: " + authToken.getUid());
		// 更新User表激活用户
		userMapper.activeUser(authToken.getUid(), accessKey, secretKey);
		logger.info("[User Activated] >> " + authToken.getUid());
		return authToken;

	}

	/*
	 * 重置用户密码
	 * 
	 * @see com.netease.vcloud.service.UserService#updatePwd(java.lang.String)
	 */
	@Override
	public void updatePwd(String email, String uid, String password) {
		// 删除相应user缓存
		try {
			redisCached.deleteCached(SerializeUtil.serialize(Constants.REDIS_USERINFO + email));
		} catch (Exception e) {
			logger.error("delete the redis cache failed!>>email:" + email);
			e.printStackTrace();
		}
		userMapper.upPwd(uid, password);
	}

	/*
	 * 根据Email判断用户是否存在
	 * 
	 * @see com.netease.vcloud.service.UserService#userExist(java.lang.String)
	 */
	@Override
	public User findUserByEmail(String email) {
		Object userObj = null;
		// 先去缓存查
		try {
			userObj = redisCached.getCached(SerializeUtil.serialize(Constants.REDIS_USERINFO + email));
		} catch (Exception e) {
			logger.error("operate redis failed!");
			e.printStackTrace();
		}
		if (userObj != null) {
			return (User) userObj;
		} else { // 去数据库查
			User user = userMapper.selectUserByEmail(email);
			if (user != null) {
				// 存redis
				try {
					redisCached.updateCached(SerializeUtil.serialize(Constants.REDIS_USERINFO + user.getEmail()),
							SerializeUtil.serialize(user), null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return user;
		}
	}

}
