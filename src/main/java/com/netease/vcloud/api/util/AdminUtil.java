package com.netease.vcloud.api.util;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class AdminUtil {

	private static final Logger logger = Logger.getLogger(AdminUtil.class);

	public static final String TIMELINE_CALLBACK_STATUS_CODE_SUCCESS = "200";
	public static final String TIMELINE_CALLBACK_STATUS_CODE_CLIENT_ERROR = "400";
	public static final String TIMELINE_CALLBACK_STATUS_CODE_SERVER_ERROR = "500";

	public static final int TIMELINE_CALLBACK_STATUS_CODE_SUCCESS_INT = 200;
	public static final int TIMELINE_CALLBACK_STATUS_CODE_CLIENT_ERROR_INT = 400;
	public static final int TIMELINE_CALLBACK_STATUS_CODE_SERVER_ERROR_INT = 500;

	public static ResponseEntity<String> genSuccessResponse(JsonObject jo,
			int code) {
		// JsonObject jo = new JsonObject();
		jo.addProperty("code", code);
		return new ResponseEntity<String>(jo.toString(), HttpStatus.OK);
	}

	public static ResponseEntity<String> genSuccessResponse(
			Map<String, Object> map, int code) {
		// JsonObject jo = new JsonObject();
		map.put("code", code);
		return new ResponseEntity<String>((new Gson()).toJson(map),
				HttpStatus.OK);
	}

	public static ResponseEntity<String> genSuccessResponse(int code) {
		JsonObject jo = new JsonObject();
		jo.addProperty("code", code);
		return new ResponseEntity<String>(jo.toString(), HttpStatus.OK);
	}

	public static ResponseEntity<String> gen400ErrorResponse(int code) {
		JsonObject jo = new JsonObject();
		jo.addProperty("code", code);
		return new ResponseEntity<String>(jo.toString(), HttpStatus.BAD_REQUEST);
	}

	public static ResponseEntity<byte[]> gen510ErrorResponse(int code) {
		JsonObject jo = new JsonObject();
		jo.addProperty("code", code);
		return new ResponseEntity<byte[]>(jo.toString().getBytes(),
				HttpStatus.NOT_EXTENDED);
	}

	public static ResponseEntity<byte[]> genErrorResponse(int code) {
		JsonObject jo = new JsonObject();
		jo.addProperty("code", code);
		return new ResponseEntity<byte[]>(jo.toString().getBytes(),
				HttpStatus.BAD_REQUEST);
	}

	public static ResponseEntity<byte[]> genErrorResponse(int code,
			String errmsg) {
		JsonObject jo = new JsonObject();
		jo.addProperty("code", code);
		jo.addProperty("errmsg", errmsg);
		return new ResponseEntity<byte[]>(jo.toString().getBytes(),
				HttpStatus.BAD_REQUEST);
	}

	public static ResponseEntity<byte[]> genSuccessResponseBytes(JsonObject jo,
			int code) {
		// JsonObject jo = new JsonObject();
		jo.addProperty("code", code);
		return new ResponseEntity<byte[]>(jo.toString().getBytes(),
				HttpStatus.OK);
	}

	public static ResponseEntity<byte[]> genSuccessResponseBytes(
			Map<String, Object> map, int code) {
		// JsonObject jo = new JsonObject();
		map.put("code", code);
		return new ResponseEntity<byte[]>((new Gson()).toJson(map).getBytes(),
				HttpStatus.OK);
	}

	public static ResponseEntity<String> genSuccessCallbackResponse(int code) {
		JsonObject jo = new JsonObject();
		jo.addProperty("code", code);
		return new ResponseEntity<String>(jo.toString(), HttpStatus.OK);
	}

}
