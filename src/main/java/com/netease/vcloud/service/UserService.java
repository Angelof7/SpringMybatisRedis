package com.netease.vcloud.service;

import com.netease.vcloud.storage.dao.domain.AuthToken;
import com.netease.vcloud.storage.dao.domain.User;

/**
 * @author hzgaochao
 * @version 创建时间：Sep 6, 2015 类说明
 */
public interface UserService extends BaseService<User> {
	public User register(User user);

	public AuthToken activate(String token);

	public void updatePwd(String email, String uid, String password);

	public User findUserByEmail(String email);

}
