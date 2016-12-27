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
		// POST - http://localhost:8080/cspdemo/testapp/restlet/v2_0/{restletName} 
		HttpResponse response = this.doPost("admin", "/restlet/v2_0/Customer", barMap);
		assertEquals(200, response.getStatusCode());
	    String barString = response.getString();
	    bar = JSON.parseObject(barString);
	}
	
	@Test
	public void shouldAllowAddANewCustomer() throws Exception {
		// Check:
	    assertNotNull(bar.get("id"));
	    assertEquals(DEFAULT_BAR_NAME, bar.get("name"));
	}
	
	@Test
	public void shouldAllowGetACustomer() throws Exception {
		// Given:
		Long barId = bar.getLong("id");	
		
		// When: reload the bar
		// GET - http://localhost:8080/cspdemo/testapp/restlet/v2_0/{restletName}/{ID}
		HttpResponse response = this.doGet("admin", "/restlet/v2_0/Customer/" + barId, null);
		assertEquals(200, response.getStatusCode());
	    String sameBarString = response.getString();
	    JSONObject sameBar = JSON.parseObject(sameBarString);
	    
	    // Then: check the two bars are same
	    assertEquals(barId, sameBar.getLong("id"));
	    assertEquals(bar.get("name"), sameBar.get("name"));
	}
	
	@Test
	public void shouldAllowUpdateACustomer() throws Exception {
		// Given:
		Long barId = bar.getLong("id");	
		String newBarName = "barbar";
		Map<String, Object> newBarMap = new HashMap<String, Object>();
		newBarMap.put("name", newBarName);	
		
		// When: update the name
		// PUT - http://localhost:8080/cspdemo/testapp/restlet/v2_0/{restletName}/{ID}
		HttpResponse response = this.doPut("admin", "/restlet/v2_0/Customer/" + barId, newBarMap);
		assertEquals(200, response.getStatusCode());
		
		// Then: check the name has updated
		response = this.doGet("admin", "/restlet/v2_0/Customer/" + barId, null);
		assertEquals(200, response.getStatusCode());
	    String updatedBarString = response.getString();
	    JSONObject updatedBar = JSON.parseObject(updatedBarString);
	    assertEquals(barId, updatedBar.getLong("id"));
	    assertEquals(newBarName, updatedBar.get("name"));
	}
	
	@Test
	public void shouldAllowDeleteACustomer() throws Exception {
		// Given:
		Long barId = bar.getLong("id");
		
		// When: delete the bar
		// DELETE - http://localhost:8080/cspdemo/testapp/restlet/v2_0/{restletName}/{ID}
		HttpResponse response = this.doDelete("admin", "/restlet/v2_0/Customer/" + barId, null);
			
		// Then: check whether the bar is deleted
		response = this.doGet("admin", "/restlet/v2_0/Customer/" + barId, null);
		assertNull(response.getString());
	}
	
	@Test
	public void shouldAllowAddANewCustomerWithAContact() throws Exception {
		// Given:
		// Customer info
		Map<String, Object> barMap = new HashMap<>();
		barMap.put("name", DEFAULT_BAR_NAME);
		// Contact info
		String fooName = "foo";
		Map<String, Object> fooMap = new HashMap<>();
		fooMap.put("name", fooName);		
		Map<String, Object> barWithFooMap = new HashMap<>(); 
		barWithFooMap.put("customer", barMap);
		barWithFooMap.put("contact", fooMap);

		// When: add contact related to the bar		
		// POST - http://localhost:8080/cspdemo/testapp/restlet/v2_0/{restletName}/{FuncName}
		HttpResponse response = this.doPost("admin", "/restlet/v2_0/Customer/WithContact", barWithFooMap);
		assertEquals(200, response.getStatusCode());		
		String fooString = response.getString();
		JSONObject barWithFoo = JSON.parseObject(fooString);
		
		// Then: check whether the foo is related to the bar
		JSONObject bar = (JSONObject) barWithFoo.get("customer");
		Long barId = bar.getLong("id");
		assertNotNull(barId);
		
		JSONObject foo = (JSONObject) barWithFoo.get("contact");
		Long fooId = foo.getLong("id");
		assertNotNull(fooId);
		Long barIdInFoo = ((JSONObject) foo.get("customer")).getLong("id");		
		assertEquals(barId, barIdInFoo);
	}
}
