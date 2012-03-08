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
        <form name="search" method="post" action="search">
			<table>
 				<tr>
  					<td>Please enter text to find</td>
  					<td><input type="text" name="search_text" id="search_text" value="<c:out value="${search_text}"/>"></td>
  					<td>
  					    <select name="price_option" id="price_option">
  					        <c:forEach items="${requestScope.price_options}" var="option" varStatus="status">
                                <option value="<c:out value="${option}"/>"
                                    <c:if test="${option == price_option}">
                                        selected
                                    </c:if>
                                ><c:out value="${option}"/></option>
      			            </c:forEach>
                        </select>
                    </td>
                    <td><input type="text" name="price" id="price" value="<c:out value="${price}"/>"></td>
                    <td><input type="submit" value="Search" id="search_button"></td>
 				</tr>
			</table>
 		</form>

        <c:if test="${requestScope.records != null}">
            <c:choose>
                <c:when test="${requestScope.no_results}">
                    Sorry no results for your request, but we have another devices:
                </c:when>
                <c:otherwise>
                    List:
                </c:otherwise>
            </c:choose>
            </br>
            <table>
                <tr>
                    <td></td>
                    <td>Description</td>
                    <td>Price</td>
                </tr>
                <c:forEach items="${requestScope.records}" var="record" varStatus="status">
                    <tr>
                        <td><input type="checkbox" value="<c:out value="${status.index+1}"/>" name="record"></td>
                        <td>'<c:out value="${record.description}"/>'</td>
                        <td><c:out value="${record.price}"/>$</td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
	</body>
</html>