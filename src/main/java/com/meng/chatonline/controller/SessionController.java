package com.meng.chatonline.controller;

import com.meng.chatonline.constant.Constants;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;

/**
 * @author xindemeng
 *
 * 会话管理控制器
 */
@Controller
@RequestMapping("/session")
public class SessionController
{
    @Resource
    private SessionDAO sessionDAO;

    @RequiresPermissions("session:query")
    @RequestMapping({"","/"})
    public String sessionsList(Map<String, Object> map)
    {
        /*
        有一种情况，就是用户不正常退出直接关闭浏览器的话，该用户的session还会残留在缓存池里，
        直到该缓存过期

        此处展示会话列表的缺点是：sessionDAO.getActiveSessions()提供了获取所有活跃会话集合，
        如果做一般企业级应用问题不大，因为在线用户不多；但是如果应用的在线用户非常多，
        此种方法就不适合了，解决方案就是分页获取.
         */
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        map.put("sessions", sessions);
        map.put("sessionCount", sessions.size());

        return "security/sessions";
    }

    @RequiresPermissions("session:forceLogout")
    @RequestMapping("/{sessionId}/forceLogout")
    public String forceLogout(@PathVariable("sessionId") String sessionId,
                              RedirectAttributes redirectAttributes)
    {
        Session session = sessionDAO.readSession(sessionId);
        if (session != null)
            session.setAttribute(Constants.SESSION_FORCE_LOGOUT, Boolean.TRUE);

        redirectAttributes.addFlashAttribute("msg", "强制退出成功");
        return "redirect:/session";
    }

}
