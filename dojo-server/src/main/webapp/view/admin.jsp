<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%--@elvariable id="release" type="org.automation.dojo.ReleaseEngine"--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>Admin page</title>
    <link href="/resources/style/bootstrap.css" rel="stylesheet">
</head>
<body>
<h3>Admin page</h3>

<div class="span10">
    <form:form commandName="configuration" action="${adminPage}" method="POST" class="well-large">
        <table>
            <tr>
                <td><form:errors path="minorReleaseFrequency"/></td>
            </tr>
            <tr>
                <td>Minor release frequency, ms: <form:input path="minorReleaseFrequency"/></td>
            </tr>
            <tr>
                <td>Manual release triggering: <form:checkbox path="manualReleaseTriggering"/></td>
            </tr>
            <tr>
                <td><form:errors path="penaltyTimeOut"/></td>
            </tr>
            <tr>
                <td>Silence timeout for penalty, ms : <form:input path="penaltyTimeOut"/></td>
            </tr>
            <tr>
                <td><form:errors path="penaltyValue"/></td>
            </tr>
            <tr>
                <td>Penalty value for silence : <form:input path="penaltyValue"/></td>
            </tr>
            <tr>
                <td>Liar weight : <form:input path="liarWeight"/></td>
            </tr>
            <tr>
                <td>Exception weight : <form:input path="exceptionWeight"/></td>
            </tr>
            <tr>
                <td><c:if test="${configuration.paused}"><a href="${ctx}/${adminPage}/resume">Resume Game</a></c:if>
                    <c:if test="${!configuration.paused}"><a href="${ctx}/${adminPage}/pause">Pause Game</a></c:if></td>
            </tr>
            <tr>
                <td><a href="${ctx}/${adminPage}/clearLogs">Clear logs</a></td>
            </tr>

            <tr>
                <td colspan="3">
                    <input type="submit" class="btn" value="Save"/>
                </td>
            </tr>
        </table>
    </form:form>
    <br/>

    <div class="row-fluid">
        Release info
        <span id="indicator">Now we have major ${majorNumber} and minor ${minorInfo}</span> </br>
        Next release in: ${configuration.nextReleaseRemaining} <br>
        Next penlty tick at: ${configuration.nextPenaltyTickTime} <br>
    </div>
    <br/>
    <table class="table">
        <tr>
            <td>Current major release</td>
            <td>${majorNumber}</td>
            <td><a name="next_major" href="${ctx}/${adminPage}/nextMajor">Next major</a></td>
        </tr>
        <tr>
            <td>Current minor release</td>
            <td>${minorNumber}</td>
            <td><a name="next_minor" href="${ctx}/${adminPage}/nextMinor">Next minor</a></td>
        </tr>
    </table>
    <br/>
    Current scenarios
    <table class="table table-bordered">
        <tr>
            <td>Scenario Id</td>
            <td>Scenario description</td>
            <td>Bug Id</td>
            <td>Bug description</td>
        </tr>
        <c:forEach items="${release.scenarios}" var="scenario" varStatus="status">
            <tr>
                <td>${scenario.id}</td>
                <td>${scenario.description}</td>
                <td>${scenario.bug.id}</td>
                <td>${scenario.bug}</td>
            </tr>
        </c:forEach>
    </table>
    </br>
    Registered players:
    <table class="table table-bordered">
        <tr>
            <td>Name</td>
        </tr>
        <c:forEach items="${players}" var="record" varStatus="status">
            <tr>
                <td><a href="${ctx}/logs/${record}">${record}</a></td>
            </tr>
        </c:forEach>
    </table>
</div>

</body>
</html>
