package com.netease.vcloud.param;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class AddChannelParam {
	
	private String uid;
	private String sid;
	private String name;
	private Long cid;
	private Long pid;
	
	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getSid() {
		return sid;
	}
	
	public void setSid(String sid) {
		this.sid = sid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getCid() {
		return cid;
	}
	
	public void setCid(long cid) {
		this.cid = cid;
	}
	
	public long getPid() {
		return pid;
	}
	
	public void setPid(long pid) {
		this.pid = pid;
	}
	
	public void parse(JsonObject joHead) throws Exception {
        JsonElement je;

        if ((je = joHead.get("uid")) != null)
            this.uid = je.getAsString();
        if ((je = joHead.get("sid")) != null)
            this.sid = je.getAsString();
        if ((je = joHead.get("name")) != null)
            this.name = je.getAsString();
        if ((je = joHead.get("cid")) != null)
            this.cid = je.getAsLong();
        if ((je = joHead.get("pid")) != null)
            this.pid = je.getAsLong();
    }
	
	public static AddChannelParam parse(String content) throws Exception {
        //logger.info("content:" + content);
		AddChannelParam param = new AddChannelParam();
        JsonParser topJp = new JsonParser();
        JsonObject topJo = topJp.parse(content).getAsJsonObject();
        param.parse(topJo);
        return param;
    }

}
