package com.meng.chatonline.constant;

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

    //当前用户在session中的key
    String CURRENT_USER = "user";
    //当前shiro主体在session的key
    String PRINCIPAL = "principal";

    //强制登出会话的key
    String SESSION_FORCE_LOGOUT = "sessionForceLogout";

    //OAuth2客户端验证失败信息
    String INVALID_CLIENT_DESCRIPTION = "客户端验证失败，如错误的client_id/client_secret。";
    String INVALID_CLIENT_AUTH_CODE = "错误的授权码";

    String RESOURCE_SERVER_NAME = "chatonline";

}
