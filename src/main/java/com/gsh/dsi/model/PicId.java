package com.gsh.dsi.model;

public class PicId {
	String picture;
	String id;

	
	
	public PicId(String picture, String id) {
		this.picture = picture;
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	
}
