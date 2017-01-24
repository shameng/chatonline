package com.meng.chatonline.exception;

/**
 * @Author xindemeng
 */
public class LoginException extends Exception
{
    //发生异常后跳转到的页面
    public static final String VIEW_NAME = "login";

    public LoginException(String message)
    {
        super(message);
    }

}
