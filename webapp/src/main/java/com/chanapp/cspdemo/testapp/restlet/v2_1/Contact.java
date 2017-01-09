package com.chanapp.cspdemo.testapp.restlet.v2_1;

import java.util.LinkedHashMap;
import java.util.Map;

import com.chanapp.cspdemo.testapp.businessobject.api.contact.IContactRow;
import com.chanapp.cspdemo.testapp.service.ContactService;
import com.chanapp.cspdemo.testapp.service.ContactServiceImpl;
import com.chanjet.csp.rest.restlet.Restlet;

public class Contact extends Restlet {
	private ContactService contactService = new ContactServiceImpl();
	
	@Override
	public Object doDeleteId(Map<String, String[]> queryParameters, String payload, Long id) {
		contactService.deleteById(id);
		return id;
	}

	@Override
	public Object doGetId(Map<String, String[]> queryParameters, Long id) {
		IContactRow contactRow = contactService.getById(id);		
		return contactRow;
	}
	
	@Override 
	public Object doGet(Map<String, String[]> queryParameters) {	
		String name = queryParameters.get("name")[0];
		return contactService.getByName(name);		
	}

	@Override
	public Object doPost(Map<String, String[]> queryParameters, String contactJsonString) {		
		LinkedHashMap<String, Object> contactMap = (LinkedHashMap<String, Object>) dataManager.jsonStringToMap(contactJsonString);
		IContactRow contactRow = contactService.addNew(contactMap);		
		return contactRow;
	}

	@Override
	public Object doPutId(Map<String, String[]> queryParameters, String payload, Long id) {
		LinkedHashMap<String, Object> updatedContactMap = (LinkedHashMap<String, Object>) dataManager.jsonStringToMap(payload);		
		IContactRow updatedContactRow = contactService.update(id, updatedContactMap);		
		return updatedContactRow;
	}
	
}

