<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Duc
  Date: 24/07/2023
  Time: 20:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>AddExam</title>
</head>
<body>
    <form:form action="${pageContext.request.contextPath}/admin/addExam" method="post">
        <label for="questionNo">QuestionNo:</label>
        <input type="text" name="questionNo" id="questionNo">
        <br>
        <label for="topic">Topic:</label>
        <c:forEach items="${topics}" var="topic">
            <input type="radio" name="topicId" id="topic" value="${topic.id}"> ${topic.name} <br>
        </c:forEach>
        <button type="submit">Add</button>
    </form:form>
</body>
</html>
