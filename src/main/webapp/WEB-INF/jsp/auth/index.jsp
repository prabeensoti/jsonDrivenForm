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
<div class="row justify-content-center">
    <jsp:include page="../admin/layout/header.jsp"/>
    <div class="col-lg-10">
        ${template}
    </div>
    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/popper.js"></script>
    <script src="/js/bootstrap.js"></script>

    <script>
        $(document).ready(function () {
            var $form = $('form');
            var fieldHTML = '<input type="hidden" name="uri" value="${uri}"/>' <c:if test="${not empty _id}"> + '<input type="hidden" name="_id" value="${_id}"/>'
                </c:if>;
            $($form).append(fieldHTML);
            <c:if test="${not empty _id}">
            loadData();
            </c:if>

            <c:if test="${not empty _id}">
            function loadData() {
                $.ajax({
                    url: "/auth/process/getId?uri=${uri}&id=${_id}",
                    type: 'GET',
                    dataType: 'json', // added data type
                    success: function (data) {
                        for (var i in data) {
                            $('input[name="' + i + '"]').val(data[i]);
                        }
                    }
                });
            }

            </c:if>
        });

    </script>
</div>

</body>
</html>


