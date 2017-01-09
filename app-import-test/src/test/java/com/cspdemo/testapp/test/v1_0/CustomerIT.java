package com.cspdemo.testapp.test.v1_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.chanjet.csp.platform.test.HttpResponse;
import com.chanjet.csp.platform.test.RestfulIT;

public class CustomerIT extends RestfulIT {
	private static final String DEFAULT_FOO_NAME = "foo";
	private JSONObject bar;
	private static final String DEFAULT_BAR_NAME = "bar";
	
	@Before
	public void setUp() throws Exception {
		Map<String, Object> barMap = new HashMap<String, Object>();
		barMap.put("name", DEFAULT_BAR_NAME);
		
		// POST - http://localhost:8080/cspdemo/testapp/services/1.0/bo/dml/{boName}
		HttpResponse response = this.doPost("admin", "/bo/dml/Customer", barMap);
		assertEquals(200, response.getStatusCode());
		
	    String barString = response.getString();
	    bar = JSON.parseObject(barString);
	}
	
	@Test
	public void shouldAllowAddANewCustomer() throws Exception {
		// Given:
		Map<String, Object> barMap = new HashMap<String, Object>();
		barMap.put("name", DEFAULT_BAR_NAME);
		
		// When: add a new Customer
		// POST - http://localhost:8080/cspdemo/testapp/services/1.0/bo/dml/{boName}
		HttpResponse response = this.doPost("admin", "/bo/dml/Customer", barMap);
		assertEquals(200, response.getStatusCode());
		
		// Then: check the bar
	    String barString = response.getString();
	    bar = JSON.parseObject(barString);
		assertNotNull(bar.get("id"));
	    assertEquals(DEFAULT_BAR_NAME, bar.get("name"));	   
	}
	
	@Test
	public void shouldAllowUpdateTheCustomer() throws Exception {
		// Given:
		Long barId = bar.getLongValue("id");
		String newBarName = "barbar";
		bar.put("name", newBarName);
		
		// When: update the name
		// PUT - http://localhost:8080/cspdemo/testapp/services/1.0/bo/dml/{boName}/{ID}
		HttpResponse response = this.doPut("admin", "bo/dml/Customer/" + barId, bar);
		assertEquals(200, response.getStatusCode());
	    
	    // Then: check whether the name has updated
	    response = this.doGet("admin", "bo/query/Customer/" + barId, null);		
	    String updatedBarString = response.getString();
	    JSONObject updatedBar = JSON.parseObject(updatedBarString);
	    assertEquals(newBarName, updatedBar.get("name"));
	}
	
	@Test
	public void shouldAllowGetTheCustomer() throws Exception {
		// Given:
		Long barId = bar.getLongValue("id");
		
		// When: reload the bar
		// GET - http://localhost:8080/cspdemo/testapp/services/1.0/bo/query/{boName}/{ID}
		HttpResponse response = this.doGet("admin", "bo/query/Customer/" + barId, null);
		assertEquals(200, response.getStatusCode());
	    String sameBarString = response.getString();
	    JSONObject sameBar = JSON.parseObject(sameBarString);
	    
	    // Then: check whether the two bars are same
	    assertEquals(barId, sameBar.getLong("id"));
	    assertEquals(bar.get("name"), sameBar.get("name"));
	}
	
	@Test
	public void shouldAllowDeleteTheCustomer() throws Exception {
		// Given:
		Long barId = bar.getLongValue("id");
		
		// When: delete the bar
		// DELETE - http://localhost:8080/cspdemo/testapp/services/1.0/bo/dml/{boName}/{ID}
		HttpResponse response = this.doDelete("admin", "bo/dml/Customer/" + barId, null);
		
		// Then: check whether the bar is deleted
		response = this.doGet("admin", "bo/query/Customer/" + barId, null);
		assertEquals(400, response.getStatusCode());	
	}
	
	@Test
	public void shouldAllowAddANewCustomerWithAContact() throws Exception {
		// Given:
		// Customer info
		Map<String, Object> barMap = new HashMap<String, Object>();
		barMap.put("name", DEFAULT_BAR_NAME);
		// Contact info
		Map<String, Object> fooMap = new HashMap<String, Object>();
		fooMap.put("name", DEFAULT_FOO_NAME);

		// When: 1. add a new Customer
		// POST - http://localhost:8080/cspdemo/testapp/services/1.0/bo/dml/{boName}
		HttpResponse response = this.doPost("admin", "/bo/dml/Customer", barMap);
		assertEquals(200, response.getStatusCode());
		String barString = response.getString();
		bar = JSON.parseObject(barString);		
		
		// 2. add a new contact related to the bar
		Long barId = bar.getLongValue("id");
		LinkedHashMap<String, Object> barIdMap = new LinkedHashMap<>();
		barIdMap.put("id", barId);
		fooMap.put("customer", barIdMap);
		// POST - http://localhost:8080/cspdemo/testapp/services/1.0/bo/dml/{boName}
		response = this.doPost("admin", "/bo/dml/Contact", fooMap);
		assertEquals(200, response.getStatusCode());	    
	    
	    // Then: check whether the foo is related to the bar
		String fooString = response.getString();
	    assertEquals(barId, (Long) JSONPath.read(fooString, "$.customer.id"));	    
	}
}
