package com.cspdemo.testapp.test.utils;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chanjet.csp.platform.test.HttpResponse;
import com.chanjet.csp.platform.test.RestfulIT;

public class ContactService extends RestfulIT {
	private static final int BAD_REQUEST = 400;
	private static final int FORBIDDEN = 403;

	public JSONObject addNewBy(String userTag, String name) throws Exception {
		setIsCspServletURL(false);
		
		Map<String, Object> contactMap = new HashMap<String, Object>();
		contactMap.put("name", name);	
		
		// POST - http://localhost:8080/cspdemo/testapp/restlet/v2_1/{restletName} 
		HttpResponse response = this.doPost(userTag, "/restlet/v2_1/Contact", contactMap);
		
		if (BAD_REQUEST == response.getStatusCode()) {
			throw new RuntimeException(response.getString());
		}
		
		String contactString = response.getString();
		JSONObject contact = JSON.parseObject(contactString);
		return contact;
	}


	public JSONObject updateBy(String userTag, Long contactId, Map<String, Object> updatedContactMap) throws Exception {
		setIsCspServletURL(false);
	
		// PUT - http://localhost:8080/cspdemo/testapp/restlet/v2_1/{restletName}/{ID}
		HttpResponse response = this.doPut(userTag, "/restlet/v2_1/Contact/" + contactId, updatedContactMap);
		if (BAD_REQUEST == response.getStatusCode()) {
			throw new RuntimeException(response.getString());
		}
		
		String updatedContactString = response.getString();
	    JSONObject updatedContact = JSON.parseObject(updatedContactString);
	    return updatedContact;
	}

	public JSONObject getBy(String userTag, Long contactId) throws Exception {
		setIsCspServletURL(false);
		
		// GET - http://localhost:8080/cspdemo/testapp/restlet/v2_1/{restletName}/{ID}
		HttpResponse response = this.doGet(userTag, "/restlet/v2_1/Contact/" + contactId, null);
		if (BAD_REQUEST == response.getStatusCode()) {
			throw new RuntimeException(response.getString());
		}
		
		String contactString = response.getString();
	    JSONObject contact = JSON.parseObject(contactString);
	    return contact;
	}
	
	public JSONArray getBy(String userTag, String name) throws Exception {
		setIsCspServletURL(false);

		// GET - http://localhost:8080/cspdemo/testapp/restlet/v2_1/{restletName}
		HttpResponse response = this.doGet(userTag, "/restlet/v2_1/Contact?name=" + name, null);
		if (BAD_REQUEST == response.getStatusCode()) {
			throw new RuntimeException(response.getString());
		}

		String contactString = response.getString();
		JSONArray contacts = JSON.parseArray(contactString);
		return contacts;
	}

	public void deleteBy(String userTag, Long contactId) throws Exception {
		setIsCspServletURL(false);		
		
		// DELETE - http://localhost:8080/cspdemo/testapp/restlet/v2_1/{restletName}/{ID}
		HttpResponse response = this.doDelete(userTag, "/restlet/v2_1/Contact/" + contactId, null);
		
		int statusCode = response.getStatusCode();
		if (BAD_REQUEST == statusCode || FORBIDDEN == statusCode) {
			throw new RuntimeException(response.getString());
		}				
	}
}
