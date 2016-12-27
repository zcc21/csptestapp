package com.cspdemo.testapp.test.v1_0;

import static org.junit.Assert.*;

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
	public void setUp() throws Exception {
		Map<String, Object> fooMap = new HashMap<String, Object>();
		fooMap.put("name", DEFAULT_FOO_NAME);
		
		// POST - http://localhost:8080/cspdemo/testapp/services/1.0/bo/dml/{boName}
		HttpResponse response = this.doPost("admin", "/bo/dml/Contact", fooMap);
		assertEquals(200, response.getStatusCode());
		
	    String fooString = response.getString();
	    foo = JSON.parseObject(fooString);
	}
	
	@Test
	public void shouldAllowAddANewContact() throws Exception {
		// Check
		assertNotNull(foo.get("id"));
	    assertEquals(DEFAULT_FOO_NAME, foo.get("name"));	   
	}
	
	@Test
	public void shouldAllowUpdateAContact() throws Exception {
		// Given:
		Long fooId = foo.getLongValue("id");
		String newFooName = "foofoo";
		foo.put("name", newFooName);
		
		// When: update the name
		// PUT - http://localhost:8080/cspdemo/testapp/services/1.0/bo/dml/{boName}/{ID}
		HttpResponse response = this.doPut("admin", "bo/dml/Contact/" + fooId, foo);
		assertEquals(200, response.getStatusCode());
	    
	    // Then: check whether the name has updated
	    response = this.doGet("admin", "bo/query/Contact/" + fooId, null);		
	    String updatedFooString = response.getString();
	    JSONObject updatedFoo = JSON.parseObject(updatedFooString);
	    assertEquals(newFooName, updatedFoo.get("name"));
	}
	
	@Test
	public void shouldAllowGetAContact() throws Exception {
		// Given:
		Long fooId = foo.getLongValue("id");
		
		// When: reload the foo
		// GET - http://localhost:8080/cspdemo/testapp/services/1.0/bo/query/{boName}/{ID}
		HttpResponse response = this.doGet("admin", "bo/query/Contact/" + fooId, null);
		assertEquals(200, response.getStatusCode());
	    String sameFooString = response.getString();
	    JSONObject sameFoo = JSON.parseObject(sameFooString);
	    
	    // Then: check whether the two foos are same
	    assertEquals(fooId, sameFoo.getLong("id"));
	    assertEquals(foo.get("name"), sameFoo.get("name"));
	}
	
	@Test
	public void shouldAllowDeleteAContact() throws Exception {
		// Given:
		Long fooId = foo.getLongValue("id");
		
		// When: delete the foo
		// DELETE - http://localhost:8080/cspdemo/testapp/services/1.0/bo/dml/{boName}/{ID}
		HttpResponse response = this.doDelete("admin", "bo/dml/Contact/" + fooId, null);
		
		// Then: check whether the foo is deleted
		response = this.doGet("admin", "bo/query/Contact/" + fooId, null);
		assertEquals(400, response.getStatusCode());	
	}
}
