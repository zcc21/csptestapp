package com.chanapp.cspdemo.testapp.service;

import java.util.LinkedHashMap;
import java.util.Map;

import com.chanapp.cspdemo.testapp.businessobject.api.customer.ICustomerRow;
import com.chanapp.cspdemo.testapp.businessobject.api.customer.ICustomerRowSet;

public interface CustomerService {

	public void deleteById(Long id);

	public ICustomerRow getById(Long id);

	public ICustomerRowSet getByName(String name);

	public ICustomerRow addNew(String name);
	
	public ICustomerRow addNew(LinkedHashMap<String, Object> customerMap);

	public ICustomerRow update(Long id, LinkedHashMap<String, Object> updatedCustomerMap);

	public Map<String, Object> addNewWithContact(Map<String, Object> customerWithContactMap);

	public boolean isBigCustomer(Long id);
}