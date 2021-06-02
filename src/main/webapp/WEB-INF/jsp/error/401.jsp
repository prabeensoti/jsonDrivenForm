<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="id" dir="ltr">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <meta name="description" content="" />
    <meta name="author" content="" />

    <!-- Title -->
    <title>Sorry, This Page Required Authentication</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css" />
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB" crossorigin="anonymous" />
</head>

<body class="bg-dark text-white py-5">
<div class="container py-5">
    <div class="row">
        <div class="col-md-2 text-center">
            <p><i class="fa fa-exclamation-triangle fa-5x"></i><br/>Status Code: 401</p>
        </div>
        <div class="col-md-10">
            <h3>OPPSSS!!!! Sorry...</h3>
            <p>Sorry, your access is refused due to security reasons of our server and also our sensitive data.<br/>Please click on login page link below to get access to this page.</p>
            <a class="btn btn-success" href=/login">Login</a>
        </div>
    </div>
</div>

</body>
