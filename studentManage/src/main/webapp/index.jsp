<%--
  Created by IntelliJ IDEA.
  User: liqi1
  Date: 2017/4/19
  Time: 15:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false" %>
<html>
<head>
    <title>学生信息管理系统</title>
    <style type="text/css">
        table {
            border-collapse: collapse;
            width: 80%;
            margin: auto;
            margin-bottom: 4px;
            margin-top: 4px;
        }

        table th {
            color: #fff;
            background-color: #555;
            border: 1px solid #555;
            font-size: 12px;
            padding: 3px;
            vertical-align: top;
            text-align: center;
        }

        tr td {
            line-height: 2em;
            width: 100px;
            height: 40px;
            border: 1px solid #d4d4d4;
            text-align: center;
            vertical-align: top;
        }

        td input {
            text-align: center;
            width: 100%;
            height: 100%;
            border: hidden;
        }

        a {
            text-decoration: none

        }

        div a {
            /*border: solid 0.1px black;*/
            text-decoration: none rgba(68, 68, 68, 0.69)
        }

        .page {
            margin-left: 50%;
        }

        .info {

        }
    </style>
</head>
<body>
<form action="${pageContext.request.contextPath}/management?action=add" method="post">
    <table width="698" border="0" cellpadding="0" cellspacing="0" id="studentInfo">
        <tr>
            <th>id</th>
            <th>name</th>
            <th>birthday</th>
            <th>description</th>
            <th>avgscore</th>
            <th>update</th>
            <th>delete</th>
        </tr>
        <c:forEach items="${studentList}" var="students">
            <tr>
                <td><input type="text" class="info_id" name="id" value="<c:out value='${students.id}'/>" readonly></td>
                <td><input type="text" class="info" name="name" value="<c:out value='${students.name}'/>" readonly></td>
                <td><input type="text" class="info" name="birthday" value="<c:out value='${students.birthday}'/>"
                           readonly></td>
                <td><input type="text" class="info" name="description" value="<c:out value='${students.description}'/>"
                           readonly></td>
                <td><input type="text" class="info" name="avgscore" value="<c:out value='${students.avgscore}'/>"
                           readonly></td>
                <td class="delete"><a
                        href="${pageContext.request.contextPath}/management?action=remove&id=${students.id}">删除</a></td>
                <td class="update">修改</td>
            </tr>
        </c:forEach>
        <tr>
            <td id="add">
                新增
            </td>
        </tr>
    </table>
</form>
<div class="page"><a href="${pageContext.request.contextPath}/management?action=listAllStudent&page=1">首页</a>
    <c:if test="${page>1}"><a href="${pageContext.request.contextPath}/management?action=listAllStudent&page=${page-1}">上一页</a></c:if>
    <c:if test="${page<pageCount}"><a
            href="${pageContext.request.contextPath}/management?action=listAllStudent&page=${page+1}">下一页</a></c:if>
    <a href="${pageContext.request.contextPath}/management?action=listAllStudent&page=${pageCount}">尾页</a>
</div>
<script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
<script type="text/javascript">
    $(function () {
        $("#add").click(function () {
            var newstudent = "<tr><td><input type='text' name='id' id='id' ></td>" +
                "<td><input type='text' name='name' id='name' ></td>" +
                "<td><input type='text' name='birthday' id='birthday'></td>" +
                "<td><input type='text' name='description' id='description' ></td>" +
                "<td><input type='text' name='avgscore' id='avgscore'></td>" +
                "<td colspan='2' id='addconfim'><input type='submit'></td></tr>";
            $("#add").parent().before(newstudent);
        });
        $(".update").click(function () {
            $(this).parent().find(".info").removeAttr("readonly");
            $(this).prev().prev().prev().prev().prev().children().focus();
            $(this).parent().find(".info_id").next().focus();
            $(this).removeClass("update");
            $("#add").unbind().html("<input type='submit'>")
        });
    });
</script>
</body>
</html>