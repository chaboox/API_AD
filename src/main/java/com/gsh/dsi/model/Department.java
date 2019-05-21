package com.gsh.dsi.model;

import com.unboundid.ldap.sdk.SearchResultEntry;

public class Department {

	private String id, name;

	public Department(String name) {
		this.name = name;
	}

	public Department(SearchResultEntry searchResultEntry, String idCity) {
		this.name = searchResultEntry.getAttributeValue("department");
		this.id = idCity + name;
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

	@Override
	public boolean equals(Object object) {
		 boolean result = false;
		    if (object == null || object.getClass() != getClass()) {
		        result = false;
		    } else {
		        Department employee = (Department) object;
		        if (this.name.equals(employee.getName())) {
		            result = true;
		        }
		    }
		    return result;
	}
	
	
	
}
