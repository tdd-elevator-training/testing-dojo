<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Search form</title>
	</head>
	<body>		
        <span id="indicator">Now we have major <c:out value="${requestScope.major}"/> and minor <c:out value="${requestScope.minor}"/></span> </br>
        <a name="next_minor" href="/Shop/releases?next_minor=true">Next minor</a></br>
        <a name="next_major" href="/Shop/releases?next_major=true">Next major</a>
	</body>
</html>