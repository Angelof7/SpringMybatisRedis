package com.netease.vcloud.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.netease.vcloud.storage.cache.IRedisCached;
import com.netease.vcloud.utils.Constants;

/**
 * @author hzgaochao
 * @version 创建时间：Sep 8, 2015 管理权限登录验证
 */
public class UserLoginInterceptor implements HandlerInterceptor {
	@Resource(name = "redisCachedImpl")
	private IRedisCached redisCached;

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object arg2) throws Exception {
		String uid = req.getParameter("uid");
		Object obj = redisCached.getCached((Constants.SESSION_LOGIN_NAME + ":" + uid).getBytes());
		if (obj == null || "".equals(obj.toString())) {
			// res.sendRedirect(Constants.LOGIN_URL);
			return false;
		}
		return true;
	}

}
