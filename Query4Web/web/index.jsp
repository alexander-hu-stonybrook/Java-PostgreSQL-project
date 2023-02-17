<%-- 
    Document   : index
    Created on : Apr 7, 2021, 8:21:45 PM
    Author     : engine
--%>

<%@page import="com.cse532project2.QueryToList"%>
<%@page import="java.util.*"%>
<%@page import="java.sql.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<jsp:useBean id="QueryResult" scope="session" class="com.cse532project2.QueryToList"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Query 4</title>
    </head>
    <body>
        <h1>Company Dominance:</h1>
        <table border="1" width="300" cellpadding="5">
            <thead>
                <tr>
                    <th>Dominating Company</th>
                    <th>Dominated Company</th>
                </tr>
            </thead>
            <tbody>
                <select>
                    <c:forEach var="element" items="${QueryResult.query4}">
                        <tr>
                            <td><c:out value="${element[0]}" /></td>
                            <td><c:out value="${element[1]}" /></td>
                        </tr>
                    </c:forEach>
                </select>
            </tbody>
        </table>
        <a href="http://localhost:8080/QueryMasterPage">Back to Index</a>
    </body>
</html>

