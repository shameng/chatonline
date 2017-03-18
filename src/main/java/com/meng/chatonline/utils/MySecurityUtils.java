package com.meng.chatonline.utils;

import com.meng.chatonline.constant.Constants;
import com.meng.chatonline.model.ActiveUser;
import com.meng.chatonline.model.User;
import com.meng.chatonline.model.security.Authority;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;

import java.util.*;

/**
 * @Author xindemeng
 *
 * 安全工具类
 */
public class MySecurityUtils
{
    //MD5加密
    public static String md5(String password, String salt, int hashIterations)
    {
        Md5Hash md5Hash = new Md5Hash(password, salt, hashIterations);
        return md5Hash.toString();
    }

    //默认散列两次
    public static String md5Default(String password, String salt)
    {
        return md5(password, salt, 2);
    }

    //生成盐
    public static String generateSalt()
    {
        String salt = UUID.randomUUID().toString();
        return salt;
    }

    //把User转换为ActiveUser
    public static ActiveUser userToActiveUser(User user)
    {
        return new ActiveUser(user.getId(), user.getAccount(), user.getName(), user.getSuperAdmin());
    }

    //把权限按菜单分开，key是菜单，value是菜单包含的权限
    public static Map<Authority, List<Authority>> getAuthoritiesSortedByMenu(List<Authority> authorities)
    {
        //LinkedHashMap它内部有一个链表，保持插入的顺序。迭代的时候，也是按照插入顺序迭代，而且迭代比HashMap快。
        HashMap<Authority, List<Authority>> map = new LinkedHashMap<Authority, List<Authority>>();
        for (Authority auth : authorities)
        {
            if (auth.getType() == Constants.MENU_TYPE)
                map.put(auth, new ArrayList<Authority>());
        }
        for (Authority auth : authorities)
        {
            if (auth.getType() == Constants.AUTH_TYPE)
                if (map.get(auth.getMenu()) != null)
                    map.get(auth.getMenu()).add(auth);
        }
        return map;
    }

    //把password和salt属性置为null
    public static List<User> passwordAndSaltBeNull(List<User> users)
    {
        for (User user : users)
        {
            user.setPassword(null);
            user.setSalt(null);
        }
        return users;
    }

    //判断当前用户是否为runAs状态
    public static boolean isRunAs()
    {
        Subject subject = SecurityUtils.getSubject();
        return subject.isRunAs();
    }
}
