<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Dashboard</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/smoke.min.css">
    <link rel="stylesheet" href="/css/sidebar.css">
    <link rel="stylesheet" href="/css/app.css">

</head>
<body>
<div class="row">
    <jsp:include page="layout/header.jsp"/>
    <jsp:include page="layout/sidebar.jsp">
        <jsp:param name="uri" value=""/>
    </jsp:include>
    <div class="col-lg-10">
        ${template}
        <c:if test="${not empty type && type eq 'search'}">
            <div id="searchList"></div>
        </c:if>
    </div>

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/popper.js"></script>
    <script src="/js/bootstrap.js"></script>
    <script src="/js/sidebar.js"></script>
    <script src="/js/app.js"></script>
    <script src="/js/smoke.min.js"></script>


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
                    url: "/admin/process/getId?uri=${uri}&id=${_id}",
                    type: 'GET',
                    dataType: 'json', // added data type
                    success: function (data) {
                        loadFormData(data);
                        // $('form').loadJSON(data);
                    }
                });
            }

            </c:if>

            function loadFormData(data) {
                for (var i in data) {
                    $('input[name="' + i + '"]').val(data[i]);
                }
            }

            <c:choose>
            <c:when test="${not empty errorMessage}">
            $.smkAlert({
                text: '${errorMessage}',
                type: 'danger'
            });
            <c:if test="${not empty record}">
            loadFormData(${record});
            </c:if>
            </c:when>
            <c:when test="${not empty successMessage}">
            console.log('test');
            $.smkAlert({
                text: '${successMessage}',
                type: 'success'
            });
            </c:when>
            </c:choose>

            <c:choose>
            <c:when test="${not empty type && type eq 'search'}">
            $('button[type="submit"]').on('click', function (e) { //use on if jQuery 1.7+
                e.preventDefault();  //prevent form from submitting
                var data = $("form :input").serializeArray();
                console.log(data); //use the console for debugging, F12 in Chrome, not alerts
                if ($('form').smkValidate()) {
                    $.ajax({
                        url: "/admin/process/search",
                        type: 'POST',
                        data: data,
                        success: function (res) {
                            $('#searchList').html(res);
                        }
                    });
                }

            });

            </c:when>
            <c:when test="${not empty type && type eq 'list'}">

            $('.deleteBtn').on('click', function (e) { //use on if jQuery 1.7+
                e.preventDefault();  //prevent form from submitting
                var href = this.href;
                $.smkConfirm({
                    text: 'Are you sure?',
                    accept: 'Yes',
                    cancel: 'No'
                }, function (res) {
                    if (res) {
                        window.location.href = href
                    }
                });
            });
            </c:when>
            <c:otherwise>
            $('button[type="submit"]').click(function (e) {
                e.preventDefault();
                if ($('form').smkValidate()) {
                    $('form').submit();
                }
            });
            </c:otherwise>
            </c:choose>


        });

    </script>
</div>

</body>
</html>


