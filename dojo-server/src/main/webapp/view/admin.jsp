<%--@elvariable id="release" type="org.automation.dojo.ReleaseEngine"--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<form:form commandName="configuration" action="admin" method="POST">
    <table>
        <tr>
            <td><form:errors path="minorReleaseFrequency"/></td>
        </tr>
        <tr>
        <td>Minor release frequency: <form:input path="minorReleaseFrequency"/></td>
        </tr>
        <tr>
            <td colspan="3">
                <input type="submit" value="Save"/>
            </td>
        </tr>
    </table>
</form:form>
<br/>
Release info
<span id="indicator">Now we have major ${majorNumber} and minor ${minorInfo}</span> </br>
<table>
    <tr>
        <td>Current Major Release</td>
        <td>${majorNumber}</td>
        <td><a name="next_major" href="<c:url value="/admin/nextMajor"/>">Next major</a></td>
    </tr>
    <tr>
        <td>Current Minor Release</td>
        <td>${minorNumber}</td>
        <td><a name="next_minor" href="<c:url value="/admin/nextMinor"/>">Next minor</a></td>
    </tr>
</table>
<br/>
Current scenarios
<table>
    <tr>
        <td>Scenario Id</td><td>Scenario description</td><td>Bug Id</td><td>Bug description</td>
    </tr>
    <c:forEach items="${release.scenarios}" var="scenario" varStatus="status">
        <tr>
            <td>${scenario.id}</td><td>${scenario.description}</td><td>${scenario.bug.id}</td><td>${scenario.bug}</td>
        </tr>
    </c:forEach>
</table>

Registered players:
<table>
    <tr>
        <td>Name</td>
    </tr>
    <c:forEach items="${players}" var="record" varStatus="status">
        <tr>
            <td><a href="/logs/${record}">${record}</a></td>
        </tr>
    </c:forEach>
</table>