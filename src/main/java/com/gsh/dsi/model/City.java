package com.gsh.dsi.model;

import com.unboundid.ldap.sdk.SearchResultEntry;

public class City {
	private String id, code, name;

	public City(String code, String name) {
		super();
		this.code = code;
		this.name = name;
		
	}

	public City(SearchResultEntry searchResultEntry, String company) {
		this.name = searchResultEntry.getAttributeValue("description");
		this.code = searchResultEntry.getAttributeValue("name");
		this.id = company + code;
		
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
	
}
