<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>JSON Driven Template</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/sidebar.css">
</head>
<body>
<div class="row">
    <jsp:include page="layout/header.jsp"/>
    <jsp:include page="layout/sidebar.jsp">
        <jsp:param name="uri" value="<%= request.getRequestURI() %>"/>
    </jsp:include>

    <div class="col-lg-10">
        <div class="col-lg-12 mt-3">
            <form method="POST" action="/admin/jsontemplate">
                <input name="_id" type="text" class="form-control mb-2" id="_id" value="${template._id}" hidden>
                <div class="align-items-center">
                    <div class="col-lg-4">
                        <label for="functionName">Function Name</label>
                        <input name="name" type="text" class="form-control mb-2" id="functionName"  value="${template.name}" placeholder="Function Name">
                    </div>

                    <div class="col-lg-4">
                        <label for="accessURL">Access URL</label>
                        <input name="url" type="text" class="form-control mb-2" id="accessURL" value="${template.url}" placeholder="Access URL">
                    </div>

                    <div class="col-lg-4">
                        <button type="submit" class="btn btn-primary mb-2">Submit</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<!-- Javascript files-->
<script src="/js/jquery-3.2.1.min.js"></script>
<script src="/js/popper.js"></script>
<script src="/js/bootstrap.js"></script>
<script src="/js/sidebar.js"></script>
</body>
</html>
