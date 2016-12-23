<!-- this is protected url  -->
<%@ page import="com.chanjet.csp.bo.api.*" %>
<%@ page import="com.chanjet.csp.appmanager.AppWorkManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<script type="text/javascript"  src="http://static.chanapp.chanjet.com/third-lib/dojo/1.9.6/dojo/dojo.js"></script>
<script type="text/javascript">
require(["dojo/on",
         "dojo/request/script",
         "dojo/request",
         "dojo/dom",
         "dojo/html",
         "dojo/domReady!"], function(on, script, request, dom, html){

	on(dom.byId("logout"),"click", function(){
		var logout_url = "services/1.0/user/logout";
		var after_logout_url = "index.jsp";
		//logout app
		request(logout_url).then(
			function(data){
				// data - response from server
				console.log(data);
				window.location.href = after_logout_url;
			},function(error){
				console.log(error);
				window.location.href = after_logout_url;
			});
	});
});
</script>
<body>
<h1> 欢迎来到畅捷通！！</h1>
<%
  BoDataAccessManager boDaMgr = AppWorkManager.getBoDataAccessManager();
  BoSession boSession = boDaMgr.getBoSession();
  IBusinessObjectHome boHome = AppWorkManager.getBusinessObjectManager().getPrimaryBusinessObjectHome("CSPUser");
  IBusinessObjectRow user = boHome.query(boSession, AppWorkManager.getCurrAppUserId());
%>

<h2>with UserId: <%= AppWorkManager.getCurrAppUserId() %>,
User Name: <%= user.getFieldValue(boSession, "username") %></h2>
<h2><a href="#" id="logout">退出登录</a></h2>

<h2><a href="services?_wadl&_type=xml">查看WADL</a></h2>

</body>
</html>
