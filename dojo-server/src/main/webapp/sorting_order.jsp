<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                            <script type='text/javascript'>
                                function sortingOrderOnChange() {
                                    document.forms[0].submit();
                                }
                            </script>
                            <select name="price_sorting_order_option" id="price_sorting_order_option" onChange="sortingOrderOnChange();">
                                <c:forEach items="${requestScope.asc_desc_options}" var="order_option" varStatus="status">
                                    <option value="${order_option}"
                                        <c:if test="${order_option == price_sorting_order_option}">
                                            selected
                                        </c:if>
                                    >${order_option}</option>
                                </c:forEach>
                            </select>