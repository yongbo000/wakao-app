package me.wakao.app.bean;

import java.io.Serializable;

public class UserObj implements Serializable {
	private String weiboId;
	private String weiboName;
	private String faceUrl;
	public String getWeiboId() {
		return weiboId;
	}
	public void setWeiboId(String weiboId) {
		this.weiboId = weiboId;
	}
	public String getWeiboName() {
		return weiboName;
	}
	public void setWeiboName(String weiboName) {
		this.weiboName = weiboName;
	}
	public String getFaceUrl() {
		return faceUrl;
	}
	public void setFaceUrl(String faceUrl) {
		this.faceUrl = faceUrl;
	}
	
}
