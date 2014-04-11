package me.wakao.app.bean;

import java.io.Serializable;

public class FunnyObj implements Serializable {
	private int id;
	private int comment_count;
	private String author;
	private String from;
	private String content;
	private String pic;
	private String createtime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getCommentCount() {
		return comment_count;
	}
	public void setCommentCount(int comment_count) {
		this.comment_count = comment_count;
	}
	public String getCreatetime() {
		return createtime.substring(0, 10);
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
	
}
