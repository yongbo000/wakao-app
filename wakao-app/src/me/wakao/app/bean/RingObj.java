package me.wakao.app.bean;

import java.io.Serializable;

public class RingObj implements Serializable {
	private String rid;
	private String name;
	private String playcnt;
	private String duration;
	private String artist;
	private String downUrl;
	private boolean isPlaying;
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPlaycnt() {
		return playcnt;
	}
	public void setPlaycnt(String playcnt) {
		this.playcnt = playcnt;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getDownUrl() {
		return downUrl;
	}
	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}
	public boolean isPlaying() {
		return isPlaying;
	}
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	
}
