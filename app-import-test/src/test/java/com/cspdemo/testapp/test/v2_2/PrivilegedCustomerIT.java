package com.cspdemo.testapp.test.v2_2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chanjet.csp.platform.test.HttpResponse;
import com.chanjet.csp.platform.test.RestfulIT;

public class PrivilegedCustomerIT extends RestfulIT {

	private CustomerService customerService = new CustomerService();
	private static final String CUSTOMER_ENTITY_NAME = "Customer";
	private static final String DEFAULT_BAR_NAME = "bar";
	
	private static final String BOSS_NAME = "boss";
	private static final String SALESMAN_NAME = "salesman";
	private static final String BOSS_ROLE_NAME = "boss";
	private static final String SALESMAN_ROLE_NAME = "salesman";
	private Long bossId;
	private Long salesmanId;
	private Long bossRoleId; 
	private Long salesmanRoleId;
	private Long bossUserRoleId; 
	private Long salesmanUserRoleId;	
	private List<Long> salesmanAssignmentIds;
	private List<Long> bossAssignmentIds;
	private static final String ALL_PRIVILEGE = "ALL_PRIVILEGE";
	private static final String INSERT_PRIVILEGE = "INSERT";
	private static final String SELECT_PRIVILEGE = "SELECT";	
	private static final String UPDATE_PRIVILEGE = "UPDATE";
	private static final String DELETE_PRIVILEGE = "DELETE";
	// 
	private static final Long CUSTOMER_CUR_URL_RULE_ID = 10001L;
	private static final Long CUSTOMER_DELETE_URL_RULE_ID = 10002L;
	private List<Long> bossRoleUrlRuleIds = new ArrayList<>();	
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		setIsCspServletURL(true);
		
		bossId = Long.valueOf(this.getAppUser(BOSS_NAME).getUserId());
		salesmanId = Long.valueOf(this.getAppUser(SALESMAN_NAME).getUserId());		

		enableAppPrivilege();
		enableEntityPrivilegeOf(CUSTOMER_ENTITY_NAME);
		enableUserPrivilegeOf(bossId);
		enableUserPrivilegeOf(salesmanId);
		
		bossRoleId = createRoleOf(BOSS_ROLE_NAME);
		salesmanRoleId = createRoleOf(SALESMAN_ROLE_NAME);
		
		bossUserRoleId = createUserRoleOf(bossId, bossRoleId);
		salesmanUserRoleId = createUserRoleOf(salesmanId, salesmanRoleId);
		
		bossAssignmentIds = assignBossPrivilegesTo(bossRoleId, CUSTOMER_ENTITY_NAME);
		salesmanAssignmentIds = assignSalesmanPrivilegesTo(salesmanRoleId, CUSTOMER_ENTITY_NAME);		
	}
	
	@After
	public void tearDown() throws Exception {
		setIsCspServletURL(true);
		
		deleteUserRoleOf(bossUserRoleId);
		deleteUserRoleOf(salesmanUserRoleId);
		
		deleteAssignedPrivilegesOf(bossAssignmentIds);
		deleteAssignedPrivilegesOf(salesmanAssignmentIds);
		
		deleteAssignedUrlRulesOf(bossRoleUrlRuleIds);
		
		deleteRoleOf(bossRoleId);
		deleteRoleOf(salesmanRoleId);
		
		disableAppPrivilege();
		disableEntityPrivilege(CUSTOMER_ENTITY_NAME);
		disableUserPrivilege(bossId);
		disableUserPrivilege(salesmanId);
		
		disableUrlAccessRule();
	}

	private void deleteAssignedUrlRulesOf(List<Long> roleUrlRuleIds) throws Exception {
		if (roleUrlRuleIds == null || roleUrlRuleIds.isEmpty()) {
			return;
		}
		
		for (Long roleUrlRuleId : roleUrlRuleIds) {
			// DELETE - http://localhost:8080/cspdemo/testapp/services/1.0/dataauth/roleurlrule/{roleUrlRuleId}
			HttpResponse response = this.doDelete("admin", "/dataauth/roleurlrule/" + roleUrlRuleId, null);
			assertEquals(200, response.getStatusCode());	
		}		
	}

	@Test
	public void shouldPermitBossToAddNew() throws Exception {	
		JSONObject bar = customerService.addNewBy(BOSS_NAME, DEFAULT_BAR_NAME);		
		assertNotNull(bar.getLong("id"));
	}
	
	@Test
	public void shouldPermitBossToQuery() throws Exception {
		// Given:
		JSONObject bar = customerService.addNewBy(BOSS_NAME, DEFAULT_BAR_NAME);		
		assertNotNull(bar.getLong("id"));

		// When:
		// enable the boss to query the bar created by self
		Long barId = bar.getLong("id");
		JSONObject sameBar = customerService.getBy(BOSS_NAME, barId);	

		// Then: check the two bars are the same
		assertEquals(barId, sameBar.getLong("id"));				
	}
	
	@Test
	public void shouldPermitBossToUpdate() throws Exception {
		// Given:
		JSONObject bar = customerService.addNewBy(BOSS_NAME, DEFAULT_BAR_NAME);		
		assertNotNull(bar.getLong("id"));
		// the name to modify
		String newBarName = "barbar";
		Map<String, Object> newBarMap = new HashMap<String, Object>();
		newBarMap.put("name", newBarName);

		// When:
		// enable the boss to update the bar
		Long barId = bar.getLong("id");
		JSONObject updatedBar = customerService.updateBy(BOSS_NAME, barId, newBarMap);	

		// Then: check the name has been updated
		assertEquals(barId, updatedBar.getLong("id"));
		assertEquals(newBarName, updatedBar.get("name"));
	}
	
	@Test
	public void shouldPermitBossToDelete() throws Exception {
		// Given:
		JSONObject bar = customerService.addNewBy(BOSS_NAME, DEFAULT_BAR_NAME);		
		assertNotNull(bar.getLong("id"));

		// When:
		// enable the boss to delete the bar
		Long barId = bar.getLong("id");
		customerService.deleteBy(BOSS_NAME, barId);	

		// Then: check the bar has been deleted	
		assertNull(customerService.getBy(SALESMAN_NAME, barId));
	}
	
	@Test
	public void shouldPermitSalesmanToQuery() throws Exception {
		// Given:
		JSONObject barCreatedByBoss = customerService.addNewBy(BOSS_NAME, DEFAULT_BAR_NAME);		
		assertNotNull(barCreatedByBoss.getLong("id"));

		// When:
		// enable the salesman to query the bar created by the boss
		Long barId = barCreatedByBoss.getLong("id");
		JSONObject sameBar = customerService.getBy(SALESMAN_NAME, barId);	

		// Then: check the two bars are the same
		assertEquals(barId, sameBar.getLong("id"));				
	}

	@Test
	public void shouldNotPermitSalesmanToAddNew() throws Exception {
		// disable the salesman to insert
		thrown.expect(RuntimeException.class);		
		customerService.addNewBy(SALESMAN_NAME, DEFAULT_BAR_NAME);
	}
	
	@Test
	public void shouldNotPermitSalesmanToUpdate() throws Exception {
		// Given:
		JSONObject barCreatedByBoss = customerService.addNewBy(BOSS_NAME, DEFAULT_BAR_NAME);		
		assertNotNull(barCreatedByBoss.getLong("id"));
		// the name to modify
		String newBarName = "barbar";
		Map<String, Object> newBarMap = new HashMap<String, Object>();
		newBarMap.put("name", newBarName);
		
		// When & Check
		// disable the salesman to update the bar created by the boss
		thrown.expect(RuntimeException.class);	
		Long barId = barCreatedByBoss.getLong("id");
		customerService.updateBy(SALESMAN_NAME, barId, newBarMap);		
	}
	
	@Test
	public void shouldNotPermitSalesmanToDelete() throws Exception {
		// Given:
		JSONObject barCreatedByBoss = customerService.addNewBy(BOSS_NAME, DEFAULT_BAR_NAME);		
		assertNotNull(barCreatedByBoss.getLong("id"));		
		
		// When & Check
		// disable the salesman to delete the bar created by the boss
		thrown.expect(RuntimeException.class);		
		Long barId = barCreatedByBoss.getLong("id");
		customerService.deleteBy(SALESMAN_NAME, barId);		
	}
	
	@Test 
	public void shouldPermitSalesmanToUpdateWhenGrantedByBoss() throws Exception {
		// Given:		
		JSONObject barCreatedByBoss = customerService.addNewBy(BOSS_NAME, DEFAULT_BAR_NAME);
		Long barId = barCreatedByBoss.getLong("id");
		assertNotNull(barId);
		// grant update privilege of the bar to salesman by boss
		Long grantId = grant(barId, BOSS_NAME, SALESMAN_NAME, CUSTOMER_ENTITY_NAME, UPDATE_PRIVILEGE);
		
		// When:
		// the name to modify
		String newBarName = "barbar";
		Map<String, Object> newBarMap = new HashMap<String, Object>();
		newBarMap.put("name", newBarName);
		// enable the salesman to update the bar	
		JSONObject updatedBar = customerService.updateBy(BOSS_NAME, barId, newBarMap);	

		// Then: check the name has been updated
		assertEquals(barId, updatedBar.getLong("id"));
		assertEquals(newBarName, updatedBar.get("name"));
		
		// Tear down
		deleteGrantOf(grantId, BOSS_NAME);
	}
	
	@Test 
	public void shouldPermitSalesmanToDeleteWhenGrantedByBoss() throws Exception {
		// Given:		
		JSONObject barCreatedByBoss = customerService.addNewBy(BOSS_NAME, DEFAULT_BAR_NAME);
		Long barId = barCreatedByBoss.getLong("id");
		assertNotNull(barId);
		// grant delete privilege of the bar to salesman by boss
		grant(barId, BOSS_NAME, SALESMAN_NAME, CUSTOMER_ENTITY_NAME, DELETE_PRIVILEGE);
		
		// When:	
		// enable the salesman to update the bar	
		customerService.deleteBy(BOSS_NAME, barId);	

		// Then: check the bar has been deleted		
		assertNull(customerService.getBy(SALESMAN_NAME, barId));
		
		// Tear down:
		//deleteGrantOf(grantId, BOSS_NAME);
	}
	
	@Test
	public void shouldNotPermitBossToDeleteWhenHasNotDeleteUrlRule() throws Exception {
		// Given:
		// enable the data auth of url rule
		enableUrlAccessRule();
		// assign the CRD url rule privileges to the boss
		bossRoleUrlRuleIds.add(assignUrlRuleTo(bossRoleId, CUSTOMER_CUR_URL_RULE_ID));
		// add a new Customer
		JSONObject bar = customerService.addNewBy(BOSS_NAME, DEFAULT_BAR_NAME);		
				
		// When & Check
		// the boss cannot delete the bar, without the delete url rule
		thrown.expect(RuntimeException.class);	
		Long barId = bar.getLong("id");
		customerService.deleteBy(BOSS_NAME, barId);		
		
	}
	
	@Test
	public void shouldPermitBossToDeleteWhenHasDeleteUrlRule() throws Exception {
		// Given:
		// enable the data auth of url rule
		enableUrlAccessRule();
		// assign the CRD url rule privileges to the boss
		bossRoleUrlRuleIds.add(assignUrlRuleTo(bossRoleId, CUSTOMER_CUR_URL_RULE_ID));		
		// add a new Customer
		JSONObject bar = customerService.addNewBy(BOSS_NAME, DEFAULT_BAR_NAME);		
				
		// When:
		// assign the delete url rule privileges to the boss
		bossRoleUrlRuleIds.add(assignUrlRuleTo(bossRoleId, CUSTOMER_DELETE_URL_RULE_ID));
		// the boss can delete the bar, with the delete url rule
		Long barId = bar.getLong("id");
		customerService.deleteBy(BOSS_NAME, barId);		
		
		// Then: check the bar has been deleted	;
		JSONObject nullBar = customerService.getBy(BOSS_NAME, barId);
		assertNull(nullBar);			
	}

	private Long assignUrlRuleTo(Long roleId, Long urlRuleId) throws Exception {
		JSONObject roleUrlRuleJson = new JSONObject();
		roleUrlRuleJson.put("roleId", roleId);
		roleUrlRuleJson.put("urlRuleId", urlRuleId);
		
		JSONArray roleUrlRules = new JSONArray();
		roleUrlRules.add(roleUrlRuleJson);
		
		setIsCspServletURL(true);
		// POST - http://localhost:8080/cspdemo/testapp/services/1.0/dataauth/roleurlrule
		HttpResponse response = this.doPost("admin", "/dataauth/roleurlrule/", roleUrlRules);
		assertEquals(200, response.getStatusCode());
		
		String roleUrlRulesString = response.getString();
		Long roleUrlRuleId = JSONArray.parseArray(roleUrlRulesString).getJSONObject(0).getLong("id");
		assertNotNull(roleUrlRuleId);
		return roleUrlRuleId;	
		
	}

	private void enableUrlAccessRule() throws Exception {
		// PUT - http://localhost:8080/cspdemo/testapp/services/1.0/dataauth/urlaccess/{status}
		HttpResponse response = this.doPut("admin", "/dataauth/urlaccess/enable", null);
		assertEquals(200, response.getStatusCode());
	}

	private void disableUrlAccessRule() throws Exception {	
		// PUT - http://localhost:8080/cspdemo/testapp/services/1.0/dataauth/urlaccess/{status}
		HttpResponse response = this.doPut("admin", "/dataauth/urlaccess/disable", null);
		assertEquals(200, response.getStatusCode());
	}

	private Long grant(Long objectId, String granter, String grantee, String entityName, String privilege) throws Exception {
		Long granterId = Long.valueOf(this.getAppUser(granter).getUserId());
		Long granteeId = Long.valueOf(this.getAppUser(grantee).getUserId());
		
		Map<String, Object> grantMap = new HashMap<>();
		grantMap.put("objectId", objectId);
		grantMap.put("entityName", entityName);
		grantMap.put("granterId", granterId);
		grantMap.put("granteeId", granteeId); 
		grantMap.put("dataAuthPrivilege", privilege); 
		
		setIsCspServletURL(true);
		// POST - http://localhost:8080/cspdemo/testapp/services/1.0/dataauth/grants
		HttpResponse response = this.doPost(granter, "/dataauth/grants/", grantMap);
		assertEquals(200, response.getStatusCode());
		
		return getIdFrom(response);
	}

	private void deleteGrantOf(Long grantId, String granter) throws Exception {
		if (grantId == null) {
			return;
		}
		
		setIsCspServletURL(true);
		
		// DELETE - http://localhost:8080/cspdemo/testapp/services/1.0/dataauth/grants/{grantId}
		HttpResponse response = this.doDelete(granter, "/dataauth/grants/" + grantId, null);
		assertEquals(200, response.getStatusCode());		
	}

	// boss role has all privileges, including insert, select, update, delete
	private List<Long> assignBossPrivilegesTo(Long roleId, String entityName) throws Exception {
		List<Long> assignmentIds = new ArrayList<>();
		assignmentIds.add(assignInsertPrivilegeTo(roleId, entityName));
		assignmentIds.add(assignAllPrivilegeTo(roleId, entityName));
		return assignmentIds;
	}

	// salesman role only has query privilege
	private List<Long> assignSalesmanPrivilegesTo(Long roleId, String entityName) throws Exception {
		List<Long> assignmentIds = new ArrayList<>();
		assignmentIds.add(assignSelectPrivilegeTo(roleId, entityName));
		return assignmentIds;
	}
	
	private Long assignSelectPrivilegeTo(Long roleId, String entityName) throws Exception {
		return assignPrivilegeTo(roleId, SELECT_PRIVILEGE, entityName);
	}
	
	private Long assignInsertPrivilegeTo(Long roleId, String entityName) throws Exception {
		return assignPrivilegeTo(roleId, INSERT_PRIVILEGE, entityName);
	}
	
	private Long assignAllPrivilegeTo(Long roleId, String entityName) throws Exception {
		return assignPrivilegeTo(roleId, ALL_PRIVILEGE, entityName);
	}
	
	private Long assignPrivilegeTo(Long roleId, String privilege, String entityName) throws Exception {
		Map<String, Object> assignmentMap = new HashMap<>();
		assignmentMap.put("entityName", entityName);
		assignmentMap.put("privilege", privilege);
		assignmentMap.put("condition", "id > 0"); 
	
		// POST - http://localhost:8080/cspdemo/testapp/services/1.0/dataauth/assignments/role/{roleId}
		HttpResponse response = this.doPost("admin", "/dataauth/assignments/role/" + roleId, assignmentMap);
		assertEquals(200, response.getStatusCode());
		
		return getIdFrom(response);
	}

	private void deleteAssignedPrivilegesOf(List<Long> assignmentIds) throws Exception {
		if (assignmentIds == null || assignmentIds.isEmpty()) {
			return;
		}
		
		for (Long assignmentId : assignmentIds) {
			// DELETE - http://localhost:8080/cspdemo/testapp/services/1.0/dataauth/assignments/{assignmentId}
			HttpResponse response = this.doDelete("admin", "/dataauth/assignments/" + assignmentId, null);
			assertEquals(200, response.getStatusCode());	
		}		
	}

	private void enableAppPrivilege() throws Exception {
		// PUT - http://localhost:8080/cspdemo/testapp/services/1.0/dataauth/privilege/apps/{status}
		HttpResponse response = this.doPut("admin", "/dataauth/privilege/apps/enable", null);
		assertEquals(200, response.getStatusCode());		
	}

	private void enableEntityPrivilegeOf(String entityName) throws Exception {
		// PUT - http://localhost:8080/cspdemo/testapp/services/1.0/dataauth/privilege/entities/{entityName}/{status}
		HttpResponse response = this.doPut("admin", "/dataauth/privilege/entities/" + entityName + "/enable", null);
		assertEquals(200, response.getStatusCode());
	}

	private void enableUserPrivilegeOf(Long userId) throws Exception {
		// PUT - http://localhost:8080/cspdemo/testapp/services/1.0/dataauth/privilege/users/{uid}/{status}
		HttpResponse response = this.doPut("admin", "/dataauth/privilege/users/" + userId + "/enable", null);
		assertEquals(200, response.getStatusCode());				
	}

	private void disableAppPrivilege() throws Exception {
		// PUT - http://localhost:8080/cspdemo/testapp/services/1.0/dataauth/privilege/apps/{status}
		HttpResponse response = this.doPut("admin", "/dataauth/privilege/apps/disable", null);
		assertEquals(200, response.getStatusCode());
	}

	private void disableEntityPrivilege(String entityName) throws Exception {
		// PUT - http://localhost:8080/cspdemo/testapp/services/1.0/dataauth/privilege/entities/{entityName}/{status}
		HttpResponse response = this.doPut("admin", "/dataauth/privilege/entities/" + entityName + "/disable", null);
		assertEquals(200, response.getStatusCode());
	}

	private void disableUserPrivilege(Long userId) throws Exception {
		// PUT - http://localhost:8080/cspdemo/testapp/services/1.0/dataauth/privilege/users/{uid}/{status}
		HttpResponse response = this.doPut("admin", "/dataauth/privilege/users/" + userId + "/disable", null);
		assertEquals(200, response.getStatusCode());
	}

	private Long createRoleOf(String roleName) throws Exception {
		Map<String, Object> roleMap = new HashMap<String, Object>();		
		roleMap.put("name", roleName);
	
		// 创建角色
		// POST - http://localhost:8080/cspdemo/testapp/services/1.0/dataauth/role
		HttpResponse response = this.doPost("admin", "/dataauth/role/", roleMap);
		assertEquals(200, response.getStatusCode());
		
	    return getIdFrom(response);
	}

	private void deleteRoleOf(Long roleId) throws Exception {
		if (roleId == null) {
			return;
		}
		
		// DELETE - http://localhost:8080/cspdemo/testapp/services/1.0/dataauth/role/{roleId}
		HttpResponse response = this.doDelete("admin", "/dataauth/role/" + roleId, null);		
		assertEquals(200, response.getStatusCode());		
	}

	private Long createUserRoleOf(Long userId, Long roleId) throws Exception {
		List<Map<String, Object>> userRoles = new ArrayList<Map<String, Object>>();
	    Map<String, Object> userRole = new HashMap<String, Object>();
	    userRole.put("userId", userId);
	    userRole.put("roleId", roleId);
	    userRoles.add(userRole);
	
	    // POST - http://localhost:8080/cspdemo/testapp/services/1.0/dataauth/userrole
	    HttpResponse response = this.doPost("admin", "/dataauth/userrole/", userRoles);		
		assertEquals(200, response.getStatusCode());
		
	    String userRoleString = response.getString();
		Long userRoleId = JSONArray.parseArray(userRoleString).getJSONObject(0).getLong("id");
		assertNotNull(userRoleId);
		return userRoleId;
	}

	private void deleteUserRoleOf(Long userRoleId) throws Exception {
		if (userRoleId == null) {
			return;
		}
		
		// DELETE - http://localhost:8080/cspdemo/testapp/services/1.0/dataauth/userrole/{userRoleId}
		HttpResponse response = this.doDelete("admin", "/dataauth/userrole/" + userRoleId, null);		
		assertEquals(200, response.getStatusCode());

	}

	private Long getIdFrom(HttpResponse response) {
		String objectString = response.getString();
		Long objectId = JSONObject.parseObject(objectString).getLong("id");
		assertNotNull(objectId);
		return objectId;
	}
}
