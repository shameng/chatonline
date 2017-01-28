package com.meng.chatonline.security;

import com.meng.chatonline.model.ActiveUser;
import com.meng.chatonline.model.User;
import com.meng.chatonline.model.security.Authority;
import com.meng.chatonline.service.AuthorityService;
import com.meng.chatonline.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.List;

import static org.apache.shiro.web.filter.mgt.DefaultFilter.user;

/**
 * @Author xindemeng
 */
public class MyRealm extends AuthorizingRealm
{
    @Resource
    private UserService userService;
    @Resource
    private AuthorityService authorityService;

    //授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection)
    {
        ActiveUser user = (ActiveUser) principalCollection.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        List<Authority> authorities = authorityService.getAuthoritiesByUserId(user.getId());
        //添加用户权限
        for (Authority authority : authorities)
        {
            authorizationInfo.addStringPermission(authority.getCode());
        }

        return authorizationInfo;
    }

    //认证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException
    {
        String account = (String) authenticationToken.getPrincipal();
        User user = userService.getUserByAccount(account);

        //如果用户不存在
        if (user == null)
            return null;

        String password = user.getPassword();
        String salt = user.getSalt();

        ActiveUser activeUser = new ActiveUser(user.getId(), user.getAccount(),user.getName());
        activeUser.setSuperAdmin(user.getSuperAdmin());

        SimpleAuthenticationInfo authenticationInfo = null;
        try
        {
            authenticationInfo = new SimpleAuthenticationInfo(
                    activeUser, password, ByteSource.Util.bytes(salt), getName());
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return authenticationInfo;
    }

    // 设置realm的名称
//    @Override
//    public String getName()
//    {
//        return MyRealm.class.getName();
//    }

    //清除当前用户的权限缓存,通常在service上修改完权限以后调用该方法
    public void clearMyCachedAuthorizationInfo()
    {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCachedAuthorizationInfo(principals);
    }

    //清除指定用户的缓存
    public void clearCachedAuthorizationInfo(Integer userId)
    {
        ActiveUser user = new ActiveUser(userId);
        SimplePrincipalCollection principals = new SimplePrincipalCollection(user, getName());
        super.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 清除所有用户授权信息缓存.
     */
    public void clearAllCachedAuthorizationInfo() {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null) {
            cache.clear();
        }
    }

    //清除指定用户登陆信息
    public void clearCachedAuthenticationInfo(Integer userId)
    {
        ActiveUser user = new ActiveUser(userId);
        SimplePrincipalCollection principals = new SimplePrincipalCollection(user, getName());
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission)
    {
        ActiveUser user = (ActiveUser) principals.getPrimaryPrincipal();
        //如果是超级管理员，则直接通过权限验证
        if (user.getSuperAdmin())
            return true;

        return super.isPermitted(principals, permission);
    }
}
