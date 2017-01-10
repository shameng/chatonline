<%--
  Created by IntelliJ IDEA.
  User: bang
  Date: 2017/1/8
  Time: 14:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>用户角色编辑</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/common.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/security.css">
    <script type="text/javascript" language="JavaScript" src="${contextPath}/js/jquery-1.12.1.js"></script>
    <script type="text/javascript">
        $(function () {
            $("#toRight").click(function () {
                var size = $("#left > option:selected").size();
                if (size == 0) {
                    $("#left > option:first-child").appendTo($("#right"));
                }
                else
                    $("#right").append($("#left > option:selected"));
            })
            $("#toLeft").click(function () {
                var size = $("#right > option:selected").size();
                if (size == 0) {
                    $("#right > option:first-child").appendTo($("#left"));
                }
                else
                    $("#left").append($("#right > option:selected"));
            })
            $("#toRightAll").click(function () {
                $("#left > option").appendTo($("#right"));
            })
            $("#toLeftAll").click(function () {
                $("#right > option").appendTo($("#left"));
            })

            //提交时全选
            $("form").submit(function () {
                $("#left > option").each(function () {
                    if (this.selected == false)
                        this.selected = true;
                });
                return true;
            })
        })
    </script>
</head>
<body>
<center>
    <div id="headDiv">
        <jsp:include page="../head.jsp"></jsp:include>
        <div id="navigation">
            <a href="${contextPath}/user">用户管理</a>&nbsp;>>&nbsp;编辑用户角色&nbsp;>>&nbsp;提交
        </div>
        <div class="headPanel">
            <div class="headText">用户角色编辑</div>
            <form action="editUserRole" method="post">
                <input type="hidden" name="userId" value="${user.id}"/>
                <table class="formTable">
                    <tr>
                        <td>
                            <table class="formTable">
                                <tr>
                                    <td>用户账号：</td>
                                    <td>${user.account}</td>
                                </tr>
                                <tr>
                                    <td>用户昵称：</td>
                                    <td>${user.name}</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>角色选择(第一个多选框为该用户拥有的角色）：</td>
                    </tr>
                    <tr>
                        <td>
                            <table>
                                <tr>
                                    <td>
                                        <select id="left" name="ownRoleIds" class="multipleSelect" multiple="multiple">
                                            <c:forEach items="${ownRoles}" var="role">
                                                <option value="${role.id}">
                                                    ${role.name}${(role.available?"":"(不可用)")}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <input type="button" id="toRight" value=">" class="btn">
                                        <br><br>
                                        <input type="button" id="toLeft" value="<" class="btn">
                                        <br><br>
                                        <input type="button" id="toRightAll" value=">>" class="btn">
                                        <br><br>
                                        <input type="button" id="toLeftAll" value="<<" class="btn">
                                    </td>
                                    <td>
                                        <select id="right" name="right" class="multipleSelect" multiple="multiple">
                                            <c:forEach items="${notOwnRoles}" var="role">
                                                <option value="${role.id}">
                                                    ${role.name}${(role.available?"":"(不可用)")}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr align="center">
                        <td><input type="submit" value="提交"></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</center>
</body>
</html>

