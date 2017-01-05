package com.chanapp.cspdemo.testapp.handler;

import com.chanapp.cspdemo.testapp.service.RestletFlowService;
import com.chanapp.cspdemo.testapp.service.RestletFlowServiceImpl;
import com.chanjet.csp.appmanager.AppWorkManager;
import com.chanjet.csp.bo.api.BoSession;
import com.chanjet.csp.bo.api.BoTransactionManager;
import com.chanjet.csp.common.base.exception.AppException;
import com.chanjet.csp.common.base.util.TransactionTracker;
import com.chanjet.csp.rest.restlet.RestletFilter;
import com.chanjet.csp.rest.restlet.RestletRequestContext;
import com.chanjet.csp.rest.restlet.RestletUtils;

public class CustomRestletFilter implements RestletFilter {

	@Override
	public Object filter(RestletRequestContext requestContext) {
		String restlet = requestContext.getRestletClassName();
		String doMethod = requestContext.getClassMethodName();
		// remove the prifix "do"
		String method = doMethod.substring(2, doMethod.length()).toUpperCase();
		
		Object result = null;
		// 
		BoSession session = AppWorkManager.getBoDataAccessManager().getBoSession();
        BoTransactionManager transactionManager = AppWorkManager.getBoTransactionManager();
        TransactionTracker transactionTracker = null;
        try {
            transactionTracker = transactionManager.beginTransaction(session);
            
            // restlet flow statistics
            RestletFlowService flowService = new RestletFlowServiceImpl();
            flowService.addOneCallOf(restlet, method);
		
            // call the restlet
            result = RestletUtils.invokeRestletMethod(requestContext);
            
            transactionManager.commitTransaction(session, transactionTracker); // commit
        } catch (Exception e) {
        	e.printStackTrace();
        	if (transactionTracker != null && session != null 
        			&& session.getTransaction()!=null && session.getTransaction().isActive()) {
        		transactionManager.rollbackTransaction(session);
        	}

        	throw new AppException(e.getMessage());
        }
        
		return result;
	}

}
