<%--
  Created by IntelliJ IDEA.
  User: bang
  Date: 2017/1/24
  Time: 22:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ include file="/WEB-INF/views/common.jsp"%>
<html>
<head>
    <title>切换/授予身份</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/common.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/security.css">
    <script type="text/javascript" language="JavaScript" src="${contextPath}/js/jquery-1.12.1.js"></script>
</head>
<body>
<center>
    <div id="headDiv">
        <jsp:include page="../head.jsp"></jsp:include>
        <jsp:include page="../navigation.jsp"></jsp:include>
        <div class="headPanel">
            <div class="headText">切换/授予身份</div>
            <div id="securityList">
                <div><font class="msg">${msg}</font></div>
                <div class="left">
                    <c:if test="${isRunAs}">
                        上一个身份：${previousUserName}&nbsp;&nbsp;
                        <a href="${contextPath}/userRunAs/switchBack">切换回该身份</a>
                        <br>
                        <a href="${contextPath}/userRunAs/switchBackToMe">切换回自己身份</a>
                    </c:if>
                </div>
                <br>
                <div class="left">切换到其他身份：</div>
                <c:choose>
                    <c:when test="${empty grantedUsersByOthers}">无</c:when>
                    <c:otherwise>
                        <table>
                            <thead>
                                <tr>
                                    <td>账号</td>
                                    <td>昵称</td>
                                    <td>操作</td>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${grantedUsersByOthers}" var="user">
                                    <tr>
                                        <td>${user.account}</td>
                                        <td>${user.name}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${user.id == runAsUserId}">
                                                    (目前为该身份)
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="${contextPath}/userRunAs/switchTo/${user.id}">切换到该身份</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
                <br>
                <div class="left">我授予的身份：</div>
                <c:choose>
                    <c:when test="${empty grantedUsersByMyself}">无</c:when>
                    <c:otherwise>
                        <table>
                            <thead>
                            <tr>
                                <td>账号</td>
                                <td>昵称</td>
                                <td>操作</td>
                            </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${grantedUsersByMyself}" var="user">
                                    <tr>
                                        <td>${user.account}</td>
                                        <td>${user.name}</td>
                                        <td><a href="${contextPath}/userRunAs/revoke/${user.id}">回收身份</a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
                <br>
                <div class="left">授予其他人我的身份：</div>
                <c:choose>
                    <c:when test="${empty grantUsersByMyself}">无</c:when>
                    <c:otherwise>
                        <table>
                            <thead>
                            <tr>
                                <td>账号</td>
                                <td>昵称</td>
                                <td>操作</td>
                            </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${grantUsersByMyself}" var="user">
                                    <tr>
                                        <td>${user.account}</td>
                                        <td>${user.name}</td>
                                        <td><a href="${contextPath}/userRunAs/grant/${user.id}">授予身份</a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</center>
</body>
</html>
