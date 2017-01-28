package com.meng.chatonline.controller;

import com.meng.chatonline.exception.RunAsException;
import com.meng.chatonline.model.ActiveUser;
import com.meng.chatonline.model.Message;
import com.meng.chatonline.model.User;
import com.meng.chatonline.service.MessageService;
import com.meng.chatonline.utils.MySecurityUtils;
import com.meng.chatonline.websocket.MyWebSocketHandler;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * @Author xindemeng
 */
@Controller
@RequestMapping("/chatRoom")
public class ChatController
{
    @Resource
    private MessageService messageService;

    @RequiresPermissions("chatRoom:chatRoom")
    @RequestMapping({"","/"})
    public String toChatRoom(HttpServletRequest request) throws Exception
    {
        Subject subject = SecurityUtils.getSubject();
        //runAs状态下不能访问聊天室
        if (subject.isRunAs())
            throw new RunAsException();

        ActiveUser user = (ActiveUser) subject.getPrincipal();

        Set<ActiveUser> usersExcludeMe = MyWebSocketHandler.getOnlineUsers();
        usersExcludeMe.remove(user);
        request.setAttribute("users", usersExcludeMe);
        return "chatRoom";
    }

    @RequiresPermissions("chatRoom:historyChatRecord")
    @ResponseBody
    @RequestMapping("/showHistoryChatRecord")
    public List<Message> showHistoryChatRecord(HttpServletRequest request) throws Exception
    {
        //runAs状态下不能访问
        if (MySecurityUtils.isRunAs())
            throw new RunAsException();

        Integer myId = ((User) request.getSession(false).getAttribute("user")).getId();
        String toUserIdStr = request.getParameter("toUserId");
        Integer toUserId = -1;
        try{
            toUserId = Integer.parseInt(toUserIdStr);
        }catch (NumberFormatException ex){
            ex.printStackTrace();
        }
        if (toUserId > 0)
        {
            List<Message> chatRecord = messageService.getHistoryChatRecord(toUserId, myId);
            return chatRecord;
        }
        return null;
    }

}
