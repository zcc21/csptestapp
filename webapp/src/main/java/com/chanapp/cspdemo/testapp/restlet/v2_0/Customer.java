package com.chanapp.cspdemo.testapp.restlet.v2_0;

import java.util.LinkedHashMap;
import java.util.Map;

import com.chanjet.csp.appmanager.AppWorkManager;
import com.chanjet.csp.bo.api.BoSession;
import com.chanjet.csp.bo.api.BoTransactionManager;
import com.chanjet.csp.bo.api.IBusinessObjectHome;
import com.chanjet.csp.bo.api.IBusinessObjectManager;
import com.chanjet.csp.bo.api.IBusinessObjectRow;
import com.chanjet.csp.common.base.json.JSONObject;
import com.chanjet.csp.common.base.util.TransactionTracker;
import com.chanjet.csp.data.api.DataManager;
import com.chanjet.csp.rest.restlet.Restlet;

public class Customer extends Restlet {
	@Override
	public Object doDeleteId(Map<String, String[]> queryParameters, String payload, Long id) {
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
		IBusinessObjectRow customerRow = null;
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
		}		

		return null;
	}

	@Override
	public Object doGetId(Map<String, String[]> queryParameters, Long id) {
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
		IBusinessObjectRow customerRow = null;
		try {
			// open transaction
			TransactionTracker transactionTrack = transactionManager.beginTransaction(boSession);
			
			// query customer
			IBusinessObjectManager boManager = AppWorkManager.getBusinessObjectManager();
			IBusinessObjectHome boHome = boManager.getPrimaryBusinessObjectHome("Customer");		
			customerRow = boHome.query(id);		
			
			// commit
			transactionManager.commitTransaction(boSession, transactionTrack);
		} catch (Exception e) {
			// roll back
			transactionManager.rollbackTransaction(boSession);
		}		
		
		return customerRow;
	}

	@Override
	public Object doPost(Map<String, String[]> queryParameters, String customerJsonString) {		
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
		IBusinessObjectRow customerRow = null;
		try {
			// open transaction
			TransactionTracker transactionTrack = transactionManager.beginTransaction(boSession);
			
			// add new customer
			IBusinessObjectManager boManager = AppWorkManager.getBusinessObjectManager();
			IBusinessObjectHome boHome = boManager.getPrimaryBusinessObjectHome("Customer");			
			DataManager dataManager = AppWorkManager.getDataManager();
			LinkedHashMap<String, Object> customerMap = dataManager.fromJSONString(customerJsonString, LinkedHashMap.class);
			customerRow = boHome.constructBORowForInsert(boSession, customerMap);
			boHome.upsert(customerRow);
			
			// commit
			transactionManager.commitTransaction(boSession, transactionTrack);
		} catch (Exception e) {
			// roll back
			transactionManager.rollbackTransaction(boSession);
		}		
		
		return customerRow;
	}

	@Override
	public Object doPutId(Map<String, String[]> queryParameters, String payload, Long id) {
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
		IBusinessObjectRow updatedCustomerRow = null;
		try {
			// open transaction
			TransactionTracker transactionTrack = transactionManager.beginTransaction(boSession);
			
			// update customer
			IBusinessObjectManager boManager = AppWorkManager.getBusinessObjectManager();
			IBusinessObjectHome boHome = boManager.getPrimaryBusinessObjectHome("Customer");			
			DataManager dataManager = AppWorkManager.getDataManager();
			LinkedHashMap<String, Object> updatedCustomerMap = dataManager.fromJSONString(payload, LinkedHashMap.class);
			updatedCustomerRow = boHome.constructBORowForUpdate(boSession, id, updatedCustomerMap);			
			boHome.upsert(updatedCustomerRow);
			
			// commit
			transactionManager.commitTransaction(boSession, transactionTrack);
		} catch (Exception e) {
			// roll back
			transactionManager.rollbackTransaction(boSession);
		}		
		
		return updatedCustomerRow;
	}
}
