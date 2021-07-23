<%--
  Created by IntelliJ IDEA.
  User: liangtao
  Date: 2021/7/23
  Time: 9:08
  To change this template use File | Settings | File Templates.
--%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page pageEncoding="UTF-8"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>UserList Info Show</title>
</head>
<body>
<h3 style="color: red;">用户信息展示 </h3>

<div id="showInfo">
    <c:forEach var="listValue" items="${userList}">
        <h4><li>${listValue}</li></h4>
    </c:forEach>
</div>
</body>
</html>
