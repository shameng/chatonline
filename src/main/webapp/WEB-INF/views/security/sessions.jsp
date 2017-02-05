<%--
  Created by IntelliJ IDEA.
  User: bang
  Date: 2017/2/2
  Time: 18:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fn" uri="http://meng.com/chatonline/tags/functions" %>
<html>
<head>
    <title>会话管理</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/common.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/security.css">
</head>
<body>
<center>
    <div id="headDiv">
        <jsp:include page="../head.jsp"></jsp:include>
        <jsp:include page="../navigation.jsp"></jsp:include>
        <div class="headPanel">
            <div class="headText">
                会话管理
            </div>
            <div id="securityList">
                <div><font class="msg">${msg}</font></div>
                <div class="left">目前共有${sessionCount}会话</div>
                <table border="1px" cellspacing="0" style="text-align: center;">
                    <thead>
                        <th style="width: 150px;">会话ID</th>
                        <th>账号</th>
                        <th>昵称</th>
                        <th>主机IP</th>
                        <th>最后访问时间</th>
                        <th>已强制退出</th>
                        <th>操作</th>
                    </thead>
                    <tbody>
                        <c:forEach items="${sessions}" var="session">
                            <c:if test="${!fn:getUserAccount(session).equals('')}">
                                <tr>
                                    <td style="width: 150px;">${session.id}</td>
                                    <td>${fn:getUserAccount(session)}</td>
                                    <td>${fn:getUserName(session)}</td>
                                    <td>${session.host}</td>
                                    <td><fmt:formatDate value="${session.lastAccessTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                                    <td>${fn:isForceLogout(session)}</td>
                                    <td>
                                        <c:if test="${!fn:isMyself(session, sessionScope.user.account)}">
                                            <c:if test="${!fn:isForceLogout(session)}">
                                                <shiro:hasPermission name="session:forceLogout">
                                                    <a href="session/${session.id}/forceLogout">强制退出</a>
                                                </shiro:hasPermission>
                                            </c:if>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</center>
</body>
</html>
