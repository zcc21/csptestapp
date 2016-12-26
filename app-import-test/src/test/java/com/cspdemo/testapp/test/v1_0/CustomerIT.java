package com.cspdemo.testapp.test.v1_0;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chanjet.csp.platform.test.HttpResponse;
import com.chanjet.csp.platform.test.RestfulIT;

public class CustomerIT extends RestfulIT {
	@Test
	public void shouldAllowAddANewCustomer() throws Exception {		
		Map<String, Object> bar = new HashMap<String, Object>();
		bar.put("name", "bar");	
		HttpResponse response = this.doPost("isvtest", "/bo/dml/Customer", bar);
		assertEquals(200, response.getStatusCode());
	    String barString = response.getString();
	    JSONObject barJson = JSON.parseObject(barString);
	    //System.out.println(barJson);
	    assertEquals("barbar", barJson.get("name"));	   
	}
}
