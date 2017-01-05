package com.chanapp.cspdemo.testapp.service;

import com.chanapp.cspdemo.testapp.businessobject.api.restletflow.IRestletFlowRow;

public interface RestletFlowService {

	public IRestletFlowRow getOf(String restletName, String method);

	public void addOneCallOf(String restlet, String method);

	public Long getCallCountOf(String restletName, String method);

}
