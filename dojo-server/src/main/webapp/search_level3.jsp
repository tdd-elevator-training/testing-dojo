<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Search form</title>
	    <script type='text/javascript'>
	        function setAction(name) {
                var input = document.getElementById('action');
                input.value = name;
            }
        </script>
	</head>
	<body>
        <form name="search" method="post" action="search">
            <input type="hidden" name="action" id="action" value="search">
			<table>
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
                    <td><input type="submit" value="Search" id="search_button" onClick="setAction('search');"></td>
 				</tr>
			</table>

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

                <table id="product_list">
                    <tr>
                        <td>Code</td>
                        <td>Description</td>
                        <td>Price
                            <select name="price_sorting_order_option" id="price_sorting_order_option">
                                <c:forEach items="${requestScope.asc_desc_options}" var="order_option" varStatus="status">
                                    <option value="<c:out value="${order_option}"/>"
                                        <c:if test="${order_option == price_sorting_order_option}">
                                            selected
                                        </c:if>
                                    ><c:out value="${order_option}"/></option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <c:forEach items="${requestScope.records}" var="record" varStatus="status">
                        <tr id="productId_${record.id}">
                            <td id="id">
                                <input type="checkbox" value="<c:out value="${record.id}"/>"
                                       name="record" id="record_${status.index+1}">
                                <c:out value="${record.id}"/>&nbsp;
                            </td>
                            <td id="description">'<c:out value="${record.description}"/>'</td>
                            <td id="price"><c:out value="${record.price}"/>$</td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td></td>
                        <td></td>
                        <td><input type="submit" value="Add to Cart" id="add_to_cart_button" onClick="setAction('cart');"></td>
                    </tr>
                </table>
            </form>
        </c:if>
	</body>
</html>