package com.netease.vcloud.common;

public class AdminCommon {
	
	public interface ICode {
        public static final int CODE_SUCCESS = 200;         // 成功
        public static final int CODE_GET_FAILED = 1;        // 查询失败
        public static final int CODE_DEL_FAILED = 2;        // 删除失败
        public static final int CODE_ADD_FAILED = 3;        // 添加失败
        public static final int CODE_AUTH_FAILED = 11;      // 授权失败
    }
}
