<%@ page import="com.meng.chatonline.constant.Constants" %><%--
  Created by IntelliJ IDEA.
  User: bang
  Date: 2017/1/6
  Time: 0:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common.jsp"%>
<html>
<head>
    <title>角色管理</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/common.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/security.css">
    <script type="text/javascript" language="JavaScript" src="${contextPath}/js/jquery-1.12.1.js"></script>
    <script type="text/javascript" language="JavaScript">
        function deleteRole(roleId) {
            var flag = confirm("确定要删除该角色吗？");
            if (flag)
            {
                var url = "role/deleteRole";
                var args = {"roleId":roleId, "time":new Date()};
                $.get(url, args, function (data) {
                    if (data == 1)
                        $("#"+roleId+"role").remove();
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
                <div class="headText">
                    角色管理
                    <span style="float: right;">
                        <a href="${contextPath}/role/newRole">新建角色</a>
                    </span>
                </div>
                <div id="securityList">
                    <table border="1px" cellspacing="0" style="text-align: center;">
                        <tr>
                            <th>角色名称</th>
                            <th>包含权限</th>
                            <th>是否公有</th>
                            <th>是否可用</th>
                            <th>编辑</th>
                            <th>删除</th>
                        </tr>
                        <c:set var="menuType" value="<%=Constants.MENU_TYPE%>"></c:set>
                        <c:forEach items="${roles}" var="role">
                            <tr id="${role.id}role">
                                <td>${role.name}</td>
                                <td>
                                    <ul>
                                        <c:forEach items="${role.authorities}" var="auth">
                                            <c:if test="${auth.type != menuType}">
                                                <li class="${auth.available?'available':'unavailable'}" title="${auth.desc}">${auth.name}</li>
                                            </c:if>
                                        </c:forEach>
                                    </ul>
                                </td>
                                <td>${role.common? "是" : "否"}</td>
                                <td>${role.available? "是" : "否"}</td>
                                <c:if test="${role.common}">
                                    <td>包含所有公共权限</td>
                                    <td>不可删除</td>
                                </c:if>
                                <c:if test="${!role.common}">
                                    <td><a href="role/editRole?roleId=${role.id}">编辑</a></td>
                                    <td><a href="javascript:deleteRole('${role.id}')">删除</a></td>
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
