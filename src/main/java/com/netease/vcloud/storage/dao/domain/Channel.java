package com.netease.vcloud.storage.dao.domain;

public class Channel {

	// 频道编号
	private long id;

	// 频道名
	private String name;

	// 默认播放器
	private long pid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	// TODO: 其他 getter/setter方法

}
