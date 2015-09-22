package com.netease.vcloud.param;


public class ContentParam {
	
	private byte[] content;
    private long uid;
    private Integer clienttype = 0;
    
    public ContentParam(long uid, byte[] content) {
        this.content = content;
        this.uid = uid;
    }
    
    public ContentParam() {}
    
    public byte[] getContent() {
        return content;
    }
    
    public void setContent(byte[] content) {
        this.content = content;
    }
    
    public long getUid() {
        return uid;
    }
    
    public void setUid(long uid) {
        this.uid = uid;
    }
    
    
    public Integer getClienttype() {
        return clienttype;
    }
    
    public void setClienttype(Integer clienttype) {
        this.clienttype = clienttype;
    }

}
