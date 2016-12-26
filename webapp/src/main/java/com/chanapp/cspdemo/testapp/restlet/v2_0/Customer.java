package com.chanapp.cspdemo.testapp.restlet.v2_0;

import java.util.Map;

import com.chanjet.csp.common.base.exception.ServiceAppException;
import com.chanjet.csp.rest.restlet.Restlet;

public class Customer extends Restlet {
	@Override
	public Object doDeleteId(Map<String, String[]> queryParameters, String payload, Long id) {
		throw new ServiceAppException("Service not implemented.");
	}

	@Override
	public Object doGetId(Map<String, String[]> queryParameters, Long id) {
		throw new ServiceAppException("Service not implemented.");
	}

	@Override
	public Object doPost(Map<String, String[]> queryParameters, String jsonString) {
		throw new ServiceAppException("Service not implemented.");
	}

	@Override
	public Object doPutId(Map<String, String[]> queryParameters, String payload, Long id) {
		throw new ServiceAppException("Service not implemented.");
	}
}
