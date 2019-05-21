package com.gsh.dsi.model;

import java.util.Collections;

import javax.xml.bind.DatatypeConverter;

import com.unboundid.ldap.sdk.SearchResultEntry;

public class Contact implements Comparable<Contact>{

	private String id, name, company, description, city, number, voip, department, mail, pictureC;
	private boolean boss;

	public Contact(String name, String firstname) {
		this.name = name;
	}
	
	
	
	
	public Contact(String name, String company, String description, String city, String number,
			String voip, String department, String mail, String pictureC) {
		this.name = name;
		this.company = company;
		this.description = description;
		this.city = city;
		this.number = number;
		this.voip = voip;
		this.department = department;
		this.mail = mail;
		this.pictureC = pictureC;}


	public Contact(SearchResultEntry searchResultEntry) {
		//byte[]  picture;
		this.id = searchResultEntry.getAttributeValue("distinguishedName");
		this.name = searchResultEntry.getAttributeValue("cn");
		this.company = searchResultEntry.getAttributeValue("company");
		this.description = searchResultEntry.getAttributeValue("description");
		this.city = searchResultEntry.getAttributeValue("l");
		this.number = searchResultEntry.getAttributeValue("telephoneNumber");
		this.voip = searchResultEntry.getAttributeValue("ipPhone");
		if(searchResultEntry.getAttributeValue("department") == null)
			this.department = "NR";
		else
		this.department = searchResultEntry.getAttributeValue("department");
		this.mail = searchResultEntry.getAttributeValue("mail");
		/*picture = searchResultEntry.getAttributeValueBytes("thumbnailPhoto");
		if(picture!= null) {
			
		this.pictureC = DatatypeConverter.printBase64Binary(picture);}
		else this.pictureC = null;*/
		this.boss = false;
	}
	
	public Contact(SearchResultEntry searchResultEntry, String city) {
		//byte[]  picture;
		this.id = searchResultEntry.getAttributeValue("distinguishedName");
		this.name = searchResultEntry.getAttributeValue("cn");
		this.company = searchResultEntry.getAttributeValue("company");
		this.description = searchResultEntry.getAttributeValue("description");
		this.city = city;
		this.number = searchResultEntry.getAttributeValue("telephoneNumber");
		this.voip = searchResultEntry.getAttributeValue("ipPhone");
		this.department = searchResultEntry.getAttributeValue("department");
		this.mail = searchResultEntry.getAttributeValue("mail");
		/*picture = searchResultEntry.getAttributeValueBytes("thumbnailPhoto");
		if(picture!= null) {
			
		this.pictureC = DatatypeConverter.printBase64Binary(picture);}
		else this.pictureC = null;*/
		this.boss = false;
	}
	
	public Contact(SearchResultEntry searchResultEntry, boolean pic) {
		this.id = searchResultEntry.getAttributeValue("distinguishedName");
		this.name = searchResultEntry.getAttributeValue("cn");
		this.company = searchResultEntry.getAttributeValue("company");
		this.description = searchResultEntry.getAttributeValue("description");
		this.city = searchResultEntry.getAttributeValue("l");
		this.number = searchResultEntry.getAttributeValue("telephoneNumber");
		this.voip = searchResultEntry.getAttributeValue("ipPhone");
		this.department = searchResultEntry.getAttributeValue("department");
		this.mail = searchResultEntry.getAttributeValue("mail");
		this.boss = false;
	}



	
	
	
	public boolean isBoss() {
		return boss;
	}




	public void setBoss(boolean boss) {
		this.boss = boss;
	}




	public String getId() {
		return id;
	}




	public void setId(String id) {
		this.id = id;
	}




	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCompany() {
		return company;
	}




	public void setCompany(String company) {
		this.company = company;
	}




	public String getDescription() {
		return description;
	}




	public void setDescription(String description) {
		this.description = description;
	}




	public String getCity() {
		return city;
	}




	public void setCity(String city) {
		this.city = city;
	}




	public String getNumber() {
		return number;
	}




	public void setNumber(String number) {
		this.number = number;
	}




	public String getVoip() {
		return voip;
	}




	public void setVoip(String voip) {
		this.voip = voip;
	}




	public String getDepartment() {
		return department;
	}




	public void setDepartment(String department) {
		this.department = department;
	}




	public String getMail() {
		return mail;
	}




	public void setMail(String mail) {
		this.mail = mail;
	}



	



	@Override
	public String toString() {
		return "Contact [name=" + name + ", company=" + company + ", description=" + description + ", city=" + city
				+ ", number=" + number + ", voip=" + voip + ", department=" + department + ", mail=" + mail + "]";
	}




	@Override
	public int compareTo(Contact o) {
		// TODO Auto-generated method stub
		return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
	}




	
	
}
