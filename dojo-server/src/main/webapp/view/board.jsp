<%@ page import="org.automation.dojo.ScoreService" %>
<%@ page import="static org.automation.dojo.ScoreService.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="refresh" content="15">
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
        <th width="5%">#</th>
        <th width="20%">Player</th>
        <th width="60%">Relative Score</th>
        <th width="15%">Absolute Score</th>
        </thead>
        <tbody>
        <c:forEach items="${records}" var="record" varStatus="status">
            <tr>
                <td>${status.index + 1}</td>
                <td>
                    <c:set var="superman"><%=SUPERMAN%></c:set>
                    <c:set var="looser"><%=LOOSER%></c:set>

                    <c:choose>
                            <c:when test="${record.player == superman}">
                                <img src="/resources/images/superman_new.png" alt="Superman">
                            </c:when>
                            <c:when test="${record.player == looser}">
                                <img src="/resources/images/tortoise_new.png" alt="Tortoise">
                            </c:when>
                            <c:otherwise>
                            <a href="/logs/${record.player}">
                                ${record.player}
                            </a>
                            </c:otherwise>
                        </c:choose>
                    </td>
                <td align="center">
                    <div class="progress">
                        <div class="bar" style="width: ${record.relativeScore}%;"></div>
                    </div>
                </td>
                <td>
                    ${record.total}
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<img/>

</body>
</html>