<%--
  Created by IntelliJ IDEA.
  User: bang
  Date: 2016/11/2
  Time: 23:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="common.jsp"%>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/common.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/login.css">
    <title>登陆</title>
</head>
<body>
<center>
    <div id="loginDiv">
        <img src="${contextPath}/images/lufei3.jpg">
        <form action="login" method="post" >
            <div class="loginText">在线聊天系统</div>
            <br>
            账号:
            <input id="username" name="account" type="text">
            <br>
            &nbsp;密码:
            <input id="password" name="password" type="password">
            <br>
            <font class="error">${errorMsg}</font>
            <br>
            <input type="submit" value="登陆">
            <div>
                <a href="#">找回密码</a>&nbsp;
                <a href="${contextPath}/register">注册账号</a>
            </div>
        </form>
    </div>
</center>
</body>
</html>
