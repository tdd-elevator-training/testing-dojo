<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Registration page</title>
    <link href="${ctx}/resources/style/bootstrap.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <jsp:include page="followus.jsp"/>
        <form:form commandName="player" action="register" method="POST">
            <table>
                <tr>
                    <td>Player name:<form:errors path="name"/></td>
                </tr>
                <tr>
                    <td><form:input path="name"/></td>
                </tr>
                <tr>
                    <td colspan="3">
                        <input type="submit" value="Register"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</body>
</html>