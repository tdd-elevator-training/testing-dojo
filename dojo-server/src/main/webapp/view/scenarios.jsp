<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
        <meta http-equiv="refresh" content="5">
        <meta http-equiv="Content-Type" content="text/html;">
		<title>Scenarios</title>
        <link href="${ctx}/resources/style/bootstrap.css" rel="stylesheet">
	</head>
	<body>
        <div class="container">
            <jsp:include page="followus.jsp"/>
            <table width="100%">
                <c:forEach items="${scenarios}" var="scenario" varStatus="statusScenario">
                    <tr>
                        <td>${scenario}</br></br></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
	</body>
</html>