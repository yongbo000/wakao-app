package me.wakao.app.bean;

import java.io.Serializable;

public class ArticleObj implements Serializable {
	private int id;
	private String title;
	private String author;
	private String intro_words;
	private String from;
	private String content;
	private int comment_count;
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
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIntro() {
		return intro_words;
	}
	public void setIntro(String intro) {
		this.intro_words = intro;
	}
	public String getIntro_words() {
		return intro_words;
	}
	public void setIntro_words(String intro_words) {
		this.intro_words = intro_words;
	}
	public int getComment_count() {
		return comment_count;
	}
	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}
}
