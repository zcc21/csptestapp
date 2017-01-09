package com.cspdemo.testapp.test.v3_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.chanjet.csp.platform.test.HttpResponse;
import com.chanjet.csp.platform.test.RestfulIT;

public class ContactGqlIT extends RestfulIT {
	private static final String CUSTOMER_BO_NAME = "Customer";
	private static final int DEFAULT_EMPLOYEE_NUMBER = 101;
	private static final String FOO_NAME = "foo";
	private static final String BAR_NAME = "bar";
	private JSONObject bar;
	private Long barId;
	private JSONObject foo;
	private Long fooId;

	@Before
	public void setup() throws Exception {	
		addCustomerWithContact();
	}
	
	@Test
	public void shouldOnlyGetTheNameOfTheCustomer() throws Exception {
		// Given:		
		String idCriteria = " \"id = :id\" ";		
		String idBind = " { Name: \"id\", Value: " + barId + " } ";
		String idQuerySpec = 
				" jsonQuerySpec: { " +
				"	CriteriaStr: " + idCriteria + "," + 
				"	BindVars: [" + idBind + " ] " +
				" } ";
		
		String gql = " 	{ bar: Customer(" + idQuerySpec + " ) { id, name } } ";	
		
		// When: query the bar by gql
		// GET - http://localhost:8080/cspdemo/testapp/service/1.0/gql?query={query}
		setIsCspServletURL(true);
		HttpResponse response = this.doGet("admin", "/gql?query=" + URLEncoder.encode(gql, "UTF-8"), null);
		assertEquals(200, response.getStatusCode());
		
		// Then: check the bar 
		String responseString = response.getString();
		System.out.println(responseString);
		String name = (String) JSONPath.read(responseString, "$.data.bar[0].name");
		assertEquals(BAR_NAME, name);					
	}
	
	@Test
	public void shouldOnlyGetTheEmployeeNumberOfTheCustomer() throws  Exception {
		// Given:		
		String idCriteria = " \"id = :id\" ";		
		String idBind = " { Name: \"id\", Value: " + barId + " } ";
		String idQuerySpec = 
				" jsonQuerySpec: { " +
				"	CriteriaStr: " + idCriteria + "," + 
				"	BindVars: [" + idBind + " ] " +
				" } ";
		
		String gql = " 	{ bar: Customer(" + idQuerySpec + " ) { id, employeeNumber } } ";		
	
		// When: query the bar by gql
		// GET - http://localhost:8080/cspdemo/testapp/service/1.0/gql?query={query}
		setIsCspServletURL(true);
		HttpResponse response = this.doGet("admin", "/gql?query=" + URLEncoder.encode(gql, "UTF-8"), null);
		assertEquals(200, response.getStatusCode());
		
		// Then: check the bar 
		String responseString = response.getString();
		System.out.println(responseString);
		int employeeNumber = (int) JSONPath.read(responseString, "$.data.bar[0].employeeNumber");
		assertEquals(DEFAULT_EMPLOYEE_NUMBER, employeeNumber);					
	}
	
	@Test
	public void shouldGetTheCountOfTheCustomers() throws Exception {
		// Given:		
		String nameCriteria = " \"name = :name\" ";		
		String nameBind = " { Name: \"name\", Value: \"" + BAR_NAME + "\" } ";
		String nameQuerySpec = 
				" { " +
				"	CriteriaStr: " + nameCriteria + "," + 
				"	BindVars: [" + nameBind + " ] " +
				" } ";		
		
		int oldCount = getCountOf(CUSTOMER_BO_NAME, nameQuerySpec);
		System.out.println(oldCount);
		
		// When: add a new Customer named bar
		addCustomerWithContact();
		
		// Then: check new == old + 1
		int newCount = getCountOf(CUSTOMER_BO_NAME, nameQuerySpec);		
		assertEquals(oldCount + 1, newCount);					
	}

	/**
	 * gql:
	 * 	{
	  		data: Contact( jsonQuerySpec: { CriteriaStr: "id = $ID"} ) {
     			id,
     			name,
     			alias:customer_Customer {
         			id,
         			name         
     			}
			}
		}	
	 */
	@Test
	public void shouldGetTheNamesOfTheCustomerAndContact() throws Exception {
		// Given:		
		String idCriteria = " \"id = :id\" ";		
		String idBind = " { Name: \"id\", Value: " + fooId + " } ";
		String idQuerySpec = 
				" jsonQuerySpec: { " +
						"	CriteriaStr: " + idCriteria + "," + 
						"	BindVars: [" + idBind + " ] " +
						" } ";

		String gql = " 	{ foo: Contact(" + idQuerySpec + " ) { id, name, bar:customer__Customer {id,	name } } } ";	

		// When: query the bar by gql
		// GET - http://localhost:8080/cspdemo/testapp/service/1.0/gql?query={query}
		setIsCspServletURL(true);
		HttpResponse response = this.doGet("admin", "/gql?query=" + URLEncoder.encode(gql, "UTF-8"), null);
		assertEquals(200, response.getStatusCode());

		// Then: check the names 
		String responseString = response.getString();
		System.out.println(responseString);
		String customerName = (String) JSONPath.read(responseString, "$.data.foo[0].bar.name");
		assertEquals(BAR_NAME, customerName);
		String contactName = (String) JSONPath.read(responseString, "$.data.foo[0].name");
		assertEquals(FOO_NAME, contactName);	
	}
	
	private int getCountOf(String boName, String querySpec) throws Exception, UnsupportedEncodingException {
		String gql = " { count: BOCount( boName: \"" + boName + "\", jsonQuerySpec: " + querySpec + " ) } ";	
		
		// GET - http://localhost:8080/cspdemo/testapp/service/1.0/gql?query={query}
		setIsCspServletURL(true);
		HttpResponse response = this.doGet("admin", "/gql?query=" + URLEncoder.encode(gql, "UTF-8"), null);
		assertEquals(200, response.getStatusCode());

		String responseString = response.getString();		
		return (int) JSONPath.read(responseString, "$.data.count");	
	}


	private void addCustomerWithContact() throws Exception {
		// Customer info
		Map<String, Object> barMap = new HashMap<>();
		barMap.put("name", BAR_NAME);
		barMap.put("employeeNumber", DEFAULT_EMPLOYEE_NUMBER);
		// Contact info	
		Map<String, Object> fooMap = new HashMap<>();
		fooMap.put("name", FOO_NAME);
		fooMap.put("mobile", "111111");
		fooMap.put("position", "manager");
		fooMap.put("gender", "male");
		
		Map<String, Object> barWithFooMap = new HashMap<>(); 
		barWithFooMap.put("customer", barMap);
		barWithFooMap.put("contact", fooMap);
		
		setIsCspServletURL(false);
		
		// When: add contact related to the bar		
		// POST - http://localhost:8080/cspdemo/testapp/restlet/v2_1/{restletName}/{FuncName}
		HttpResponse response = this.doPost("admin", "/restlet/v2_1/Customer/WithContact", barWithFooMap);
		assertEquals(200, response.getStatusCode());		
		String fooString = response.getString();
		JSONObject barWithFoo = JSON.parseObject(fooString);

		// Then: check whether the foo is related to the bar
		bar = (JSONObject) barWithFoo.get("customer");
		barId = bar.getLong("id");
		assertNotNull(barId);

		foo = (JSONObject) barWithFoo.get("contact");
		fooId = foo.getLong("id");
		assertNotNull(fooId);
		Long barIdInFoo = ((JSONObject) foo.get("customer")).getLong("id");		
		assertEquals(barId, barIdInFoo);
	}
}
