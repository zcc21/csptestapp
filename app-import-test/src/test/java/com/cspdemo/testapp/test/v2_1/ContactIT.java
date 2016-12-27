package com.cspdemo.testapp.test.v2_1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chanjet.csp.platform.test.HttpResponse;
import com.chanjet.csp.platform.test.RestfulIT;

public class ContactIT extends RestfulIT {
	private JSONObject foo;
	private static final String DEFAULT_FOO_NAME = "foo";
	
	@Before
	public void setup() throws Exception {
		setIsCspServletURL(false);
		
		Map<String, Object> fooMap = new HashMap<String, Object>();
		fooMap.put("name", DEFAULT_FOO_NAME);
		// POST - http://localhost:8080/cspdemo/testapp/restlet/v2_1/{restletName} 
		HttpResponse response = this.doPost("testRegular", "/restlet/v2_1/Contact", fooMap);
		assertEquals(200, response.getStatusCode());
	    String fooString = response.getString();
	    foo = JSON.parseObject(fooString);
	}
	
	@Test
	public void shouldAllowAddANewContact() throws Exception {
		// Check:
	    assertNotNull(foo.get("id"));
	    assertEquals(DEFAULT_FOO_NAME, foo.get("name"));
	}
	
	@Test
	public void shouldAllowGetAContact() throws Exception {
		// Given:
		Long fooId = foo.getLong("id");	
		
		// When: reload the foo
		// GET - http://localhost:8080/cspdemo/testapp/restlet/v2_1/{restletName}/{ID}
		HttpResponse response = this.doGet("admin", "/restlet/v2_1/Contact/" + fooId, null);
		assertEquals(200, response.getStatusCode());
	    String sameFooString = response.getString();
	    JSONObject sameFoo = JSON.parseObject(sameFooString);
	    
	    // Then: check the two foos are same
	    assertEquals(fooId, sameFoo.getLong("id"));
	    assertEquals(foo.get("name"), sameFoo.get("name"));
	}
	
	@Test
	public void shouldAllowUpdateAContact() throws Exception {
		// Given:
		Long fooId = foo.getLong("id");	
		String newFooName = "foofoo";
		Map<String, Object> newFooMap = new HashMap<String, Object>();
		newFooMap.put("name", newFooName);	
		
		// When: update the name
		// PUT - http://localhost:8080/cspdemo/testapp/restlet/v2_1/{restletName}/{ID}
		HttpResponse response = this.doPut("admin", "/restlet/v2_1/Contact/" + fooId, newFooMap);
		assertEquals(200, response.getStatusCode());
		
		// Then: check the name has updated
		response = this.doGet("admin", "/restlet/v2_1/Contact/" + fooId, null);
		assertEquals(200, response.getStatusCode());
	    String updatedFooString = response.getString();
	    JSONObject updatedFoo = JSON.parseObject(updatedFooString);
	    assertEquals(fooId, updatedFoo.getLong("id"));
	    assertEquals(newFooName, updatedFoo.get("name"));
	}
	
	@Test
	public void shouldAllowDeleteAContact() throws Exception {
		// Given:
		Long fooId = foo.getLong("id");
		
		// When: delete the foo
		// DELETE - http://localhost:8080/cspdemo/testapp/restlet/v2_1/{restletName}/{ID}
		HttpResponse response = this.doDelete("admin", "/restlet/v2_1/Contact/" + fooId, null);
			
		// Then: check whether the foo is deleted
		response = this.doGet("admin", "/restlet/v2_1/Contact/" + fooId, null);
		assertNull(response.getString());
	}
}
