<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Welcome codenjoyer!</title>
    <link href="${ctx}/resources/style/bootstrap.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <jsp:include page="followus.jsp"/>
        <table>
            <tr>
                <td><a href="${ctx}/resources/files/template-java.zip">Dojo client</a> (<a href="http://codenjoy.com/portal/?p=46#instructions">how to start</a>)</td>
            </tr>
            <tr>
                <td><a href="${ctx}/register">Registration page</a></td>
            </tr>
            <tr>
                <td><a href="${ctx}/search">Application page (non-stable version)</a></td>
            </tr>
            <tr>
                <td><a href="${ctx}/production">Application page (production server)</a></td>
            </tr>
            <tr>
                <td><a href="${ctx}/scenarios">Current scenarios</a></td>
            </tr>
            <tr>
                <td><a href="${ctx}/board">Leader board</a></td>
            </tr>
        </table>
    </div>
</body>
</html>