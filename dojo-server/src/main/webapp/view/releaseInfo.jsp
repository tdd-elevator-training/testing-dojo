<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<br>
<h1>Current major release ${majorNumber} <br></h1>
<h1>Current minor release ${minorNumber} <br></h1>
<h1>Next minor release in: ${configuration.nextReleaseRemaining} </h1><br>
<h2><a href="${ctx}/scenarios">Scenarios implemented in current release</a></h2> <br>
<br>