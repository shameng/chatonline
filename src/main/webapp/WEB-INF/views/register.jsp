<%--
  Created by IntelliJ IDEA.
  User: bang
  Date: 2016/11/16
  Time: 23:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="common.jsp"%>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="${contextPath}/styles/register.css">
    <link type="text/css" rel="stylesheet" href="${contextPath}/styles/common.css">
    <title>注册用户</title>
    <script type="text/javascript" language="JavaScript" src="${contextPath}/js/jquery-1.12.1.js"></script>
    <script type="text/javascript" language="JavaScript">
        $(function () {
            $("form").submit(function () {
                var account = $.trim($("#account").val());
                var name = $.trim($("#name").val());
                var password = $.trim($("#password").val());
                var confirmPassword = $.trim($("#confirmPassword").val());
                $("#account").next("font").remove();
                $("#name").next("font").remove();
                $("#password").next("font").remove();
                $("#confirmPassword").next("font").remove();
                var flag = true;
                if (account == "")
                {
                    $("#account").after("<font class='error'>&nbsp;账号不能为空</font>");
                    flag = false;
                }
                else
                {
                    var url = "register/checkUserAccount";
                    var args = {"account":account, "time":new Date()};
                    $.get(url, args, function (data) {
                        //若已被注册
                        if (data == 1)
                        {
                            $("#account").after("<font class='error'>&nbsp;该账号已经被注册</font>");
                            flag = false;
                        }
                    })
                }
                if (name == "")
                {
                    $("#name").after("<font class='error'>&nbsp;用户名不能为空</font>");
                    flag = false;
                }
                else
                {
                    var url = "register/checkUserName";
                    var args = {"name":name, "time":new Date()};
                    $.get(url, args, function (data) {
                        //若已被注册
                        if (data == 1)
                        {
                            $("#name").after("<font class='error'>&nbsp;该用户名已经被注册</font>");
                            flag = false;
                        }
                    })
                }
                if (password == "")
                {
                    $("#password").after("<font class='error'>&nbsp;密码不能为空</font>");
                    flag = false;
                }
                if (password != confirmPassword)
                {
                    $("#confirmPassword").after("<font class='error'>&nbsp;两次输入的密码不一致</font>");
                    flag = false;
                }
                if (flag)
                {
                    var url = "register";
                    var args = {"account":account, "name":name, "password":password, "time":new Date()};
                    $.post(url, args, function (data) {
                        if (data == 1)
                        {
                            alert("注册成功，跳转到登录页面");
                            window.location.href = "login";
                        }
                        else
                        {
                            alert("服务器发生错误，注册失败，请稍后重试");
                        }
                    })
                }

                return false;
            })
        })
    </script>
</head>
<body>
    <center>
        <div id="headDiv">
            <div class="headPanel">
                <div class="headText">注册新用户</div>
                <div>
                    <form:form action="register" method="post" modelAttribute="user">
                        <table>
                            <tr>
                                <td class="tdText">账号：</td>
                                <td><form:input path="account"/></td>
                            </tr>
                            <tr>
                                <td class="tdText">用户名：</td>
                                <td><form:input path="name"/></td>
                            </tr>
                            <tr>
                                <td class="tdText">密码：</td>
                                <td><form:password path="password"/></td>
                            </tr>
                            <tr>
                                <td class="tdText">再次输入密码：</td>
                                <td><input type="password" id="confirmPassword"/></td>
                            </tr>
                            <tr>
                                <td></td>
                                <td><input type="submit" value="注册"/></td>
                            </tr>
                        </table>
                    </form:form>
                </div>
            </div>
        </div>
    </center>
</body>
</html>
