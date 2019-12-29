package com.gsh.dsi.model;

import com.unboundid.ldap.sdk.SearchResultEntry;

public class RouaData {
	private String mail, managerMail, managerAD;

	public RouaData(String mail, String managerMail, String managerAD) {
		this.mail = mail;
		this.managerMail = managerMail;
		this.managerAD = managerAD;
		
	}


	public String getManagerMail() {
		return managerMail;
	}

	public void setManagerMail(String managerMail) {
		this.managerMail = managerMail;
	}

	public String getManagerAD() {
		return managerAD;
	}

	public void setManagerAD(String managerAD) {
		this.managerAD = managerAD;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	
	
	
}
