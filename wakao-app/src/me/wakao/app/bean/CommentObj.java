package me.wakao.app.bean;

import java.io.Serializable;

public class CommentObj implements Serializable {
	private String funnyId;
	private String userId;
	private String userName;
	private String content;
	private String avatar;
	public String getFunnyId() {
		return funnyId;
	}
	public void setFunnyId(String funnyId) {
		this.funnyId = funnyId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}
