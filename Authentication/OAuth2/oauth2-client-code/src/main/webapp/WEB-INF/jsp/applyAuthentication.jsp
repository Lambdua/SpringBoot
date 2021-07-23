<%--
  Created by IntelliJ IDEA.
  User: liangtao
  Date: 2021/7/23
  Time: 9:08
  To change this template use File | Settings | File Templates.
--%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page pageEncoding="UTF-8"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Get Employees</title>
</head>
<body>
<h3 style="color: red;">Apply Authentication </h3>

<div id="getEmployees">
    <form:form action="${serverAuthenticationAddr}"
               method="post" modelAttribute="user">
    <p>
        <label>Enter Employee Id</label>
        <input type="text" name="response_type" value="${response_type}" />
        <input type="text" name="client_id" value="${client_id}" />
        <input type="text" name="redirect_uri" value="${redirect_uri}" />
        <input type="text" name="scope" value="${scope}" />
        <input type="text" name="state" value="${state}">
        <input type="SUBMIT" value="Get Employee info" />
        </form:form>
</div>
</body>
</html>
