package com.chanapp.cspdemo.testapp.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.chanapp.cspdemo.testapp.businessobject.api.contact.IContactRow;
import com.chanapp.cspdemo.testapp.businessobject.api.customer.ICustomerRow;
import com.chanjet.csp.appmanager.AppWorkManager;
import com.chanjet.csp.bo.api.BoSession;
import com.chanjet.csp.bo.api.BoTransactionManager;
import com.chanjet.csp.bo.api.IBusinessObjectHome;
import com.chanjet.csp.bo.api.IBusinessObjectManager;
import com.chanjet.csp.common.base.util.TransactionTracker;

public class CustomerServiceImpl implements CustomerService {
	private static final Long MIN_EMPLOYEE_NUMBER_OF_BIG_CUSTOMER = 1000L;
	private ContactService contactService = new ContactServiceImpl();

	@Override
	public void deleteById(Long id) {
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
		try {
			// open transaction
			TransactionTracker transactionTrack = transactionManager.beginTransaction(boSession);
			
			// delete customer
			IBusinessObjectManager boManager = AppWorkManager.getBusinessObjectManager();
			IBusinessObjectHome boHome = boManager.getPrimaryBusinessObjectHome("Customer");		
			boHome.delete(id);
			
			// commit
			transactionManager.commitTransaction(boSession, transactionTrack);
		} catch (Exception e) {
			// roll back
			transactionManager.rollbackTransaction(boSession);
			throw new RuntimeException(e);
		}
	}

	@Override
	public ICustomerRow getById(Long id) {
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
		ICustomerRow customerRow = null;
		try {
			// open transaction
			TransactionTracker transactionTrack = transactionManager.beginTransaction(boSession);
			
			// query customer
			IBusinessObjectManager boManager = AppWorkManager.getBusinessObjectManager();
			IBusinessObjectHome boHome = boManager.getPrimaryBusinessObjectHome("Customer");		
			customerRow = (ICustomerRow) boHome.query(id);		
			
			// commit
			transactionManager.commitTransaction(boSession, transactionTrack);
		} catch (Exception e) {
			// roll back
			transactionManager.rollbackTransaction(boSession);
			throw new RuntimeException(e);
		}
		return customerRow;
	}

	@Override
	public ICustomerRow addNew(String name) {
		LinkedHashMap<String, Object> customerMap = new LinkedHashMap<>();
		customerMap.put("name", name);
		
		return addNew(customerMap);
	}

	@Override
	public ICustomerRow addNew(LinkedHashMap<String, Object> customerMap) {
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
		ICustomerRow customerRow = null;
		try {
			// open transaction
			TransactionTracker transactionTrack = transactionManager.beginTransaction(boSession);
			
			// add new customer
			IBusinessObjectManager boManager = AppWorkManager.getBusinessObjectManager();
			IBusinessObjectHome boHome = boManager.getPrimaryBusinessObjectHome("Customer");			
			customerRow = (ICustomerRow) boHome.constructBORowForInsert(boSession, customerMap);
			boHome.upsert(customerRow);
			
			// commit
			transactionManager.commitTransaction(boSession, transactionTrack);
		} catch (Exception e) {
			// roll back
			transactionManager.rollbackTransaction(boSession);
			throw new RuntimeException(e);
		}
		return customerRow;
	}

	@Override
	public ICustomerRow update(Long id, LinkedHashMap<String, Object> updatedCustomerMap) {
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
		ICustomerRow updatedCustomerRow = null;
		try {
			// open transaction
			TransactionTracker transactionTrack = transactionManager.beginTransaction(boSession);
			
			// update customer
			IBusinessObjectManager boManager = AppWorkManager.getBusinessObjectManager();
			IBusinessObjectHome boHome = boManager.getPrimaryBusinessObjectHome("Customer");
			updatedCustomerRow = (ICustomerRow) boHome.constructBORowForUpdate(boSession, id, updatedCustomerMap);			
			boHome.upsert(updatedCustomerRow);
			
			// commit
			transactionManager.commitTransaction(boSession, transactionTrack);
		} catch (Exception e) {
			// roll back
			transactionManager.rollbackTransaction(boSession);
			throw new RuntimeException(e);
		}
		return updatedCustomerRow;
	}

	@Override
	public Map<String, Object> addNewWithContact(Map<String, Object> customerWithContactMap) {
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
		ICustomerRow customerRow = null;
		IContactRow contactRow = null;
		try {
			// open transaction
			TransactionTracker transactionTrack = transactionManager.beginTransaction(boSession);
			
			// add new Customer
			LinkedHashMap<String, Object> customerMap = (LinkedHashMap<String, Object>) customerWithContactMap.get("customer");
			customerRow = (ICustomerRow) addNew(customerMap);
			
			// add new Contact
			LinkedHashMap<String, Object> contactMap = (LinkedHashMap<String, Object>) customerWithContactMap.get("contact");
			LinkedHashMap<String, Object> customerIdMap = new LinkedHashMap<>();
			customerIdMap.put("id", customerRow.getId());
			contactMap.put("customer", customerIdMap);
			contactRow = contactService .addNew(contactMap);
			
			// commit
			transactionManager.commitTransaction(boSession, transactionTrack);
		} catch (Exception e) {
			// roll back
			transactionManager.rollbackTransaction(boSession);
			throw new RuntimeException(e);
		}	
		
		// 
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("customer", customerRow);
		result.put("contact", contactRow);
		return result;
	}

	@Override
	public boolean isBigCustomer(Long id) {
		ICustomerRow customer = getById(id);
		return (customer.getEmployeeNumber() >= MIN_EMPLOYEE_NUMBER_OF_BIG_CUSTOMER); 
	}
}
