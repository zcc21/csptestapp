package com.chanapp.cspdemo.testapp.handler;

import java.util.LinkedHashMap;

import com.chanapp.cspdemo.testapp.businessobject.api.contact.IContactRowSet;
import com.chanapp.cspdemo.testapp.service.ContactService;
import com.chanapp.cspdemo.testapp.service.ContactServiceImpl;
import com.chanjet.csp.appmanager.AppWorkManager;
import com.chanjet.csp.bo.api.BoSession;
import com.chanjet.csp.bo.api.BoTransactionManager;
import com.chanjet.csp.common.base.rest.RestAppContext;
import com.chanjet.csp.common.base.util.TransactionTracker;
import com.chanjet.csp.enterprise.ext.AppContextHandler;

public class CustomAppContextHandler implements AppContextHandler {

	private static final String DEFAULT_CONTATC_NAME = "FOO";

	@Override
	public boolean startup(RestAppContext c) {	
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
		try {
			// open transaction
			TransactionTracker transactionTrack = transactionManager.beginTransaction(boSession);
			
			// add the default contacts			
			ContactService contactService = new ContactServiceImpl();
			IContactRowSet defaultContacts = contactService.getByName(DEFAULT_CONTATC_NAME);
			if (defaultContacts == null || defaultContacts.getRows().isEmpty()) {
				LinkedHashMap<String, Object> contactMap = new LinkedHashMap<>();
				contactMap.put("name", DEFAULT_CONTATC_NAME);
				contactService.addNew(contactMap);			
			}			
			
			// commit
			transactionManager.commitTransaction(boSession, transactionTrack);
		} catch (Exception e) {
			// roll back
			transactionManager.rollbackTransaction(boSession);
			return false;
		}
		
		System.out.println("The app has started up.");
		return true;
	}

	@Override
	public void shutdown(RestAppContext c) {
		/*BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
		try {
			// open transaction
			TransactionTracker transactionTrack = transactionManager.beginTransaction(boSession);
			
			// delete the retail customers
			CustomerService customerService = new CustomerServiceImpl();
			ICustomerRowSet retailCustomers = customerService.getByName(RETAIL_CUSTOMER);
			for (ICustomerRow aRetailer : retailCustomers.getCustomerRows()) {
				customerService.deleteById(aRetailer.getId());
			}			
			
			// commit
			transactionManager.commitTransaction(boSession, transactionTrack);
		} catch (Exception e) {
			// roll back
			transactionManager.rollbackTransaction(boSession);
			throw new RuntimeException(e);
		}*/
		
	}

	public CustomAppContextHandler() {
		super();
		// TODO Auto-generated constructor stub
	}
}
