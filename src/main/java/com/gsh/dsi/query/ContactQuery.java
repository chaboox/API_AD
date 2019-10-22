package com.gsh.dsi.query;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.gsh.dsi.model.City;
import com.gsh.dsi.model.Company;
import com.gsh.dsi.model.Contact;
import com.gsh.dsi.model.Department;
import com.gsh.dsi.model.Pic;
import com.gsh.dsi.model.PicId;
import com.unboundid.asn1.ASN1OctetString;
import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldap.sdk.controls.SimplePagedResultsControl;
import com.unboundid.ldap.sdk.extensions.PasswordModifyExtendedRequest;
import com.unboundid.util.LDAPTestUtils;

import org.springframework.web.bind.annotation.*;
import static com.unboundid.util.StaticUtils.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

@RestController
public class ContactQuery {
	
	 @RequestMapping("/AD")
	 public String test2() {
		return "WORK!";
	 }
	 
	public LDAPConnection connexion() throws LDAPException {
		return new LDAPConnection("10.10.10.10", 389, "CT_TEST@groupe-hasnaoui.local", "123456");
	/*LDAPConnection cn = new LDAPConnection("10.10.10.10", 389);
		cn.bind("deboosere_am@groupe-hasnaoui.local", "2Boos138re");
		return cn;*/
	}
	
	@PostMapping("/login")
	public String login(@RequestParam("username") String username, @RequestParam("password") String password)  {
		LDAPConnection connection = null;
		try {
			byte[] decodedPassword = DatatypeConverter.parseBase64Binary(password);
			//String code = 
			System.out.println("DECODE =" + password + " _______ " + new String(decodedPassword));
			connection = new LDAPConnection("10.10.10.10", 389, username, new String(decodedPassword));
			
		} catch (LDAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0";
		}
		
		if(connection.isConnected()) {
			connection.close();
			return "yoman";
		}
		
		else {
			connection.close();
			return "0";
		}
		
	}
	
	@PostMapping("/loginInfo")
	public String[] loginInfo(@RequestParam("username") String username, @RequestParam("password") String password) throws LDAPException  {
		String usernameWithtoutA =  "";
		for (int i = 0; i < username.length(); i++) {
			if(username.charAt(i) == '@')
				break;
			else
				usernameWithtoutA = usernameWithtoutA + username.charAt(i);
		}
		LDAPConnection connection = null;
		try {
			byte[] decodedPassword = DatatypeConverter.parseBase64Binary(password);
			//String code = 
			System.out.println("DECODE =" + password + " _______ " + new String(decodedPassword));
			connection = new LDAPConnection("10.10.10.10", 389, username, new String(decodedPassword));
			
		} catch (LDAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new String[1];
		}
		
		if(connection.isConnected()) {
			Filter[] filterElements =
			    {
			    		  Filter.createEqualityFilter("objectCategory", "CN=Person,CN=Schema,CN=Configuration,DC=groupe-hasnaoui,DC=local"),
			    		  Filter.create("(|(sAMAccountName="+ usernameWithtoutA + ")(mail="+ username+"))"),
			    };

		 Filter filter = Filter.createANDFilter(filterElements);
		 SearchRequest searchRequest = new SearchRequest("DC=groupe-hasnaoui,DC=local", SearchScope.SUB, filter,  "thumbnailPhoto" , "distinguishedName", "mail", "sAMAccountName");
			
		 SearchResult searchResult = connection.search(searchRequest);
		 String name = "", mail = "", sAMAccountName = "", pic = "";
		 for(int i = 0;i < searchResult.getEntryCount(); i++) {
				 byte[] picture = searchResult.getSearchEntries().get(i).getAttributeValueBytes("thumbnailPhoto");
				  name = searchResult.getSearchEntries().get(i).getAttributeValue("cn");
				  mail = searchResult.getSearchEntries().get(i).getAttributeValue("mail");
				  sAMAccountName= searchResult.getSearchEntries().get(i).getAttributeValue("sAMAccountName");
				 if(picture!= null) {
				  pic = DatatypeConverter.printBase64Binary(picture);
				 }
					
					else { pic = null;}
		 }
		 String [] re = {name, mail, sAMAccountName, pic};
			connection.close();
			return re;
		}
		
		else {
			connection.close();
			return new String[1];
		}
		
	}


	@GetMapping("/contacts2")
	public ArrayList<Contact> getAll(){
		//Big up ADEL ACHOUR!
		ArrayList<Contact> cs = new ArrayList<>();
		cs.add(new Contact("ACHOUR", "Adel"));
		//cs.add(new Contact("DEBOOSERE", "Adam"));
	//	Gson gson = new Gson();
		//String json = gson.toJson(cs);
		return cs;		
	}
	
	@GetMapping("/AD")
	public String AD() throws LDAPException {
		 LDAPConnection con = connexion();
		 
		// Filter filter = Filter.createEqualityFilter("objectCategory", "CN=Person,CN=Schema,CN=Configuration,DC=groupe-hasnaoui,DC=local");
		 
		
		 Filter[] filterElements =
			    {
			      Filter.create("(cn=*m*)"),
			      Filter.createEqualityFilter("objectCategory", "CN=Person,CN=Schema,CN=Configuration,DC=groupe-hasnaoui,DC=local")
			    };

			    Filter filter = Filter.createANDFilter(filterElements);
		 SearchRequest searchRequest = new SearchRequest("OU=SBA,OU=GSHA,DC=groupe-hasnaoui,DC=local", SearchScope.SUB, filter, "cn");
		 SearchResult searchResult = con.search(searchRequest);
		 SearchResultEntry[] searchResultEntries = new SearchResultEntry[searchResult.getEntryCount()];
		 String result = " ";
		 
		 for(int i = 0;i < searchResult.getEntryCount(); i++) {
			 searchResultEntries[i] = searchResult.getSearchEntries().get(i);
		 }
		 
		 for(int i = 0; i < searchResultEntries.length; i++) {
			 result = result + " " + searchResultEntries[i].getAttributeValue("cn");
		 }
		return result;
	}
	
	@PostMapping("/contactsByDepartment")
	ArrayList<Contact> getContactsByDepartment(@RequestParam("company") String filial, @RequestParam("department") String departement, @RequestParam("city") String city) throws LDAPException {
		 LDAPConnection con = connexion();
		 Filter[] filterElements =
			    {
			      Filter.create("(department=" + departement +")"),
			      Filter.createEqualityFilter("objectCategory", "CN=Person,CN=Schema,CN=Configuration,DC=groupe-hasnaoui,DC=local"),
			      Filter.create("(|(telephoneNumber=*)(mail=*))")
			    };

		 Filter filter = Filter.createANDFilter(filterElements);
		 SearchRequest searchRequest = new SearchRequest("OU=" + city + ",OU=" + filial + ",DC=groupe-hasnaoui,DC=local", SearchScope.SUB, filter, "cn", "company", "description", "telephoneNumber",  "ipPhone", "department", "mail", "distinguishedName");
		 SearchResult searchResult = con.search(searchRequest);
		 ArrayList<Contact> contacts = new ArrayList<>();
		 for(int i = 0;i < searchResult.getEntryCount(); i++) {
			 contacts.add(new Contact(searchResult.getSearchEntries().get(i), city));
		 }
		 Collections.sort(contacts);
		return contacts;
	}

	@PostMapping("/contactsByCompany")
	ArrayList<Contact> getContactsByCompany(@RequestParam("company") String filial, @RequestParam("city") String city) throws LDAPException {
		 LDAPConnection con = connexion();
		 Filter[] filterElements =
			    {
			      Filter.createEqualityFilter("objectCategory", "CN=Person,CN=Schema,CN=Configuration,DC=groupe-hasnaoui,DC=local")
			    };
		 int c = 0;
		 Filter filter = Filter.createANDFilter(filterElements);
		 SearchRequest searchRequest = new SearchRequest("OU=" + city + ",OU=" + filial + ",DC=groupe-hasnaoui,DC=local", SearchScope.SUB, filter, "cn", "company", "description", "l", "telephoneNumber",  "ipPhone", "department", "mail");
		 SearchResult searchResult = con.search(searchRequest);
		 ArrayList<Contact> contacts = new ArrayList<>();
		 for(int i = 0;i < searchResult.getEntryCount(); i++) {
			 contacts.add(new Contact(searchResult.getSearchEntries().get(i)));
			 c++;
		 }
		 System.out.print("YO " + c);
		return contacts;
	}
	
	@PostMapping("/contactsByName")
	ArrayList<Contact> getContactsByName(@RequestParam("name") String name, @RequestParam("number") int number) throws LDAPException {
		 LDAPConnection con = connexion();
		 int count = 0;
		 Filter[] filterElements =
			    {
			      Filter.createEqualityFilter("objectCategory", "CN=Person,CN=Schema,CN=Configuration,DC=groupe-hasnaoui,DC=local"),
			      Filter.create("(cn=*" + name + "*)"),
			      Filter.create("(|(telephoneNumber=*)(mail=*))")
			    };

		 Filter filter = Filter.createANDFilter(filterElements);
		 ASN1OctetString resumeCookie = null;
		 SearchRequest searchRequest = new SearchRequest("DC=groupe-hasnaoui,DC=local", SearchScope.SUB, filter, "cn", "company", "description", "l", "telephoneNumber",  "ipPhone", "department", "mail", "thumbnailPhoto");
		 searchRequest.setControls(
	                new SimplePagedResultsControl(1000, resumeCookie));
		 ArrayList<Contact> contacts = new ArrayList<>();
		 while (true)
		    {
		        searchRequest.setControls(
		                new SimplePagedResultsControl(number, resumeCookie));
		        SearchResult searchResult = con.search(searchRequest);
		        for (SearchResultEntry e : searchResult.getSearchEntries())
		        {
		          
		                System.out.println("->" + e.getAttributeValue("cn") + count ++ );
		                contacts.add(new Contact(e));
		        }

		        LDAPTestUtils.assertHasControl(searchResult,
		                SimplePagedResultsControl.PAGED_RESULTS_OID);
		        SimplePagedResultsControl responseControl =
		                SimplePagedResultsControl.get(searchResult);
		        if (responseControl.moreResultsToReturn())
		        {
		        	break;
		           //resumeCookie = responseControl.getCookie();
		          
		        }
		        else
		        {
		            break;
		        }}

		    
		return contacts;
	}
	
	
	@PostMapping("/contactsWithNullDepartment")
	ArrayList<Contact> getContactNull(@RequestParam("number") int number) throws LDAPException {
		 LDAPConnection con = connexion();
		 int count = 0;
		 Filter[] filterElements =
			    {
			      Filter.createEqualityFilter("objectCategory", "CN=Person,CN=Schema,CN=Configuration,DC=groupe-hasnaoui,DC=local"),
			      Filter.create("!(department=*)"),
			      Filter.create("(|(telephoneNumber=*)(mail=*))"),
			      Filter.create("(company=*)")
			   
			      
			      
			    };

		 Filter filter = Filter.createANDFilter(filterElements);
		 ASN1OctetString resumeCookie = null;
		 SearchRequest searchRequest = new SearchRequest("DC=groupe-hasnaoui,DC=local", SearchScope.SUB, filter, "cn", "company", "description", "telephoneNumber",  "ipPhone", "department", "mail", "distinguishedName");
		 searchRequest.setControls(
	                new SimplePagedResultsControl(1000, resumeCookie));
		 ArrayList<Contact> contacts = new ArrayList<>();
		 while (true)
		    {
		        searchRequest.setControls(
		                new SimplePagedResultsControl(number, resumeCookie));
		        SearchResult searchResult = con.search(searchRequest);
		        for (SearchResultEntry e : searchResult.getSearchEntries())
		        {
		          
		                System.out.println("->" + e.getAttributeValue("cn") + count ++ );
		                contacts.add(new Contact(e));
		        }

		        LDAPTestUtils.assertHasControl(searchResult,
		                SimplePagedResultsControl.PAGED_RESULTS_OID);
		        SimplePagedResultsControl responseControl =
		                SimplePagedResultsControl.get(searchResult);
		        if (responseControl.moreResultsToReturn())
		        {
		        	break;
		           //resumeCookie = responseControl.getCookie();
		          
		        }
		        else
		        {
		            break;
		        }}

		    
		return contacts;
	}
	
	@GetMapping("/allContacts")
	ArrayList<Contact> getContactsWithoutPic() throws LDAPException {
		 LDAPConnection con = connexion();
		 int count = 0;
		 Filter[] filterElements =
			    {
			      Filter.createEqualityFilter("objectCategory", "CN=Person,CN=Schema,CN=Configuration,DC=groupe-hasnaoui,DC=local"),
			      //TODO change test
			      Filter.create("(|(telephoneNumber=*)(mail=*))"),
			      Filter.create("(company=*)"),
			      Filter.create("(!(userAccountControl:1.2.840.113556.1.4.803:=2))")
			      
			    };

		 Filter filter = Filter.createANDFilter(filterElements);
		 ASN1OctetString resumeCookie = null;
		 SearchRequest searchRequest = new SearchRequest("DC=groupe-hasnaoui,DC=local", SearchScope.SUB, filter, "cn", "company", "description", "l", "telephoneNumber",  "ipPhone", "department", "mail", "distinguishedName", "manager");
		 searchRequest.setControls(
	                new SimplePagedResultsControl(1000, resumeCookie));
		 ArrayList<Contact> contacts = new ArrayList<>();
		 ArrayList<String> bosses = new ArrayList<>(); 
		 while (true)
		    {
		        searchRequest.setControls(
		                new SimplePagedResultsControl(500, resumeCookie));
		        SearchResult searchResult = con.search(searchRequest);
		        for (SearchResultEntry e : searchResult.getSearchEntries())
		        { 
		          
		                System.out.println("->" + e.getAttributeValue("cn") + count ++ );
		                contacts.add(new Contact(e));
		               if(!bosses.contains(e.getAttributeValue("manager")) && e.getAttributeValue("manager")!= null)
		                bosses.add((e.getAttributeValue("manager")));
		        }
		        
		        
		        System.out.println("->DD" + bosses.size() + bosses.get(0));

		        LDAPTestUtils.assertHasControl(searchResult,
		                SimplePagedResultsControl.PAGED_RESULTS_OID);
		        SimplePagedResultsControl responseControl =
		                SimplePagedResultsControl.get(searchResult);
		        if (responseControl.moreResultsToReturn())
		        {
		        	
		           resumeCookie = responseControl.getCookie();
		          
		        }
		        else
		        {
		            break;
		        }}
		 
		 for(Contact c : contacts) {
			 if(bosses.contains(c.getId()))
				 c.setBoss(true);
		 }
		    
		return contacts;
	}
	
	
	@GetMapping("/allContactsPics")
	ArrayList<Contact> getContactsWithPic() throws LDAPException {
		 LDAPConnection con = connexion();
		 int count = 0;
		 Filter[] filterElements =
			    {
			      Filter.createEqualityFilter("objectCategory", "CN=Person,CN=Schema,CN=Configuration,DC=groupe-hasnaoui,DC=local"),
			      //TODO change test
			      Filter.create("(|(telephoneNumber=*)(mail=*))"),
			      Filter.create("(company=*)"),
			      Filter.create("(!(userAccountControl:1.2.840.113556.1.4.803:=2))")
			      
			    };

		 Filter filter = Filter.createANDFilter(filterElements);
		 ASN1OctetString resumeCookie = null;
		 SearchRequest searchRequest = new SearchRequest("DC=groupe-hasnaoui,DC=local", SearchScope.SUB, filter, "cn", "company", "description", "l", "telephoneNumber",  "ipPhone", "department", "mail", "distinguishedName", "manager", "thumbnailPhoto");
		 searchRequest.setControls(
	                new SimplePagedResultsControl(1000, resumeCookie));
		 ArrayList<Contact> contacts = new ArrayList<>();
		 ArrayList<String> bosses = new ArrayList<>(); 
		 while (true)
		    {
		        searchRequest.setControls(
		                new SimplePagedResultsControl(500, resumeCookie));
		        SearchResult searchResult = con.search(searchRequest);
		        for (SearchResultEntry e : searchResult.getSearchEntries())
		        { 
		          
		                System.out.println("->" + e.getAttributeValue("cn") + count ++ );
		                contacts.add(new Contact(e));
		               if(!bosses.contains(e.getAttributeValue("manager")) && e.getAttributeValue("manager")!= null)
		                bosses.add((e.getAttributeValue("manager")));
		        }
		        
		        
		        System.out.println("->DD" + bosses.size() + bosses.get(0));

		        LDAPTestUtils.assertHasControl(searchResult,
		                SimplePagedResultsControl.PAGED_RESULTS_OID);
		        SimplePagedResultsControl responseControl =
		                SimplePagedResultsControl.get(searchResult);
		        if (responseControl.moreResultsToReturn())
		        {
		        	
		           resumeCookie = responseControl.getCookie();
		          
		        }
		        else
		        {
		            break;
		        }}
		 
		 for(Contact c : contacts) {
			 if(bosses.contains(c.getId()))
				 c.setBoss(true);
		 }
		    
		return contacts;
	}
	
	
	@GetMapping("/getDuplicat")
	ArrayList<Contact> getDuplicat() throws LDAPException {
		 LDAPConnection con = connexion();
		 int count = 0;
		 Filter[] filterElements =
			    {
			      Filter.createEqualityFilter("objectCategory", "CN=Person,CN=Schema,CN=Configuration,DC=groupe-hasnaoui,DC=local"),
			      //TODO change test
			      Filter.create("(|(telephoneNumber=*)(mail=*))"),
			      Filter.create("(company=*)"),
			      Filter.create("(!(userAccountControl:1.2.840.113556.1.4.803:=2))")
			      
			    };

		 Filter filter = Filter.createANDFilter(filterElements);
		 ASN1OctetString resumeCookie = null;
		 SearchRequest searchRequest = new SearchRequest("DC=groupe-hasnaoui,DC=local", SearchScope.SUB, filter, "cn", "company", "description", "l", "telephoneNumber",  "ipPhone", "department", "mail", "distinguishedName", "manager");
		 searchRequest.setControls(
	                new SimplePagedResultsControl(1000, resumeCookie));
		 ArrayList<Contact> contacts = new ArrayList<>();
		 ArrayList<Contact> contactsDupl = new ArrayList<>();
	
		 while (true)
		    {
		        searchRequest.setControls(
		                new SimplePagedResultsControl(500, resumeCookie));
		        SearchResult searchResult = con.search(searchRequest);
		        for (SearchResultEntry e : searchResult.getSearchEntries())
		        { 
		          
		                System.out.println("->" + e.getAttributeValue("cn") + count ++ );
		                Contact contact = new Contact(e);
		                if(contacts.size()>0)
		                for(Contact c: contacts) {
		                	if(contact.getNumber()!= null && c.getNumber() != null && c.getNumber().length() > 9)
		                	if(contact.getNumber().equals(c.getNumber())) {
		                		contactsDupl.add(c);
		                		contactsDupl.add(contact);
		                	}
		                }
		                contacts.add(new Contact(e));
		              
		        }
		        
		        
		        
		        LDAPTestUtils.assertHasControl(searchResult,
		                SimplePagedResultsControl.PAGED_RESULTS_OID);
		        SimplePagedResultsControl responseControl =
		                SimplePagedResultsControl.get(searchResult);
		        if (responseControl.moreResultsToReturn())
		        {
		        	
		           resumeCookie = responseControl.getCookie();
		          
		        }
		        else
		        {
		            break;
		        }}
		 
		
		    
		return contactsDupl;
	}
	
	@GetMapping("/allDisabledContact")
	ArrayList<Contact> getContactsDisabled() throws LDAPException {
		 LDAPConnection con = connexion();
		 int count = 0;
		 Filter[] filterElements =
			    {
			      Filter.createEqualityFilter("objectCategory", "CN=Person,CN=Schema,CN=Configuration,DC=groupe-hasnaoui,DC=local"),
			      //TODO change test
			      Filter.create("(|(telephoneNumber=*)(mail=*))"),
			      Filter.create("(company=*)"),
			      Filter.create("(userAccountControl:1.2.840.113556.1.4.803:=2)")
			      
			    };

		 Filter filter = Filter.createANDFilter(filterElements);
		 ASN1OctetString resumeCookie = null;
		 SearchRequest searchRequest = new SearchRequest("DC=groupe-hasnaoui,DC=local", SearchScope.SUB, filter, "distinguishedName");
		 searchRequest.setControls(
	                new SimplePagedResultsControl(1000, resumeCookie));
		 ArrayList<Contact> contacts = new ArrayList<>();
		 while (true)
		    {
		        searchRequest.setControls(
		                new SimplePagedResultsControl(500, resumeCookie));
		        SearchResult searchResult = con.search(searchRequest);
		        for (SearchResultEntry e : searchResult.getSearchEntries())
		        { 
		          
		                System.out.println("->" + e.getAttributeValue("cn") + count ++ );
		                contacts.add(new Contact(e));
		        }

		        LDAPTestUtils.assertHasControl(searchResult,
		                SimplePagedResultsControl.PAGED_RESULTS_OID);
		        SimplePagedResultsControl responseControl =
		                SimplePagedResultsControl.get(searchResult);
		        if (responseControl.moreResultsToReturn())
		        {
		        	
		           resumeCookie = responseControl.getCookie();
		          
		        }
		        else
		        {
		            break;
		        }}

		    
		return contacts;
	}
	
	
	//Get All available companies
	@GetMapping("/company")
	public ArrayList<Company> getCompanies() throws LDAPException{
		ArrayList<Company> companies = new ArrayList<>();
		LDAPConnection con = connexion();
		 Filter[] filterElements =
			    {
			      Filter.createEqualityFilter("objectCategory", "CN=Organizational-Unit,CN=Schema,CN=Configuration,DC=groupe-hasnaoui,DC=local"),
			      Filter.create("(&(objectClass=organizationalUnit)(!(ou=Domain Controllers))(!(ou=GSHR))(!(ou=OFLZ))(!(ou=AZUD))(!(ou=Microsoft Exchange Security Groups)))")
			      ,Filter.create("!(ou=CLIN)")
			      ,Filter.create("!(ou=IPTV)")
			      ,Filter.create("!(ou=MDCR)")
			      ,Filter.create("!(ou=RYML)")
			      ,Filter.create("!(ou=DRNA)")
			      ,Filter.create("!(ou=HPSS)")
			      ,Filter.create("!(ou=HTTV)")
			      ,Filter.create("!(ou=RPSO)")
			    };
		 Filter filter = Filter.createANDFilter(filterElements);
		 SearchRequest searchRequest = new SearchRequest("DC=groupe-hasnaoui,DC=local", SearchScope.ONE, filter, "name", "description", "displayname", "street");
		 SearchResult searchResult = con.search(searchRequest);
		 for(int i = 0;i < searchResult.getEntryCount(); i++) {
			 companies.add(new Company(searchResult.getSearchEntries().get(i)));
		 }

		return companies;
		
	}
	


	@PostMapping("/DirectionByCompany")
	ArrayList<Department> getDirectionByCompany(@RequestParam("company") String filial, @RequestParam("city") String city) throws LDAPException {
		 LDAPConnection con = connexion();
		 Department department;
		 Filter[] filterElements =
			    {
			    		Filter.create("(&(objectClass=user)(objectCategory=person)(!(userAccountControl:1.2.840.113556.1.4.803:=2))(company=*)(|(mail=*)(telephoneNumber=*)))")
			    };

		 Filter filter = Filter.createANDFilter(filterElements);
		 SearchRequest searchRequest = new SearchRequest("OU=" + city + ",OU=" + filial + ",DC=groupe-hasnaoui,DC=local", SearchScope.SUB, filter,  "department");
		 SearchResult searchResult = con.search(searchRequest);
		 ArrayList<Department> departments = new ArrayList<>();
		 for(int i = 0;i < searchResult.getEntryCount(); i++) {
			 department = new Department(searchResult.getSearchEntries().get(i), filial + city);
			 System.out.println("DEP1 " + department.getName());
			 if(department.getName() == null)
				 department.setName("NR");
			 if(!departments.contains(department))
			 departments.add(department);
		 }

		return departments;
	}
	
	
	@PostMapping("/CitiesByCompany")
	ArrayList<City> getCitiesByCompany(@RequestParam("company") String filial) throws LDAPException {
		 LDAPConnection con = connexion();
		 City city;
		 Filter[] filterElements =
			    {
			    		Filter.create("(objectCategory=organizationalUnit)")
			    };

		 Filter filter = Filter.createANDFilter(filterElements);
		 SearchRequest searchRequest = new SearchRequest("OU=" + filial + ",DC=groupe-hasnaoui,DC=local", SearchScope.ONE, filter, "name", "description");
		 SearchResult searchResult = con.search(searchRequest);
		 ArrayList<City> cities= new ArrayList<>();
		 for(int i = 0;i < searchResult.getEntryCount(); i++) {
			 city = new City(searchResult.getSearchEntries().get(i),  filial);
				 cities.add(city);
		 }

		return cities;
	}
	
	
	@PostMapping("/getPicById")
	Pic getPicById(@RequestParam("id") String id) throws LDAPException {
			
			 LDAPConnection con = connexion();
			 City city;
			 Filter[] filterElements =
				    {
				    		  Filter.createEqualityFilter("objectCategory", "CN=Person,CN=Schema,CN=Configuration,DC=groupe-hasnaoui,DC=local"),
						      Filter.create("(distinguishedName=" + id+ ")"),
				    };

			 Filter filter = Filter.createANDFilter(filterElements);
			 SearchRequest searchRequest = new SearchRequest("DC=groupe-hasnaoui,DC=local", SearchScope.SUB, filter,  "thumbnailPhoto");
				
			 SearchResult searchResult = con.search(searchRequest);
	
			 for(int i = 0;i < searchResult.getEntryCount(); i++) {
					 byte[] picture = searchResult.getSearchEntries().get(i).getAttributeValueBytes("thumbnailPhoto");
						if(picture!= null) {
							
						return  new Pic(DatatypeConverter.printBase64Binary(picture));}
						else return null;
			 }
			 return null;
			 
			

		
			
		}
	
	@PostMapping("/getPicByIdAd2000")
	Pic getPicByAd2000(@RequestParam("id") String id) throws LDAPException {
			
			 LDAPConnection con = connexion();
			 City city;
			 Filter[] filterElements =
				    {
				    		  Filter.createEqualityFilter("objectCategory", "CN=Person,CN=Schema,CN=Configuration,DC=groupe-hasnaoui,DC=local"),
						      Filter.create("(sAMAccountName=" + id+ ")"),
				    };

			 Filter filter = Filter.createANDFilter(filterElements);
			 SearchRequest searchRequest = new SearchRequest("DC=groupe-hasnaoui,DC=local", SearchScope.SUB, filter,  "thumbnailPhoto");
				
			 SearchResult searchResult = con.search(searchRequest);
	
			 for(int i = 0;i < searchResult.getEntryCount(); i++) {
					 byte[] picture = searchResult.getSearchEntries().get(i).getAttributeValueBytes("thumbnailPhoto");
						if(picture!= null) {
							
						return  new Pic(DatatypeConverter.printBase64Binary(picture));}
						else return null;
			 }
			 return null;
			 
			

		
			
		}
	
	@PostMapping("/getPicStringById")
	byte[]  getPicStringById(@RequestParam("id") String id) throws LDAPException {
			
			 LDAPConnection con = connexion();
			 City city;
			 Filter[] filterElements =
				    {
				    		  Filter.createEqualityFilter("objectCategory", "CN=Person,CN=Schema,CN=Configuration,DC=groupe-hasnaoui,DC=local"),
						      Filter.create("(distinguishedName=" + id+ ")"),
				    };

			 Filter filter = Filter.createANDFilter(filterElements);
			 SearchRequest searchRequest = new SearchRequest("DC=groupe-hasnaoui,DC=local", SearchScope.SUB, filter,  "thumbnailPhoto");
				
			 SearchResult searchResult = con.search(searchRequest);
	
			 for(int i = 0;i < searchResult.getEntryCount(); i++) {
				 	byte[]  picture = searchResult.getSearchEntries().get(i).getAttributeValueBytes("thumbnailPhoto");
					 
						if(picture!= null) {
							
						return  picture;}
						else return null;
			 }
			 return null;
			 
			

		
			
		}
	
	
	@PostMapping("/getPicByIds")
	ArrayList<PicId> getPicByIds(@RequestParam("id") String id) throws LDAPException {
			 ArrayList<PicId> picid = new ArrayList<>();
			 LDAPConnection con = connexion();
			 City city;
			 Filter[] filterElements =
				    {
				    		  Filter.createEqualityFilter("objectCategory", "CN=Person,CN=Schema,CN=Configuration,DC=groupe-hasnaoui,DC=local"),
						      Filter.create(id),
				    };

			 Filter filter = Filter.createANDFilter(filterElements);
			 SearchRequest searchRequest = new SearchRequest("DC=groupe-hasnaoui,DC=local", SearchScope.SUB, filter,  "thumbnailPhoto" , "distinguishedName");
				
			 SearchResult searchResult = con.search(searchRequest);
	
			 for(int i = 0;i < searchResult.getEntryCount(); i++) {
					 byte[] picture = searchResult.getSearchEntries().get(i).getAttributeValueBytes("thumbnailPhoto");
						if(picture!= null) {
						picid.add(new PicId(DatatypeConverter.printBase64Binary(picture), searchResult.getSearchEntries().get(i).getAttributeValue("distinguishedName")));	
						}
						else picid.add(new PicId(null,searchResult.getSearchEntries().get(i).getAttributeValue("distinguishedName")));
			 }
			 return picid;
			
		}
	
	private String getFilterListIds(String id[]) {
		String result = "(|";
		for(int i = 0; i < id.length; i++) {
			result = result + "(distinguishedName=" + id[i]+ ")";
		}
		result = result + ")";
		return result;
	}
	
	

}
	
