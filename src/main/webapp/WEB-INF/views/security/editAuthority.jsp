<%--
  Created by IntelliJ IDEA.
  User: bang
  Date: 2017/1/6
  Time: 13:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common.jsp"%>
<%@ page import="com.meng.chatonline.constant.Constants" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>权限编辑</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/common.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/security.css">
    <script type="text/javascript" language="JavaScript" src="${contextPath}/js/jquery-1.12.1.js"></script>
    <script type="text/javascript">
        $(function () {
            hideOrShowAuthTr($(":radio[name='type']:checked"));
            $(":radio[name='type']").change(function () {
                hideOrShowAuthTr(this);
            })

            //隐藏还是显示所属菜单表单
            function hideOrShowAuthTr(typeRadio) {
                if ($(typeRadio).val() == <%=Constants.AUTH_TYPE%>)
                    $(".authTr").show();
                else
                    $(".authTr").hide();
            }

            $("form").submit(function () {
                $(":text[name='url']").next("font").remove();
                var url = $(":text[name='url']").val();
                if(url.indexOf("/") != 0)
                {
                    $(":text[name='url']").after("<font class='error'>必须以\"/\"开头</font>");
                    return false;
                }
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
                <a href="${contextPath}/authority">权限管理</a>&nbsp;>>&nbsp;编辑权限&nbsp;>>&nbsp;提交
            </div>
            <div class="headPanel">
                <div class="headText">权限编辑</div>
                <form:form action="editAuthority" method="post" modelAttribute="authority">
                    <form:hidden path="id"/>
                    <table class="formTable">
                        <tr>
                            <td>权限名称：</td>
                            <td>
                                <form:input path="name" class="text"/>
                                <span class="error"><form:errors path="name"/></span>
                            </td>
                        </tr>
                        <tr>
                            <td>权限类型：</td>
                            <td>
                                <c:set var="menuType" value="<%=Constants.MENU_TYPE%>"></c:set>
                                <c:choose>
                                    <c:when test="${authority != null && authority.type == menuType}">
                                        菜单
                                        <form:hidden path="type"/>
                                    </c:when>
                                    <c:otherwise>
                                        <form:radiobutton path="type" value="${menuType}"/>菜单
                                        <form:radiobutton path="type" value="<%=Constants.AUTH_TYPE%>"/>权限
                                        <span class="error"><form:errors path="type"/></span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <td>URL：</td>
                            <td>
                                <form:input path="url" class="text"/>
                                <span class="error"><form:errors path="url"/></span>
                            </td>
                        </tr>
                        <tr>
                            <td>权限代码：</td>
                            <td>
                                <form:input path="code" class="text"/>
                                <span class="error"><form:errors path="code"/></span>
                            </td>
                        </tr>
                        <tr class="authTr">
                            <td>所属菜单：</td>
                            <td><form:select path="menu.id" items="${menus}" itemLabel="name" itemValue="id"/></td>
                        </tr>
                        <tr class="authTr">
                            <td>是否公共的：</td>
                            <td><form:checkbox path="common"/></td>
                        </tr>
                        <tr class="authTr">
                            <td>是否可用：</td>
                            <td><form:checkbox path="available"/></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td><input type="submit" value="提交"/></td>
                        </tr>
                    </table>
                </form:form>
            </div>
        </div>
    </center>
</body>
</html>
