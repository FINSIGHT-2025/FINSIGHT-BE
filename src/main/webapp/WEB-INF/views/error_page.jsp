<%--
  Created by IntelliJ IDEA.
  User: leejinwook
  Date: 2025/06/10
  Time: 11:42 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
  <h4><c:out value="${exception.getMessage()}"></c:out></h4>
  <ul>
    <c:forEach items="${exception.getStackTrace() }" var="stack">
      <li><c:out value="${stack}"></c:out></li> </c:forEach>
  </ul>
</body>
</html>
