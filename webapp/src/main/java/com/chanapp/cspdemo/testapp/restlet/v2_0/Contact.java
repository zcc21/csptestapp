package com.chanapp.cspdemo.testapp.restlet.v2_0;

import java.util.LinkedHashMap;
import java.util.Map;

import com.chanjet.csp.appmanager.AppWorkManager;
import com.chanjet.csp.bo.api.BoSession;
import com.chanjet.csp.bo.api.BoTransactionManager;
import com.chanjet.csp.bo.api.IBusinessObjectHome;
import com.chanjet.csp.bo.api.IBusinessObjectManager;
import com.chanjet.csp.bo.api.IBusinessObjectRow;
import com.chanjet.csp.common.base.exception.AppException;
import com.chanjet.csp.common.base.util.TransactionTracker;
import com.chanjet.csp.data.api.DataManager;
import com.chanjet.csp.rest.restlet.Restlet;

public class Contact extends Restlet {
	@Override
	public Object doDeleteId(Map<String, String[]> queryParameters, String payload, Long id) {
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
		try {
			// open transaction
			TransactionTracker transactionTrack = transactionManager.beginTransaction(boSession);
			
			// delete contact
			IBusinessObjectManager boManager = AppWorkManager.getBusinessObjectManager();
			IBusinessObjectHome boHome = boManager.getPrimaryBusinessObjectHome("Contact");		
			boHome.delete(id);
			
			// commit
			transactionManager.commitTransaction(boSession, transactionTrack);
		} catch (Exception e) {
			// roll back
			transactionManager.rollbackTransaction(boSession);
			throw new AppException(e.getMessage());
		}		

		return id;
	}

	@Override
	public Object doGetId(Map<String, String[]> queryParameters, Long id) {
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
		IBusinessObjectRow contactRow = null;
		try {
			// open transaction
			TransactionTracker transactionTrack = transactionManager.beginTransaction(boSession);
			
			// query contact
			IBusinessObjectManager boManager = AppWorkManager.getBusinessObjectManager();
			IBusinessObjectHome boHome = boManager.getPrimaryBusinessObjectHome("Contact");		
			contactRow = boHome.query(id);		
			
			// commit
			transactionManager.commitTransaction(boSession, transactionTrack);
		} catch (Exception e) {
			// roll back
			transactionManager.rollbackTransaction(boSession);
			throw new AppException(e.getMessage());
		}		
		
		return contactRow;
	}

	@Override
	public Object doPost(Map<String, String[]> queryParameters, String contactJsonString) {		
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
		IBusinessObjectRow contactRow = null;
		try {
			// open transaction
			TransactionTracker transactionTrack = transactionManager.beginTransaction(boSession);
			
			// add new contact
			IBusinessObjectManager boManager = AppWorkManager.getBusinessObjectManager();
			IBusinessObjectHome boHome = boManager.getPrimaryBusinessObjectHome("Contact");			
			DataManager dataManager = AppWorkManager.getDataManager();
			LinkedHashMap<String, Object> contactMap = dataManager.fromJSONString(contactJsonString, LinkedHashMap.class);
			contactRow = boHome.constructBORowForInsert(boSession, contactMap);
			boHome.upsert(contactRow);
			
			// commit
			transactionManager.commitTransaction(boSession, transactionTrack);
		} catch (Exception e) {
			// roll back
			transactionManager.rollbackTransaction(boSession);
			throw new AppException(e.getMessage());
		}		
		
		return contactRow;
	}

	@Override
	public Object doPutId(Map<String, String[]> queryParameters, String payload, Long id) {
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
		IBusinessObjectRow updatedContactRow = null;
		try {
			// open transaction
			TransactionTracker transactionTrack = transactionManager.beginTransaction(boSession);
			
			// update contact
			IBusinessObjectManager boManager = AppWorkManager.getBusinessObjectManager();
			IBusinessObjectHome boHome = boManager.getPrimaryBusinessObjectHome("Contact");			
			DataManager dataManager = AppWorkManager.getDataManager();
			LinkedHashMap<String, Object> updatedContactMap = dataManager.fromJSONString(payload, LinkedHashMap.class);
			updatedContactRow = boHome.constructBORowForUpdate(boSession, id, updatedContactMap);			
			boHome.upsert(updatedContactRow);
			
			// commit
			transactionManager.commitTransaction(boSession, transactionTrack);
		} catch (Exception e) {
			// roll back
			transactionManager.rollbackTransaction(boSession);
			throw new AppException(e.getMessage());
		}		
		
		return updatedContactRow;
	}
}
