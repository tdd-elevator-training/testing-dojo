<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
			<script type='text/javascript'>
	            function validatePrice() {
                    <c:if test="${requestScope.validate_price_number}">
                        var value = document.getElementById('price').value;
                        var validationInfo = document.getElementById('validation_info');
                        var searchButton = document.getElementById('search_button');
                        if (!isInteger(value)) {
                            validationInfo.innerHTML = "price must be an positive integer";
                            searchButton.disabled = true;
                        } else {
                            validationInfo.innerHTML = "";
                            searchButton.disabled = false;
                        }

                        function isInteger(n) {
                            return !(/[^0-9]/).test(n);
                        }
                    </c:if>
                }
            </script>
			<table id="search_box">
 				<tr>
  					<td>Please enter text to find</td>
                    <td><input type="text" name="search_text" id="search_text" value="${search_text}" maxLength="${search_text_max_length}"/></td>
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
                    <td><input type="text" name="price" id="price" value="${price}" maxLength="10" onBlur="validatePrice();"/></td>
                    <td><span id="validation_info"></span></td>
                    <td><input type="submit" value="Search" id="search_button"/></td>
 				</tr>
			</table>