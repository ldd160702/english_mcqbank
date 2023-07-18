<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Duc
  Date: 17/07/2023
  Time: 19:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Logs</title>
</head>
<body>
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Action</th>
            <th>Status</th>
            <th>Time</th>
            <th>User</th>
        </tr>
        <c:forEach var="log" items="${logs}">
            <tr>
                <td>${log.id}</td>
                <td>${log.name}</td>
                <td>
                    <c:if test="${log.status == 1}">
                        Success
                    </c:if>
                    <c:if test="${log.status == 0}">
                        Fail
                    </c:if>
                </td>
                <td>${log.datetime}</td>
                <td>
                    <c:if test="${log.user != null}">
                        ${log.user.username}
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>

    <a href="${pageContext.request.contextPath}/user/profile">Back</a>
</body>
</html>
