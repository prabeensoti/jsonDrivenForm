<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>JSON Driven Template</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css" />
    <style>
        .center-content {
            min-height: 80%; /* Fallback for browsers do NOT support vh unit */
            min-height: 80vh; /* These two lines are counted as one :-)       */

            display: flex;
            align-items: center;
        }

        main {
            color: white;
        }
    </style>
</head>
<body class="bg-dark">
<main class="container">
    <div class="float-right mt-3">
        <a href="/login" class="mt-2 font-weight-bold text-white">Sign In</a>
    </div>
    <div class="row center-content justify-content-center">
        <h1><img src="images/json.png" height="50px" class="d-inline-block align-top" alt="">
            Driven Template</h1>
    </div>
</main>
</body>
</html>
