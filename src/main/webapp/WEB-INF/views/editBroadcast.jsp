<%--
  Created by IntelliJ IDEA.
  User: bang
  Date: 2016/11/13
  Time: 15:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="common.jsp"%>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/common.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/broadcast.css">
    <title>编辑公告</title>
    <script type="text/javascript" language="JavaScript" src="${contextPath}/js/jquery-1.12.1.js"></script>
    <script type="text/javascript" language="JavaScript">
        $(function () {
            $("form").submit(function () {
                var flag = confirm("确定要提交公告？");
                return flag;
            })
        })
    </script>
</head>
<body>
<center>
    <div id="headDiv">
        欢迎你，<font class="userName">${sessionScope.user.name}</font>
        <div id="broadcastPanel">
            <div id="broadcastHeadText">编辑公告</div>
            <div>
                <form:form action="newBroadcast" method="post" modelAttribute="broadcast">
                    <table>
                        <tr>
                            <td>&nbsp;发起人:</td>
                            <td>
                                ${sessionScope.user.name}
                            </td>
                        </tr>
                        <tr>
                            <td>公告标题:</td>
                            <td>
                                <form:input path="title"/>
                                <span class="error"><form:errors path="title"/></span>
                            </td>
                        </tr>
                        <tr>
                            <td>公告内容:</td>
                            <td>
                                <form:textarea path="content"/>
                                <span class="error"><form:errors path="content"/></span>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td align="center">
                                <input type="submit" value="提交"/>
                            </td>
                        </tr>
                    </table>
                </form:form>
            </div>
        </div>
    </div>
</center>
</body>
</html>