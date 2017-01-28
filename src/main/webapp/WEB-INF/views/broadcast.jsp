<%--
  Created by IntelliJ IDEA.
  User: bang
  Date: 2016/11/11
  Time: 16:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ include file="/WEB-INF/views/common.jsp"%>
<html>
<head>
    <title>公告</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/common.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/broadcast.css">
    <script type="text/javascript" language="JavaScript" src="${contextPath}/js/jquery-1.12.1.js"></script>
    <script type="text/javascript" language="JavaScript">
        $(function () {
            $(".broadcastContent").each(function () {
                var $this = $(this);
                var content = $this.html();
                $this.html(content.substring(0, 100));
                if (content.length > 100)
                    $this.html($this.html() + "...");

                var aHtml = content.length<100?"":"展开";
                $this.next("div").children("a").html(aHtml);

                $this.next("div").children("a").click(function () {
                    var $athis = $(this);
                    if ($athis.html() == "展开")
                    {
                        $this.html(content);
                        $athis.html("收起");
                    }
                    else
                    {
                        $this.html(content.substring(0, 100) + "...");
                        $athis.html("展开");
                    }
                })
            })
        })
        
        function deleteBroadcast(id) {
            var flag = confirm("确定要删除该公告？");
            if (flag) {
                var url = "broadcast/deleteBroadcast";
                var args = {"id": id, "time": new Date()};
                $.get(url, args, function (data) {
                    if (data == 1) {
                        $("#"+id+"broadcast").remove();
                    }
                    else if (data == 0) {
                        alert("未知错误，删除失败!")
                    }
                })
            }
        }
    </script>
</head>

<body>
    <center>
        <div id="headDiv">
            <jsp:include page="head.jsp"></jsp:include>
            <jsp:include page="navigation.jsp"></jsp:include>
            <div class="headPanel">
                <div class="headText">
                    公告
                    <shiro:hasPermission name="broadcast:new">
                        <span style="float: right;">
                            <a href="${contextPath}/broadcast/newBroadcast">新建公告</a>
                        </span>
                    </shiro:hasPermission>
                </div>
                <div id="broadcastList">
                    <ul>
                        <c:forEach items="${broadcastList}" var="broadcast">
                            <li id="${broadcast.id}broadcast">
                                <div class="broadcastMsgPanel">
                                    <div class="broadcastTitle">${broadcast.title}</div>
                                    <div class="broadcastContent">${broadcast.content}</div>
                                    <div class="controlContent"><a href="javascript:void(0)"></a></div>
                                    <div class="broadcastFrom">
                                        ${broadcast.utterer.name}&nbsp;发表于&nbsp;
                                        <fmt:formatDate value="${broadcast.date}" pattern="yyyy-MM-dd HH:mm"></fmt:formatDate>
                                        <c:if test="${broadcast.utterer.id == sessionScope.principal.id}">
                                            <shiro:hasPermission name="broadcast:delete">
                                                <a href="javascript:deleteBroadcast('${broadcast.id}')">删除</a>
                                            </shiro:hasPermission>
                                        </c:if>
                                    </div>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
    </center>
</body>
</html>
