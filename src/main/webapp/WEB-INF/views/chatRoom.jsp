<%@ page import="com.meng.chatonline.constant.Constants" %><%--
  Created by IntelliJ IDEA.
  User: bang
  Date: 2016/11/3
  Time: 19:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="common.jsp"%>
<%
    String path = request.getContextPath();
    String basePath = request.getServerName() + ":"
            + request.getServerPort() + path + "/";
    String basePath2 = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <title>在线聊天室</title>
    <%--<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>--%>
    <c:set var="messagePromptColor" value="#FF6A25"></c:set>
    <c:set var="messagePromptRGBColor" value="rgb(255, 106, 37)"></c:set>
    <c:set var="clickUserDivColor" value="#FFF007"></c:set>
    <c:set var="messagePromptImage" value="${contextPath}/images/new.gif"></c:set>
    <c:set var="imageWidth" value="25px"></c:set>
    <c:set var="imageHeight" value="20px"></c:set>
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/chatRoom.css"/>
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/common.css"/>
    <script type="text/javascript" src="${contextPath}/js/jquery-1.12.1.js"></script>
    <script type="text/javascript">
        $(function () {
            var path = "<%=basePath%>";
            var toUserId = "-1";
            var toUserName = "";

            //给所有已经在线的用户添加聊天面板，并且默认是隐藏起来的
            $(".userDiv").each(function () {
                var userId = $(this).children(":hidden").val();
                addChatContentPanel(userId);
            })

            //添加指定用户的聊天内容面板
            function addChatContentPanel(userId) {
                $("#chatRecord").append("<div class='content' id='with"+userId+"content'></div>");
            }

            $(".userDiv").click(function () {
                clickUser(this);
            })

            function clickUser(userDiv) {
                $("#message").attr("disabled", false);

                toUserName = $(userDiv).attr("title");
                toUserId = $(userDiv).children(":hidden").val();

                generateWithWhoPanel(toUserId, toUserName);

                //隐藏除指定ID以外的聊天面板
                showToUserChatPanel(toUserId);

                //改变背景颜色
                <%--$(".userDiv").each(function () {--%>
                    <%--//如果背景颜色不为 #FF6A25即rgb(255, 106, 37)(提示该用户有消息的颜色)--%>
                    <%--if ($(this).css("background-color") != "${messagePromptRGBColor}")--%>
                        <%--$(this).css("background-color", "");--%>
                <%--});--%>
                $(".userDiv").css("background-color", "");
                //设置该div的背景颜色为 #FFF007(点击后的颜色)
                $(userDiv).css("background-color", "${clickUserDivColor}");
                //删除提示消息的图片
                $(userDiv).find("img").remove();
            }

            var websocket;
            //创建 websocket 对象
            if ('WebSocket' in window) {
                websocket = new WebSocket("ws://" + path + "/ws?uid=${sessionScope.user.id}");
            } else if ('MozWebSocket' in window) {
                websocket = new MozWebSocket("ws://" + path + "/ws${sessionScope.user.id}");
            } else {
                //SockJS 是一个浏览器上运行的 JavaScript 库，如果浏览器不支持 WebSocket，
                //该库可以模拟对 WebSocket 的支持，实现浏览器和 Web 服务器之间低延迟、全双工、跨域的通讯通道。
                websocket = new SockJS("http://" + path + "/ws/sockjs${sessionScope.user.id}");
            }

            websocket.onopen = function (event) {
                console.log("WebSocket已连接")
                console.log(event);
            }
            websocket.onerror = function (event) {
                console.log("WebSocket发生错误");
                console.log(event);
            }
            websocket.onclose = function (event) {
                console.log("WebSocket已关闭");
                console.log(event);
            }
            websocket.onmessage = function (event) {
                var data = JSON.parse(event.data);
                //如果不是公告
                if (data.type == null) {
                    console.log("WebSocket:收到一条消息", data);
                    $("#with" + data.fromUser.id + "content").append("<div class='remoteMsgPanel'>" +
                            "<div class='remoteMsgDate'>" + data.fromUser.name + "&nbsp;" + data.date +
                            "</div><div class='remoteMsg'>" + data.text + "</div></div>");

                    //如果当前正在聊天的对象不等于发来消息的对象
                    if (data.fromUser.id != toUserId) {
                        //把对应用户列表中的用户添加“新消息”的图案
                        //当后面没有这个图案时才添加
                        if ($("#" + data.fromUser.id + "colorUserDiv img").length == 0)
                            $("#" + data.fromUser.id + "colorUserDiv li").append("&nbsp;" +
                                "<img src='${messagePromptImage}' width='${imageWidth}' height='${imageHeight}'>");
                    }

                    scrollToBottom("with" + data.fromUser.id + "content");
                }
                else{
                    //如果是登陆广播
                    if (data.type == <%=Constants.LOGIN_BROADCAST_TYPE%>) {
                        var user = data.utterer;
                        if ($("#"+user.id+"colorUserDiv")[0] == null) {
                            console.log("用户"+user.name+"登陆.");
                            $("#userList ul").append("<div id='" + user.id + "colorUserDiv' class='userDiv'" +
                                    " title='" + user.name + "'><li>" + user.name + "</li>" +
                                    "<input type='hidden' value='" + user.id + "'/></div>")
                                    .find("#" + user.id + "colorUserDiv").click(function () {
                                clickUser(this);
                            });
                            addChatContentPanel(user.id);
                        }
                    }
                    //如果是注销广播
                    else if (data.type == <%=Constants.LOGOUT_BROADCAST_TYPE%>) {
                        var user = data.utterer;
                        console.log("用户" + user.name + "注销登录.");
                        $("#" + user.id + "colorUserDiv").remove();
                        $("#with" + user.id + "content").remove();
                        if (toUserId == user.id)
                            $("#withWhoPanel").empty();
                    }
                    //如果是公告广播
                    else {
                        if (data.utterer.id != ${sessionScope.user.id}) {
                            console.log(data.utterer.name+"发布了新公告", data);
                            var flag = confirm("有新公告发布，是否马上前往查看？");
                            if (flag) {
                                window.open("broadcast");
                            }
                        }
                    }
                }
            }

            $("#sendMsg").click(function () {
                if ($("#message").attr("disabled") != "disabled")
                    sendMsg();
            })

            function sendMsg() {
                if(toUserId == -1)
                    return;
                var message = $("#message").val();
                if(message != "")
                {
                    var data = {};
                    data["toUser"] = {"id":toUserId};
                    data["fromUser"] = {"id":"${sessionScope.user.id}"};
                    data["text"] = message;
                    websocket.send(JSON.stringify(data));

                    $("#with" + toUserId + "content").append("<div class='myMsgPanel'><div class='myMsgDate'>" +
                            "我&nbsp;" + new Date().Format("yyyy-MM-dd hh:mm:ss") +
                            "</div><div class='myMsg'>" + message + "</div></div>");
                    $("#message").val("");
                    scrollToBottom("with" + toUserId + "content");
                }
            }

            //如果按下enter则发送消息
            //先把jQuery对象转化为DOM对象
            $("#message")[0].onkeydown = function(event){
                var code;
                if(window.event){
                    code = window.event.keyCode; // IE
                }else{
                    code = event.which; // Firefox
                }
                if(code==13){
                    sendMsg();
                }
            }

            //返回聊天界面
//            $(".goBackChatRecord").click(function () {
//                generateWithWhoPanel();
//                showToUserChatPanel(toUserId);
//                $("#message").removeAttr("disabled");
//                return false;
//            })

        })

        function generateWithWhoPanel(toUserId, toUserName) {
            var href = "javascript:showHistoryChatRecord("+toUserId+",'"+toUserName+"')";
            $("#withWhoPanel").empty().append("与" + toUserName + "聊天中...")
                    .append("<span style='float:right'>" +
                            "<a href="+href+">聊天记录</a></span>");
        }

        //隐藏除指定ID以外的聊天面板
        function showToUserChatPanel(toUserId) {
            $(".content").hide();
//                var $toUserChatPanelDiv = $("#with" + toUserId + "content");
            var div = document.getElementById("with" + toUserId + "content");
            if (div == null){
                addChatContentPanel(toUserId);
            }
            $("#with" + toUserId + "content").show();
        }

        //把滚动条移到最下面
        function scrollToBottom(id){
            var div = document.getElementById(id);
            //			alert(div.scrollTop);
            //scrollTop就是卷起来的部分，也就是我们随着下拉，上面看不见的部分。
            //scrollHeight就是整个窗口可以滑动的高度。
            div.scrollTop = div.scrollHeight;
            //			alert(div.scrollTop);
        }

        function showHistoryChatRecord(toUserId, toUserName) {
            //设置不能输入消息
            $("#message").attr("disabled", "disabled");

            var href = "javascript:goBackChatRecord("+toUserId+",'"+toUserName+"')";
            $("#withWhoPanel").empty().append("与"+toUserName+"的聊天记录")
                    .append("<a href="+href+" style='float:right'>返回</a>");
            $("#with" + toUserId + "content").hide();
            $("#showHistoryChatRecord").show().empty();

            var url = "chatRoom/showHistoryChatRecord";
            var args = {"toUserId":toUserId, "time":new Date()};
            $.getJSON(url, args, function (data) {
                if(data == "")
                {
                    $("#showHistoryChatRecord").append("你与该用户没有历史聊天记录.");
                }
                else
                {
                    for (var i = 0; i < data.length; i++) {
                        var msg = data[i];
                        var clazz = msg.fromUser.id==${sessionScope.user.id}?"myMsg":"remoteMsg";
                        var name = msg.fromUser.name;
                        $("#showHistoryChatRecord").prepend("<div class='"+clazz+"Panel'>" +
                                "<div class='"+clazz+"Date'>"+name + "&nbsp;" +
                                new Date(msg.date).Format("yyyy-MM-dd hh:mm:ss")+"</div>" +
                                "<div class='"+clazz+"'>"+msg.text+"</div></div>");
                    }

                    //把滚动条移到最下面
                    scrollToBottom("showHistoryChatRecord");
                }
            })
        }

        //返回聊天界面
       function goBackChatRecord(toUserId, toUserName) {
           generateWithWhoPanel(toUserId, toUserName);
           showToUserChatPanel(toUserId);
           $("#showHistoryChatRecord").empty();
           $("#message").removeAttr("disabled");
       }

        //Javascript扩展Date的prototype实现时间format函数
        Date.prototype.Format = function (fmt) { //author: meizz
            var o = {
                "M+": this.getMonth() + 1, //月份
                "d+": this.getDate(), //日
                "h+": this.getHours(), //小时
                "m+": this.getMinutes(), //分
                "s+": this.getSeconds(), //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds() //毫秒
            };
            if (/(y+)/.test(fmt))
                fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt))
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }
    </script>
</head>
<%--设置背景图片--%>
<body>
    <center>
        <div id="headDiv">
            <jsp:include page="head.jsp"></jsp:include>
            <jsp:include page="navigation.jsp"></jsp:include>
            <div id="userPanel">
                <div id="userPanelText">在线用户</div>
                <div id="userList">
                    <ul>
                        <c:forEach items="${users}" var="toUser">
                            <div id="${toUser.id}colorUserDiv" class="userDiv" title="${toUser.name}">
                                <li>${toUser.name}</li>
                                <input type="hidden" value="${toUser.id}"/>
                            </div>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <div id="chatPanel">
                <div id="chatRecord">
                    <div id="withWhoPanel"></div>
                    <div class="content" id="showHistoryChatRecord"></div>
                </div>
                <textarea name="message" id="message" style="margin:0;"></textarea>
                <button style="float:right" id="sendMsg">发送</button>
            </div>
        </div>
    </center>
</body>
</html>
