package com.chanapp.cspdemo.testapp.restlet;

import java.util.Map;

import com.chanapp.cspdemo.testapp.service.RestletFlowService;
import com.chanapp.cspdemo.testapp.service.RestletFlowServiceImpl;
import com.chanjet.csp.rest.restlet.Restlet;

public class RestletFlow extends Restlet {
	private RestletFlowService restletFlowService = new RestletFlowServiceImpl();
	
	@Override
	public Object doGet(Map<String, String[]> queryParameters) {
		String restletName = queryParameters.get("name")[0];
		String method = queryParameters.get("method")[0];
		Long callCount = restletFlowService.getCallCountOf(restletName, method);
		return callCount;
	}
	
}
