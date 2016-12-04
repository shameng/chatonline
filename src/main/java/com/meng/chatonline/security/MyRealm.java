package com.meng.chatonline.security;

import com.meng.chatonline.model.ActiveUser;
import com.meng.chatonline.model.User;
import com.meng.chatonline.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;

/**
 * @Author xindemeng
 */
public class MyRealm extends AuthorizingRealm
{
    @Resource
    private UserService userService;

    //授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection)
    {
        return null;
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

        ActiveUser activeUser = new ActiveUser();
        activeUser.setId(user.getId());
        activeUser.setAccount(user.getAccount());
        activeUser.setName(user.getName());

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
    @Override
    public String getName()
    {
        return this.getClass().getSimpleName();
    }
}
