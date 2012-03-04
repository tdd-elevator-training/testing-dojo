<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form:form>
    <table>
        <tr>
            <td>Player name:</td>
            <td><form:input path="playerName" /></td>
            <td><form:errors path="playerName" /></td>
           </tr>
        <tr>
            <td colspan="3">
                <input type="submit" value="Register" />
            </td>
        </tr>
    </table>
</form:form>