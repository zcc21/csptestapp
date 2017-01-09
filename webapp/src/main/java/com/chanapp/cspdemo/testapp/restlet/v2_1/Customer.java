package com.chanapp.cspdemo.testapp.restlet.v2_1;

import java.util.LinkedHashMap;
import java.util.Map;

import com.chanapp.cspdemo.testapp.businessobject.api.customer.ICustomerRow;
import com.chanapp.cspdemo.testapp.service.CustomerService;
import com.chanapp.cspdemo.testapp.service.CustomerServiceImpl;
import com.chanjet.csp.bo.api.IBusinessObjectRow;
import com.chanjet.csp.rest.restlet.Restlet;

public class Customer extends Restlet {
	private CustomerService customerService = new CustomerServiceImpl();
	
	@Override
	public Object doDeleteId(Map<String, String[]> queryParameters, String payload, Long id) {
		customerService.deleteById(id);
		return id;
	}

	@Override
	public Object doGetId(Map<String, String[]> queryParameters, Long id) {
		// query the Customer
		ICustomerRow customerRow = customerService.getById(id);		
		return customerRow;
	}

	@Override
	public Object doPost(Map<String, String[]> queryParameters, String customerJsonString) {		
		// add new Customer
		LinkedHashMap<String, Object> customerMap = (LinkedHashMap<String, Object>) dataManager.jsonStringToMap(customerJsonString);
		IBusinessObjectRow customerRow = customerService.addNew(customerMap);		
		return customerRow;
	}

	@Override
	public Object doPutId(Map<String, String[]> queryParameters, String payload, Long id) {
		// update the Customer
		LinkedHashMap<String, Object> updatedCustomerMap = (LinkedHashMap<String, Object>) dataManager.jsonStringToMap(payload);		
		ICustomerRow updatedCustomerRow = customerService.update(id, updatedCustomerMap);		
		return updatedCustomerRow;
	}

	public Object doPostWithContact(Map<String, String[]> queryParameters, String customerWithContactJsonString) {
		// add new Customer with Contact
		Map<String, Object> customerWithContactMap = dataManager.jsonStringToMap(customerWithContactJsonString);
		Map<String, Object> result = customerService.addNewWithContact(customerWithContactMap);
		return result;
	}
}
