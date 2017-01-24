package com.meng.chatonline;

/**
 * @author uthor xindemeng
 */
public interface Constants
{
    //广播类型
    int NOTICE_BROADCAST_TYPE = 0;
    int LOGIN_BROADCAST_TYPE = 1;
    int LOGOUT_BROADCAST_TYPE = 2;

    //菜单类型
    int MENU_TYPE = 0;
    //权限类型，应该说是按钮类型更好
    int AUTH_TYPE = 1;

    //shiro登陆失败参数名
    String SHIRO_LOGIN_FAILURE = "shiroLoginFailure";
    //错误信息
    String ERROR_MSG = "errorMsg";
}