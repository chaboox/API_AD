package com.gsh.dsi.model;

import com.unboundid.ldap.sdk.SearchResultEntry;

public class Company {

	private String nameAD, name, description, pole;

	public Company(String nameAD, String name) {
		super();
		this.nameAD = nameAD;
		this.name = name;
	}

	public Company(SearchResultEntry searchResultEntry) {
		
		this.name = searchResultEntry.getAttributeValue("displayname");
		this.nameAD = searchResultEntry.getAttributeValue("name");
		this.description = searchResultEntry.getAttributeValue("description");
		this.pole = searchResultEntry.getAttributeValue("street");
		
	}

	public String getNameAD() {
		return nameAD;
	}

	public void setNameAD(String nameAD) {
		this.nameAD = nameAD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPole() {
		return pole;
	}

	public void setPole(String pole) {
		this.pole = pole;
	}
	
	
	
}
