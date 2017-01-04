package com.cspdemo.testapp.test.v2_3;

import static org.junit.Assert.*;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chanjet.csp.platform.test.RestfulIT;
import com.cspdemo.testapp.test.utils.ContactService;

public class PresetContactIT extends RestfulIT {
	private ContactService contactService = new ContactService();	
	
	@Test
	public void shouldGetThePresetContact() throws Exception {
		// Given: 
		String presetContactName = "FOO";
		
		// When: query the preset contact
		JSONArray foos = contactService.getBy("admin", presetContactName);
		
		// Then:
		JSONObject foo = foos.getJSONObject(0);		
		assertNotNull(foo.getLong("id"));
		assertEquals(presetContactName, foo.get("name"));			
	}	
	
}
