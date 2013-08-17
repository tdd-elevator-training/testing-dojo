<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Registration page</title>
    <link href="/resources/style/bootstrap.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <jsp:include page="followus.jsp"/>
        <form:form commandName="player" action="register" method="POST">
            <table>
                <tr>
                    <td>Player name:<form:errors path="name"/></td>
                </tr>
                <tr>
                    <td><form:input path="name"/></td>
                </tr>
                <tr>
                    <td colspan="3">
                        <input type="submit" value="Register"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</body>
</html>