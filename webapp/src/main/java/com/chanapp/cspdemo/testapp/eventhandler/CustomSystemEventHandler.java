package com.chanapp.cspdemo.testapp.eventhandler;

import java.util.List;

import com.chanapp.cspdemo.testapp.service.CustomerService;
import com.chanapp.cspdemo.testapp.service.CustomerServiceImpl;
import com.chanjet.csp.bo.api.BoSession;
import com.chanjet.csp.bo.api.IBusinessObjectRow;
import com.chanjet.csp.cmr.api.metadata.userschema.type.businessObject.IBusinessObject;
import com.chanjet.csp.cmr.api.metadata.userschema.type.entity.IEntity;
import com.chanjet.csp.ess.api.messageBus.BOEventPayload;
import com.chanjet.csp.event.SystemEventHandler;

public class CustomSystemEventHandler extends SystemEventHandler {	
	@Override
	public boolean checkCanDelete(BoSession session, String sourceAppId, IEntity entity, Long rowId,
			List<IBusinessObject> affectedBoList) {		
		boolean canDelete = true;		
		if (entity.getName().equals("Customer")) {	
			CustomerService customerService = new CustomerServiceImpl();			
			if (customerService.isBigCustomer(rowId)) {
				canDelete = false;
			}			
		}
		
		return canDelete;
	}
	
	@Override
	public void handlePreDelete(BoSession session, List<IBusinessObject> bos, BOEventPayload eventPayload, Long rowid,
			Long userId, String appId) {		
		System.out.println("pre delete");
	}

	@Override
	public void handlePostDelete(BoSession session, List<IBusinessObject> bos, BOEventPayload eventPayload, Long rowid,
			Long userId, String appId) {
		IBusinessObject bo = bos.get(0);
		IBusinessObjectRow row = eventPayload.constructBORow(session, boManager, bo);
		System.out.println(appId);
	}
}
