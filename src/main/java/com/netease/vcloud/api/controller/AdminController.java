package com.netease.vcloud.api.controller;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.JsonObject;
import com.netease.vcloud.api.util.AdminUtil;
import com.netease.vcloud.common.AdminCommon;
import com.netease.vcloud.param.AddChannelParam;
import com.netease.vcloud.param.ContentParam;
import com.netease.vcloud.service.impl.AdminService;

/**
 * 管理后台对外接口类
 *
 * @author sf
 *
 */
@Controller("adminController")
public class AdminController {

	public static final Logger logger = Logger.getLogger(AdminController.class);

	@Autowired
	private AdminService as;

	@PostConstruct
	public void init() {
		try {
			as.init();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 创建频道 (POST模式)
	 */
	@RequestMapping(value = "/addchannel", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> addChannel(@RequestParam(required = true) String uid,
			@RequestParam(required = true) String sid, @RequestParam(required = true) String name,
			@RequestParam(required = true) Long cid, @RequestParam(required = true) Long pid) {
		try {
			// TODO: auth 授权逻辑
			logger.info("addchannel?uid=" + uid + "&sid=" + sid + "&name=" + name + "&cid=" + cid + "&pid=" + pid);

			JsonObject jo = new JsonObject();
			as.addChannel(uid, name, cid, pid, jo);
			return AdminUtil.genSuccessResponse(jo, 200);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return AdminUtil.gen400ErrorResponse(AdminCommon.ICode.CODE_ADD_FAILED);
	}

	@RequestMapping(value = "/live/admin/channel/add", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> addChannel2(HttpServletRequest request) {
		try {
			ContentParam cp = getContentMeta(request, false);
			byte[] content = cp.getContent();
			AddChannelParam param = AddChannelParam.parse(new String(content));
			String uid = param.getUid();
			String sid = param.getSid();
			String name = param.getName();
			long cid = param.getCid();
			long pid = param.getPid();

			// TODO: auth 授权逻辑
			logger.info("addchannel?uid=" + uid + "&sid=" + sid + "&name=" + name + "&cid=" + cid + "&pid=" + pid);

			JsonObject jo = new JsonObject();
			as.addChannel(uid, name, cid, pid, jo);
			return AdminUtil.genSuccessResponse(jo, 200);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return AdminUtil.gen400ErrorResponse(AdminCommon.ICode.CODE_ADD_FAILED);
	}

	private ContentParam getContentMeta(HttpServletRequest request, boolean withoutContent) throws Exception {
		return ControllerUtil.getContentMeta(request, withoutContent);
	}

}
