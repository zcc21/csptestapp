package com.chanapp.cspdemo.testapp.service;

import java.util.LinkedHashMap;

import com.chanapp.cspdemo.testapp.businessobject.api.restletflow.IRestletFlowRow;
import com.chanapp.cspdemo.testapp.businessobject.api.restletflow.IRestletFlowRowSet;
import com.chanjet.csp.appmanager.AppWorkManager;
import com.chanjet.csp.bo.api.BoSession;
import com.chanjet.csp.bo.api.BoTransactionManager;
import com.chanjet.csp.bo.api.IBusinessObjectHome;
import com.chanjet.csp.bo.api.IBusinessObjectManager;
import com.chanjet.csp.common.base.util.TransactionTracker;
import com.chanjet.csp.ui.util.Criteria;
import com.chanjet.csp.ui.util.JsonQueryBuilder;

public class RestletFlowServiceImpl implements RestletFlowService {

	@Override
	public void addOneCallOf(String restletName, String method) {
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
		try {
			// open transaction
			TransactionTracker transactionTrack = transactionManager.beginTransaction(boSession);
			
			doAddOneCallOf(restletName, method);			
			
			// commit
			transactionManager.commitTransaction(boSession, transactionTrack);
		} catch (Exception e) {
			// roll back
			transactionManager.rollbackTransaction(boSession);
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public IRestletFlowRow getOf(String restletName, String method) {
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
		IRestletFlowRow flowRow = null;
		try {
			// open transaction
			TransactionTracker transactionTrack = transactionManager.beginTransaction(boSession);
			
			// query flow
			Criteria criteria = Criteria.AND();
			criteria.eq("name", restletName);
			criteria.eq("method", method);

			JsonQueryBuilder jsonQueryBuilder = JsonQueryBuilder.getInstance();		       
			jsonQueryBuilder.addCriteria(criteria);			 
			
			IBusinessObjectManager boManager = AppWorkManager.getBusinessObjectManager();
			IBusinessObjectHome boHome = boManager.getPrimaryBusinessObjectHome("RestletFlow");		
			IRestletFlowRowSet flowRows = (IRestletFlowRowSet) boHome.query(jsonQueryBuilder.toJsonQuerySpec());		
			if (flowRows != null && !flowRows.getRows().isEmpty()) {
				flowRow = flowRows.getRow(0);
			}
			
			// commit
			transactionManager.commitTransaction(boSession, transactionTrack);
		} catch (Exception e) {
			// roll back
			transactionManager.rollbackTransaction(boSession);
			throw new RuntimeException(e);
		}
		
		return flowRow;
	}


	private IRestletFlowRow doAddNew(String restletName, String method) {
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		IBusinessObjectManager boManager = AppWorkManager.getBusinessObjectManager();
		IBusinessObjectHome boHome = boManager.getPrimaryBusinessObjectHome("RestletFlow");	

		// add new restlet flow
		LinkedHashMap<String, Object> flowMap = new LinkedHashMap<>();
		flowMap.put("name", restletName);
		flowMap.put("method", method);
		flowMap.put("callCount", 1);
		IRestletFlowRow flowRow = (IRestletFlowRow) boHome.constructBORowForInsert(boSession, flowMap);
		boHome.upsert(flowRow);
		
		return flowRow;		
	}

	private void doAddOneCallOf(String restletName, String method) {
		IRestletFlowRow flow = getOf(restletName, method);
		if (flow == null) {				
			doAddNew(restletName, method);
		} else {						
			doAddOneCallOf(flow);
		}
	}

	private IRestletFlowRow doAddOneCallOf(IRestletFlowRow flow) {		
		BoSession boSession = AppWorkManager.getBoDataAccessManager().getBoSession();
		IBusinessObjectManager boManager = AppWorkManager.getBusinessObjectManager();
		IBusinessObjectHome boHome = boManager.getPrimaryBusinessObjectHome("RestletFlow");
		
		// call count add one
		Long newCallCount = flow.getCallCount() + 1;
		LinkedHashMap<String, Object> updatedFlowMap = new LinkedHashMap<>();		
		updatedFlowMap.put("callCount", newCallCount);
		
		Long flowId = flow.getId();
		
		IRestletFlowRow flowRow = (IRestletFlowRow) boHome.constructBORowForUpdate(boSession, flowId, updatedFlowMap);
		boHome.upsert(flowRow);
		return flowRow;
	}

	@Override
	public Long getCallCountOf(String restletName, String method) {
		IRestletFlowRow flow = getOf(restletName, method);
		if (flow == null) {
			return 0L;
		} else {
			return flow.getCallCount();
		}
		
	}

}
