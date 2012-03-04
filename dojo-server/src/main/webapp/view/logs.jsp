<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;">
		<title>Logs for ${playerName}</title>
	</head>
	<body>
	Logs for ${playerName}:<br>
	    <table>
	        <tr>
	            <td>Scenario</td><td>Test result</td><td>Description</td><td>Log type</td>
	        </tr>
            <c:forEach items="${releaseLogs}" var="releaseLog" varStatus="statusRelease">
                <tr>
                    <td colspan=4>Release ${statusRelease.index + 1}</td>
                </tr>

                <c:forEach items="${releaseLog}" var="playerRecord" varStatus="statusRecord">
                <tr>
                    <td>${playerRecord.scenario.id}</td><td>${playerRecord.passed}</td><td>${playerRecord.description}</td><td>${playerRecord.type}</td>
                </tr>
                </c:forEach>
            </c:forEach>
	    </table>
	</body>
</html>