package com.meng.chatonline.controller;

import com.meng.chatonline.constant.Constants;
import com.meng.chatonline.annotation.CurrentUser;
import com.meng.chatonline.model.ActiveUser;
import com.meng.chatonline.model.User;
import com.meng.chatonline.security.MyRealm;
import com.meng.chatonline.service.UserRunAsService;
import com.meng.chatonline.service.UserService;
import com.meng.chatonline.utils.MySecurityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @author xindemeng
 *
 * ？有时候会出现InvalidSessionException会话失效异常
 */
@Controller
@RequestMapping("/userRunAs")
public class UserRunAsController
{
    @Resource
    private UserRunAsService userRunAsService;
    @Resource
    private UserService userService;

    @RequiresPermissions("runAs:query")
    @RequestMapping({"","/"})
    public String userRunAsList(@CurrentUser User loginUser, Map<String, Object> map)
    {
        //获得被授予该用户身份的用户
        List<User> grantedUsersByMyself = this.userRunAsService.findGrantedUsersByMySelf(loginUser);
        map.put("grantedUsersByMyself", grantedUsersByMyself);
        //获得可授予该用户身份的用户
        List<User> grantUsersByMyself = this.userRunAsService.findCanBeGrantedUserByMySelf(grantedUsersByMyself, loginUser);
        map.put("grantUsersByMyself", grantUsersByMyself);
        //获得该用户被授予的身份
        List<User> grantedUsersByOthers = this.userRunAsService.findGrantedUsersByOthers(loginUser);
        map.put("grantedUsersByOthers", grantedUsersByOthers);

        Subject subject = SecurityUtils.getSubject();
        if (subject.isRunAs())
        {
            ActiveUser previousUser = (ActiveUser) subject.getPreviousPrincipals().getPrimaryPrincipal();
            map.put("previousUserName", previousUser.getName());
            ActiveUser user = (ActiveUser) subject.getPrincipals().getPrimaryPrincipal();
            map.put("runAsUserId", user.getId());
        }
        map.put("isRunAs", subject.isRunAs());

        return "security/userRunAs";
    }

    //授予用户身份
    @RequiresPermissions("runAs:grant")
    @RequestMapping("/grant/{toUserId}")
    public String grant(@CurrentUser User loginUser, @PathVariable("toUserId") Integer toUserId,
                        RedirectAttributes redirectAttributes)
    {
        User toUser = this.userService.getEntity(toUserId);
        if (loginUser.getId().equals(toUserId))
            redirectAttributes.addFlashAttribute("msg", "不能授予身份给自己！");
        else if (toUser == null)
            redirectAttributes.addFlashAttribute("msg", "不存在的用户！");
        else
        {
            if (this.userRunAsService.existRunAs(loginUser.getId(), toUserId))
                redirectAttributes.addFlashAttribute("msg", "该授予身份关系已存在！");
            else
            {
                this.userRunAsService.grantRunAs(loginUser.getId(), toUserId);
                redirectAttributes.addFlashAttribute("msg", "操作成功！");
            }
        }
        return "redirect:/userRunAs";
    }

    //回收用户身份
    @RequiresPermissions("runAs:revoke")
    @RequestMapping("/revoke/{toUserId}")
    public String revoke(@CurrentUser User loginUser, @PathVariable("toUserId") Integer toUserId,
                         RedirectAttributes redirectAttributes)
    {
        User toUser = this.userService.getEntity(toUserId);
        if (toUser == null)
            redirectAttributes.addFlashAttribute("msg", "不存在的用户！");
        else
        {
            this.userRunAsService.revokeRunAs(loginUser.getId(), toUserId);
            redirectAttributes.addFlashAttribute("msg", "操作成功！");
        }
        return "redirect:/userRunAs";
    }

    //切换身份
    @RequiresPermissions("runAs:switchTo")
    @RequestMapping("/switchTo/{fromUserId}")
    public String switchTo(@CurrentUser User loginUser, @PathVariable("fromUserId") Integer fromUserId,
                           RedirectAttributes redirectAttributes, HttpSession session)
    {
        User fromUser = this.userService.getEntity(fromUserId);
        if (loginUser.getId().equals(fromUserId))
            redirectAttributes.addFlashAttribute("msg", "不能切换到自己的身份！");
        else if (fromUser == null)
            redirectAttributes.addFlashAttribute("msg", "不存在的用户！");
        else if (!this.userRunAsService.existRunAs(fromUserId, loginUser.getId()))
            redirectAttributes.addFlashAttribute("msg", "对方没有授予你身份，不能切换！");
        else
        {
            ActiveUser user = MySecurityUtils.userToActiveUser(fromUser);
            Subject subject = SecurityUtils.getSubject();
            subject.runAs(new SimplePrincipalCollection(user, new MyRealm().getName()));
            redirectAttributes.addFlashAttribute("msg", "操作成功！");

            session.setAttribute("isRunAs", true);
            session.setAttribute(Constants.PRINCIPAL, user);
        }
        return "redirect:/userRunAs";
    }

    //切换回到上一个身份
    @RequiresPermissions("runAs:switchTo")
    @RequestMapping("/switchBack")
    public String switchBack(@CurrentUser User loginUser, HttpSession session,
                             RedirectAttributes redirectAttributes)
    {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isRunAs())
        {
            subject.releaseRunAs();
            redirectAttributes.addFlashAttribute("msg", "操作成功！");

            ActiveUser principal = (ActiveUser) subject.getPrincipals().getPrimaryPrincipal();
            if (principal.getId().equals(loginUser.getId()))
            {
                session.setAttribute("isRunAs", false);
                session.setAttribute(Constants.PRINCIPAL, loginUser);
            }
        }
        return "redirect:/userRunAs";
    }

    //切换回到自己身份
    @RequiresPermissions("runAs:switchTo")
    @RequestMapping("/switchBackToMe")
    public String switchBackToMe(@CurrentUser User loginUser, HttpSession session,
                                 RedirectAttributes redirectAttributes)
    {
        Subject subject = SecurityUtils.getSubject();
        while (subject.isRunAs())
        {
            subject.releaseRunAs();
        }
        redirectAttributes.addFlashAttribute("msg", "操作成功！");

        session.setAttribute("isRunAs", false);
        session.setAttribute(Constants.PRINCIPAL, loginUser);
        return "redirect:/userRunAs";
    }

}
