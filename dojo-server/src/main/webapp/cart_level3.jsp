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
            <input name="price_sorting_order_option" type="hidden" value="<c:out value="${price_sorting_order_option}"/>">
			<table>   <!-- TODO extract to xml -->
 				<tr>
  					<td>Please enter text to find</td>
  					<td><input type="text" name="search_text" id="search_text" value="<c:out value="${search_text}"/>"></td>
  					<td>
  					    <select name="price_search_option" id="price_search_option">
  					        <c:forEach items="${requestScope.price_search_options}" var="search_option" varStatus="status">
                                <option value="<c:out value="${search_option}"/>"
                                    <c:if test="${search_option == price_search_option}">
                                        selected
                                    </c:if>
                                ><c:out value="${search_option}"/></option>
      			            </c:forEach>
                        </select>
                    </td>
                    <td><input type="text" name="price" id="price" value="<c:out value="${price}"/>"></td>
                    <td><input type="submit" value="Search" id="search_button"></td>
 				</tr>
			</table>

            <c:if test="${requestScope.records != null}">
                <c:choose>
                    <c:when test="${requestScope.no_results}">
                        Your cart is empty now!
                    </c:when>
                    <c:otherwise>
                        Your cart list:
                    </c:otherwise>
                </c:choose>
                </br>
                <table id="product_list">
                    <tr>
                        <td>Code</td>
                        <td>Description</td>
                        <td>Price</td>
                    </tr>
                    <c:forEach items="${requestScope.records}" var="record" varStatus="status">
                        <tr id="productId_${record.id}">
                            <td id="id"><c:out value="${record.id}"/></td>
                            <td id="description">'<c:out value="${record.description}"/>'</td>
                            <td id="price"><c:out value="${record.price}"/>$</td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td>Total:</td>
                        <td></td>
                        <td><span id="total_price"><c:out value="${requestScope.total_price}"/>$</span></td>
                    </tr>
                </table>
                <td><input type="submit" value="Back" id="search_button"></td>
            </c:if>
	    </form>
	</body>
</html>