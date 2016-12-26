package com.cspdemo.testapp.test.v1_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
	public void setUp() throws Exception {
		Map<String, Object> barMap = new HashMap<String, Object>();
		barMap.put("name", DEFAULT_BAR_NAME);
		
		// POST - http://localhost:8080/cspdemo/testapp/services/1.0/bo/dml/{boName}
		HttpResponse response = this.doPost("isvtest", "/bo/dml/Customer", barMap);
		assertEquals(200, response.getStatusCode());
		
	    String barString = response.getString();
	    bar = JSON.parseObject(barString);
	}
	
	@Test
	public void shouldAllowAddANewCustomer() throws Exception {
		// Check
		assertNotNull(bar.get("id"));
	    assertEquals(DEFAULT_BAR_NAME, bar.get("name"));	   
	}
	
	@Test
	public void shouldAllowUpdateACustomer() throws Exception {
		// Given:
		Long barId = bar.getLongValue("id");
		String newBarName = "barbar";
		bar.put("name", newBarName);
		
		// When: update the name
		// PUT - http://localhost:8080/cspdemo/testapp/services/1.0/bo/dml/{boName}/{ID}
		HttpResponse response = this.doPut("isvtest", "bo/dml/Customer/" + barId, bar);
		assertEquals(200, response.getStatusCode());
	    
	    // Then: check whether the name has updated
	    response = this.doGet("isvtest", "bo/query/Customer/" + barId, null);		
	    String updatedBarString = response.getString();
	    JSONObject updatedBar = JSON.parseObject(updatedBarString);
	    assertEquals(newBarName, updatedBar.get("name"));
	}
	
	@Test
	public void shouldAllowGetACustomer() throws Exception {
		// Given:
		Long barId = bar.getLongValue("id");
		
		// When: reload the bar
		// GET - http://localhost:8080/cspdemo/testapp/services/1.0/bo/query/{boName}/{ID}
		HttpResponse response = this.doGet("isvtest", "bo/query/Customer/" + barId, null);
		assertEquals(200, response.getStatusCode());
	    String sameBarString = response.getString();
	    JSONObject sameBar = JSON.parseObject(sameBarString);
	    
	    // Then: check whether the two bars are same
	    assertEquals(barId, sameBar.getLong("id"));
	    assertEquals(bar.get("name"), sameBar.get("name"));
	}
	
	@Test
	public void shouldAllowDeleteACustomer() throws Exception {
		// Given:
		Long barId = bar.getLongValue("id");
		
		// When: delete the bar
		// DELETE - http://localhost:8080/cspdemo/testapp/services/1.0/bo/dml/{boName}/{ID}
		HttpResponse response = this.doDelete("isvtest", "bo/dml/Customer/" + barId, null);
		
		// Then: check whether the bar is deleted
		response = this.doGet("isvtest", "bo/query/Customer/" + barId, null);
		assertEquals(400, response.getStatusCode());	
	}
}
