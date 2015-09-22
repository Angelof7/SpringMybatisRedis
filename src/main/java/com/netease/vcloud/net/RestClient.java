package com.netease.vcloud.net;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.netease.vcloud.model.CloudUserInfo;
import com.netease.vcloud.model.CloudCredentials;
import com.netease.vcloud.model.RegUserInfo;
import com.netease.vcloud.utils.Constants;

/**
 * @author hzgaochao
 * @version 创建时间：Sep 8, 2015 RestTemplate调用REST资源
 */
@Component
public class RestClient {
	private static Logger logger = Logger.getLogger(RestClient.class);

	@Autowired
	private RestTemplate restTemplate;

	/*
	 * 向云计算注册用户并获取tenantId
	 */
	public String register(String userName, String password, String email) throws RestClientException {
		RegUserInfo regUser = new RegUserInfo();
		regUser.setUserName(userName);
		regUser.setPassword(password);
		regUser.setEmail(email);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<RegUserInfo> request = new HttpEntity<RegUserInfo>(regUser, headers);

		logger.info(new Gson().toJson(regUser));
		CloudUserInfo result = restTemplate.postForObject(Constants.CLOUD_REG_URL, request, CloudUserInfo.class);
		if (result.getCode() == 200) {
			logger.info(new Gson().toJson(result));
			return result.getResult().getTenantId();
		}
		logger.info("获取tenantId失败" + ">>email: " + email);
		return null;
	}

	/*
	 * 向云计算注册用户并获取tenantId
	 */
	public CloudCredentials credentials(String tenantId) throws RestClientException {
		CloudCredentials result = restTemplate.getForObject(Constants.CLOUD_CREDENTIALS_URL + "/" + tenantId,
				CloudCredentials.class);
		if (result.getCode() == 200) {
			logger.info(new Gson().toJson(result));
			return result;
		}
		logger.error("获取Credentials失败" + ">>tenantId: " + tenantId);
		return null;
	}
}
