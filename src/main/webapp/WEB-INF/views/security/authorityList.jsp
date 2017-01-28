<%--
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
    <title>权限管理</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/common.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/security.css">
    <script type="text/javascript" language="JavaScript" src="${contextPath}/js/jquery-1.12.1.js"></script>
    <script type="text/javascript" language="JavaScript">
        function deleteAuthority(authId) {
            //菜单类型还是权限类型
            var clazz = $("#"+authId+"authority").attr("class");
            var flag = false;
            if (clazz == "menu")
                flag = confirm("确定要删除该菜单类型权限吗？\r\n提示：其他属于该菜单类型权限的权限也会一起被删除");
            else
                flag = confirm("确定要删除该权限吗？");
            if (flag)
            {
                var url = "authority/deleteAuthority";
                var args = {"authId":authId, "time":new Date()};
                $.get(url, args, function (data) {
                    if (data == 1)
                        if (clazz == "menu")
                            $("#"+authId+"menu").remove();
                        else
                            $("#"+authId+"authority").remove();
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
                    权限管理
                    <span style="float: right;">
                        <a href="${contextPath}/authority/newAuthority">新建权限</a>
                    </span>
                </div>
                <div id="securityList">
                    <table border="1px" cellspacing="0" style="text-align: left">
                        <tr>
                            <th>权限名称</th>
                            <th>权限类型</th>
                            <th>URL</th>
                            <th>权限代码</th>
                            <th>可用的</th>
                            <th>公共的</th>
                            <th>编辑</th>
                            <th>删除</th>
                        </tr>
                        <c:forEach items="${authorities}" var="menu">
                            <tbody id="${menu.key.id}menu">
                                <tr class="menu" id="${menu.key.id}authority">
                                    <td>${menu.key.name}</td>
                                    <td>菜单</td>
                                    <td>${menu.key.url}</td>
                                    <td>${menu.key.code}</td>
                                    <td></td>
                                    <td></td>
                                    <%--<td>${menu.key.available? "是" : "否"}</td>--%>
                                    <%--<td>${menu.key.common? "是" : "否"}</td>--%>
                                    <td><a href="authority/editAuthority?authId=${menu.key.id}">编辑</a></td>
                                    <td><a href="javascript:deleteAuthority('${menu.key.id}')">删除</a></td>
                                </tr>
                                <c:forEach items="${menu.value}" var="auth">
                                    <tr class="auth" id="${auth.id}authority">
                                        <td>${auth.name}</td>
                                        <td>权限</td>
                                        <td>${auth.url}</td>
                                        <td>${auth.code}</td>
                                        <td>${auth.available? "是" : "否"}</td>
                                        <td>${auth.common? "是" : "否"}</td>
                                        <td><a href="authority/editAuthority?authId=${auth.id}">编辑</a></td>
                                        <td><a href="javascript:deleteAuthority('${auth.id}')">删除</a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>
    </center>
</body>
</html>
