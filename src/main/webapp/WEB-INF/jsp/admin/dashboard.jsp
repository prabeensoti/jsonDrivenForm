
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Dashboard</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/sidebar.css">
</head>
<body>
<div class="row">
    <jsp:include page="layout/header.jsp"/>
    <jsp:include page="layout/sidebar.jsp">
        <jsp:param name="uri" value=""/>
    </jsp:include>

    dashboard here
    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/popper.js"></script>
    <script src="/js/bootstrap.js"></script>
    <script src="/js/sidebar.js"></script>
</div>

</body>
</html>
