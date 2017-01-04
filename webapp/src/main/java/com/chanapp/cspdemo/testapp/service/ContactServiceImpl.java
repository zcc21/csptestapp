package com.chanapp.cspdemo.testapp.service;

import java.util.LinkedHashMap;

import com.chanapp.cspdemo.testapp.businessobject.api.contact.IContactRow;
import com.chanapp.cspdemo.testapp.businessobject.api.contact.IContactRowSet;
import com.chanjet.csp.appmanager.AppWorkManager;
import com.chanjet.csp.bo.api.BoSession;
import com.chanjet.csp.bo.api.BoTransactionManager;
import com.chanjet.csp.bo.api.IBusinessObjectHome;
import com.chanjet.csp.bo.api.IBusinessObjectManager;
import com.chanjet.csp.common.base.util.TransactionTracker;
import com.chanjet.csp.ui.util.Criteria;
import com.chanjet.csp.ui.util.JsonQueryBuilder;

public class ContactServiceImpl implements ContactService {

	@Override
	public void deleteById(Long id) {
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
			throw new RuntimeException(e);
		}
	}


	@Override
	public IContactRow getById(Long id) {
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
		IContactRow contactRow = null;
		try {
			// open transaction
			TransactionTracker transactionTrack = transactionManager.beginTransaction(boSession);
			
			// query contact
			IBusinessObjectManager boManager = AppWorkManager.getBusinessObjectManager();
			IBusinessObjectHome boHome = boManager.getPrimaryBusinessObjectHome("Contact");		
			contactRow = (IContactRow) boHome.query(id);		
			
			// commit
			transactionManager.commitTransaction(boSession, transactionTrack);
		} catch (Exception e) {
			// roll back
			transactionManager.rollbackTransaction(boSession);
			throw new RuntimeException(e);
		}
		return contactRow;
	}

	@Override
	public IContactRowSet getByName(String name) {
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
		IContactRowSet contactRows = null;
		try {
			// open transaction
			TransactionTracker transactionTrack = transactionManager.beginTransaction(boSession);
			
			// query contact by name
			Criteria criteria = Criteria.AND();
			criteria.eq("name", name);
			
			JsonQueryBuilder jsonQueryBuilder = JsonQueryBuilder.getInstance();		       
			jsonQueryBuilder.addCriteria(criteria);
			
			IBusinessObjectManager boManager = AppWorkManager.getBusinessObjectManager();
			IBusinessObjectHome boHome = boManager.getPrimaryBusinessObjectHome("Contact");		
			contactRows = (IContactRowSet) boHome.query(jsonQueryBuilder.toJsonQuerySpec());		
			
			// commit
			transactionManager.commitTransaction(boSession, transactionTrack);
		} catch (Exception e) {
			// roll back
			transactionManager.rollbackTransaction(boSession);
			throw new RuntimeException(e);
		}
		return contactRows;
	}


	@Override
	public IContactRow addNew(LinkedHashMap<String, Object> contactMap) {
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
		IContactRow contactRow = null;
		try {
			// open transaction
			TransactionTracker transactionTrack = transactionManager.beginTransaction(boSession);
			
			// add new contact
			IBusinessObjectManager boManager = AppWorkManager.getBusinessObjectManager();
			IBusinessObjectHome boHome = boManager.getPrimaryBusinessObjectHome("Contact");			
			contactRow = (IContactRow) boHome.constructBORowForInsert(boSession, contactMap);
			boHome.upsert(contactRow);
			
			// commit
			transactionManager.commitTransaction(boSession, transactionTrack);
		} catch (Exception e) {
			// roll back
			transactionManager.rollbackTransaction(boSession);
			throw new RuntimeException(e);
		}
		return contactRow;
	}

	@Override
	public IContactRow update(Long id, LinkedHashMap<String, Object> updatedContactMap) {
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
		IContactRow updatedContactRow = null;
		try {
			// open transaction
			TransactionTracker transactionTrack = transactionManager.beginTransaction(boSession);
			
			// update contact
			IBusinessObjectManager boManager = AppWorkManager.getBusinessObjectManager();
			IBusinessObjectHome boHome = boManager.getPrimaryBusinessObjectHome("Contact");
			updatedContactRow = (IContactRow) boHome.constructBORowForUpdate(boSession, id, updatedContactMap);			
			boHome.upsert(updatedContactRow);
			
			// commit
			transactionManager.commitTransaction(boSession, transactionTrack);
		} catch (Exception e) {
			// roll back
			transactionManager.rollbackTransaction(boSession);
			throw new RuntimeException(e);
		}
		return updatedContactRow;
	}

}
