package com.cspdemo.testapp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chanjet.csp.platform.test.HttpMethodEnum;
import com.chanjet.csp.platform.test.HttpResponse;
import com.chanjet.csp.platform.test.RestfulIT;

public class DemoIT extends RestfulIT {
  
  @Test
  public void testGetUserInfoByMobileToken() throws Exception{
    HttpResponse response = this.doAppHttpByMobileToken("isvtest","/user/info", HttpMethodEnum.GET, null, null, null, false);
    assertEquals(200, response.getStatusCode());
    String body = response.getString();
    JSONObject bodyObject = JSON.parseObject(body);
    assertNotNull(bodyObject.get("username"));
  }
  
  @Test
  public void testGetUserInfoByWebSession() throws Exception{
    HttpResponse response = this.doAppHttpByWebSession("isvtest","/user/info", HttpMethodEnum.GET, null, null, null, false);
    assertEquals(200, response.getStatusCode());
    String body = response.getString();
    JSONObject bodyObject = JSON.parseObject(body);
    assertNotNull(bodyObject.get("username"));
  }
  
  @Test
  public void testWebLogout() throws Exception{
    HttpResponse response = this.doGet("isvtest","/user/info",null);
    assertEquals(200, response.getStatusCode());
    String body = response.getString();
    JSONObject bodyObject = JSON.parseObject(body);
    assertNotNull(bodyObject.get("username"));
    
    //logout
    this.getAppUser("isvtest").webLogout();
    response = this.doGet("isvtest","/user/info", null);
    assertEquals(401, response.getStatusCode());
    //relogin
    this.getAppUser("isvtest").getWebSession(true);
    
    response = this.doGet("isvtest","/user/info", null);
    assertEquals(200, response.getStatusCode());
    body = response.getString();
    bodyObject = JSON.parseObject(body);
    assertNotNull(bodyObject.get("username"));
  }
  
  @Test
  public void testMobileLogout() throws Exception{
    HttpResponse response = this.doAppHttpByMobileToken("isvtest","/user/info", HttpMethodEnum.GET, null, null, null, false);
    assertEquals(200, response.getStatusCode());
    String body = response.getString();
    JSONObject bodyObject = JSON.parseObject(body);
    assertNotNull(bodyObject.get("username"));
    
    //logout
    this.getAppUser("isvtest").mobileLogout();
    response = this.doAppHttpByMobileToken("isvtest","/user/info", HttpMethodEnum.GET, null, null, null, false);
    assertEquals(401, response.getStatusCode());
    //relogin
    this.getAppUser("isvtest").getMobileToken(true);
    
    response = this.doAppHttpByMobileToken("isvtest","/user/info", HttpMethodEnum.GET, null, null, null, false);
    assertEquals(200, response.getStatusCode());
    body = response.getString();
    bodyObject = JSON.parseObject(body);
    assertNotNull(bodyObject.get("username"));
  }

}
