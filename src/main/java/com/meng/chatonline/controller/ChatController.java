package com.meng.chatonline.controller;

import com.meng.chatonline.model.ActiveUser;
import com.meng.chatonline.model.Message;
import com.meng.chatonline.model.User;
import com.meng.chatonline.service.MessageService;
import com.meng.chatonline.service.UserService;
import com.meng.chatonline.websocket.MyWebSocketHandler;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
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

    @RequestMapping({"","/"})
    public String toChatRoom(HttpServletRequest request, HttpSession session)
    {
        Subject subject = SecurityUtils.getSubject();
        ActiveUser user = (ActiveUser) subject.getPrincipal();
        session.setAttribute("user", user);

        Set<ActiveUser> usersExcludeMe = MyWebSocketHandler.getOnlineUsers();
        usersExcludeMe.remove(user);
        request.setAttribute("users", usersExcludeMe);
        return "chatRoom";
    }

    @ResponseBody
    @RequestMapping("/showHistoryChatRecord")
    public List<Message> showHistoryChatRecord(HttpServletRequest request)
    {
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
