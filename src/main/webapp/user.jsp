<%--
  Created by IntelliJ IDEA.
  User: bang
  Date: 2017/1/13
  Time: 0:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>测试user</title>
</head>
<body>
你好!<shiro:principal property="name"/>
<shiro:user>测试通过666666666666</shiro:user>
</body>
</html>
