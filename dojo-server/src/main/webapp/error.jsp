<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Error</title>
	</head>
	<body>
		<h2>Error</h2>
		<b><span id="error_info"><c:out value="${requestScope.error_message}"/></span></b></br>
		<span id="link_home">Please <a href="/Shop/index.jsp">go home</a> and try again.</span>
	</body>
</html>