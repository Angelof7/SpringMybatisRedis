package com.netease.vcloud.storage.dao.mapper;

import com.netease.vcloud.storage.dao.domain.User;

/**
 * @author hzgaochao
 * @version 创建时间：Sep 8, 2015 类说明
 */
public interface UserMapper {
	// 根据UID查找用户
	public User selectUserByID(String uid);

	// 根据邮箱查找用户
	public User selectUserByEmail(String email);

	// 添加新用户
	public void addUser(User user);

	// 激活新用户
	public void activeUser(String uid, String accessKey, String secretKey);

	// 重置密码
	public void upPwd(String uid, String password);
}
