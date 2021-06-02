    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <aside class="col-xs-12 col-md-12 col-lg-2 side-menu pr-0">
        <nav class="left-nav hidden-xs hidden-sm hidden-md">

        <ul class="nolist">
        <li>
        <a href="/admin/dashboard" >Dashboard</a>
        </li>
        <li>
        <a href="/admin/editor" >Editor</a>
        </li>
        <li>
        <a href="/admin/jsontemplate" >JSON Template</a>
        </li>
        <li>
        <a href="#">Employee</a>
        <ul class="nolist">
            <li>
            <a href="/admin/employee?type=list">List</a>
            </li>
        <li>
        <a href="/admin/employee?type=create">Add</a>
        </li>

            <li>
            <a href="/admin/employee?type=search">Search</a>
            </li>
        </ul>
        </li>
        </ul>
        </nav>
        </aside>

