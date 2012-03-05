<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;">
		<title>Leader Board</title>
	</head>
	<body>
	Leader Board<br>
	    <table>
	        <tr>
	            <td>#</td><td>Player</td><td>Total Score</td>
	        </tr>
            <c:forEach items="${records}" var="record" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td><td>${record.player}</td><td>${record.total}</td>
                </tr>
            </c:forEach>
	    </table>
	</body>
</html>