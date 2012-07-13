<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
        <meta http-equiv="refresh" content="5">
		<meta http-equiv="Content-Type" content="text/html;">
		<title>Leader Board</title>
        <link href="/resources/style/bootstrap.css" rel="stylesheet">
    </head>
	<body>
    <div class="container">
	    <h3>Leader Board</h3>
        <jsp:include page="releaseInfo.jsp"/>
	    <table id="table-logs" class="table table-striped">
            <thead>
                <th>#</th><th>Player</th><th>Total Score</th>
            </thead>
            <tbody>
            <c:forEach items="${records}" var="record" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td><td><a href="/logs/${record.player}">${record.player}</a></td><td>${record.total}</td>
                </tr>
            </c:forEach>
            </tbody>
	    </table>
    </div>
	</body>
</html>