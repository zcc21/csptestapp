package com.cspdemo.testapp.test.v2_0;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chanjet.csp.platform.test.HttpResponse;
import com.chanjet.csp.platform.test.RestfulIT;

public class CustomerIT extends RestfulIT {
	private JSONObject bar;
	private static final String DEFAULT_BAR_NAME = "bar";
	
	@Before
	public void setup() throws Exception {
		setIsCspServletURL(false);
		
		Map<String, Object> barMap = new HashMap<String, Object>();
		barMap.put("name", DEFAULT_BAR_NAME);		
		HttpResponse response = this.doPost("isvtest", "/restlet/v2_0/Customer", barMap);
		assertEquals(200, response.getStatusCode());
	    String barString = response.getString();
	    bar = JSON.parseObject(barString);
	}
	
	@Test
	public void shouldAllowAddANewCustomer() throws Exception {		
	    assertNotNull(bar.get("id"));
	    assertEquals(DEFAULT_BAR_NAME, bar.get("name"));
	}
	
	@Test
	public void shouldAllowGetACustomer() throws Exception {
		Long barId = bar.getLong("id");				
		HttpResponse response = this.doGet("isvtest", "/restlet/v2_0/Customer/" + barId, null);
		assertEquals(200, response.getStatusCode());
	    String sameBarString = response.getString();
	    JSONObject sameBar = JSON.parseObject(sameBarString);
	    assertEquals(bar.get("name"), sameBar.get("name"));
	}
	
	@Test
	public void shouldAllowUpdateACustomer() throws Exception {
		Long barId = bar.getLong("id");	
		String newBarName = "barbar";
		Map<String, Object> newBarMap = new HashMap<String, Object>();
		newBarMap.put("name", newBarName);		
		HttpResponse response = this.doPut("isvtest", "/restlet/v2_0/Customer/" + barId, newBarMap);
		assertEquals(200, response.getStatusCode());
	    String newBarString = response.getString();
	    JSONObject newBar = JSON.parseObject(newBarString);
	    
	    assertEquals(barId, newBar.getLong("id"));
	    assertEquals(newBarName, newBar.get("name"));
	}
	
	@Test
	public void shouldAllowDeleteACustomer() throws Exception {
		Long barId = bar.getLong("id");
		HttpResponse response = this.doDelete("isvtest", "/restlet/v2_0/Customer/" + barId, null);
			
		// check whether the bar is deleted
		response = this.doGet("isvtest", "/restlet/v2_0/Customer/" + barId, null);
		assertNull(response.getString());
	}
}
