<%--
  Created by IntelliJ IDEA.
  User: bang
  Date: 2017/1/6
  Time: 15:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ include file="common.jsp"%>
<div id="navigation">
    <script type="text/javascript">
        //使指向本页面的超链接失效，防止用户重复点击
        $(function () {
            var url = document.location + "";
            url = url.substring(url.lastIndexOf("/") + 1);
            $("#navigation a").each(function () {
                var href = $(this).attr("href");
                href = href.substring(href.lastIndexOf("/") + 1);
                $(this).click(function () {
                    if (url.indexOf(href) >= 0)
                        return false;
                })
            })
        })
    </script>
    <shiro:hasPermission name="chatRoom:chatRoom">
        [<a href="${contextPath}/chatRoom" target="_blank">聊天室</a>]&nbsp;&nbsp;
    </shiro:hasPermission>
    <shiro:hasPermission name="broadcast:query">
        [<a href="${contextPath}/broadcast" target="_blank">公告</a>]&nbsp;&nbsp;
    </shiro:hasPermission>
    <shiro:hasPermission name="user:query">
        [<a href="${contextPath}/user" target="_blank">用户管理</a>]&nbsp;&nbsp;
    </shiro:hasPermission>
    <shiro:hasPermission name="role:query">
        [<a href="${contextPath}/role" target="_blank">角色管理</a>]&nbsp;&nbsp;
    </shiro:hasPermission>
    <shiro:hasPermission name="authority:query">
        [<a href="${contextPath}/authority" target="_blank">权限管理</a>]
    </shiro:hasPermission>
</div>

