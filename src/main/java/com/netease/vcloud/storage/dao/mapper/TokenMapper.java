package com.netease.vcloud.storage.dao.mapper;

import com.netease.vcloud.storage.dao.domain.AuthToken;

/**
* @author hzgaochao
* @version 创建时间：Sep 9, 2015
* 类说明
*/
public interface TokenMapper {
	//验证Token是否存在
	public AuthToken selectUserByToken(String token);
	//插入Token
	public void addToken(AuthToken token);
}
