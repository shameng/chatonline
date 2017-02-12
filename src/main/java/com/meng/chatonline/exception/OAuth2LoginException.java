package com.meng.chatonline.exception;

/**
 * @author xindemeng
 */
public class OAuth2LoginException extends Exception
{
    //发生异常后跳转到的页面
    public static final String VIEW_NAME = "oauth2/oauth2Login";

    public OAuth2LoginException(){}

    public OAuth2LoginException(String msg)
    {
        super(msg);
    }
}
