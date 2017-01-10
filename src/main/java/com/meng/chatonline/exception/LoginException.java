package com.meng.chatonline.exception;

/**
 * @Author xindemeng
 */
public class LoginException extends Exception
{
    //发生异常后跳转到的页面
    private String viewName;

    public LoginException(String message, String viewName)
    {
        super(message);
        this.viewName = viewName;
    }

    public String getViewName()
    {
        return viewName;
    }

    public void setViewName(String viewName)
    {
        this.viewName = viewName;
    }
}
