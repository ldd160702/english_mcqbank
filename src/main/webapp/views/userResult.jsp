<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Duc
  Date: 24/07/2023
  Time: 22:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>${user.fullName}'s result</h1>
<hr>
<table border="1">
    <tr>
        <th>Result ID</th>
        <th>User Name</th>
        <th>Exam Id</th>
        <th>Score</th>
        <th>Date</th>
    </tr>
    <c:forEach items="${results}" var="result">
        <tr>
            <td>${result.id}</td>
            <td>${result.user.username}</td>
            <td>${result.exam.id}</td>
            <td>${result.score}/${result.exam.questionNo}</td>
            <td>${result.time}</td>
        </tr>
    </c:forEach>
</table>
<c:if test="${currentPage > 0}">
    <a href="?page=${currentPage - 1}">Prev</a>
</c:if>
<c:if test="${hasNext}">
    <a href="?page=${currentPage + 1}">Next</a>
</c:if>
<hr>

<a href="${pageContext.request.contextPath}/user/profile">Back</a>
</body>
</html>
