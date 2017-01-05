package com.cspdemo.testapp.test.v2_3;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chanjet.csp.platform.test.HttpResponse;
import com.chanjet.csp.platform.test.RestfulIT;

public class UserLogIT extends RestfulIT {

	@Test
	public void shouldGetUserLoginLog() throws Exception {
		// Given:
		Long adminId = Long.valueOf(this.getAppUser("admin").getUserId());
		Map<String, String> params = new HashMap<>();
		params.put("userId", adminId.toString());
		
		setIsCspServletURL(true);
		
		// When: log in by admin and query the login info
		HttpResponse response = this.doGet("admin", "/bo/query/UserLog", params);
		assertEquals(200, response.getStatusCode());
		
		// Then:
	    String userLogsString = response.getString();
	    JSONArray userLogs = JSON.parseArray(userLogsString);
	    assertTrue(userLogs.size() > 0);
	}
}
