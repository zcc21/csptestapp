package com.chanapp.cspdemo.testapp.eventhandler;

import java.util.List;

import com.chanjet.csp.bo.api.BoSession;
import com.chanjet.csp.cmr.api.metadata.userschema.type.businessObject.IBusinessObject;
import com.chanjet.csp.cmr.api.metadata.userschema.type.entity.IEntity;
import com.chanjet.csp.ess.api.messageBus.BOEventPayload;
import com.chanjet.csp.event.SystemEventHandler;

public class CustomSystemEventHandler extends SystemEventHandler {
	@Override
	public boolean checkCanDelete(BoSession session, String sourceAppId, IEntity entity, Long rowId,
			List<IBusinessObject> affectedBoList) {
		return true;
	}

	@Override
	public void handlePostDelete(BoSession session, List<IBusinessObject> bos, BOEventPayload eventPayload, Long rowid,
			Long userId, String appId) {
		System.out.println(appId);
	}
}
