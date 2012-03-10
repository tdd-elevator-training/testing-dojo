<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
			<table id="search_box">
 				<tr>
  					<td>Please enter text to find</td>
  					<td><input type="text" name="search_text" id="search_text" value="${search_text}"/></td>
  					<td>
  					    <select name="price_search_option" id="price_search_option">
  					        <c:forEach items="${requestScope.price_search_options}" var="search_option" varStatus="status">
                                <option value="${search_option}"
                                    <c:if test="${search_option == price_search_option}">
                                        selected
                                    </c:if>
                                >${search_option}</option>
      			            </c:forEach>
                        </select>
                    </td>
                    <td><input type="text" name="price" id="price" value="${price}"/></td>
                    <td><input type="submit" value="Search" id="search_button"/></td>
 				</tr>
			</table>