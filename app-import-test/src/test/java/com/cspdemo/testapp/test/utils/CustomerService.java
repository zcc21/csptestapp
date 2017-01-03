package com.cspdemo.testapp.test.utils;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chanjet.csp.platform.test.HttpResponse;
import com.chanjet.csp.platform.test.RestfulIT;

public class CustomerService extends RestfulIT {
	private static final int BAD_REQUEST = 400;
	private static final int FORBIDDEN = 403;

	public JSONObject addNewBy(String userTag, String name, Long employeeNumber) throws Exception {
		setIsCspServletURL(false);
		
		Map<String, Object> customerMap = new HashMap<String, Object>();
		customerMap.put("name", name);
		customerMap.put("employeeNumber", employeeNumber);		
		
		// POST - http://localhost:8080/cspdemo/testapp/restlet/v2_1/{restletName} 
		HttpResponse response = this.doPost(userTag, "/restlet/v2_1/Customer", customerMap);
		
		if (BAD_REQUEST == response.getStatusCode()) {
			throw new RuntimeException(response.getString());
		}
		
		String customerString = response.getString();
		JSONObject customer = JSON.parseObject(customerString);
		return customer;
	}
	
	public JSONObject addNewBy(String userTag, String name) throws Exception {
		return addNewBy(userTag, name, 0L);
	}

	public JSONObject updateBy(String userTag, Long customerId, Map<String, Object> updatedCustomerMap) throws Exception {
		setIsCspServletURL(false);
	
		// PUT - http://localhost:8080/cspdemo/testapp/restlet/v2_1/{restletName}/{ID}
		HttpResponse response = this.doPut(userTag, "/restlet/v2_1/Customer/" + customerId, updatedCustomerMap);
		if (BAD_REQUEST == response.getStatusCode()) {
			throw new RuntimeException(response.getString());
		}
		
		String updatedCustomerString = response.getString();
	    JSONObject updatedCustomer = JSON.parseObject(updatedCustomerString);
	    return updatedCustomer;
	}

	public JSONObject getBy(String userTag, Long customerId) throws Exception {
		setIsCspServletURL(false);
		
		// GET - http://localhost:8080/cspdemo/testapp/restlet/v2_1/{restletName}/{ID}
		HttpResponse response = this.doGet(userTag, "/restlet/v2_1/Customer/" + customerId, null);
		if (BAD_REQUEST == response.getStatusCode()) {
			throw new RuntimeException(response.getString());
		}
		
		String customerString = response.getString();
	    JSONObject customer = JSON.parseObject(customerString);
	    return customer;
	}

	public void deleteBy(String userTag, Long barId) throws Exception {
		setIsCspServletURL(false);		
		
		// DELETE - http://localhost:8080/cspdemo/testapp/restlet/v2_1/{restletName}/{ID}
		HttpResponse response = this.doDelete(userTag, "/restlet/v2_1/Customer/" + barId, null);
		
		int statusCode = response.getStatusCode();
		if (BAD_REQUEST == statusCode || FORBIDDEN == statusCode) {
			throw new RuntimeException(response.getString());
		}				
	}
}
