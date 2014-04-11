package me.wakao.app.bean;

public class CategoryItem {
	private String desc;
	private int icon;
	private String name;
	
	public CategoryItem(int icon, String desc) {
		this.desc = desc;
		this.icon = icon;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	
}
