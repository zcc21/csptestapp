package com.chanapp.cspdemo.testapp.handler;

import java.sql.Timestamp;

import com.chanjet.csp.appmanager.AppWorkManager;
import com.chanjet.csp.bo.api.BoSession;
import com.chanjet.csp.bo.api.BoTransactionManager;
import com.chanjet.csp.bo.api.IBusinessObjectHome;
import com.chanjet.csp.bo.api.IBusinessObjectManager;
import com.chanjet.csp.bo.api.IBusinessObjectRow;
import com.chanjet.csp.ccs.api.cia.UserInfo;
import com.chanjet.csp.common.base.rest.RestRequest;
import com.chanjet.csp.common.base.rest.RestResponse;
import com.chanjet.csp.common.base.util.TransactionTracker;
import com.chanjet.csp.enterprise.ext.AppUserLoginSessionInitializer;

public class CustomAppUserLoginSessionInitializer implements AppUserLoginSessionInitializer {

	@Override
	public void initUserSession(Long userId, RestRequest request, RestResponse response) {
		IBusinessObjectManager boManager = AppWorkManager.getBusinessObjectManager();
		IBusinessObjectHome userLogHome = boManager.getPrimaryBusinessObjectHome("UserLog");

		BoSession session = AppWorkManager.getBoDataAccessManager().getBoSession();
		BoTransactionManager tranxManager = AppWorkManager.getBoTransactionManager();
		TransactionTracker tracker = tranxManager.beginTransaction(session);
		try {
			IBusinessObjectRow userLog = userLogHome.createRow(session);
			userLog.setFieldValue(session, "userId", userId);
			userLog.setFieldValue(session, "loginTime", new Timestamp(System.currentTimeMillis()));	            
			userLog.setFieldValue(session, "ip", request.getRemoteAddr());

			userLogHome.upsert(session, userLog);
			tranxManager.commitTransaction(session, tracker);
		} catch (Exception e) {
			if (session != null) {
				tranxManager.rollbackTransaction(session);
			}
		}	
	}
}
