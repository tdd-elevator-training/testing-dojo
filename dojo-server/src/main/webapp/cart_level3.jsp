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
            <input name="price_sorting_order_option" type="hidden" value="${price_sorting_order_option}"/>
            <input type="hidden" name="action" id="action" value="search"/>
			<jsp:include page="search_box.jsp"/>

            <c:if test="${requestScope.records != null}">
                <span id="search_info">
                    <c:choose>
                        <c:when test="${requestScope.no_results}">
                            Your cart is empty now!
                        </c:when>
                        <c:otherwise>
                            Your cart list:
                        </c:otherwise>
                    </c:choose>
                </span></br>
                <table id="product_list">
                    <tr>
                        <td>Description</td>
                        <td>Price</td>
                    </tr>
                    <c:forEach items="${requestScope.records}" var="record" varStatus="status">
                        <tr id="productId_${record.id}">
                            <td id="element_description">'${record.description}'</td>
                            <td id="element_price">${record.price}$</td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td>Total:</td>
                        <td><span id="total_price">${requestScope.total_price}$</span></td>
                    </tr>
                </table>
                <td><input type="submit" value="Back" id="search_button"></td>
            </c:if>
	    </form>
	</body>
</html>