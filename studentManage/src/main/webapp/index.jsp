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

        td input,input:disabled {
            text-align: center;
            width: 100%;
            height: 100%;
            border: hidden ;
            background: #ffffff;
        }

        a {
            text-decoration: none
        }
        a:link,a:visited,a:active{
            color: black;
        }

        div a {
            /*border: solid 0.1px black;*/
            text-decoration: none rgba(68, 68, 68, 0.69)
        }
        .update,#add {
            cursor: hand;
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
                <td><input type="text" class="info_id" name="id" value="<c:out value='${students.id}'/>"  disabled></td>
                <td><input type="text" class="info" name="name" value="<c:out value='${students.name}'/>"  disabled></td>
                <td><input type="text" class="info" name="birthday" value="<c:out value='${students.birthday}'/>"
                           disabled></td>
                <td><input type="text" class="info" name="description" value="<c:out value='${students.description}'/>"
                           placeholder="" disabled></td>
                <td><input type="text" class="info" name="avgscore" value="<c:out value='${students.avgscore}'/>"
                           disabled></td>
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
                var newstudent = "<tr><td><input type='text' name='id' id='id'  placeholder='ID' ></td>" +
                    "<td><input type='text' name='name' id='name' placeholder='姓名'></td>" +
                    "<td><input type='text' name='birthday' id='birthday' placeholder='2017-01-01' ></td>" +
                    "<td><input type='text' name='description' id='description' ></td>" +
                    "<td><input type='text' name='avgscore' id='avgscore' placeholder='80'></td>" +
                    "<td colspan='2' id='addconfim' ><input type='submit' style='cursor: hand'></td></tr>";
                $("#add").parent().before(newstudent);
            $(this).unbind();
        });
        $(".update").click(function () {
            $("input").attr("disabled","true");
            $(this).parent().find(".info").removeAttr("disabled");
            $(this).parent().find(".info_id").removeAttr("disabled").attr("readonly","true");
            $(this).prev().prev().prev().prev().prev().children().focus();
            $(this).removeClass("update");
            $("#add").unbind().html("<input id='add' type='submit'>")
        });
    });
</script>
</body>
</html>