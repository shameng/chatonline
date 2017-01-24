<%--
  Created by IntelliJ IDEA.
  User: bang
  Date: 2017/1/8
  Time: 11:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common.jsp"%>
<html>
<head>
    <title>用户管理</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/common.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/security.css">
    <script type="text/javascript" language="JavaScript" src="${contextPath}/js/jquery-1.12.1.js"></script>
    <script type="text/javascript">
        function clearAuthorities(userId)
        {
            var flag = confirm("确定要清除该用户的权限吗？");
            if (flag)
            {
                var url = "user/clearAuthorities";
                var args = {"userId":userId, "time":new Date()};
                $.get(url, args, function (data) {
                    if (data == 1)
                        $("#"+userId+"ulRole").remove();
                    else
                        alert("出错，请刷新页面或稍后重试！");
                })
            }
        }
        function deleteUser(userId) {
            var flag = confirm("确定要删除该用户吗？");
            if (flag)
            {
                var url = "user/deleteUser";
                var args = {"userId":userId, "time":new Date()};
                $.get(url, args, function (data) {
                    if (data == 1)
                        $("#"+userId+"user").remove();
                    else
                        alert("出错，请刷新页面或稍后重试！");
                })
            }
        }
    </script>
</head>
<body>
<center>
    <div id="headDiv">
        <jsp:include page="../head.jsp"></jsp:include>
        <jsp:include page="../navigation.jsp"></jsp:include>
        <div class="headPanel">
            <div class="headText">用户管理</div>
            <div id="securityList">
                <table border="1px" cellspacing="0" style="text-align: center;">
                    <tr>
                        <th>用户账号</th>
                        <th>用户昵称</th>
                        <th>包含角色</th>
                        <th>编辑角色</th>
                        <th>清空权限</th>
                        <th>删除用户</th>
                    </tr>
                    <c:forEach items="${users}" var="user">
                        <tr id="${user.id}user">
                            <td>${user.account}</td>
                            <td>${user.name}</td>
                            <td>
                                <ul id="${user.id}ulRole">
                                    <c:if test="${user.superAdmin}">
                                        <li>超级管理员</li>
                                    </c:if>
                                    <c:if test="${!user.superAdmin}">
                                        <c:forEach items="${user.roles}" var="role">
                                            <li class="${role.available?'available':'unavailable'}" title="${role.desc}">${role.name}</li>
                                        </c:forEach>
                                    </c:if>
                                </ul>
                            </td>
                            <c:if test="${user.superAdmin}">
                                <td></td>
                                <td></td>
                                <td></td>
                            </c:if>
                            <c:if test="${!user.superAdmin}">
                                <td><a href="user/editUserRole?userId=${user.id}">编辑角色</a></td>
                                <td><a href="javascript:clearAuthorities('${user.id}')">清空权限</a></td>
                                <td><a href="javascript:deleteUser('${user.id}')">删除用户</a></td>
                            </c:if>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
    </div>
</center>
</body>
</html>
