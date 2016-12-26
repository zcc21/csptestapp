package com.cspdemo.testapp.test.v2_0;

import static org.junit.Assert.*;

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
		setIsCspServletURL(false);
		HttpResponse response = this.doPost("isvtest", "/restlet/v2_0/Customer", bar);
		assertEquals(200, response.getStatusCode());
	    String barString = response.getString();
	    JSONObject barJson = JSON.parseObject(barString);
	    assertNotNull(barJson.get("id"));
	    assertEquals("bar", barJson.get("name"));
	}
}
