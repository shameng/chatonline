<%--
  Created by IntelliJ IDEA.
  User: bang
  Date: 2017/2/12
  Time: 14:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common.jsp"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>客户端管理</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/common.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/security.css">
    <script type="text/javascript" language="JavaScript" src="${contextPath}/js/jquery-1.12.1.js"></script>
    <script type="text/javascript" language="JavaScript">
        function newClient() {
            var clientName = prompt("新建客户端名：", "");
            if (clientName == null)
                return;
            if (clientName == "")
            {
                alert("客户端名不能为空！");
                return;
            }
            var url = "${contextPath}/client/newClient";
            var args = {"clientName":clientName, "time":new Date()};
            $.post(url, args, function (data) {
                if (data == null)
                    alert("发生错误，请稍后重试！");
                else
                {
                    $("tbody").append("<tr><td>"+data.clientName+"</td><td>"+data.clientId+"</td>" +
                            "<td>"+data.clientSecret+"</td>" +
                            "<td><a href='${contextPath}/client/editClient?cId="+data.id+"'>编辑</a>&nbsp;" +
                            "<a href='${contextPath}/client/deleteClient?cId="+data.id+"'>删除</a>" +
                            "</td></tr>");
                }
            })
        }
        function editClient(cId, clientName) {
            var clientName = prompt("新建客户端名：", clientName);
            if (clientName == null)
                return;
            if (clientName == "")
            {
                alert("客户端名不能为空！");
                return;
            }
            var url = "${contextPath}/client/editClient";
            var args = {"cId":cId, "clientName":clientName, "time":new Date()};
            $.post(url, args, function (data) {
                if (data == 0)
                    alert("发生错误，请稍后重试！");
                else if (data == 1)
                    $("#"+cId+"clientName").html(clientName);
            })
        }
        function deleteClient(cId) {
            if (cId == null)
                return;
            var flag = confirm("确定要删除该客户端吗？");
            if (flag)
            {
                var url = "${contextPath}/client/deleteClient";
                var args = {"cId":cId, "time":new Date()};
                $.post(url, args, function (data) {
                    if (data == 0)
                        alert("发生错误，请稍后重试！");
                    else if (data == 1)
                        $("#"+cId+"clientName").parent().remove();
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
                    客户端管理
                    <shiro:hasPermission name="client:create">
                        <span style="float: right;">
                            <a href="javascript:newClient()">新建客户端</a>
                        </span>
                    </shiro:hasPermission>
                </div>
                <div id="securityList">
                    <table border="1px" cellspacing="0">
                        <thead>
                            <tr>
                                <th width="140px">客户端名</th>
                                <th>客户端ID</th>
                                <th>客户端安全KEY</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${clients}" var="client">
                                <tr>
                                    <td id="${client.id}clientName" width="140px">${client.clientName}</td>
                                    <td>${client.clientId}</td>
                                    <td>${client.clientSecret}</td>
                                    <td>
                                        <shiro:hasPermission name="client:update">
                                            <a href="javascript:editClient('${client.id}','${client.clientName}')">编辑</a>
                                        </shiro:hasPermission>
                                        <shiro:hasPermission name="client:delete">
                                            <a href="javascript:deleteClient('${client.id}')">删除</a>
                                        </shiro:hasPermission>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </center>
</body>
</html>
