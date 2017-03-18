package com.meng.chatonline.taglib;

import com.meng.chatonline.constant.Constants;
import com.meng.chatonline.model.ActiveUser;
import org.apache.shiro.session.Session;

/**
 * @author xindemeng
 */
public class Functions
{
    //返回该会话是否已经被强制登出
    public static boolean isForceLogout(Session session)
    {
        return session.getAttribute(Constants.SESSION_FORCE_LOGOUT) != null;
    }

    public static String getUserAccount(Session session)
    {
        ActiveUser user = (ActiveUser) session.getAttribute(Constants.CURRENT_USER);
        return user == null? "": user.getAccount();
    }

    public static String getUserName(Session session)
    {
        ActiveUser user = (ActiveUser) session.getAttribute(Constants.CURRENT_USER);
        return user == null? "": user.getName();
    }

    //返回该session是否属于该账号
    public static boolean isMyself(Session session, String myAccount)
    {
        ActiveUser user = (ActiveUser) session.getAttribute(Constants.CURRENT_USER);
        if (user == null)
            return false;
        return myAccount.equals(user.getAccount());
    }
}
