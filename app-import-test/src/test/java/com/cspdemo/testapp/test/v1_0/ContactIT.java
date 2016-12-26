package com.cspdemo.testapp.test.v1_0;

import static org.junit.Assert.assertEquals;

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
		HttpResponse response = this.doPost("isvtest", "/bo/dml/Contact", fooMap);
		assertEquals(200, response.getStatusCode());
	    String fooString = response.getString();
	    foo = JSON.parseObject(fooString);
	}
	
	@Test
	public void shouldAllowAddANewContact() throws Exception {		
	    //System.out.println(fooJson);
	    assertEquals(DEFAULT_FOO_NAME, foo.get("name"));	   
	}
	
	@Test
	public void shouldAllowUpdateAContact() throws Exception {		
		// Map<String, Object> foo2Map = new HashMap<String, Object>();
		String foo2Name = "foo2";
		foo.put("name", foo2Name);
		HttpResponse response = this.doPut("isvtest", "bo/dml/Contact/" + foo.get("id"), foo);
		assertEquals(200, response.getStatusCode());
	    String fooString = response.getString();
	    JSONObject fooJson = JSON.parseObject(fooString); 
	    assertEquals(foo2Name, fooJson.get("name"));
	}
}
