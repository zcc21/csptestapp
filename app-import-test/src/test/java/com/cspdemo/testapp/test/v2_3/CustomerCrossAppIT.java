package com.cspdemo.testapp.test.v2_3;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.alibaba.fastjson.JSONObject;
import com.chanjet.csp.platform.test.RestfulIT;
import com.cspdemo.testapp.test.utils.CustomerService;

public class CustomerCrossAppIT extends RestfulIT {
	private static final Long EMPLOYEE_NUMBER_OF_BIG_CUSTOMER = 1001L;
	private static final Long EMPLOYEE_NUMBER_OF_SMALL_CUSTOMER = 101L;
	private CustomerService customerService = new CustomerService();
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void shouldNotAllowDeleteBigCustomer() throws Exception {
		// Given: add a big customer
		JSONObject bar = customerService.addNewBy("admin", "bar", EMPLOYEE_NUMBER_OF_BIG_CUSTOMER);
		Long barId = bar.getLong("id");
		assertNotNull(barId);
		
		// When & Check
		// Disable to delete the big customer in event handler
		thrown.expect(RuntimeException.class);		
		customerService.deleteBy("admin", barId);		
	}
	
	@Test
	public void shouldAllowDeleteSmallCustomer() throws Exception {
		// Given: add a small customer
		JSONObject bar = customerService.addNewBy("admin", "bar", EMPLOYEE_NUMBER_OF_SMALL_CUSTOMER);
		Long barId = bar.getLong("id");
		assertNotNull(barId);
		
		// When: delete the small bar
		customerService.deleteBy("admin", barId);
		
		// Then: check the small bar has been deleted
		assertNull(customerService.getBy("admin", barId));				
	}
}
