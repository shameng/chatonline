<%--
  Created by IntelliJ IDEA.
  User: bang
  Date: 2017/2/7
  Time: 15:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>授权登陆</title>
</head>
<body>
    <div>使用chatonline帐号访问 [${client.clientName}] ，并同时登录chatonline</div>
    <div class="error">${errorMsg}</div>

    <form action="" method="post">
        用户名：<input type="text" name="account" value="<shiro:principal/>"><br/>
        密码：<input type="password" name="password"><br/>
        <input type="submit" value="登录并授权">
    </form>
</body>
</html>
