package com.cspdemo.testapp.test.v2_3;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chanjet.csp.platform.test.HttpResponse;
import com.chanjet.csp.platform.test.RestfulIT;
import com.cspdemo.testapp.test.utils.ContactService;

public class ContactFlowIT extends RestfulIT {
	private ContactService contactService = new ContactService();
	private static final String FOO_NAME = "foo";
	private static final String RESTLET_NAME = "com.chanapp.cspdemo.testapp.restlet.v2_1.Contact";
	private static final String POST_METHOD_NAME = "POST";
	
	@Test
	public void shouldAddCallCountOfPostWhenAddContact() throws Exception {
		// Given: 
		int oldPostCallCount = getCallCountOf(RESTLET_NAME, POST_METHOD_NAME);		
		
		// When: do post to add a new contact
		JSONObject foo = contactService.addNewBy("admin", FOO_NAME);
		assertNotNull(foo.getLong("id"));
		
		// Then: check new == old + 1
		int newPostCallCount = getCallCountOf(RESTLET_NAME, POST_METHOD_NAME);		
		assertEquals(oldPostCallCount + 1, newPostCallCount);			
	}

	private int getCallCountOf(String restletName, String method) throws Exception {
		setIsCspServletURL(false);
		
		Map<String, String> params = new HashMap<>();
		params.put("name", restletName);
		params.put("method", method);

		// GET - http://localhost:8080/cspdemo/testapp/restlet/RestletFlow
		HttpResponse response = this.doGet("admin", "/restlet/RestletFlow/", params);
	
		String callCountString = response.getString();
		return Integer.valueOf(callCountString);
	}	
	
}
